package app;

import model.Customer;
import model.PolicyDetail;
import model.VehicleType;
import service.NoClaimBonusCalculator;
import service.PolicyValidator;
import service.PremiumCalculable;
import service.StandardPremiumCalculator;

import java.util.Scanner;

public class PremiumCalculatorApp {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        PolicyValidator validator = new PolicyValidator();

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

                System.out.print(
                        "\nEnter Number Of Policies: "
                );

                count = scanner.nextInt();
                scanner.nextLine();

                validator.validatePolicyCount(count);

                break;

            } catch (Exception e) {

                System.out.println(
                        "Validation Error : "
                                + e.getMessage()
                );
            }
        }

        PolicyDetail[] policies =
                new PolicyDetail[count];

        for (int i = 0; i < count; i++) {

            System.out.println(
                    "\n========== POLICY "
                            + (i + 1)
                            + " =========="
            );

            String policyNum;

            while (true) {

                try {

                    System.out.print(
                            "Policy Number : "
                    );

                    policyNum =
                            scanner.nextLine();

                    validator.validatePolicyNumber(
                            policyNum
                    );

                    break;

                } catch (Exception e) {

                    System.out.println(
                            "Validation Error : "
                                    + e.getMessage()
                    );
                }
            }

            System.out.print(
                    "Customer Name : "
            );

            String customerName =
                    scanner.nextLine();

            int customerAge;

            while (true) {

                try {

                    System.out.print(
                            "Customer Age : "
                    );

                    customerAge =
                            scanner.nextInt();

                    scanner.nextLine();

                    validator.validateAge(
                            customerAge
                    );

                    break;

                } catch (Exception e) {

                    System.out.println(
                            "Validation Error : "
                                    + e.getMessage()
                    );
                }
            }

            Customer customer =
                    new Customer(
                            customerName,
                            customerAge
                    );

            VehicleType vehicleType;

            while (true) {

                try {

                    System.out.print(
                            "Vehicle Type (BIKE/CAR/TRUCK): "
                    );

                    vehicleType =
                            VehicleType.valueOf(
                                    scanner.nextLine()
                                            .trim()
                                            .toUpperCase()
                            );

                    break;

                } catch (Exception e) {

                    System.out.println(
                            "Validation Error : Invalid Vehicle Type"
                    );
                }
            }

            int previousClaims;

            while (true) {

                try {

                    System.out.print(
                            "Previous Claim Count : "
                    );

                    previousClaims =
                            scanner.nextInt();

                    scanner.nextLine();

                    validator.validateClaimCount(
                            previousClaims
                    );

                    break;

                } catch (Exception e) {

                    System.out.println(
                            "Validation Error : "
                                    + e.getMessage()
                    );
                }
            }

            policies[i] =
                    new PolicyDetail(
                            policyNum,
                            customer,
                            vehicleType,
                            previousClaims
                    );
        }

        System.out.println(
                "\n========== PREMIUM SUMMARY =========="
        );

        for (PolicyDetail policy : policies) {

            double standardPremium =
                    standardCalculator
                            .calculatePremium(policy);

            double noClaimPremium =
                    noClaimCalculator
                            .calculatePremium(policy);

            System.out.println(
                    "\nPolicy Number : "
                            + policy.getPolicyNum());

            System.out.println(
                    "Customer Name : "
                            + policy.getCustomer()
                            .getCustomerName());

            System.out.println(
                    "Status : "
                            + policy.getStatus());

            System.out.println(
                    "Standard Premium : Rs."
                            + standardPremium);

            System.out.println(
                    "Final Premium : Rs."
                            + noClaimPremium);
        }

        while (true) {

            System.out.println(
                    "\n========== POLICY MENU =========="
            );

            System.out.println(
                    "1. Record Claim"
            );
            System.out.println(
                    "2. Expire Policy"
            );
            System.out.println(
                    "3. Renew Policy"
            );
            System.out.println(
                    "4. Show Policy"
            );
            System.out.println(
                    "5. Exit"
            );

            System.out.print(
                    "Enter Choice : "
            );

            int choice =
                    scanner.nextInt();

            scanner.nextLine();

            if (choice == 5) {

                System.out.println(
                        "Application Closed"
                );

                break;
            }

            System.out.print(
                    "Enter Policy Number : "
            );

            String number =
                    scanner.nextLine();

            PolicyDetail selectedPolicy =
                    null;

            for (PolicyDetail policy : policies) {

                if (policy.getPolicyNum()
                        .equals(number)) {

                    selectedPolicy = policy;
                    break;
                }
            }

            if (selectedPolicy == null) {

                System.out.println(
                        "Policy Not Found"
                );

                continue;
            }

            try {

                switch (choice) {

                    case 1:

                        selectedPolicy.recordClaim();

                        System.out.println(
                                "Claim Added Successfully"
                        );

                        System.out.println(
                                "Claims : "
                                        + selectedPolicy
                                        .getPreviousClaimCount()
                        );

                        break;

                    case 2:

                        selectedPolicy.expire();

                        System.out.println(
                                "Policy Expired"
                        );

                        System.out.println(
                                "Status : "
                                        + selectedPolicy
                                        .getStatus()
                        );

                        break;

                    case 3:

                        selectedPolicy.renew();

                        System.out.println(
                                "Policy Renewed"
                        );

                        System.out.println(
                                "Status : "
                                        + selectedPolicy
                                        .getStatus()
                        );

                        break;

                    case 4:

                        System.out.println(
                                "\nPolicy Number : "
                                        + selectedPolicy
                                        .getPolicyNum()
                        );

                        System.out.println(
                                "Customer Name : "
                                        + selectedPolicy
                                        .getCustomer()
                                        .getCustomerName()
                        );

                        System.out.println(
                                "Customer Age : "
                                        + selectedPolicy
                                        .getCustomer()
                                        .getCustomerAge()
                        );

                        System.out.println(
                                "Vehicle Type : "
                                        + selectedPolicy
                                        .getVehicleType()
                        );

                        System.out.println(
                                "Claims : "
                                        + selectedPolicy
                                        .getPreviousClaimCount()
                        );

                        System.out.println(
                                "Status : "
                                        + selectedPolicy
                                        .getStatus()
                        );

                        break;

                    default:

                        System.out.println(
                                "Invalid Choice"
                        );
                }

            } catch (Exception e) {

                System.out.println(
                        "REJECTED : "
                                + e.getMessage()
                );
            }
        }

        scanner.close();
    }
}