package service;

import model.PolicyDetail;
import model.VehicleType;

public class StandardPremiumCalculator implements PremiumCalculable {

    private static final int YOUNG_DRIVER_AGE_LIMIT = 25;
    private static final double YOUNG_DRIVER_LOADING_PERCENT = 20.0;
    private static final double EXPERIENCED_DRIVER_LOADING_PERCENT = 10.0;
    private static final int CLAIM_SURCHARGE_PER_CLAIM = 300;

    @Override
    public double calculatePremium(PolicyDetail policy) {

        int basePremium = getBasePremium(policy.getVehicleType());

        double finalPremium = basePremium;

        if (isYoungDriver(policy.getCustomer()
                .getCustomerAge())) {
            finalPremium += (basePremium * YOUNG_DRIVER_LOADING_PERCENT) / 100;
        } else {
            finalPremium += (basePremium * EXPERIENCED_DRIVER_LOADING_PERCENT) / 100;
        }

        finalPremium += policy.getPreviousClaimCount() * CLAIM_SURCHARGE_PER_CLAIM;

        return finalPremium;
    }

    private int getBasePremium(VehicleType vehicleType) {

        switch (vehicleType) {
            case BIKE:
                return 300;

            case CAR:
                return 500;

            case TRUCK:
                return 800;

            default:
                throw new IllegalArgumentException("Invalid vehicle type.");
        }
    }

    private boolean isYoungDriver(int age) {
        return age <= YOUNG_DRIVER_AGE_LIMIT;
    }
}