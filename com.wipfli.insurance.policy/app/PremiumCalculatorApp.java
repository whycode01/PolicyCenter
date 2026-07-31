package app;

import model.Customer;
import model.PolicyDetail;
import model.VehicleType;
import model.CarPolicy;
import model.BikePolicy;
import model.TruckPolicy;
import service.NoClaimBonusCalculator;
import service.PolicyValidator;
import service.PolicyRegister;                       // >>> CHANGE 1: import register
import service.PremiumCalculable;
import service.StandardPremiumCalculator;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import exception.InvalidPolicyDataException;
import exception.PolicyBusinessException;
import exception.PolicyNotFoundException;            // >>> CHANGE 1: import checked exception

import java.util.Scanner;

public class PremiumCalculatorApp {

    private static final int MINIMUM_TRUCK_DRIVER_AGE = 21;

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        PolicyValidator validator = new PolicyValidator();

        // >>> CHANGE 2: create the register (Collections-backed store)
        PolicyRegister register = new PolicyRegister();

        PremiumCalculable standardCalculator =
                new StandardPremiumCalculator();

        PremiumCalculable noClaimCalculator =
                new NoClaimBonusCalculator();

        System.out.println("================================================");
        System.out.println("      INSURANCE PREMIUM CALCULATOR");
        System.out.println("================================================");

        System.out.println("\nVEHICLE BASE PREMIUMS");
        System.out.println("--------------------------------");
        System.out.println("BIKE  : Rs. 300");
        System.out.println("CAR   : Rs. 500");
        System.out.println("TRUCK : Rs. 800");

        System.out.println("\nPREMIUM RULES");
        System.out.println("--------------------------------");
        System.out.println("Age <= 25  : +20% Loading");
        System.out.println("Age > 25   : +10% Loading");
        System.out.println("Each Claim : +Rs. 300");
        System.out.println("No Claim Bonus : 10% Discount");


        int count;

        while (true) {

            try {

                System.out.print("\nEnter Number Of Policies: ");

                count = scanner.nextInt();
                scanner.nextLine();

                validator.validatePolicyCount(count);

                break;

            } catch (InvalidPolicyDataException e) {

                System.out.println(
                        "REFUSED [" + e.getClass().getSimpleName() + "] "
                                + e.getMessage()
                );
            }
        }

        // >>> CHANGE 3: no more PolicyDetail[] array — the register IS the store

        for (int i = 0; i < count; i++) {

            System.out.println(
                    "\n========== POLICY " + (i + 1) + " =========="
            );

            // ---------- Policy Number ----------
            String policyNum;

            while (true) {

                try {

                    System.out.print("Policy Number : ");

                    policyNum = scanner.nextLine();

                    validator.validatePolicyNumber(policyNum);

                    break;

                } catch (InvalidPolicyDataException e) {

                    System.out.println(
                            "REFUSED [" + e.getClass().getSimpleName() + "] "
                                    + e.getMessage()
                    );
                }
            }

            // ---------- Customer Name ----------
            System.out.print("Customer Name : ");

            String customerName = scanner.nextLine();

            // ---------- Vehicle Type (before age for truck rule) ----------
            VehicleType vehicleType;

            while (true) {

                try {

                    System.out.print("Vehicle Type (BIKE/CAR/TRUCK): ");

                    vehicleType = VehicleType.valueOf(
                            scanner.nextLine().trim().toUpperCase()
                    );

                    break;

                } catch (IllegalArgumentException e) {
                    System.out.println(
                            "REFUSED [InvalidPolicyDataException] "
                                    + policyNum
                                    + ": vehicle type must be BIKE, CAR or TRUCK"
                    );
                }
            }

            // ---------- Customer Age (truck rule thrown as custom exception) ----------
            int customerAge;

            while (true) {

                try {

                    System.out.print("Customer Age : ");

                    customerAge = scanner.nextInt();
                    scanner.nextLine();

                    validator.validateAge(customerAge);

                    if (vehicleType == VehicleType.TRUCK
                            && customerAge < MINIMUM_TRUCK_DRIVER_AGE) {

                        throw new InvalidPolicyDataException(
                                policyNum,
                                "a truck policy needs a driver of at least "
                                        + MINIMUM_TRUCK_DRIVER_AGE
                                        + ", this driver is " + customerAge
                        );
                    }

                    break;

                } catch (InvalidPolicyDataException e) {

                    System.out.println(
                            "REFUSED [" + e.getClass().getSimpleName() + "] "
                                    + e.getPolicyNumber() + ": " + e.getMessage()
                    );
                }
            }

            Customer customer =
                    new Customer(customerName, customerAge);

            // ---------- Previous Claims ----------
            int previousClaims;

            while (true) {

                try {

                    System.out.print("Previous Claim Count : ");

                    previousClaims = scanner.nextInt();
                    scanner.nextLine();

                    validator.validateClaimCount(previousClaims);

                    break;

                } catch (InvalidPolicyDataException e) {

                    System.out.println(
                            "REFUSED [" + e.getClass().getSimpleName() + "] "
                                    + e.getMessage()
                    );
                }
            }

            // ---------- Expiry Date ----------
            LocalDate expiryDate;

            while (true) {

                try {

                    System.out.print("Expiry Date (yyyy-MM-dd): ");

                    expiryDate = LocalDate.parse(
                            scanner.nextLine().trim()
                    );

                    if (expiryDate.isBefore(LocalDate.now())) {

                        throw new InvalidPolicyDataException(
                                policyNum,
                                "expiry date cannot be in the past, got " + expiryDate
                        );
                    }

                    break;

                } catch (DateTimeParseException e) {

                    System.out.println(
                            "REFUSED [InvalidPolicyDataException] "
                                    + policyNum
                                    + ": date must be in yyyy-MM-dd format"
                    );

                } catch (InvalidPolicyDataException e) {

                    System.out.println(
                            "REFUSED [" + e.getClass().getSimpleName() + "] "
                                    + e.getPolicyNumber() + ": " + e.getMessage()
                    );
                }
            }

            // ---------- Build subclass + REGISTER it ----------
            try {

                PolicyDetail newPolicy = null;

                switch (vehicleType) {

                    case CAR:

                        System.out.print("Registration Number : ");
                        String registrationNumber = scanner.nextLine();

                        newPolicy = new CarPolicy(
                                policyNum,
                                customer,
                                previousClaims,
                                expiryDate,
                                registrationNumber
                        );

                        break;

                    case BIKE:

                        System.out.print("Engine CC : ");
                        String engineText = scanner.nextLine().trim();

                        int engineCC;
                        try {
                            engineCC = Integer.parseInt(engineText);
                        } catch (NumberFormatException nfe) {
                            throw new InvalidPolicyDataException(
                                    policyNum,
                                    "engine CC must be a number, got '" + engineText + "'",
                                    nfe
                            );
                        }

                        newPolicy = new BikePolicy(
                                policyNum,
                                customer,
                                previousClaims,
                                expiryDate,
                                engineCC
                        );

                        break;

                    case TRUCK:

                        System.out.print("Load Capacity (Tons) : ");
                        String loadText = scanner.nextLine().trim();

                        double loadCapacity;
                        try {
                            loadCapacity = Double.parseDouble(loadText);
                        } catch (NumberFormatException nfe) {
                            throw new InvalidPolicyDataException(
                                    policyNum,
                                    "load capacity must be a number, got '" + loadText + "'",
                                    nfe
                            );
                        }

                        newPolicy = new TruckPolicy(
                                policyNum,
                                customer,
                                previousClaims,
                                expiryDate,
                                loadCapacity
                        );

                        break;
                }


                // If 101 already exists, this throws DuplicatePolicyNumberException.
                register.add(newPolicy);

            } catch (PolicyBusinessException e) {

                System.out.println(
                        "REFUSED [" + e.getClass().getSimpleName() + "] "
                                + e.getPolicyNumber() + ": " + e.getMessage()
                );

                if (e.getCause() != null) {
                    System.out.println("   caused by -> " + e.getCause());
                }

                i--;
            }
        }

