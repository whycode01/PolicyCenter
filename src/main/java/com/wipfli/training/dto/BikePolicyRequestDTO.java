package com.wipfli.training.dto;

import java.time.LocalDate;

public class BikePolicyRequestDTO {

    private String policyNum;
    private String customerId;
    private String customerName;
    private int customerAge;
    private int previousClaimCount;
    private LocalDate expiryDate;
    private int engineCC;

    public BikePolicyRequestDTO() {
    }

    public BikePolicyRequestDTO(
            String policyNum,
            String customerId,
            String customerName,
            int customerAge,
            int previousClaimCount,
            LocalDate expiryDate,
            int engineCC) {

        this.policyNum = policyNum;
        this.customerId = customerId;
        this.customerName = customerName;
        this.customerAge = customerAge;
        this.previousClaimCount = previousClaimCount;
        this.expiryDate = expiryDate;
        this.engineCC = engineCC;
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

    public int getEngineCC() {
        return engineCC;
    }

    public void setEngineCC(int engineCC) {
        this.engineCC = engineCC;
    }
}
