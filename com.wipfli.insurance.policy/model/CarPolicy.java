package model;

public class CarPolicy extends PolicyDetail {

    private String registrationNumber;

    public CarPolicy(String policyNum,
                     Customer customer,
                     int previousClaimCount,
                     String registrationNumber) {

        super(
                policyNum,
                customer,
                VehicleType.CAR,
                previousClaimCount
        );

        this.registrationNumber = registrationNumber;
    }


    public CarPolicy(String policyNum,
                     Customer customer,
                     String registrationNumber) {

        super(
                policyNum,
                customer,
                VehicleType.CAR
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
                + " | Registration: "
                + registrationNumber;
    }
}