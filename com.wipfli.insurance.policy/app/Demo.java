package app;

import model.Customer;
import model.PolicyDetail;
import model.CarPolicy;
import model.BikePolicy;
import model.TruckPolicy;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


import service.PremiumCalculable;
import service.StandardPremiumCalculator;
import service.NoClaimBonusCalculator;

import service.PolicyValidator;




public class Demo {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        List<PolicyDetail> policies = new ArrayList<>();

        PolicyValidator validator = new PolicyValidator();



        System.out.println("===== POLYMORPHISM DEMO (Assignment 5) =====");
        System.out.println("We will create one Car ,truck and bike Policy\n");

        System.out.println("---- Enter Car Policy ----");
        System.out.print("Policy Number : ");
        String carNum = scanner.nextLine();
       validator.validatePolicyNumber(carNum);

        System.out.print("Customer Name : ");
        String carName = scanner.nextLine();

        System.out.print("Customer Age : ");
        int carAge = scanner.nextInt();
        scanner.nextLine();
        validator.validateAge(carAge);

        System.out.print("Previous Claims : ");
        int carPreviousClaims = scanner.nextInt();
        scanner.nextLine();
        validator.validateClaimCount(carPreviousClaims);

        System.out.print("Registration Number : ");
        String registration = scanner.nextLine();

        policies.add(
                new CarPolicy(
                        carNum,
                        new Customer(carName, carAge),
                        carPreviousClaims,
                        registration
                )
        );



        System.out.println("---- Enter Truck Policy ----");
        System.out.print("Policy Number : ");
        String truckNum = scanner.nextLine();
        validator.validatePolicyNumber(truckNum);

        System.out.print("Customer Name : ");
        String truckName = scanner.nextLine();

        System.out.print("Customer Age : ");
        int truckAge = scanner.nextInt();
        scanner.nextLine();
        validator.validateAge(truckAge);

        System.out.print("Previous Claims : ");
        int truckPreviousClaims = scanner.nextInt();
        scanner.nextLine();
        validator.validateClaimCount(truckPreviousClaims);

        System.out.print("Ton unit : ");
        int tonSize = scanner.nextInt();
        scanner.nextLine();

        policies.add(
                new TruckPolicy(
                        truckNum,
                        new Customer(truckName, truckAge),
                        truckPreviousClaims,
                        tonSize
                )
        );




        System.out.println("\n========== POLYMORPHISM OUTPUT ==========");

        for (PolicyDetail policy : policies) {

            System.out.println(policy.getPolicyDetails());

        }

        PremiumCalculable standardCalculator =
                new StandardPremiumCalculator();

        PremiumCalculable noClaimCalculator =
                new NoClaimBonusCalculator();

        for (PolicyDetail policy : policies) {

            double standardPremium =
                    standardCalculator.calculatePremium(policy);

            double noClaimPremium =
                    noClaimCalculator.calculatePremium(policy);

            System.out.println("Standard Premium : Rs." + standardPremium);
            System.out.println("Final Premium : Rs." + noClaimPremium);

        }

        scanner.close();
    }
}