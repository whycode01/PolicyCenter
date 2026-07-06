import java.util.*;

class Policy {

    private String policyNumber;
    private String holderName;

    public Policy(String policyNumber, String holderName) {
        this.policyNumber = policyNumber;
        this.holderName = holderName;
    }

    public String getPolicyNumber() {
        return policyNumber;
    }

    public String getHolderName() {
        return holderName;
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj)
            return true;

        if (obj == null || getClass() != obj.getClass())
            return false;

        Policy other = (Policy) obj;

        return policyNumber.equalsIgnoreCase(other.policyNumber);
    }

    @Override
    public int hashCode() {
        return policyNumber.toLowerCase().hashCode();
    }

    @Override
    public String toString() {
        return "Policy Number: " + policyNumber +
                ", Holder Name: " + holderName;
    }
}

public class Exercise3 {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);



        System.out.print("Enter number of customers: ");
        int customerCount = sc.nextInt();
        sc.nextLine();

        TreeSet<String> customerSet =
                new TreeSet<>(String.CASE_INSENSITIVE_ORDER);

        System.out.println("Enter customer names:");

        for (int i = 0; i < customerCount; i++) {
            customerSet.add(sc.nextLine());
        }

        System.out.println("\nUnique Sorted Customer List:");
        for (String customer : customerSet) {
            System.out.println(customer);
        }



        System.out.print("\nEnter number of policies: ");
        int policyCount = sc.nextInt();
        sc.nextLine();

        Set<Policy> policySet = new HashSet<>();

        System.out.println("\nEnter Policy Details:");

        for (int i = 0; i < policyCount; i++) {

            System.out.print("Enter Policy Number: ");
            String policyNumber = sc.nextLine();

            System.out.print("Enter Holder Name: ");
            String holderName = sc.nextLine();

            Policy policy = new Policy(policyNumber, holderName);

            if (!policySet.add(policy)) {
                System.out.println(
                        "Duplicate Policy Found: "
                                + policyNumber);
            }
        }

        System.out.println("\nUnique Policies:");

        for (Policy policy : policySet) {
            System.out.println(policy);
        }

        sc.close();
    }
}