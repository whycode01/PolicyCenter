package com.wipfli.training.dto;

import com.wipfli.training.model.PolicyStatus;
import com.wipfli.training.model.VehicleType;

import java.time.LocalDate;

public class PolicyResponseDTO {

    private String policyNum;
    private String customerName;
    private int customerAge;
    private VehicleType vehicleType;
    private int previousClaimCount;
    private PolicyStatus status;
    private LocalDate expiryDate;

    public PolicyResponseDTO(
            String policyNum,
            String customerName,
            int customerAge,
            VehicleType vehicleType,
            int previousClaimCount,
            PolicyStatus status,
            LocalDate expiryDate) {

        this.policyNum = policyNum;
        this.customerName = customerName;
        this.customerAge = customerAge;
        this.vehicleType = vehicleType;
        this.previousClaimCount = previousClaimCount;
        this.status = status;
        this.expiryDate = expiryDate;
    }

    public String getPolicyNum() {
        return policyNum;
    }

    public String getCustomerName() {
        return customerName;
    }

    public int getCustomerAge() {
        return customerAge;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public int getPreviousClaimCount() {
        return previousClaimCount;
    }

    public PolicyStatus getStatus() {
        return status;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }
}