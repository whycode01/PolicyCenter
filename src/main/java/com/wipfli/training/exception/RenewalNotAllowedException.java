package com.wipfli.training.exception;

public class RenewalNotAllowedException extends PolicyBusinessException {
    public RenewalNotAllowedException(String policyNumber, String message) {
        super(policyNumber, message);
    }
}