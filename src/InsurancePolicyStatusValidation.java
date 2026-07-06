import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

class InsurancePolicy {
    private String policyNumber;
    private LocalDate expiryDate;

    public InsurancePolicy(String policyNumber, LocalDate expiryDate) {
        this.policyNumber = policyNumber;
        this.expiryDate = expiryDate;
    }

    public String getPolicyNumber() {
        return policyNumber;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public long getDaysRemaining() {
        return ChronoUnit.DAYS.between(LocalDate.now(), expiryDate);
    }

    public String getStatus() {
        long daysRemaining = getDaysRemaining();

        if (daysRemaining < 0) {
            return "Expired";
        } else if (daysRemaining <= 30) {
            return "Due for Renewal";
        } else {
            return "Active";
        }
    }
}

public class InsurancePolicyStatusValidation {
    public static void main(String[] args) {

        InsurancePolicy policy1 =
                new InsurancePolicy("P1001", LocalDate.of(2026, 12, 15));

        InsurancePolicy policy2 =
                new InsurancePolicy("P1002", LocalDate.of(2026, 7, 10));

        InsurancePolicy policy3 =
                new InsurancePolicy("P1003", LocalDate.of(2026, 3, 5));

        InsurancePolicy[] policies = {policy1, policy2, policy3};

        System.out.printf("%-10s %-15s %-18s %-20s%n",
                "Policy No", "Expiry Date", "Days Remaining", "Status");

        for (InsurancePolicy policy : policies) {
            System.out.printf("%-10s %-15s %-18d %-20s%n",
                    policy.getPolicyNumber(),
                    policy.getExpiryDate(),
                    policy.getDaysRemaining(),
                    policy.getStatus());
        }
    }
}