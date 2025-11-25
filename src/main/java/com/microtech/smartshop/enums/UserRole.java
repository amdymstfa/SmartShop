package com.microtech.smartshop.enums;

public enum UserRole
{
    ADMIN ,CLIENT;

    // Business logic
    public boolean isAdmin(){
        return this == ADMIN ;
    }

    public boolean isClient(){
        return this == CLIENT ;
    }
}