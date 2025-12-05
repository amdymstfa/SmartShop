package com.microtech.smartshop.exception;

public class CashLimitExceededException extends BusinessRuleException {
    public CashLimitExceededException(String message) {
        super(message);
    }
}