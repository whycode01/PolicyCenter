import java.util.Scanner;

public class PremiumCalculator {


    enum VehicleType {
        BIKE(300),
        CAR(500),
        TRUCK(800);

        private final int basePremium;

        VehicleType(int basePremium) {
            this.basePremium = basePremium;
        }

        public int getBasePremium() {
            return basePremium;
        }
    }


    private static final int YOUNG_DRIVER_AGE_LIMIT = 25;
    private static final double YOUNG_DRIVER_LOADING_PERCENT = 20.0;
    private static final double EXPERIENCED_DRIVER_LOADING_PERCENT = 10.0;
    private static final int CLAIM_LOADING_PER_CLAIM = 150;

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        try {

            System.out.println("==============================================");
            System.out.println("        INSURANCE PREMIUM CALCULATOR");
            System.out.println("==============================================");

            System.out.println("\nPremium Calculation Rules:");
            System.out.println("----------------------------------------------");

            System.out.println("Vehicle Base Premiums:");
            for (VehicleType vehicleType : VehicleType.values()) {
                System.out.println(vehicleType.name()
                        + " : Rs. "
                        + vehicleType.getBasePremium());
            }

            System.out.println("\nAdditional Charges:");
            System.out.println("Young Driver (Age <= "
                    + YOUNG_DRIVER_AGE_LIMIT
                    + ") : +"
                    + YOUNG_DRIVER_LOADING_PERCENT
                    + "% of Base Premium");

            System.out.println("Experienced Driver (Age > "
                    + YOUNG_DRIVER_AGE_LIMIT
                    + ") : +"
                    + EXPERIENCED_DRIVER_LOADING_PERCENT
                    + "% of Base Premium");

            System.out.println("Each Previous Claim : +Rs. "
                    + CLAIM_LOADING_PER_CLAIM);

            System.out.println("\nInput Requirements:");
            System.out.println("- Age must be between 18 and 100");
            System.out.println("- Vehicle Type must be BIKE, CAR, or TRUCK");
            System.out.println("- Number of Claims cannot be negative");
            System.out.println("----------------------------------------------");


            System.out.print("\nEnter Driver Age: ");
            int age = scanner.nextInt();
            scanner.nextLine();
            validateAge(age);


            System.out.print("Enter Vehicle Type (BIKE/CAR/TRUCK): ");
            String vehicleType = scanner.nextLine();
            validateVehicleType(vehicleType);


            System.out.print("Enter Number of Previous Claims: ");
            int claims = scanner.nextInt();
            validateClaims(claims);

            double premium = calculatePremium(age,
                    vehicleType,
                    claims);

            int basePremium = getVehicleType(vehicleType)
                    .getBasePremium();

            System.out.println("\n========== PREMIUM BREAKDOWN ==========");
            System.out.println("Base Premium : Rs. " + basePremium);

            if (isYoungDriver(age)) {

                double youngDriverCharge =
                        (basePremium
                                * YOUNG_DRIVER_LOADING_PERCENT)
                                / 100;

                System.out.println("Young Driver Loading ("
                        + YOUNG_DRIVER_LOADING_PERCENT
                        + "%) : Rs. "
                        + youngDriverCharge);

            } else {

                double experiencedDriverCharge =
                        (basePremium
                                * EXPERIENCED_DRIVER_LOADING_PERCENT)
                                / 100;

                System.out.println("Experienced Driver Loading ("
                        + EXPERIENCED_DRIVER_LOADING_PERCENT
                        + "%) : Rs. "
                        + experiencedDriverCharge);
            }

            System.out.println("Claims Loading ("
                    + claims
                    + " x Rs. "
                    + CLAIM_LOADING_PER_CLAIM
                    + ") : Rs. "
                    + (claims * CLAIM_LOADING_PER_CLAIM));

            System.out.println("---------------------------------------");
            System.out.println("Final Premium : Rs. " + premium);

        } catch (IllegalArgumentException e) {

            System.out.println("\nValidation Error: " + e.getMessage());

        } catch (Exception e) {

            System.out.println("\nUnexpected Error: " + e.getMessage());

        } finally {

            System.out.println("\nProcess Completed.");
            scanner.close();
        }
    }

    public static double calculatePremium(
            int age,
            String vehicleType,
            int numberOfClaims) {

        int basePremium = getVehicleType(vehicleType)
                .getBasePremium();

        double finalPremium = basePremium;

        if (isYoungDriver(age)) {

            finalPremium +=
                    (basePremium
                            * YOUNG_DRIVER_LOADING_PERCENT)
                            / 100;

        } else {

            finalPremium +=
                    (basePremium
                            * EXPERIENCED_DRIVER_LOADING_PERCENT)
                            / 100;
        }

        finalPremium +=
                numberOfClaims
                        * CLAIM_LOADING_PER_CLAIM;

        return finalPremium;
    }

    public static VehicleType getVehicleType(String vehicleType) {

        try {
            return VehicleType.valueOf(
                    vehicleType.trim().toUpperCase());
        } catch (Exception e) {
            throw new IllegalArgumentException(
                    "Invalid Vehicle Type. Allowed values are BIKE, CAR, and TRUCK.");
        }
    }

    public static boolean isYoungDriver(int age) {
        return age <= YOUNG_DRIVER_AGE_LIMIT;
    }


    public static void validateAge(int age) {

        if (age < 0) {
            throw new IllegalArgumentException(
                    "Age cannot be negative.");
        }

        if (age < 18 || age > 100) {
            throw new IllegalArgumentException(
                    "Age must be between 18 and 100.");
        }
    }

    public static void validateVehicleType(String vehicleType) {

        if (vehicleType == null
                || vehicleType.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Vehicle Type cannot be null or empty.");
        }


        getVehicleType(vehicleType);
    }

    public static void validateClaims(int claims) {

        if (claims < 0) {
            throw new IllegalArgumentException(
                    "Number of Claims cannot be negative.");
        }
    }
}