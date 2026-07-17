package app;

import model.PolicyDetail;
import model.VehicleType;
import service.NoClaimBonusCalculator;
import service.PolicyValidator;
import service.PremiumCalculable;
import service.StandardPremiumCalculator;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

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

        System.out.println("\nINPUT INSTRUCTIONS");
        System.out.println("--------------------------------");
        System.out.println("1. Number of policies > 0");
        System.out.println("2. Policy Number must be alphanumeric");
        System.out.println("3. Age must be between 18 and 100");
        System.out.println("4. Vehicle Type = BIKE / CAR / TRUCK");
        System.out.println("5. Claims cannot be negative");

        System.out.println("\nFINAL PREMIUM FORMULA");
        System.out.println("--------------------------------");
        System.out.println("Base Premium");
        System.out.println("+ Age Loading");
        System.out.println("+ Claim Charges");
        System.out.println("- No Claim Bonus (if eligible)");

        int count;

        while (true) {

            try {

                System.out.print("\nEnter Number Of Policies: ");

                count = scanner.nextInt();
                scanner.nextLine();

                validator.validatePolicyCount(count);

                break;

            } catch (IllegalArgumentException e) {

                System.out.println(
                        "Validation Error: "
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
                            "Policy Number: "
                    );

                    policyNum =
                            scanner.nextLine();

                    validator.validatePolicyNumber(
                            policyNum
                    );

                    break;

                } catch (IllegalArgumentException e) {

                    System.out.println(
                            "Validation Error: "
                                    + e.getMessage()
                    );
                }
            }

            System.out.print(
                    "Customer Name: "
            );

            String customerName =
                    scanner.nextLine();

            int customerAge;

            while (true) {

                try {

                    System.out.print(
                            "Customer Age: "
                    );

                    customerAge =
                            scanner.nextInt();

                    scanner.nextLine();

                    validator.validateAge(
                            customerAge
                    );

                    break;

                } catch (IllegalArgumentException e) {

                    System.out.println(
                            "Validation Error: "
                                    + e.getMessage()
                    );
                }
            }

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

                    validator.validateVehicleType(
                            vehicleType
                    );

                    break;

                } catch (Exception e) {

                    System.out.println(
                            "Validation Error: Invalid Vehicle Type"
                    );
                }
            }

            int previousClaimCount;

            while (true) {

                try {

                    System.out.print(
                            "Previous Claim Count: "
                    );

                    previousClaimCount =
                            scanner.nextInt();

                    scanner.nextLine();

                    validator.validateClaimCount(
                            previousClaimCount
                    );

                    break;

                } catch (IllegalArgumentException e) {

                    System.out.println(
                            "Validation Error: "
                                    + e.getMessage()
                    );
                }
            }

            policies[i] =
                    new PolicyDetail(
                            policyNum,
                            customerName,
                            customerAge,
                            vehicleType,
                            previousClaimCount
                    );
        }

        System.out.println(
                "\n================================================"
        );
        System.out.println(
                "             PREMIUM SUMMARY"
        );
        System.out.println(
                "================================================"
        );

        for (PolicyDetail policy : policies) {

            double standardPremium =
                    standardCalculator
                            .calculatePremium(policy);

            double noClaimPremium =
                    noClaimCalculator
                            .calculatePremium(policy);

            double bonusAmount =
                    standardPremium
                            - noClaimPremium;

            System.out.println(
                    "\nPolicy Number : "
                            + policy.getPolicyNum()
            );

            System.out.println(
                    "Customer Name : "
                            + policy.getCustomerName()
            );

            System.out.println(
                    "Standard Premium : Rs. "
                            + standardPremium
            );

            System.out.println(
                    "No Claim Bonus : Rs. "
                            + bonusAmount
            );

            System.out.println(
                    "Final Premium : Rs. "
                            + noClaimPremium
            );

            System.out.println(
                    "--------------------------------"
            );
        }

        System.out.println(
                "\n========== HASHSET CHECK =========="
        );

        Set<PolicyDetail> policySet =
                new HashSet<>();

        for (PolicyDetail policy : policies) {
            policySet.add(policy);
        }

        System.out.println(
                "Total Policies Entered : "
                        + policies.length
        );

        System.out.println(
                "Unique Policies In HashSet : "
                        + policySet.size()
        );

        scanner.close();
    }
}