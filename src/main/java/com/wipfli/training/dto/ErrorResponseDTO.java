package com.wipfli.training.dto;

public class ErrorResponseDTO {

    private String policyNumber;
    private String message;

    public ErrorResponseDTO(String policyNumber, String message) {
        this.policyNumber = policyNumber;
        this.message = message;
    }

    public String getPolicyNumber() {
        return policyNumber;
    }

    public String getMessage() {
        return message;
    }
}
