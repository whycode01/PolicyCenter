package app;

import model.Customer;
import model.PolicyDetail;
import model.VehicleType;
import service.PolicyValidator;
import service.PolicyRegister;
import service.PolicyFactory;
import service.PremiumCalculable;
import service.StandardPremiumCalculator;
import service.NoClaimBonusCalculator;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Optional;
import exception.InvalidPolicyDataException;
import exception.PolicyBusinessException;
import exception.PolicyNotFoundException;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class PremiumCalculatorApp {

    private static final int MINIMUM_TRUCK_DRIVER_AGE = 21;
    private static final LocalDate REFERENCE_DATE = LocalDate.of(2026, 8, 9);

    private final Scanner scanner = new Scanner(System.in);
    private final PolicyValidator validator = new PolicyValidator();
    private final PolicyRegister register;
    private final PremiumCalculable standardCalculator;
    private final PremiumCalculable noClaimCalculator;

    public PremiumCalculatorApp() {
        this.standardCalculator = new StandardPremiumCalculator();
        this.noClaimCalculator = new NoClaimBonusCalculator(standardCalculator);
        this.register = new PolicyRegister(noClaimCalculator);
    }

    public static void main(String[] args) {
        new PremiumCalculatorApp().run();
    }

    public void run() {

        printBanner();
        seedData();

        while (true) {

            printMenu();

            int choice = readInt("Enter Choice : ");

            switch (choice) {

                case 1:
                    handleAddPolicy();
                    break;

                case 2:
                    handleRecordClaim();
                    break;

                case 3:
                    handleExpirePolicy();
                    break;

                case 4:
                    handleRenewPolicy();
                    break;

                case 5:
                    handleShowPolicy();
                    break;

                case 6:
                    handleViewByCustomer();
                    break;

                case 7:
                    handleViewByVehicleType();
                    break;

                case 8:
                    handleViewExpiringSoon();
                    break;

                case 9:
                    handleViewSummary();
                    break;

                case 10:
                    handleFindSimilarPolicyHolders();
                    break;

                case 11:
                    handleFindByPolicyNumber();
                    break;

                case 12:
                    handleFindByCustomerName();
                    break;

                case 13:
                    handleTotalPremiumForVehicleType();
                    break;

                case 14:
                    handleCountPoliciesByVehicleType();
                    break;

                case 15:
                    handlePoliciesExpiringWithin();
                    break;

                case 16:
                    handleCustomerWithMostPolicies();
                    break;

                case 17:
                    handleTop5ByPremium();
                    break;

                case 18:
                    System.out.println("Application Closed");
                    scanner.close();
                    return;

                default:
                    System.out.println("Invalid Choice");
            }
        }
    }

    private void printBanner() {

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
    }

    private void handleAddPolicy() {

        System.out.println("\n========== ADD POLICY ==========");

        String policyNum = readPolicyNumber();
        String customerName = readLine("Customer Name : ");
        VehicleType vehicleType = readVehicleType(policyNum);
        int customerAge = readCustomerAge(policyNum, vehicleType);
        Customer customer = new Customer(customerName, customerAge);
        int previousClaims = readPreviousClaims();
        LocalDate expiryDate = readExpiryDate(policyNum);

        try {

            PolicyDetail newPolicy = buildPolicy(
                    policyNum,
                    customer,
                    vehicleType,
                    previousClaims,
                    expiryDate
            );

            register.add(newPolicy);
            System.out.println("Policy Added Successfully");

        } catch (PolicyBusinessException e) {

            printRefusal(e);

            if (e.getCause() != null) {
                System.out.println("   caused by -> " + e.getCause());
            }
        }
    }

    private String readPolicyNumber() {

        while (true) {

            try {

                String policyNum = readLine("Policy Number : ");
                validator.validatePolicyNumber(policyNum);
                return policyNum;

            } catch (InvalidPolicyDataException e) {
                printRefusal(e);
            }
        }
    }

    private VehicleType readVehicleType(String policyNum) {

        while (true) {

            try {

                String input = readLine("Vehicle Type (BIKE/CAR/TRUCK): ");
                return VehicleType.valueOf(input.trim().toUpperCase());

            } catch (IllegalArgumentException e) {
                System.out.println(
                        "REFUSED [InvalidPolicyDataException] "
                                + policyNum
                                + ": vehicle type must be BIKE, CAR or TRUCK"
                );
            }
        }
    }

    private int readCustomerAge(String policyNum, VehicleType vehicleType) {

        while (true) {

            try {

                int customerAge = readInt("Customer Age : ");
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

                return customerAge;

            } catch (InvalidPolicyDataException e) {
                printRefusal(e);
            }
        }
    }

    private int readPreviousClaims() {

        while (true) {

            try {

                int previousClaims = readInt("Previous Claim Count : ");
                validator.validateClaimCount(previousClaims);
                return previousClaims;

            } catch (InvalidPolicyDataException e) {
                printRefusal(e);
            }
        }
    }

    private LocalDate readExpiryDate(String policyNum) {

        while (true) {

            try {

                String input = readLine("Expiry Date (yyyy-MM-dd): ");
                LocalDate expiryDate = LocalDate.parse(input.trim());

                if (expiryDate.isBefore(LocalDate.now())) {

                    throw new InvalidPolicyDataException(
                            policyNum,
                            "expiry date cannot be in the past, got " + expiryDate
                    );
                }

                return expiryDate;

            } catch (DateTimeParseException e) {

                System.out.println(
                        "REFUSED [InvalidPolicyDataException] "
                                + policyNum
                                + ": date must be in yyyy-MM-dd format"
                );

            } catch (InvalidPolicyDataException e) {
                printRefusal(e);
            }
        }
    }

    private PolicyDetail buildPolicy(String policyNum,
                                     Customer customer,
                                     VehicleType vehicleType,
                                     int previousClaims,
                                     LocalDate expiryDate) {

        switch (vehicleType) {

            case CAR:

                String registrationNumber = readLine("Registration Number : ");
                return PolicyFactory.createCarPolicy(
                        policyNum,
                        customer,
                        previousClaims,
                        expiryDate,
                        registrationNumber
                );

            case BIKE:

                int engineCC = readEngineCC(policyNum);
                return PolicyFactory.createBikePolicy(
                        policyNum,
                        customer,
                        previousClaims,
                        expiryDate,
                        engineCC
                );

            case TRUCK:

                double loadCapacity = readLoadCapacity(policyNum);
                return PolicyFactory.createTruckPolicy(
                        policyNum,
                        customer,
                        previousClaims,
                        expiryDate,
                        loadCapacity
                );

            default:
                throw new InvalidPolicyDataException(
                        policyNum,
                        "unsupported vehicle type: " + vehicleType
                );
        }
    }

    private int readEngineCC(String policyNum) {

        while (true) {

            try {

                String engineText = readLine("Engine CC : ");
                return Integer.parseInt(engineText.trim());

            } catch (NumberFormatException nfe) {

                System.out.println(
                        "REFUSED [InvalidPolicyDataException] "
                                + policyNum
                                + ": engine CC must be a number"
                );
            }
        }
    }

    private double readLoadCapacity(String policyNum) {

        while (true) {

            try {

                String loadText = readLine("Load Capacity (Tons) : ");
                return Double.parseDouble(loadText.trim());

            } catch (NumberFormatException nfe) {

                System.out.println(
                        "REFUSED [InvalidPolicyDataException] "
                                + policyNum
                                + ": load capacity must be a number"
                );
            }
        }
    }


    private void printMenu() {

        System.out.println("\n========== POLICY MENU ==========");
        System.out.println("1. Add Policy");
        System.out.println("2. Record Claim");
        System.out.println("3. Expire Policy");
        System.out.println("4. Renew Policy");
        System.out.println("5. Show Policy");
        System.out.println("6. View Policies By Customer");
        System.out.println("7. View Policies By Vehicle Type");
        System.out.println("8. View Policies Expiring Soon");
        System.out.println("9. View Premium Summary By Vehicle Type");
        System.out.println("10. Find Similar Policy Holders");
        System.out.println("11. Find Policy By Number (Optional)");
        System.out.println("12. Find Policies By Customer Name");
        System.out.println("13. Total Premium For Vehicle Type");
        System.out.println("14. Count Policies By Vehicle Type");
        System.out.println("15. Policies Expiring Within N Days");
        System.out.println("16. Customer With Most Policies");
        System.out.println("17. Top 5 Policies By Premium");
        System.out.println("18. Exit");
    }

    private void handleRecordClaim() {

        try {

            PolicyDetail selectedPolicy = readSelectedPolicy();
            selectedPolicy.recordClaim();

            System.out.println("Claim Added Successfully");
            System.out.println("Claims : " + selectedPolicy.getPreviousClaimCount());

        } catch (PolicyNotFoundException | PolicyBusinessException e) {
            printRefusal(e);
        }
    }

    private void handleExpirePolicy() {

        try {

            PolicyDetail selectedPolicy = readSelectedPolicy();
            selectedPolicy.expire();

            System.out.println("Policy Expired");
            System.out.println("Status : " + selectedPolicy.getStatus());

        } catch (PolicyNotFoundException | PolicyBusinessException e) {
            printRefusal(e);
        }
    }

    private void handleRenewPolicy() {

        try {

            PolicyDetail selectedPolicy = readSelectedPolicy();
            LocalDate today = readTodayDate();

            selectedPolicy.renew(today);

            System.out.println("Policy Renewed");
            System.out.println("Status : " + selectedPolicy.getStatus());

        } catch (PolicyNotFoundException | PolicyBusinessException e) {
            printRefusal(e);
        }
    }

    private void handleShowPolicy() {

        try {

            PolicyDetail selectedPolicy = readSelectedPolicy();
            System.out.println(selectedPolicy.getPolicyDetails());

        } catch (PolicyNotFoundException | PolicyBusinessException e) {
            printRefusal(e);
        }
    }

    private void handleViewByCustomer() {

        String customerName = readLine("Enter Customer Name : ");
        List<PolicyDetail> policies = register.findByCustomer(customerName);

        if (policies.isEmpty()) {
            System.out.println("No policies found for customer: " + customerName);
            return;
        }

        System.out.println("\nPolicies for " + customerName + ":");

        for (PolicyDetail policy : policies) {
            System.out.println(policy.getPolicyDetails());
        }
    }

    private void handleViewByVehicleType() {

        VehicleType vehicleType = readVehicleTypeForLookup();
        List<PolicyDetail> policies = register.findByVehicleType(vehicleType);

        if (policies.isEmpty()) {
            System.out.println("No policies found for vehicle type: " + vehicleType);
            return;
        }

        System.out.println("\n" + vehicleType + " Book:");

        for (PolicyDetail policy : policies) {
            System.out.println(policy.getPolicyDetails());
        }
    }

    private void handleViewExpiringSoon() {

        int days = readInt("Enter number of days : ");
        LocalDate today = readTodayDate();

        List<PolicyDetail> expiringSoon = register.findExpiringWithin(days, today);

        if (expiringSoon.isEmpty()) {
            System.out.println("No policies expiring in the next " + days + " days.");
            return;
        }

        System.out.println("\nPolicies expiring within " + days + " days:");

        for (PolicyDetail policy : expiringSoon) {
            System.out.println(policy.getPolicyDetails());
        }
    }

    private void handleViewSummary() {

        Map<VehicleType, Double> summary = register.totalPremiumByVehicleType();

        System.out.println("\n========== PREMIUM SUMMARY BY VEHICLE TYPE ==========");

        for (Map.Entry<VehicleType, Double> entry : summary.entrySet()) {
            System.out.println(entry.getKey() + " : Rs." + entry.getValue());
        }
    }

    private PolicyDetail readSelectedPolicy() throws PolicyNotFoundException {

        String number = readLine("Enter Policy Number : ");
        return register.findByPolicyNumber(number)
                .orElseThrow(() -> new PolicyNotFoundException(number, "no policy with this number"));
    }

    private VehicleType readVehicleTypeForLookup() {

        while (true) {

            try {

                String input = readLine("Enter Vehicle Type (BIKE/CAR/TRUCK): ");
                return VehicleType.valueOf(input.trim().toUpperCase());

            } catch (IllegalArgumentException e) {
                System.out.println("Invalid vehicle type. Must be BIKE, CAR or TRUCK.");
            }
        }
    }

    private LocalDate readTodayDate() {

        try {

            String input = readLine("Enter Today's Date (yyyy-MM-dd): ");
            return LocalDate.parse(input.trim());

        } catch (DateTimeParseException e) {

            System.out.println("Invalid date format. Using system date instead.");
            return LocalDate.now();
        }
    }

    private String readLine(String prompt) {

        System.out.print(prompt);
        return scanner.nextLine();
    }

    private int readInt(String prompt) {

        while (true) {

            try {

                System.out.print(prompt);
                int value = scanner.nextInt();
                scanner.nextLine();
                return value;

            } catch (Exception e) {

                System.out.println("Invalid input. Please enter a whole number.");
                scanner.nextLine();
            }
        }
    }

    private void printRefusal(Exception e) {

        String policyNumber = "unknown";

        if (e instanceof PolicyBusinessException) {
            policyNumber = ((PolicyBusinessException) e).getPolicyNumber();
        } else if (e instanceof PolicyNotFoundException) {
            policyNumber = ((PolicyNotFoundException) e).getPolicyNumber();
        }

        System.out.println(
                "REFUSED [" + e.getClass().getSimpleName() + "] "
                        + policyNumber + ": " + e.getMessage()
        );
    }

    private void seedData() {

        register.add(PolicyFactory.createCarPolicy(
                "POL-2001",
                new Customer("Ravi Kumar", 22),
                1,
                LocalDate.of(2026, 8, 15),
                "KA-05-1234"
        ));

        register.add(PolicyFactory.createTruckPolicy(
                "POL-2002",
                new Customer("Ravi Kumar", 22),
                0,
                LocalDate.of(2026, 9, 1),
                12.5
        ));

        register.add(PolicyFactory.createBikePolicy(
                "POL-2003",
                new Customer("Meena Iyer", 34),
                2,
                LocalDate.of(2026, 8, 20),
                150
        ));

        register.add(PolicyFactory.createCarPolicy(
                "POL-2004",
                new Customer("Ajay Verma", 41),
                0,
                LocalDate.of(2027, 1, 10),
                "MH-12-5678"
        ));

        register.add(PolicyFactory.createBikePolicy(
                "POL-2005",
                new Customer("Sneha Rao", 29),
                0,
                LocalDate.of(2026, 8, 25),
                125
        ));

        register.add(PolicyFactory.createCarPolicy(
                "POL-2006",
                new Customer("Ravi Kumar", 22),
                3,
                LocalDate.of(2026, 12, 1),
                "TN-09-9012"
        ));

        System.out.println("\nSeed data loaded: " + register.size() + " policies");
    }

    // NEW: Stream query handlers for Assignment 8

    private void handleFindByPolicyNumber() {

        String number = readLine("Enter Policy Number : ");
        Optional<PolicyDetail> optionalPolicy = register.findByPolicyNumber(number);

        optionalPolicy.ifPresentOrElse(
                policy -> System.out.println(policy.getPolicyDetails()),
                () -> System.out.println("No policy found with number: " + number)
        );
    }

    private void handleFindByCustomerName() {

        String customerName = readLine("Enter Customer Name : ");
        List<PolicyDetail> policies = register.findByCustomerName(customerName);

        if (policies.isEmpty()) {
            System.out.println("No policies found for customer: " + customerName);
            return;
        }

        System.out.println("\nPolicies for " + customerName + ":");
        policies.forEach(policy -> System.out.println(policy.getPolicyDetails()));
    }

    private void handleTotalPremiumForVehicleType() {

        VehicleType vehicleType = readVehicleTypeForLookup();
        double total = register.totalPremiumForVehicleType(vehicleType);
        System.out.println(vehicleType + ": " + total);
    }

    private void handleCountPoliciesByVehicleType() {

        Map<VehicleType, Long> counts = register.countPoliciesByVehicleType();

        System.out.println("\n========== POLICY COUNT BY VEHICLE TYPE ==========");
        counts.forEach((vehicleType, count) ->
                System.out.println(vehicleType + " : " + count)
        );
    }

    private void handlePoliciesExpiringWithin() {

        int days = readInt("Enter number of days : ");
        List<PolicyDetail> expiringSoon = register.policiesExpiringWithin(days, REFERENCE_DATE);

        if (expiringSoon.isEmpty()) {
            System.out.println("No policies expiring in the next " + days + " days.");
            return;
        }

        System.out.println("\nPolicies expiring within " + days + " days of " + REFERENCE_DATE + ":");
        expiringSoon.forEach(policy ->
                System.out.println(policy.getPolicyNum()
                        + " | " + policy.getCustomer().getCustomerName()
                        + " | " + policy.getVehicleType()
                        + " | expires " + policy.getExpiryDate())
        );
    }

    private void handleCustomerWithMostPolicies() {

        Optional<String> customer = register.customerWithMostPolicies();

        customer.ifPresentOrElse(
                name -> {
                    long count = register.findByCustomerName(name).size();
                    System.out.println(name + " (" + count + " policies)");
                },
                () -> System.out.println("No customers found.")
        );
    }

    private void handleTop5ByPremium() {

        List<PolicyDetail> top5 = register.top5ByPremium();

        System.out.println("\n========== TOP 5 PREMIUMS ==========");
        top5.forEach(policy ->
                System.out.println(policy.getPolicyNum()
                        + " | " + policy.getCustomer().getCustomerName()
                        + " | " + policy.getVehicleType()
                        + " | Claims: " + policy.getPreviousClaimCount()
                        + " -> " + noClaimCalculator.calculatePremium(policy))
        );
    }

    private void handleFindSimilarPolicyHolders() {

        try {

            PolicyDetail selectedPolicy = readSelectedPolicy();
            String customerName = selectedPolicy.getCustomer().getCustomerName();

            List<PolicyDetail> similarPolicies = register.findSimilarPolicyHolders(
                    selectedPolicy.getPolicyNum()
            );

            if (similarPolicies.isEmpty()) {
                System.out.println(
                        "No other customers share the same vehicle-type pattern as "
                                + customerName
                );
                return;
            }

            System.out.println(
                    "\nOther customers with the same vehicle-type pattern as "
                            + customerName + ":"
            );

            for (PolicyDetail policy : similarPolicies) {
                System.out.println(policy.getPolicyDetails());
            }

        } catch (PolicyNotFoundException | PolicyBusinessException e) {
            printRefusal(e);
        }
    }
}
