package com.wipfli.training.model;

import com.wipfli.training.exception.InvalidPolicyDataException;
import java.time.LocalDate;                         // CHANGE: needed for expiryDate

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("TRUCK")
public class TruckPolicy extends PolicyDetail {

    // Wrapper type + nullable: CAR and BIKE rows share this table and have NULL here.
    // A primitive double cannot hold NULL when Hibernate reads another type's row.
    @Column(name = "load_capacity_tons", nullable = true)
    private Double loadCapacityTons;

    // CHANGE: minimum truck driver age lives HERE, because the rule is truck-only
    private static final int MINIMUM_TRUCK_DRIVER_AGE = 21;

    // Required by Hibernate - it instantiates the entity, then fills the fields
    protected TruckPolicy() {
    }

    // Constructor WITH claims history
    public TruckPolicy(String policyNum,
                       Customer customer,
                       int previousClaimCount,
                       LocalDate expiryDate,
                       double loadCapacityTons) {

        super(
                policyNum,
                customer,
                VehicleType.TRUCK,
                previousClaimCount,
                expiryDate
        );

        // THE TRUCK RULE — enforced by the class that owns it
        checkDriverAge(policyNum, customer);

        this.loadCapacityTons = loadCapacityTons;
    }

    // Overloaded constructor WITHOUT claims history (assumes 0 claims)
    public TruckPolicy(String policyNum,
                       Customer customer,
                       LocalDate expiryDate,
                       double loadCapacityTons) {

        super(
                policyNum,
                customer,
                VehicleType.TRUCK,
                expiryDate
        );

        // THE TRUCK RULE — enforced here too
        checkDriverAge(policyNum, customer);

        this.loadCapacityTons = loadCapacityTons;
    }

    // CHANGE: private helper so both constructors enforce the same rule (no duplication)
    private void checkDriverAge(String policyNum, Customer customer) {

        if (customer.getCustomerAge() < MINIMUM_TRUCK_DRIVER_AGE) {

            throw new InvalidPolicyDataException(
                    policyNum,
                    "a truck policy needs a driver of at least "
                            + MINIMUM_TRUCK_DRIVER_AGE
                            + ", this driver is "
                            + customer.getCustomerAge()
            );
        }
    }

    public Double getLoadCapacityTons() {
        return loadCapacityTons;
    }

    @Override
    public String getPolicyDetails() {

        return "Policy "
                + getPolicyNum()
                + " | Customer: "
                + getCustomer().getCustomerName()
                + " | Status: "
                + getStatus()
                + " | Vehicle: "
                + getVehicleType()
                + " | Expires: "
                + getExpiryDate()
                + " | Load Capacity: "
                + loadCapacityTons
                + " tons";
    }
}