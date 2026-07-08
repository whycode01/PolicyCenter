import java.util.Scanner;

public class InsurancePremium {

    private static final int BASE_PREMIUM = 3000;
    private static final int CLAIM_LOADING_PER_CLAIM = 500;

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        System.out.println("BASE PREMIUM IS: " + BASE_PREMIUM);
        System.out.println("CLAIM LOADING PER PREVIOUS CLAIM IS: " + CLAIM_LOADING_PER_CLAIM);

        System.out.print("Enter your age: ");
        int age = scan.nextInt();
        scan.nextLine();

        System.out.print("Enter your Vehicle Type (SUV/SEDAN/HATCHBACK): ");
        String vehicleType = scan.nextLine();

        System.out.print("Enter Number of claims made till now: ");
        int numberOfClaims = scan.nextInt();
        scan.nextLine();

        int finalPremium = calculatePremium(age, vehicleType, numberOfClaims);

        System.out.println("------------------------------------------");
        System.out.println("Final Insurance Premium: Rs. " + finalPremium);

        scan.close();
    }

    public static boolean isDriverYoung(int age){
        if(age<=25){
            return true;
        }
        else{
            return false;
        }
    }

    public static int calculatePremium(int age, String vehicleType, int numberOfClaims) {

        int finalPremium = BASE_PREMIUM;


        if (isDriverYoung(age)) {
            finalPremium += (BASE_PREMIUM * 20) / 100;
        } else{
            finalPremium += (BASE_PREMIUM * 10) / 100;
        }



        if (vehicleType.equalsIgnoreCase("SUV")) {
            finalPremium += (BASE_PREMIUM * 25) / 100;
        } else if (vehicleType.equalsIgnoreCase("SEDAN")) {
            finalPremium += (BASE_PREMIUM * 15) / 100;
        }



        finalPremium += (CLAIM_LOADING_PER_CLAIM * numberOfClaims);

        return finalPremium;
    }
}
