package model;

public class BikePolicy extends PolicyDetail {

    private int engineCC;

    public BikePolicy(String policyNum,
                      Customer customer,
                      int previousClaimCount,
                      int engineCC) {

        super(
                policyNum,
                customer,
                VehicleType.BIKE,
                previousClaimCount
        );

        this.engineCC = engineCC;
    }

    public BikePolicy(String policyNum,
                      Customer customer,
                      int engineCC) {

        super(
                policyNum,
                customer,
                VehicleType.BIKE
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
                + " | Engine CC: "
                + engineCC;
    }
}