package com.wipfli.training.service;

import com.wipfli.training.exception.InvalidPolicyDataException;
import com.wipfli.training.model.Customer;
import com.wipfli.training.model.PolicyDetail;
import com.wipfli.training.model.VehicleType;

public class PolicyValidator {

    private static final int MINIMUM_AGE = 18;
    private static final int MAXIMUM_AGE = 100;

    public void validatePolicyCount(int count) {

        if (count <= 0) {

            // CHANGE: policy number unknown here, so pass null
            throw new InvalidPolicyDataException(
                    null,
                    "number of policies must be greater than 0"
            );
        }
    }

    public void validatePolicyNumber(String policyNum) {

        if (policyNum == null || policyNum.trim().isEmpty()) {

            throw new InvalidPolicyDataException(
                    policyNum,
                    "policy number cannot be empty"
            );
        }

        if (!policyNum.matches("[A-Za-z0-9-]+")) {

            throw new InvalidPolicyDataException(
                    policyNum,
                    "policy number must be alphanumeric"
            );
        }
    }

    public void validateAge(int age) {

        if (age < MINIMUM_AGE || age > MAXIMUM_AGE) {

            throw new InvalidPolicyDataException(
                    null,
                    "age must be between " + MINIMUM_AGE
                            + " and " + MAXIMUM_AGE
                            + ", got " + age
            );
        }
    }

    public void validateVehicleType(VehicleType vehicleType) {

        if (vehicleType == null) {

            throw new InvalidPolicyDataException(
                    null,
                    "vehicle type cannot be null"
            );
        }
    }

    public void validateClaimCount(int claimCount) {

        if (claimCount < 0) {

            throw new InvalidPolicyDataException(
                    null,
                    "claim count cannot be negative, got " + claimCount
            );
        }
    }

    // CHANGE: isValid() boolean method REMOVED
    // A boolean tells the caller *that* something failed, not *why*.
    // Throwing carries the reason + policy number all the way up.

    public void validatePolicy(PolicyDetail policy) {

        if (policy == null) {

            throw new InvalidPolicyDataException(
                    null,
                    "policy cannot be null"
            );
        }

        validatePolicyNumber(policy.getPolicyNum());

        Customer customer = policy.getCustomer();

        if (customer == null) {

            throw new InvalidPolicyDataException(
                    policy.getPolicyNum(),
                    "customer cannot be null"
            );
        }

        validateAge(customer.getCustomerAge());

        validateVehicleType(policy.getVehicleType());

        validateClaimCount(policy.getPreviousClaimCount());
    }
}