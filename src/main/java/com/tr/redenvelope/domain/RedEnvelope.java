package com.tr.redenvelope.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.*;

@Data
public class RedEnvelope {
    final static public  int TOTAL_COUNT = 100;
    final  static public int TOTAL_AMOUNT = 500;


    //限制为1到5
    private int id;

    @JsonIgnore
    private List<SubRedEnvelope> subRedEnvelopes;
    private Set<Integer> bindUserIds;

    public static int generateRandomId() {
        //[1,5]
        Random random = new Random();
        return random.nextInt(5) + 1;
    }


    public static int generateRandomIdFrom1(int upperBound) {
        //[1,upperBound]
        if (upperBound ==0) return 0;
        Random random = new Random();
        return random.nextInt(upperBound) + 1;
    }

   static public List<Integer>  randomGenerateSubEnvelopeAmount() {
        int minimumBaseSubEnvelopeAmount =1;
        int leftTotalAmount= TOTAL_AMOUNT-TOTAL_COUNT*minimumBaseSubEnvelopeAmount;
        List<Integer>  amounts=new ArrayList<>();
        for(int i=0;i<TOTAL_COUNT;i++){
          int randomAmount=  generateRandomIdFrom1(leftTotalAmount);
            amounts.add(randomAmount+minimumBaseSubEnvelopeAmount);
            leftTotalAmount-=randomAmount;
        }
        return amounts;
    }

    public static void main(String[] args) {
        System.out.println(randomGenerateSubEnvelopeAmount());
    }

    public RedEnvelope build() {

        this.id = generateRandomId();
        this.subRedEnvelopes = new ArrayList<>();
        List<Integer> randomSubEnvelopeAmounts=randomGenerateSubEnvelopeAmount();

        for (int i = 0; i < TOTAL_COUNT; i++) {
            this.subRedEnvelopes.add(new SubRedEnvelope().build(this.id, i + 1, randomSubEnvelopeAmounts.get(i)));

        }
        return this;
    }



}
