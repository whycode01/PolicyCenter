package service;

import exception.DuplicatePolicyNumberException;
import exception.InvalidPolicyDataException;
import exception.PolicyBusinessException;
import exception.PolicyNotFoundException;
import fixtures.TestFixtures;
import model.PolicyDetail;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Proves the exception hierarchy from Assignment 6:
 * - PolicyBusinessException is the unchecked base for data/business rule violations.
 * - PolicyNotFoundException is a checked exception for missing lookups.
 */
class ExceptionHierarchyTest {

    @Test
    void invalidPolicyDataException_isUncheckedPolicyBusinessException() {
        InvalidPolicyDataException exception = assertThrows(
                InvalidPolicyDataException.class,
                () -> new PolicyValidator().validatePolicy(null)
        );

        assertTrue(exception instanceof PolicyBusinessException);
    }

    @Test
    void duplicatePolicyNumberException_isUncheckedPolicyBusinessException() {
        PolicyRegister register = TestFixtures.seededRegister();
        PolicyDetail duplicate = TestFixtures.pol2001();

        DuplicatePolicyNumberException exception = assertThrows(
                DuplicatePolicyNumberException.class,
                () -> register.add(duplicate)
        );

        assertTrue(exception instanceof PolicyBusinessException);
    }

    @Test
    void missingLookup_throwsCheckedPolicyNotFoundException() {
        PolicyRegister register = TestFixtures.seededRegister();

        assertThrows(PolicyNotFoundException.class,
                () -> register.findByNumber("POL-0000"));
    }
}

