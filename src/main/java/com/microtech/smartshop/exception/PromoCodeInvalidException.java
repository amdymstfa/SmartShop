package com.microtech.smartshop.exception;

public class PromoCodeInvalidException extends BusinessRuleException {
    public PromoCodeInvalidException(String message) {
        super(message);
    }

    public PromoCodeInvalidException(String code, String reason) {
        super(String.format("Promo code '%s' is invalid: %s", code, reason));
    }
}