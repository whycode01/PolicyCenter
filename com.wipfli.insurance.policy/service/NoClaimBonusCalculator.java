package service;

import model.PolicyDetail;

public class NoClaimBonusCalculator implements PremiumCalculable {

    private static final double NO_CLAIM_DISCOUNT_PERCENT = 10.0;

    private final PremiumCalculable wrappedCalculator;

    public NoClaimBonusCalculator(PremiumCalculable wrappedCalculator) {
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