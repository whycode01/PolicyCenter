import java.util.Scanner;

interface PremiumCalculable {
    double calculatePremium();
}

class HealthPolicyPremium implements PremiumCalculable {
    private int age;
    private String coverageTier;

    public HealthPolicyPremium(int age, String coverageTier) {
        this.age = age;
        this.coverageTier = coverageTier;
    }

    @Override
    public double calculatePremium() {
        double premium = 5000;

        if (age > 50) {
            premium += 3000;
        } else if (age > 30) {
            premium += 1500;
        }

        if (coverageTier.equalsIgnoreCase("Silver")) {
            premium += 2000;
        } else if (coverageTier.equalsIgnoreCase("Gold")) {
            premium += 4000;
        } else if (coverageTier.equalsIgnoreCase("Platinum")) {
            premium += 6000;
        }

        return premium;
    }
}

class VehiclePolicyPremium implements PremiumCalculable {
    private String vehicleType;
    private int claimHistory;

    public VehiclePolicyPremium(String vehicleType, int claimHistory) {
        this.vehicleType = vehicleType;
        this.claimHistory = claimHistory;
    }

    @Override
    public double calculatePremium() {
        double premium = 3000;

        if (vehicleType.equalsIgnoreCase("Car")) {
            premium += 5000;
        } else if (vehicleType.equalsIgnoreCase("Bike")) {
            premium += 2000;
        } else if (vehicleType.equalsIgnoreCase("Truck")) {
            premium += 8000;
        }

        premium += claimHistory * 1000;

        return premium;
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Health Policy ===");
        System.out.print("Enter Age: ");
        int age = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter Coverage Tier (Silver/Gold/Platinum): ");
        String coverageTier = scanner.nextLine();

        PremiumCalculable healthPolicy =
                new HealthPolicyPremium(age, coverageTier);

        System.out.println("Health Policy Premium: ₹"
                + healthPolicy.calculatePremium());

        System.out.println("\n=== Vehicle Policy ===");
        System.out.print("Enter Vehicle Type (Car/Bike/Truck): ");
        String vehicleType = scanner.nextLine();

        System.out.print("Enter Number of Previous Claims: ");
        int claimHistory = scanner.nextInt();

        PremiumCalculable vehiclePolicy =
                new VehiclePolicyPremium(vehicleType, claimHistory);

        System.out.println("Vehicle Policy Premium: ₹"
                + vehiclePolicy.calculatePremium());

        scanner.close();
    }
}