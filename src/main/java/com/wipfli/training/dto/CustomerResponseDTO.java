package com.wipfli.training.dto;

public class CustomerResponseDTO {

    private String customerName;
    private int customerAge;

    public CustomerResponseDTO(
            String customerName,
            int customerAge) {

        this.customerName = customerName;
        this.customerAge = customerAge;
    }

    public String getCustomerName() {
        return customerName;
    }

    public int getCustomerAge() {
        return customerAge;
    }
}