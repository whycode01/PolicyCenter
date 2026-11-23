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
        standardCalculator = new StandardPremiumCalculator();
        calculator = new NoClaimBonusCalculator(standardCalculator);
    }

    @Test
    void policyWithNoClaims_appliesTenPercentDiscountOnTopOfStandardCalculation() {
        PolicyDetail policy = TestFixtures.pol2004();
        double standardPremium = standardCalculator.calculatePremium(policy);
        double premium = calculator.calculatePremium(policy);
        assertEquals(standardPremium * 0.90, premium, 0.01);
    }

    @Test
    void policyWithClaims_doesNotApplyDiscount() {
        PolicyDetail policy = TestFixtures.pol2001();
        double standardPremium = standardCalculator.calculatePremium(policy);
        double premium = calculator.calculatePremium(policy);
        assertEquals(standardPremium, premium, 0.01);
    }
}
