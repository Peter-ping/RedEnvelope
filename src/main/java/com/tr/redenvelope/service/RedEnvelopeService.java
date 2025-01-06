package com.tr.redenvelope.service;

import com.tr.redenvelope.domain.RedEnvelope;
import com.tr.redenvelope.domain.SubRedEnvelope;
import com.tr.redenvelope.repo.EnvelopeRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class RedEnvelopeService {


    @Resource
    private EnvelopeRepository envelopeRepository;

    public RedEnvelope generateEnvelope() {


        RedEnvelope redEnvelope = new RedEnvelope().build();
        this.envelopeRepository.saveLua(redEnvelope);
        return redEnvelope;
    }

    public SubRedEnvelope grab(String userId, String redEnvelopeIdParam) {
        int redEnvelopeId = Integer.parseInt(redEnvelopeIdParam);
        if (!(redEnvelopeId >= 1 && redEnvelopeId <= 5)) throw new IllegalArgumentException("hbid not valid");

        SubRedEnvelope subRedEnvelope = this.envelopeRepository.luaScriptGetSubEnvelopeAndBindUser(redEnvelopeId, Integer.parseInt(userId));
        subRedEnvelope.bindUser(Integer.parseInt(userId));
        this.envelopeRepository.dbUpdateSubEnvelope(subRedEnvelope);
        return subRedEnvelope;

    }


}


