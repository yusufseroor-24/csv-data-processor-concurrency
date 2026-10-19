package com.ga.CSVProcessor.enums;

import java.math.BigDecimal;

public enum Roles {


    DIRECTOR(new BigDecimal("0.05")),
    MANAGER(new BigDecimal("0.02")),
    EMPLOYEE(new BigDecimal("0.01"));


    private final BigDecimal raiseRate;

    Roles(BigDecimal raiseRate){
        this.raiseRate = raiseRate;
    }

    public BigDecimal getRaiseRate() {
        return raiseRate;
    }

    public static Roles fromString(String text){
        if(text == null){
            throw new IllegalArgumentException("Role cannot be null");
        }
        return Roles.valueOf(text.trim().toUpperCase());
    }
}
