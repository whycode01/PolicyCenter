package service;

import fixtures.TestFixtures;
import model.BikePolicy;
import model.CarPolicy;
import model.Customer;
import model.PolicyDetail;
import model.TruckPolicy;
import model.VehicleType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test list for StandardPremiumCalculator.calculatePremium(PolicyDetail):
 *
 * 1. carPolicy_youngDriverWithOneClaim_returnsBasePlusSurchargeAndClaims
 * 2. bikePolicy_experiencedDriverWithTwoClaims_returnsBasePlusClaimsOnly
 * 3. truckPolicy_youngDriverWithNoClaims_returnsBasePlusSurcharge
 * 4. carPolicy_experiencedDriverWithZeroClaims_returnsBaseOnly
 * 5. bikePolicy_youngDriverWithZeroClaims_returnsBasePlusSurcharge
 * 6. carPolicy_multipleClaims_addsPerClaimSurcharge
 */
class StandardPremiumCalculatorTest {

    private StandardPremiumCalculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new StandardPremiumCalculator();
    }

    @Test
    void carPolicy_youngDriverWithOneClaim_returnsBasePlusSurchargeAndClaims() {
        PolicyDetail policy = TestFixtures.pol2001();

        double premium = calculator.calculatePremium(policy);

        // CAR base = 500; young driver (age 22) +20% = 100; 1 claim * 150 = 150
        assertEquals(750.0, premium, 0.01);
    }

    @Test
    void bikePolicy_experiencedDriverWithTwoClaims_returnsBasePlusClaimsOnly() {
        PolicyDetail policy = TestFixtures.pol2003();

        double premium = calculator.calculatePremium(policy);

        // BIKE base = 300; age 34 -> 0% loading; 2 claims * 150 = 300
        assertEquals(600.0, premium, 0.01);
    }

    @Test
    void truckPolicy_youngDriverWithNoClaims_returnsBasePlusSurcharge() {
        PolicyDetail policy = TestFixtures.pol2002();

        double premium = calculator.calculatePremium(policy);

        // TRUCK base = 800; young driver (age 22) +20% = 160; 0 claims
        assertEquals(960.0, premium, 0.01);
    }

    @Test
    void carPolicy_experiencedDriverWithZeroClaims_returnsBaseOnly() {
        PolicyDetail policy = TestFixtures.pol2004();

        double premium = calculator.calculatePremium(policy);

        // CAR base = 500; age 41 -> 0% loading; 0 claims
        assertEquals(500.0, premium, 0.01);
    }

    @Test
    void bikePolicy_youngDriverWithZeroClaims_returnsBasePlusSurcharge() {
        Customer youngRider = new Customer("Young Rider", 25);
        BikePolicy policy = new BikePolicy(
                "POL-BIKE-YOUNG",
                youngRider,
                0,
                LocalDate.of(2026, 9, 1),
                110
        );

        double premium = calculator.calculatePremium(policy);

        // BIKE base = 300; age 25 -> +20% = 60; 0 claims
        assertEquals(360.0, premium, 0.01);
    }

    @Test
    void carPolicy_multipleClaims_addsPerClaimSurcharge() {
        PolicyDetail policy = TestFixtures.pol2006();

        double premium = calculator.calculatePremium(policy);

        // CAR base = 500; young driver (age 22) +20% = 100; 3 claims * 150 = 450
        assertEquals(1050.0, premium, 0.01);
    }
}

