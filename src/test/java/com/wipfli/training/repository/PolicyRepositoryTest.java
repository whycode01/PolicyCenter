package com.wipfli.training.repository;

import com.wipfli.training.exception.IllegalStatusChangeException;
import com.wipfli.training.model.BikePolicy;
import com.wipfli.training.model.CarPolicy;
import com.wipfli.training.model.Customer;
import com.wipfli.training.model.PolicyDetail;
import com.wipfli.training.model.PolicyStatus;
import com.wipfli.training.model.TruckPolicy;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
class PolicyRepositoryTest {

    // Fixed reference date - never LocalDate.now(), so the tests are repeatable.
    private static final LocalDate REF = LocalDate.of(2026, 8, 9);

    @Autowired
    private PolicyRepository repository;

    @Autowired
    private TestEntityManager em;

    // flush() forces Hibernate to run the INSERT/UPDATE against the database NOW,
    // and clear() empties the first-level (persistence context) cache.
    // So every findById / query below is a REAL SELECT from the database,
    // not Hibernate handing back the same in-memory object we just saved.
    private void flushAndClear() {
        em.flush();
        em.clear();
    }

    private Customer amit() {
        return new Customer("CUST-001", "Amit", 32);
    }

    private Customer neha() {
        return new Customer("CUST-002", "Neha", 28);
    }

    @Test
    void eachPolicyTypeIsReadBackAsItsOwnSubclassWithItsOwnField() {
        repository.save(new CarPolicy("CAR-1", amit(), 0, REF.plusDays(60), "MH12AB1234"));
        repository.save(new BikePolicy("BIKE-1", amit(), 0, REF.plusDays(60), 150));
        repository.save(new TruckPolicy("TRUCK-1", amit(), 0, REF.plusDays(60), 12.5));
        flushAndClear();

        PolicyDetail car = repository.findById("CAR-1").orElseThrow();
        PolicyDetail bike = repository.findById("BIKE-1").orElseThrow();
        PolicyDetail truck = repository.findById("TRUCK-1").orElseThrow();

        assertThat(car).isInstanceOf(CarPolicy.class);
        assertThat(((CarPolicy) car).getRegistrationNumber()).isEqualTo("MH12AB1234");

        assertThat(bike).isInstanceOf(BikePolicy.class);
        assertThat(((BikePolicy) bike).getEngineCC()).isEqualTo(150);

        assertThat(truck).isInstanceOf(TruckPolicy.class);
        assertThat(((TruckPolicy) truck).getLoadCapacityTons()).isEqualTo(12.5);
    }

    @Test
    void expiredStatusSurvivesReload() {
        PolicyDetail policy = new CarPolicy("CAR-EXP", amit(), 0, REF.plusDays(60), "MH12AB1234");
        policy.expire();
        PolicyDetail saved = repository.save(policy);
        flushAndClear();

        PolicyDetail loaded = repository.findById("CAR-EXP").orElseThrow();

        assertThat(loaded.getStatus()).isEqualTo(PolicyStatus.EXPIRED);
        // A different Java object than the one we saved -> it really came from the DB.
        assertThat(loaded).isNotSameAs(saved);
    }

    @Test
    void expiredPolicyReadBackStillRefusesRenew() {
        PolicyDetail policy = new CarPolicy("CAR-EXP2", amit(), 0, REF.plusDays(60), "MH12AB1234");
        policy.expire();
        repository.save(policy);
        flushAndClear();

        PolicyDetail loaded = repository.findById("CAR-EXP2").orElseThrow();

        assertThatThrownBy(() -> loaded.renew(REF))
                .isInstanceOf(IllegalStatusChangeException.class);
    }

    @Test
    void recordedClaimSurvivesReload() {
        PolicyDetail policy = new CarPolicy("CAR-CLAIM", amit(), 0, REF.plusDays(60), "MH12AB1234");
        policy.recordClaim();
        repository.save(policy);
        flushAndClear();

        PolicyDetail loaded = repository.findById("CAR-CLAIM").orElseThrow();

        assertThat(loaded.getPreviousClaimCount()).isEqualTo(1);
    }

