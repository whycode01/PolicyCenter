package app;

import model.Customer;
import model.PolicyDetail;
import service.PolicyRegister;
import service.PolicyFactory;
import service.PremiumCalculable;
import service.StandardPremiumCalculator;

import exception.PolicyBusinessException;
import exception.PolicyNotFoundException;
import exception.InvalidPolicyDataException;

import java.time.LocalDate;

public class ExceptionDemo {

    public static void main(String[] args) {

        PremiumCalculable calculator = new StandardPremiumCalculator();
        PolicyRegister register = new PolicyRegister(calculator);

        // Injected "today" so renewal-window results are reproducible,
        // independent of the real system date.
        LocalDate today = LocalDate.of(2026, 7, 25);

        PolicyDetail carPolicy = null;

        System.out.println("--- Creating policies ---");

        // 1) A GOOD policy: created, registered, and priced
        try {

            carPolicy = PolicyFactory.createCarPolicy(
                    "POL-1001",
                    new Customer("Ravi", 22),
                    1,                              // claims history
                    LocalDate.of(2026, 9, 30),      // expiry (future)
                    "KA-05-1234"
            );

            register.add(carPolicy);                // register on success

            System.out.println(carPolicy.getPolicyDetails());
            System.out.println("Premium: " + calculator.calculatePremium(carPolicy));

        } catch (PolicyBusinessException e) {
            printRefusal(e);
        } finally {
            System.out.println("AUDIT: create POL-1001 -> "
                    + (carPolicy != null ? "OK" : "FAILED"));
        }

        // 2) A 19-year-old TRUCK driver: refused (truck-only rule)
        try {

            PolicyDetail badTruck = PolicyFactory.createTruckPolicy(
                    "POL-1002",
                    new Customer("Imran", 19),
                    0,
                    LocalDate.of(2026, 12, 1),
                    8.0
            );

            register.add(badTruck);
            System.out.println(badTruck.getPolicyDetails());
            System.out.println("AUDIT: create POL-1002 -> OK");

        } catch (PolicyBusinessException e) {
            printRefusal(e);
            System.out.println("AUDIT: create POL-1002 -> FAILED");
        }

        // ---------------------------------------------------------
        System.out.println("\n--- Register ---");

        System.out.println("POL-1001 registered");   // already added above

        // 3) Duplicate policy number: refused
        try {
            register.add(carPolicy);   // same number again
        } catch (PolicyBusinessException e) {
            printRefusal(e);
        }

        // 4) Lookup a number that does not exist: refused (checked exception)
        try {
            register.findByNumber("POL-9999");
        } catch (PolicyNotFoundException e) {
            System.out.println("REFUSED [" + e.getClass().getSimpleName()
                    + "] " + e.getPolicyNumber() + ": " + e.getMessage());
        }

        // ---------------------------------------------------------
        System.out.println("\n--- Renewal ---");

        // 5) Renew too early: refused, says how many days early
        try {
            System.out.println("renew POL-1001 on " + today);
            carPolicy.renew(today);                 // opens 2026-08-31
        } catch (PolicyBusinessException e) {
            printRefusal(e);
        } finally {
            System.out.println("AUDIT: renew POL-1001 -> FAILED");
        }

        // 6) Renew with 3+ claims: refused -> underwriting
        carPolicy.recordClaim();
        carPolicy.recordClaim();
        System.out.println("recordClaim() x2 -> claims is now "
                + carPolicy.getPreviousClaimCount());

        try {
            LocalDate inWindow = LocalDate.of(2026, 9, 15);   // within 30-day window
            System.out.println("renew POL-1001 on " + inWindow);
            carPolicy.renew(inWindow);
        } catch (PolicyBusinessException e) {
            printRefusal(e);
        } finally {
            System.out.println("AUDIT: renew POL-1001 -> FAILED");
        }

        // ---------------------------------------------------------
        System.out.println("\n--- Status ---");

        // 7) Expire then try to renew: refused (illegal status change)
        carPolicy.expire();
        System.out.println("expire() -> status is now " + carPolicy.getStatus());

        try {
            carPolicy.renew(LocalDate.of(2026, 9, 15));
        } catch (PolicyBusinessException e) {
            printRefusal(e);
        } finally {
            System.out.println("AUDIT: renew POL-1001 -> FAILED");
        }

        // ---------------------------------------------------------
        // BONUS: exception chaining demo (Step 12)
        System.out.println("\n--- Chained exception ---");
        try {
            parseEngineCc("POL-1003", "abc");   // not a number
        } catch (PolicyBusinessException e) {
            printRefusal(e);
            System.out.println("   caused by -> " + e.getCause());
        }

        // The whole point: program survived every failure.
        System.out.println("\nProgram finished normally.");
    }

    // one place that formats a business-rule refusal
    private static void printRefusal(PolicyBusinessException e) {
        System.out.println("REFUSED [" + e.getClass().getSimpleName()
                + "] " + e.getPolicyNumber() + ": " + e.getMessage());
    }

    // demonstrates wrapping NumberFormatException as the cause
    private static void parseEngineCc(String policyNum, String text) {
        try {
            Integer.parseInt(text);
        } catch (NumberFormatException nfe) {
            throw new InvalidPolicyDataException(
                    policyNum,
                    "engine CC must be a number, got '" + text + "'",
                    nfe            // original kept as the cause
            );
        }
    }
}