package com.tr.redenvelope.repo;

import com.tr.redenvelope.domain.RedEnvelope;
import com.tr.redenvelope.domain.SubRedEnvelope;
import com.tr.redenvelope.util.IOUtils;
import com.tr.redenvelope.util.JsonUtils;
import io.micrometer.common.util.StringUtils;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RScript;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Repository
public class EnvelopeRepository {
    final public static String SUB_RED_ENVELOPE_KEY_PREFIX = "SUB_RED_ENVELOPE_";
    final public static String BOUND_USERS_KEY_PREFIX = "BOUND_USERS_";


    private RedissonClient redisson;
    @Resource
    private JdbcTemplate jdbcTemplate;

    private String luaSha;
    @Value("${redis.url}")
    private String redisUrl;

    @PostConstruct
    public void init() throws IOException {
        log.info("redisUrl: {}", redisUrl);

        Config config = new Config();
        config.useSingleServer().setAddress(redisUrl);
        this.redisson = Redisson.create(config);
        ClassPathResource luaResource = new ClassPathResource("grab.lua");
        if (!luaResource.exists()) {
            throw new RuntimeException("grab.lua not exist");

        } else {
            String lua = IOUtils.convertInputStream(luaResource.getInputStream(), "utf-8");
            luaSha = redisson.getScript().scriptLoad(lua);
            log.info("lua script load luaSha : {}", luaSha);

        }

    }

    public void saveLua(RedEnvelope redEnvelope) {
        //1.插入数据库 两张表 RedEnvelope SubRedEnvelope 2.写入redis RedEnvelope SubRedEnvelopes存为list，key为RedEnvelope id
        this.dbSave(redEnvelope);
        //有时间改成lua脚本，确保原子性
        //  this.redisson.getBucket(RED_ENVELOPE_KEY_PREFIX + redEnvelope.getId(), JsonJacksonCodec.INSTANCE).set(redEnvelope);
        redEnvelope.getSubRedEnvelopes().forEach(subRedEnvelope -> {
            this.redisson.getList(SUB_RED_ENVELOPE_KEY_PREFIX + redEnvelope.getId(), JsonJacksonCodec.INSTANCE).add(subRedEnvelope);
        });

    }

    public SubRedEnvelope luaScriptGetSubEnvelopeAndBindUser(Integer redEnvelopeId, int userId) {
        List<Object> keys = new ArrayList<>();
        keys.add(SUB_RED_ENVELOPE_KEY_PREFIX + redEnvelopeId);
        keys.add(BOUND_USERS_KEY_PREFIX + redEnvelopeId);
        keys.add(userId);
        List<Object> values = new ArrayList<>();
        values.add(userId);
        String result = redisson.getScript(StringCodec.INSTANCE).evalSha(RScript.Mode.READ_WRITE, luaSha, RScript.ReturnType.MULTI, keys, values.toArray());
        log.info("lus return value:{}", result);
        if (!StringUtils.isEmpty(result)) return JsonUtils.fromJSON(result, SubRedEnvelope.class);
        else throw new RuntimeException("can't find sub red envelope");
    }


    private void dbSave(RedEnvelope redEnvelope) {
        String red_envelope_insert_sql = "insert into red_envelope values(?)";
        String sub_red_envelope_insert_sql = "insert into sub_red_envelope values(?,?,?,?)";

        this.jdbcTemplate.update(red_envelope_insert_sql, redEnvelope.getId());
        this.jdbcTemplate.batchUpdate(sub_red_envelope_insert_sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                SubRedEnvelope subRedEnvelope = redEnvelope.getSubRedEnvelopes().get(i);
                ps.setInt(1, subRedEnvelope.getId());
                ps.setInt(2, subRedEnvelope.getParentId());
                ps.setInt(3, subRedEnvelope.getUserId());
                ps.setInt(4, subRedEnvelope.getAmount());


            }

            @Override
            public int getBatchSize() {
                return redEnvelope.getSubRedEnvelopes().size();
            }
        });
        // this.jdbcTemplate.u
    }


    public void dbUpdateSubEnvelope(SubRedEnvelope subRedEnvelope) {
        this.jdbcTemplate.update("update sub_red_envelope set user_id=? where id=? and parent_id=?", subRedEnvelope.getUserId(), subRedEnvelope.getId(), subRedEnvelope.getParentId());
    }
}
