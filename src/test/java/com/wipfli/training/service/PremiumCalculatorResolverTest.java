package com.wipfli.training.service;

import com.wipfli.training.exception.InvalidPolicyDataException;
import com.wipfli.training.exception.PolicyNotFoundException;
import com.wipfli.training.model.CarPolicy;
import com.wipfli.training.model.Customer;
import com.wipfli.training.model.PolicyDetail;
import com.wipfli.training.model.VehicleType;
import com.wipfli.training.repository.PolicyRepository;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PremiumCalculatorResolverTest {

    private final StandardPremiumCalculator standardPremiumCalculator = new StandardPremiumCalculator();
    private final NoClaimBonusCalculator noClaimBonusCalculator =
            new NoClaimBonusCalculator(standardPremiumCalculator);
    private final PremiumCalculatorResolver resolver =
            new PremiumCalculatorResolver(standardPremiumCalculator, noClaimBonusCalculator);

    @Test
    void zeroClaimsSelectsNoClaimBonusCalculator() {
        PolicyDetail policy = new CarPolicy(
                "CAR-100",
                new Customer("CUST-100", "Amit", 32),
                0,
                LocalDate.now().plusDays(60),
                "MH12AB1234"
        );

        assertSame(noClaimBonusCalculator, resolver.resolve(policy));
    }

    @Test
    void oneClaimSelectsStandardCalculator() {
        PolicyDetail policy = new CarPolicy(
                "CAR-101",
                new Customer("CUST-101", "Amit", 32),
                1,
                LocalDate.now().plusDays(60),
                "MH12AB1234"
        );

        assertSame(standardPremiumCalculator, resolver.resolve(policy));
    }

    @Test
    void decoratorAppliesDiscountOnceForZeroClaims() {
        PolicyDetail policy = new CarPolicy(
                "CAR-102",
                new Customer("CUST-102", "Amit", 32),
                0,
                LocalDate.now().plusDays(60),
                "MH12AB1234"
        );

        double expected = standardPremiumCalculator.calculatePremium(policy);
        double actual = noClaimBonusCalculator.calculatePremium(policy);

        assertEquals(expected - (expected * 10.0 / 100.0), actual, 0.0001);
    }

    @Test
    void nullPolicyIsRejectedExplicitly() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> resolver.resolve(null)
        );

        assertEquals("Policy cannot be null.", exception.getMessage());
    }

    @Test
    void incompletePolicyDataIsRejectedExplicitly() {
        PolicyDetail policy = new CarPolicy(
                null,
                new Customer("CUST-NULL", "Amit", 32),
                0,
                LocalDate.now().plusDays(60),
                "MH12AB1234"
        );

        InvalidPolicyDataException exception = assertThrows(
                InvalidPolicyDataException.class,
                () -> resolver.resolve(policy)
        );

        assertEquals("Policy number cannot be blank.", exception.getMessage());
    }

    @Test
    void policyRegisterUsesResolverReturnedCalculator() {
        PremiumCalculatorResolver resolverMock = mock(PremiumCalculatorResolver.class);
        PolicyRepository repository = mock(PolicyRepository.class);
        PremiumCalculable selectedCalculator = mock(PremiumCalculable.class);

        PolicyDetail policy = new CarPolicy(
                "CAR-103",
                new Customer("CUST-103", "Amit", 32),
                0,
                LocalDate.now().plusDays(60),
                "MH12AB1234"
        );

        when(repository.findByVehicleType(VehicleType.CAR)).thenReturn(Collections.singletonList(policy));
        when(resolverMock.resolve(policy)).thenReturn(selectedCalculator);
        when(selectedCalculator.calculatePremium(policy)).thenReturn(450.0);

        PolicyRegister policyRegister = new PolicyRegister(resolverMock, repository);

        assertEquals(450.0, policyRegister.totalPremiumForVehicleType(VehicleType.CAR));
        verify(resolverMock).resolve(policy);
    }

    @Test
    void missingPolicyNumberProducesNotFoundException() {
        PolicyRepository repository = mock(PolicyRepository.class);
        when(repository.findById("MISSING-123")).thenReturn(Optional.empty());
        PolicyRegister policyRegister = new PolicyRegister(resolver, repository);

        PolicyNotFoundException exception = assertThrows(
                PolicyNotFoundException.class,
                () -> policyRegister.findByNumber("MISSING-123")
        );

        assertEquals("MISSING-123", exception.getPolicyNumber());
    }
}