        // ================= PREMIUM SUMMARY =================
        System.out.println("\n========== PREMIUM SUMMARY ==========");

        // >>> CHANGE 6: loop over the register, not an array
        for (PolicyDetail policy : register.getAllPolicies()) {

            double standardPremium =
                    standardCalculator.calculatePremium(policy);

            double noClaimPremium =
                    noClaimCalculator.calculatePremium(policy);

            System.out.println("\n" + policy.getPolicyDetails());
            System.out.println("Standard Premium : Rs." + standardPremium);
            System.out.println("Final Premium    : Rs." + noClaimPremium);
        }

        // ================= POLICY MENU =================
        while (true) {

            System.out.println("\n========== POLICY MENU ==========");
            System.out.println("1. Record Claim");
            System.out.println("2. Expire Policy");
            System.out.println("3. Renew Policy");
            System.out.println("4. Show Policy");
            System.out.println("5. Exit");

            System.out.print("Enter Choice : ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice == 5) {
                System.out.println("Application Closed");
                break;
            }

            System.out.print("Enter Policy Number : ");
            String number = scanner.nextLine();

            // >>> CHANGE 7: use register.findByNumber() — never returns null,
            // throws PolicyNotFoundException (checked) for a missing number
            PolicyDetail selectedPolicy;

            try {
                selectedPolicy = register.findByNumber(number);
            } catch (PolicyNotFoundException e) {
                System.out.println(
                        "REFUSED [" + e.getClass().getSimpleName() + "] "
                                + e.getPolicyNumber() + ": " + e.getMessage()
                );
                continue;
            }

            // ONE catch point for all business-rule failures in the menu
            try {

                switch (choice) {

                    case 1:

                        selectedPolicy.recordClaim();

                        System.out.println("Claim Added Successfully");
                        System.out.println(
                                "Claims : " + selectedPolicy.getPreviousClaimCount());

                        break;

                    case 2:

                        selectedPolicy.expire();

                        System.out.println("Policy Expired");
                        System.out.println(
                                "Status : " + selectedPolicy.getStatus());

                        break;

                    case 3:

                        LocalDate today;

                        try {
                            System.out.print("Enter Today's Date (yyyy-MM-dd): ");
                            today = LocalDate.parse(scanner.nextLine().trim());
                        } catch (DateTimeParseException e) {
                            System.out.println(
                                    "Invalid date format. Using system date instead.");
                            today = LocalDate.now();
                        }

                        selectedPolicy.renew(today);

                        System.out.println("Policy Renewed");
                        System.out.println(
                                "Status : " + selectedPolicy.getStatus());

                        break;

                    case 4:

                        System.out.println(selectedPolicy.getPolicyDetails());

                        break;

                    default:

                        System.out.println("Invalid Choice");
                }

            } catch (PolicyBusinessException e) {
                System.out.println(
                        "REFUSED [" + e.getClass().getSimpleName() + "] "
                                + e.getPolicyNumber() + ": " + e.getMessage()
                );
            }
        }

        System.out.println("\nProgram finished normally.");

        scanner.close();
    }
}