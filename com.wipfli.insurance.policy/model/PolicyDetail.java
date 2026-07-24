package model;

import java.util.Objects;

public abstract class PolicyDetail {

    public abstract String getPolicyDetails();

    private final String policyNum;
    private Customer customer;
    private VehicleType vehicleType;
    private int previousClaimCount;
    private PolicyStatus status;

    // Constructor with claims history
    public PolicyDetail(String policyNum,
                        Customer customer,
                        VehicleType vehicleType,
                        int previousClaimCount) {

        this.policyNum = policyNum;
        this.customer = customer;
        this.vehicleType = vehicleType;
        this.previousClaimCount = previousClaimCount;
        this.status = PolicyStatus.ACTIVE;
    }

    // Overloaded constructor (no claim history)
    public PolicyDetail(String policyNum,
                        Customer customer,
                        VehicleType vehicleType) {

        this(
                policyNum,
                customer,
                vehicleType,
                0
        );
    }

    public String getPolicyNum() {
        return policyNum;
    }

    public Customer getCustomer() {
        return customer;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public int getPreviousClaimCount() {
        return previousClaimCount;
    }

    public PolicyStatus getStatus() {
        return status;
    }

    private void ensureActive() {

        if (status != PolicyStatus.ACTIVE) {

            throw new IllegalStateException(
                    "Cannot perform operation on policy "
                            + policyNum
                            + ". Status is "
                            + status
            );
        }
    }

    public void expire() {

        ensureActive();

        status = PolicyStatus.EXPIRED;
    }

    public void renew() {

        ensureActive();

        status = PolicyStatus.RENEWED;
    }

    public void recordClaim() {

        previousClaimCount++;
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }

        if (!(obj instanceof PolicyDetail)) {
            return false;
        }

        PolicyDetail other = (PolicyDetail) obj;

        return policyNum.equals(other.policyNum);
    }

    @Override
    public int hashCode() {

        return Objects.hash(policyNum);
    }

    @Override
    public String toString() {

        return "PolicyDetail{" +
                "policyNum='" + policyNum + '\'' +
                ", customer=" + customer +
                ", vehicleType=" + vehicleType +
                ", previousClaimCount=" + previousClaimCount +
                ", status=" + status +
                '}';
    }
}