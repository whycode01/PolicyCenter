package service;

import fixtures.TestFixtures;
import model.PolicyDetail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StandardPremiumCalculatorTest {

    private StandardPremiumCalculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new StandardPremiumCalculator();
    }

    @Test
    void carPolicy_youngDriverWithOneClaim_returns750() {
        PolicyDetail policy = TestFixtures.pol2001();
        double premium = calculator.calculatePremium(policy);
        assertEquals(750.0, premium, 0.01);
    }

    @Test
    void bikePolicy_experiencedDriverWithTwoClaims_returns600() {
        PolicyDetail policy = TestFixtures.pol2003();
        double premium = calculator.calculatePremium(policy);
        assertEquals(600.0, premium, 0.01);
    }

    @Test
    void truckPolicy_youngDriverWithNoClaims_returns960() {
        PolicyDetail policy = TestFixtures.pol2002();
        double premium = calculator.calculatePremium(policy);
        assertEquals(960.0, premium, 0.01);
    }
}

