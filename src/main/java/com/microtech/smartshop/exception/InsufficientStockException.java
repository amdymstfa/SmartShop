package com.microtech.smartshop.exception;

public class InsufficientStockException extends BusinessRuleException {
    public InsufficientStockException(String message) {
        super(message);
    }
}
