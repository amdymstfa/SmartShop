package com.microtech.smartshop.enums;

public enum CustomerTier
{
    BASIC ,SILVER ,GOLD, PLATINUM;

    // Business logic
    public boolean isBasic(){
        return this == BASIC ;
    }

    public boolean isSilver(){
        return this == SILVER ;
    }

    public boolean isGold(){
        return this == GOLD ;
    }

    public boolean isPlatinum(){
        return this == PLATINUM ;
    }
}