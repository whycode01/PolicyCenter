package service;

import exception.DuplicatePolicyNumberException;
import exception.PolicyBusinessException;
import exception.PolicyNotFoundException;
import fixtures.TestFixtures;
import model.PolicyDetail;
import model.VehicleType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PolicyRegisterTest {

    private PolicyRegister register;

    @BeforeEach
    void setUp() {
        register = TestFixtures.seededRegister();
    }

    @Test
    void findByPolicyNumber_existingNumber_returnsPolicy() {
        Optional<PolicyDetail> found = register.findByPolicyNumber("POL-2001");

        assertTrue(found.isPresent());
        assertEquals("POL-2001", found.get().getPolicyNum());
    }

    @Test
    void findByPolicyNumber_missingNumber_returnsEmptyOptional() {
        Optional<PolicyDetail> found = register.findByPolicyNumber("POL-9999");

        assertTrue(found.isEmpty());
    }

    @Test
    void countPoliciesByVehicleType_groupsFixedDatasetCorrectly() {
        Map<VehicleType, Long> counts = register.countPoliciesByVehicleType();

        assertEquals(3L, counts.get(VehicleType.CAR));
        assertEquals(2L, counts.get(VehicleType.BIKE));
        assertEquals(1L, counts.get(VehicleType.TRUCK));
    }

    @Test
    void policiesExpiringWithin16DaysOfReferenceDate_returnsExpectedPolicies() {
        List<PolicyDetail> expiring = register.policiesExpiringWithin(16, TestFixtures.REFERENCE_DATE);

        // Expires between 2026-08-10 and 2026-08-25 inclusive:
        // POL-2001 (2026-08-15), POL-2003 (2026-08-20), POL-2005 (2026-08-25)
        assertEquals(3, expiring.size());
        assertEquals("POL-2001", expiring.get(0).getPolicyNum());
        assertEquals("POL-2003", expiring.get(1).getPolicyNum());
        assertEquals("POL-2005", expiring.get(2).getPolicyNum());
    }

    @Test
    void policiesExpiringWithinZeroDaysOfReferenceDate_returnsEmptyList() {
        List<PolicyDetail> expiring = register.policiesExpiringWithin(0, TestFixtures.REFERENCE_DATE);

        assertTrue(expiring.isEmpty());
    }

    @Test
    void addDuplicatePolicyNumber_throwsDuplicatePolicyNumberException() {
        PolicyDetail duplicate = TestFixtures.pol2001();

        assertThrows(DuplicatePolicyNumberException.class,
                () -> register.add(duplicate));
    }

    @Test
    void findByNumber_missingPolicy_throwsCheckedPolicyNotFoundException() {
        assertThrows(PolicyNotFoundException.class,
                () -> register.findByNumber("POL-9999"));
    }

    @Test
    void findByNumber_existingPolicy_returnsPolicy() throws PolicyNotFoundException {
        PolicyDetail found = register.findByNumber("POL-2002");

        assertEquals("POL-2002", found.getPolicyNum());
    }

    @Test
    void totalPremiumForVehicleType_carUsesCalculator() {
        StandardPremiumCalculator calculator = new StandardPremiumCalculator();

        double total = register.totalPremiumForVehicleType(VehicleType.CAR, calculator);

        // POL-2001 = 750, POL-2004 = 500, POL-2006 = 1050
        assertEquals(2300.0, total, 0.01);
    }

    @Test
    void findByCustomerName_returnsAllPoliciesForThatCustomer() {
        List<PolicyDetail> raviPolicies = register.findByCustomerName("Ravi Kumar");

        assertEquals(3, raviPolicies.size());
        assertTrue(raviPolicies.stream().allMatch(p -> p.getCustomer().getCustomerName().equals("Ravi Kumar")));
    }
}

