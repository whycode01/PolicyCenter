package com.wipfli.training.service;

import com.wipfli.training.model.BikePolicy;
import com.wipfli.training.model.CarPolicy;
import com.wipfli.training.model.Customer;
import com.wipfli.training.model.PolicyDetail;
import com.wipfli.training.model.TruckPolicy;

import java.time.LocalDate;

public final class PolicyFactory {

    private PolicyFactory() {
        // private constructor to prevent instantiation
    }

    public static PolicyDetail createCarPolicy(String policyNum,
                                               Customer customer,
                                               int previousClaimCount,
                                               LocalDate expiryDate,
                                               String registrationNumber) {
        return new CarPolicy(
                policyNum,
                customer,
                previousClaimCount,
                expiryDate,
                registrationNumber
        );
    }

    public static PolicyDetail createTruckPolicy(String policyNum,
                                                 Customer customer,
                                                 int previousClaimCount,
                                                 LocalDate expiryDate,
                                                 double loadCapacityTons) {
        return new TruckPolicy(
                policyNum,
                customer,
                previousClaimCount,
                expiryDate,
                loadCapacityTons
        );
    }



    public static PolicyDetail createBikePolicy(String policyNum,
                                                Customer customer,
                                                int previousClaimCount,
                                                LocalDate expiryDate,
                                                int engineCC) {
        return new BikePolicy(
                policyNum,
                customer,
                previousClaimCount,
                expiryDate,
                engineCC
        );
    }



}

