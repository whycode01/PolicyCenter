package fixtures;

import model.PolicyDetail;
import model.Customer;
import service.PolicyRegister;
import service.PolicyFactory;
import service.PremiumCalculable;
import service.StandardPremiumCalculator;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

/**
 * Fixed, repeatable test dataset from Assignment 8.
 * REFERENCE_DATE is used for all expiry-relative queries so tests never depend on LocalDate.now().
 */
public final class TestFixtures {

    public static final LocalDate REFERENCE_DATE = LocalDate.of(2026, 8, 9);

    private TestFixtures() {
        // utility class
    }

    public static Customer raviKumar() {
        return new Customer("Ravi Kumar", 22);
    }

    public static Customer meenaIyer() {
        return new Customer("Meena Iyer", 34);
    }

    public static Customer ajayVerma() {
        return new Customer("Ajay Verma", 41);
    }

    public static Customer snehaRao() {
        return new Customer("Sneha Rao", 29);
    }

    public static PolicyDetail pol2001() {
        return PolicyFactory.createCarPolicy(
                "POL-2001",
                raviKumar(),
                1,
                LocalDate.of(2026, 8, 15),
                "KA-05-1234"
        );
    }

    public static PolicyDetail pol2002() {
        return PolicyFactory.createTruckPolicy(
                "POL-2002",
                raviKumar(),
                0,
                LocalDate.of(2026, 9, 1),
                12.5
        );
    }

    public static PolicyDetail pol2003() {
        return PolicyFactory.createBikePolicy(
                "POL-2003",
                meenaIyer(),
                2,
                LocalDate.of(2026, 8, 20),
                150
        );
    }

    public static PolicyDetail pol2004() {
        return PolicyFactory.createCarPolicy(
                "POL-2004",
                ajayVerma(),
                0,
                LocalDate.of(2027, 1, 10),
                "MH-12-5678"
        );
    }

    public static PolicyDetail pol2005() {
        return PolicyFactory.createBikePolicy(
                "POL-2005",
                snehaRao(),
                0,
                LocalDate.of(2026, 8, 25),
                125
        );
    }

    public static PolicyDetail pol2006() {
        return PolicyFactory.createCarPolicy(
                "POL-2006",
                raviKumar(),
                3,
                LocalDate.of(2026, 12, 1),
                "TN-09-9012"
        );
    }

    public static List<PolicyDetail> allPolicies() {
        return Arrays.asList(
                pol2001(),
                pol2002(),
                pol2003(),
                pol2004(),
                pol2005(),
                pol2006()
        );
    }

    public static PolicyRegister seededRegister(PremiumCalculable calculator) {
        PolicyRegister register = new PolicyRegister(calculator);
        for (PolicyDetail policy : allPolicies()) {
            register.add(policy);
        }
        return register;
    }

    public static PolicyRegister seededRegister() {
        return seededRegister(new StandardPremiumCalculator());
    }
}
