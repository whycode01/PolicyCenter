package model;

public class TruckPolicy extends PolicyDetail {

    private double loadCapacityTons;

    public TruckPolicy(String policyNum,
                       Customer customer,
                       int previousClaimCount,
                       double loadCapacityTons) {

        super(
                policyNum,
                customer,
                VehicleType.TRUCK,
                previousClaimCount
        );

        this.loadCapacityTons = loadCapacityTons;
    }

    public TruckPolicy(String policyNum,
                       Customer customer,
                       double loadCapacityTons) {

        super(
                policyNum,
                customer,
                VehicleType.TRUCK
        );

        this.loadCapacityTons = loadCapacityTons;
    }

    public double getLoadCapacityTons() {
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
                + " | Load Capacity: "
                + loadCapacityTons
                + " tons";
    }
}