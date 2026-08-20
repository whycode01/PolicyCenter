package service;

import fixtures.TestFixtures;
import model.PolicyDetail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NoClaimBonusCalculatorTest {

    private NoClaimBonusCalculator calculator;
    private StandardPremiumCalculator standardCalculator;

    @BeforeEach
    void setUp() {
        calculator = new NoClaimBonusCalculator();
        standardCalculator = new StandardPremiumCalculator();
    }

    @Test
    void policyWithNoClaims_appliesTenPercentDiscountOnTopOfStandardCalculation() {
        PolicyDetail policy = TestFixtures.pol2004();
        double standardPremium = standardCalculator.calculatePremium(policy);

        double premium = calculator.calculatePremium(policy);

        // 10% discount applied to the standard premium, not a replacement
        double expected = standardPremium * 0.90;
        assertEquals(expected, premium, 0.01);
    }

    @Test
    void policyWithClaims_doesNotApplyDiscount() {
        PolicyDetail policy = TestFixtures.pol2001();
        double standardPremium = standardCalculator.calculatePremium(policy);

        double premium = calculator.calculatePremium(policy);

        assertEquals(standardPremium, premium, 0.01);
    }

    @Test
    void youngDriverWithNoClaims_getsDiscountOnLoadedPremium() {
        PolicyDetail policy = TestFixtures.pol2002();
        double standardPremium = standardCalculator.calculatePremium(policy);

        double premium = calculator.calculatePremium(policy);

        double expected = standardPremium * 0.90;
        assertEquals(expected, premium, 0.01);
    }
}