    @Test
    void statusColumnHoldsTextNotANumber() {
        PolicyDetail policy = new CarPolicy("CAR-TXT", amit(), 0, REF.plusDays(60), "MH12AB1234");
        policy.expire();
        repository.save(policy);
        em.flush();   // flush only - the native query reads the raw column value

        Object value = em.getEntityManager()
                .createNativeQuery("SELECT status FROM policy WHERE policy_num = ?1")
                .setParameter(1, "CAR-TXT")
                .getSingleResult();

        assertThat(value).isNotInstanceOf(Number.class);
        assertThat(value.toString()).isEqualTo("EXPIRED");
    }

    @Test
    void findByCustomerCustomerNameReturnsOnlyThatCustomersPolicies() {
        repository.save(new CarPolicy("CAR-A1", amit(), 0, REF.plusDays(60), "REG-A1"));
        repository.save(new CarPolicy("CAR-A2", amit(), 0, REF.plusDays(60), "REG-A2"));
        repository.save(new CarPolicy("CAR-N1", neha(), 0, REF.plusDays(60), "REG-N1"));
        flushAndClear();

        List<PolicyDetail> amits = repository.findByCustomerCustomerName("Amit");

        assertThat(amits)
                .extracting(PolicyDetail::getPolicyNum)
                .containsExactlyInAnyOrder("CAR-A1", "CAR-A2");
    }

    @Test
    void findExpiringBetweenRespectsExclusiveFromAndInclusiveTo() {
        LocalDate from = REF;
        LocalDate to = REF.plusDays(30);

        // expiry == from  -> EXCLUDED (query is expiryDate > from)
        repository.save(new CarPolicy("EXP-ON-FROM", amit(), 0, from, "REG-1"));
        // expiry == from+1 -> INCLUDED
        repository.save(new CarPolicy("EXP-AFTER-FROM", amit(), 0, from.plusDays(1), "REG-2"));
        // expiry == to    -> INCLUDED (query is expiryDate <= to)
        repository.save(new CarPolicy("EXP-ON-TO", amit(), 0, to, "REG-3"));
        // expiry == to+1  -> EXCLUDED
        repository.save(new CarPolicy("EXP-AFTER-TO", amit(), 0, to.plusDays(1), "REG-4"));
        flushAndClear();

        List<PolicyDetail> result = repository.findExpiringBetween(from, to);

        // containsExactly also proves the ORDER BY expiryDate (from+1 before to).
        assertThat(result)
                .extracting(PolicyDetail::getPolicyNum)
                .containsExactly("EXP-AFTER-FROM", "EXP-ON-TO");
    }

    @Test
    void savingSamePolicyNumberTwiceOverwritesBecauseSaveCallsMerge() {
        repository.save(new CarPolicy("DUP-1", amit(), 0, REF.plusDays(60), "FIRST-REG"));
        flushAndClear();

        // A DIFFERENT policy object, but the SAME policy number.
        repository.save(new CarPolicy("DUP-1", neha(), 0, REF.plusDays(60), "SECOND-REG"));
        flushAndClear();

        // save() with an assigned @Id calls merge(), and merge() UPDATES the existing row
        // instead of failing. So the second save silently overwrote the first.
        // This is exactly why PolicyRegister.add() must call existsById() itself
        // to return 409 Conflict instead of letting a duplicate overwrite data.
        assertThat(repository.count()).isEqualTo(1);

        PolicyDetail loaded = repository.findById("DUP-1").orElseThrow();
        assertThat(((CarPolicy) loaded).getRegistrationNumber()).isEqualTo("SECOND-REG");
        assertThat(loaded.getCustomer().getCustomerName()).isEqualTo("Neha");
    }
}