package com.annapoorna.exception;

public class InsufficientInventoryException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public InsufficientInventoryException(String message) {
        super(message);
    }

    public InsufficientInventoryException(String message, Throwable cause) {
        super(message, cause);
    }
}
