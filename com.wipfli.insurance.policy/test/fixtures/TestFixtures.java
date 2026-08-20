package fixtures;

import model.CarPolicy;
import model.BikePolicy;
import model.TruckPolicy;
import model.Customer;
import model.PolicyDetail;
import service.PolicyRegister;

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

    public static CarPolicy pol2001() {
        return new CarPolicy(
                "POL-2001",
                raviKumar(),
                1,
                LocalDate.of(2026, 8, 15),
                "KA-05-1234"
        );
    }

    public static TruckPolicy pol2002() {
        return new TruckPolicy(
                "POL-2002",
                raviKumar(),
                0,
                LocalDate.of(2026, 9, 1),
                12.5
        );
    }

    public static BikePolicy pol2003() {
        return new BikePolicy(
                "POL-2003",
                meenaIyer(),
                2,
                LocalDate.of(2026, 8, 20),
                150
        );
    }

    public static CarPolicy pol2004() {
        return new CarPolicy(
                "POL-2004",
                ajayVerma(),
                0,
                LocalDate.of(2027, 1, 10),
                "MH-12-5678"
        );
    }

    public static BikePolicy pol2005() {
        return new BikePolicy(
                "POL-2005",
                snehaRao(),
                0,
                LocalDate.of(2026, 8, 25),
                125
        );
    }

    public static CarPolicy pol2006() {
        return new CarPolicy(
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

    public static PolicyRegister seededRegister() {
        PolicyRegister register = new PolicyRegister();
        for (PolicyDetail policy : allPolicies()) {
            register.add(policy);
        }
        return register;
    }
}
