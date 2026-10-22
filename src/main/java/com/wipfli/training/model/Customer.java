package com.wipfli.training.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "customer")
public class Customer {

    @Id
    @Column(name = "user_id", updatable = false, nullable = false)
    private String userID;

    @Column(name = "customer_name", nullable = false)
    private String customerName;

    @Column(name = "customer_age", nullable = false)
    private int customerAge;

    // Required by Hibernate - it instantiates the entity, then fills the fields
    protected Customer() {
    }

    public Customer(String userID, String customerName, int customerAge) {

        if(userID == null ||
                userID.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "User ID cannot be blank."
            );
        }

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

        this.userID = userID;
        this.customerName = customerName;
        this.customerAge = customerAge;
    }

    public String getUserID() {
        return userID;
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