package com.wipfli.training.dto;

import java.time.LocalDate;

public class CarPolicyRequestDTO {

    private String policyNum;
    private String customerId;
    private String customerName;
    private int customerAge;
    private int previousClaimCount;
    private LocalDate expiryDate;
    private String registrationNumber;

    public CarPolicyRequestDTO() {
    }

    public CarPolicyRequestDTO(
            String policyNum,
            String customerId,
            String customerName,
            int customerAge,
            int previousClaimCount,
            LocalDate expiryDate,
            String registrationNumber) {

        this.policyNum = policyNum;
        this.customerId = customerId;
        this.customerName = customerName;
        this.customerAge = customerAge;
        this.previousClaimCount = previousClaimCount;
        this.expiryDate = expiryDate;
        this.registrationNumber = registrationNumber;
    }

    public String getPolicyNum() {
        return policyNum;
    }

    public void setPolicyNum(String policyNum) {
        this.policyNum = policyNum;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public int getCustomerAge() {
        return customerAge;
    }

    public void setCustomerAge(int customerAge) {
        this.customerAge = customerAge;
    }

    public int getPreviousClaimCount() {
        return previousClaimCount;
    }

    public void setPreviousClaimCount(int previousClaimCount) {
        this.previousClaimCount = previousClaimCount;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }
}