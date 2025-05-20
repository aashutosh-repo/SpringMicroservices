package com.spring.customer.error;

public class CustomerNotFoundException extends RuntimeException {
    private final ErrorResponse errorResponse;

    public CustomerNotFoundException(ErrorResponse errorResponse) {
        super(errorResponse.getErrorDescription());
        this.errorResponse = errorResponse;
    }

    public ErrorResponse getErrorResponse() {
        return errorResponse;
    }
}
