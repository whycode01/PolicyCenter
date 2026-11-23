package service;

import exception.DuplicatePolicyNumberException;
import exception.PolicyBusinessException;
import exception.PolicyNotFoundException;
import fixtures.TestFixtures;
import model.PolicyDetail;
import model.VehicleType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PolicyRegisterTest {

    private PolicyRegister register;

    @BeforeEach
    void setUp() {
        register = TestFixtures.seededRegister();
    }

    @Test
    void findByPolicyNumber_hitAndMiss_worksWithOptional() {
        Optional<PolicyDetail> hit = register.findByPolicyNumber("POL-2001");
        Optional<PolicyDetail> miss = register.findByPolicyNumber("POL-9999");


        assertTrue(hit.isPresent());
        assertEquals("POL-2001", hit.get().getPolicyNum());
        assertTrue(miss.isEmpty());
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
        assertEquals(3, expiring.size());
        assertEquals("POL-2001", expiring.get(0).getPolicyNum());
        assertEquals("POL-2003", expiring.get(1).getPolicyNum());
        assertEquals("POL-2005", expiring.get(2).getPolicyNum());
    }

    @Test
    void exceptionHierarchy_duplicateNumberIsUnchecked_and_missingNumberIsChecked() {
        assertThrows(PolicyBusinessException.class,
                () -> register.add(TestFixtures.pol2001()));
        assertThrows(PolicyNotFoundException.class,
                () -> register.findByNumber("POL-9999"));
    }
}
