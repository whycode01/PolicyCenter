package com.wipfli.training.service;

import com.wipfli.training.model.PolicyDetail;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("noClaimBonusCalculator")
public class NoClaimBonusCalculator implements PremiumCalculable {

    private static final double NO_CLAIM_DISCOUNT_PERCENT = 10.0;

    private final PremiumCalculable wrappedCalculator;

    public NoClaimBonusCalculator(@Qualifier("standardPremiumCalculator") PremiumCalculable wrappedCalculator) {
        this.wrappedCalculator = wrappedCalculator;
    }

    @Override
    public double calculatePremium(PolicyDetail policy) {

        double finalPremium = wrappedCalculator.calculatePremium(policy);

        if (policy.getPreviousClaimCount() == 0) {
            finalPremium -= (finalPremium * NO_CLAIM_DISCOUNT_PERCENT) / 100;
        }

        return finalPremium;
    }
}