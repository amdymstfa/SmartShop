package com.microtech.smartshop.exception;

public class OrderAlreadyFinalizedException extends BusinessRuleException {
    public OrderAlreadyFinalizedException(String message) {
        super(message);
    }

    public OrderAlreadyFinalizedException(Long orderId, String status) {
        super(String.format("Order %d is already finalized with status: %s", orderId, status));
    }
}