--幂等判断 返回值 0:已执行，1：第一次执行

local existed = redis.call('hexists', KEYS[2],KEYS[3]);

---redis.log(redis.LOG_WARNING, accountRecordJson)
if (existed ~= 0) then return "";

else
 local sub_envelope_json=   redis.call('lpop', KEYS[1]);

  redis.call('hset', KEYS[2], KEYS[3], '');
    return sub_envelope_json;
end



----幂等判断 返回值 0:已执行，1：第一次执行
--
--local accountRecordJson = redis.call('get', KEYS[2]);
--
--redis.log(redis.LOG_WARNING, accountRecordJson)
--if (accountRecordJson ~= false) then return 0;
--
--else
--  --save account record
--  redis.call('setex', KEYS[2], ARGV[3], '');
--  local accountJson = redis.call('hget', KEYS[3], KEYS[1]);
--  local account = cjson.decode(accountJson);
--  account["totalBalance"] = ARGV[1];
--  account["frozenBalance"] = ARGV[2];
--  account["updateTime"] = tonumber(ARGV[5]);
--  redis.call('hset', KEYS[3], KEYS[1], cjson.encode(account));
--  return 1;
--end

