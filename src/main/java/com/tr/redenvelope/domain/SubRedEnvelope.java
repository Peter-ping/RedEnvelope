package com.tr.redenvelope.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SubRedEnvelope {
    private int parentId;


    private int id;


    private int amount;


    private int userId;

    public SubRedEnvelope build(int parentId, int id, int amount) {
        this.parentId = parentId;
        this.id = id;
        this.amount = amount;
        return this;
    }

    public void bindUser(int userId) {
        this.userId = userId;
    }

}
