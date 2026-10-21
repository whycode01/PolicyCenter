package com.wipfli.training.dto;

import java.time.LocalDate;

public class TruckPolicyRequestDTO {

    private String policyNum;
    private String customerName;
    private int customerAge;
    private int previousClaimCount;
    private LocalDate expiryDate;
    private double loadCapacityTons;

    public TruckPolicyRequestDTO() {
    }

    public TruckPolicyRequestDTO(
            String policyNum,
            String customerName,
            int customerAge,
            int previousClaimCount,
            LocalDate expiryDate,
            double loadCapacityTons) {

        this.policyNum = policyNum;
        this.customerName = customerName;
        this.customerAge = customerAge;
        this.previousClaimCount = previousClaimCount;
        this.expiryDate = expiryDate;
        this.loadCapacityTons = loadCapacityTons;
    }

    public String getPolicyNum() {
        return policyNum;
    }

    public void setPolicyNum(String policyNum) {
        this.policyNum = policyNum;
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

    public double getLoadCapacityTons() {
        return loadCapacityTons;
    }

    public void setLoadCapacityTons(double loadCapacityTons) {
        this.loadCapacityTons = loadCapacityTons;
    }
}