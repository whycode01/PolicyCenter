package com.wipfli.training.dto;

public class CustomerRequestDTO {

    private String customerName;
    private int customerAge;

    public CustomerRequestDTO() {
    }

    public CustomerRequestDTO(
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

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setCustomerAge(int customerAge) {
        this.customerAge = customerAge;
    }
}