package com.wipfli.training.model;

public class Customer {

    private String customerName;
    private int customerAge;

    public Customer(String customerName, int customerAge) {

        if(customerName == null ||
                customerName.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Customer name cannot be blank."
            );
        }

        if(customerAge < 18 ||
                customerAge > 100) {

            throw new IllegalArgumentException(
                    "Age must be between 18 and 100."
            );
        }

        this.customerName = customerName;
        this.customerAge = customerAge;
    }

    public String getCustomerName() {
        return customerName;
    }

    public int getCustomerAge() {
        return customerAge;
    }

    // Package-private method
    boolean isAdult() {
        return customerAge >= 18;
    }
}