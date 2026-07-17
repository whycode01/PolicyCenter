package service;

import model.PolicyDetail;

public class NoClaimBonusCalculator implements PremiumCalculable {

    private static final double NO_CLAIM_DISCOUNT_PERCENT = 10.0;

    @Override
    public double calculatePremium(PolicyDetail policy) {

        PremiumCalculable standardCalculator = new StandardPremiumCalculator();

        double finalPremium = standardCalculator.calculatePremium(policy);

        if (policy.getPreviousClaimCount() == 0) {
            finalPremium -= (finalPremium * NO_CLAIM_DISCOUNT_PERCENT) / 100;
        }

        return finalPremium;
    }
}