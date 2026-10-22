package com.wipfli.training.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("BIKE")
public class BikePolicy extends PolicyDetail {

    // Wrapper type + nullable: CAR and TRUCK rows share this table and have NULL here.
    // A primitive int cannot hold NULL when Hibernate reads another type's row.
    @Column(name = "engine_cc", nullable = true)
    private Integer engineCC;

    // Required by Hibernate - it instantiates the entity, then fills the fields
    protected BikePolicy() {
    }

    // Constructor WITH claims history
    public BikePolicy(String policyNum,
                      Customer customer,
                      int previousClaimCount,
                      LocalDate expiryDate,
                      int engineCC) {

        super(
                policyNum,
                customer,
                VehicleType.BIKE,
                previousClaimCount,
                expiryDate
        );

        this.engineCC = engineCC;
    }

    // Overloaded constructor WITHOUT claims history (assumes 0 claims)
    public BikePolicy(String policyNum,
                      Customer customer,
                      LocalDate expiryDate,   // CHANGE: added expiryDate
                      int engineCC) {

        super(
                policyNum,
                customer,
                VehicleType.BIKE,
                expiryDate                    // CHANGE: pass expiryDate up
        );

        this.engineCC = engineCC;
    }

    public Integer getEngineCC() {
        return engineCC;
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
                + getExpiryDate()             // CHANGE: show expiry (optional)
                + " | Engine CC: "
                + engineCC;
    }
}