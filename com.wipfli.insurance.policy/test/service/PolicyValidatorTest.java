package service;

import exception.InvalidPolicyDataException;
import fixtures.TestFixtures;
import model.CarPolicy;
import model.Customer;
import model.PolicyDetail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PolicyValidatorTest {

    private PolicyValidator validator;

    @BeforeEach
    void setUp() {
        validator = new PolicyValidator();
    }

    @Test
    void validPolicy_passesWithoutThrowing() {
        PolicyDetail policy = TestFixtures.pol2004();
        assertDoesNotThrow(() -> validator.validatePolicy(policy));
    }

    @Test
    void nullPolicy_throwsInvalidPolicyDataException() {
        assertThrows(InvalidPolicyDataException.class,
                () -> validator.validatePolicy(null));
    }

    @Test
    void emptyPolicyNumber_throwsInvalidPolicyDataException() {
        PolicyDetail policy = new CarPolicy(
                "   ",
                new Customer("Test", 30),
                0,
                LocalDate.of(2026, 12, 1),
                "REG-123"
        );
        assertThrows(InvalidPolicyDataException.class,
                () -> validator.validatePolicy(policy));
    }
}
