package com.wipfli.training.model;

import java.time.LocalDate;

public class BikePolicy extends PolicyDetail {

    private int engineCC;

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

    public int getEngineCC() {
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