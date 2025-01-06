package com.tr.redenvelope.api;

import com.tr.redenvelope.domain.RedEnvelope;
import com.tr.redenvelope.service.RedEnvelopeService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api")
public class RedEnvelopeController {
    @Resource
    private RedEnvelopeService redEnvelopeService;


    @GetMapping("/get")
    public Object grap(@RequestParam("hbid") String redEnvelopeId, @RequestParam("uid") String userId) {

        try {
            return this.redEnvelopeService.grab(userId, redEnvelopeId);

        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }

    }

    @GetMapping("/generate")
    public RedEnvelope generate() {
        return this.redEnvelopeService.generateEnvelope();
    }


}
