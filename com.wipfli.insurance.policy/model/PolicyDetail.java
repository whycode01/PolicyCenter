package model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

// CHANGE: import your custom exceptions (no more generic exceptions)
import exception.InvalidPolicyDataException;
import exception.IllegalStatusChangeException;
import exception.RenewalNotAllowedException;

public abstract class PolicyDetail {

    public abstract String getPolicyDetails();

    private final String policyNum;
    private Customer customer;
    private VehicleType vehicleType;
    private int previousClaimCount;
    private PolicyStatus status;

    // CHANGE: every policy now has an expiry date
    private final LocalDate expiryDate;

    // CHANGE: renewal window is a shared business constant
    private static final int RENEWAL_WINDOW_DAYS = 30;

    // CHANGE: claims cap constant
    private static final int CLAIMS_CAP = 3;

    // Constructor with claims history
    // CHANGE: now takes expiryDate and validates it
    public PolicyDetail(String policyNum,
                        Customer customer,
                        VehicleType vehicleType,
                        int previousClaimCount,
                        LocalDate expiryDate) {

        // CHANGE: expiry cannot be missing; date-past checks use the fixed reference date in tests
        if (expiryDate == null) {
            throw new InvalidPolicyDataException(
                    policyNum,
                    "expiry date is missing"
            );
        }

        this.policyNum = policyNum;
        this.customer = customer;
        this.vehicleType = vehicleType;
        this.previousClaimCount = previousClaimCount;
        this.expiryDate = expiryDate;
        this.status = PolicyStatus.ACTIVE;
    }

    // Overloaded constructor (no claim history)
    // CHANGE: also takes expiryDate now
    public PolicyDetail(String policyNum,
                        Customer customer,
                        VehicleType vehicleType,
                        LocalDate expiryDate) {

        this(
                policyNum,
                customer,
                vehicleType,
                0,
                expiryDate
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

    // CHANGE: getter for the new field
    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    // CHANGE: helper now throws YOUR exception instead of IllegalStateException
    private void ensureActive(String action) {

        if (status != PolicyStatus.ACTIVE) {

            throw new IllegalStatusChangeException(
                    policyNum,
                    "cannot " + action + " a policy that is " + status
            );
        }
    }

    // CHANGE: throws IllegalStatusChangeException (not IllegalStateException)
    public void expire() {

        ensureActive("expire");

        status = PolicyStatus.EXPIRED;
    }

    // CHANGE: renew() now takes today's date and enforces the two new rules
    public void renew(LocalDate today) {

        // Rule: must still be ACTIVE
        ensureActive("renew");

        // Rule: claims cap — 3 or more must go to underwriting
        if (previousClaimCount >= CLAIMS_CAP) {

            throw new RenewalNotAllowedException(
                    policyNum,
                    previousClaimCount
                            + " claims on record, "
                            + CLAIMS_CAP
                            + " or more must be referred to underwriting"
            );
        }

        // Rule: renewal window — only in the last 30 days before expiry
        LocalDate renewalOpens = expiryDate.minusDays(RENEWAL_WINDOW_DAYS);

        if (today.isBefore(renewalOpens)) {

            long daysEarly = ChronoUnit.DAYS.between(today, renewalOpens);

            throw new RenewalNotAllowedException(
                    policyNum,
                    "renewal opens on " + renewalOpens
                            + ", that is " + daysEarly + " days away"
            );
        }

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
                ", expiryDate=" + expiryDate +
                '}';
    }
}