package service;

import model.Customer;
import model.PolicyDetail;
import model.VehicleType;

public class PolicyValidator {

    private static final int MINIMUM_AGE = 18;
    private static final int MAXIMUM_AGE = 100;

    public void validatePolicyCount(int count) {

        if (count <= 0) {

            throw new IllegalArgumentException(
                    "Number of policies must be greater than 0."
            );
        }
    }

    public void validatePolicyNumber(String policyNum) {

        if (policyNum == null ||
                policyNum.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Policy Number cannot be empty."
            );
        }

        if (!policyNum.matches("[A-Za-z0-9]+")) {

            throw new IllegalArgumentException(
                    "Policy Number must be alphanumeric."
            );
        }
    }

    public void validateAge(int age) {

        if (age < MINIMUM_AGE ||
                age > MAXIMUM_AGE) {

            throw new IllegalArgumentException(
                    "Age must be between 18 and 100."
            );
        }
    }

    public void validateVehicleType(
            VehicleType vehicleType) {

        if (vehicleType == null) {

            throw new IllegalArgumentException(
                    "Vehicle Type cannot be null."
            );
        }
    }

    public void validateClaimCount(
            int claimCount) {

        if (claimCount < 0) {

            throw new IllegalArgumentException(
                    "Claim count cannot be negative."
            );
        }
    }

    public boolean isValid(
            PolicyDetail policy) {

        try {

            validatePolicy(policy);

            return true;

        } catch (IllegalArgumentException e) {

            return false;
        }
    }

    public void validatePolicy(
            PolicyDetail policy) {

        if (policy == null) {

            throw new IllegalArgumentException(
                    "Policy cannot be null."
            );
        }

        validatePolicyNumber(
                policy.getPolicyNum()
        );

        Customer customer =
                policy.getCustomer();

        if (customer == null) {

            throw new IllegalArgumentException(
                    "Customer cannot be null."
            );
        }

        validateAge(
                customer.getCustomerAge()
        );

        validateVehicleType(
                policy.getVehicleType()
        );

        validateClaimCount(
                policy.getPreviousClaimCount()
        );
    }
}