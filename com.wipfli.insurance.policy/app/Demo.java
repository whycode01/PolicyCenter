package app;

import model.Customer;
import model.PolicyDetail;
import model.VehicleType;

public class Demo {

    public static void main(String[] args) {

        Customer customer =
                new Customer(
                        "Gaurav",
                        22
                );

        PolicyDetail policy =
                new PolicyDetail(
                        "POL101",
                        customer,
                        VehicleType.CAR,
                        1
                );

        System.out.println(
                "Created "
                        + policy.getPolicyNum()
                        + " for "
                        + policy.getCustomer()
                        .getCustomerName()
                        + " (age "
                        + policy.getCustomer()
                        .getCustomerAge()
                        + "), "
                        + policy.getVehicleType()
                        + ", "
                        + policy.getPreviousClaimCount()
                        + " claim(s)"
                        + " - Status "
                        + policy.getStatus()
        );

        policy.recordClaim();

        System.out.println(
                "\nrecordClaim() called"
        );

        System.out.println(
                "Claims are now : "
                        + policy.getPreviousClaimCount()
        );

        policy.expire();

        System.out.println(
                "\nexpire() called"
        );

        System.out.println(
                "Status is now : "
                        + policy.getStatus()
        );

        try {

            policy.renew();

        } catch (Exception e) {

            System.out.println(
                    "\nrenew() called"
            );

            System.out.println(
                    "REJECTED : "
                            + e.getMessage()
            );
        }
    }
}