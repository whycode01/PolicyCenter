package com.wipfli.training.service;

import com.wipfli.training.exception.InvalidPolicyDataException;
import com.wipfli.training.model.PolicyDetail;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class PremiumCalculatorResolver {

    private final PremiumCalculable standardPremiumCalculator;
    private final PremiumCalculable noClaimBonusCalculator;

    public PremiumCalculatorResolver(
            @Qualifier("standardPremiumCalculator") PremiumCalculable standardPremiumCalculator,
            @Qualifier("noClaimBonusCalculator") PremiumCalculable noClaimBonusCalculator) {

        this.standardPremiumCalculator = standardPremiumCalculator;
        this.noClaimBonusCalculator = noClaimBonusCalculator;
    }

    public PremiumCalculable resolve(PolicyDetail policy) {

        if (policy == null) {
            throw new IllegalArgumentException("Policy cannot be null.");
        }

        if (policy.getCustomer() == null) {
            throw new IllegalArgumentException("Policy customer cannot be null.");
        }

        if (policy.getPolicyNum() == null || policy.getPolicyNum().trim().isEmpty()) {
            throw new InvalidPolicyDataException(
                    null,
                    "Policy number cannot be blank."
            );
        }

        if (policy.getPreviousClaimCount() == 0) {
            return noClaimBonusCalculator;
        }

        return standardPremiumCalculator;
    }
}
