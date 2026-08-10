package model;

import java.time.LocalDate;   // CHANGE: needed for expiryDate

public class CarPolicy extends PolicyDetail {

    private String registrationNumber;

    // Constructor WITH claims history
    public CarPolicy(String policyNum,
                     Customer customer,
                     int previousClaimCount,
                     LocalDate expiryDate,
                     String registrationNumber) {

        super(
                policyNum,
                customer,
                VehicleType.CAR,
                previousClaimCount,
                expiryDate
        );

        this.registrationNumber = registrationNumber;
    }

    // Overloaded constructor WITHOUT claims history (assumes 0 claims)
    public CarPolicy(String policyNum,
                     Customer customer,
                     LocalDate expiryDate,
                     String registrationNumber) {

        super(
                policyNum,
                customer,
                VehicleType.CAR,
                expiryDate
        );

        this.registrationNumber = registrationNumber;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
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
                + getExpiryDate()          // CHANGE: show expiry date
                + " | Registration: "
                + registrationNumber;
    }
}