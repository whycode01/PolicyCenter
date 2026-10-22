package com.wipfli.training.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

import com.wipfli.training.exception.IllegalStatusChangeException;
import com.wipfli.training.exception.InvalidPolicyDataException;
import com.wipfli.training.exception.RenewalNotAllowedException;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;


@Entity
@Table(name = "policy")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "policy_type", discriminatorType = DiscriminatorType.STRING)
public abstract class PolicyDetail {

    public abstract String getPolicyDetails();


    @Id
    @Column(name = "policy_num", updatable = false, nullable = false)
    private String policyNum;


    @ManyToOne(optional = false, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "customer_user_id",nullable = false)
    private Customer customer;


    @Enumerated(EnumType.STRING)
    @Column(name = "vehicle_type", nullable = false)
    private VehicleType vehicleType;

    @Column(name = "previous_claim_count", nullable = false)
    private int previousClaimCount;

    // Stores "ACTIVE" / "EXPIRED" / "RENEWED" as text.
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private PolicyStatus status;

    // 'final' removed for the same reason as policyNum. There is still no setter.
    @Column(name = "expiry_date", nullable = false)
    private LocalDate expiryDate;

    // static fields are ignored by JPA, so these are not columns
    private static final int RENEWAL_WINDOW_DAYS = 30;
    private static final int CLAIMS_CAP = 3;


    protected PolicyDetail() {
    }

    public PolicyDetail(String policyNum,
                        Customer customer,
                        VehicleType vehicleType,
                        int previousClaimCount,
                        LocalDate expiryDate) {

        if (expiryDate == null) {
            throw new InvalidPolicyDataException(
                    policyNum,
                    "expiry date is missing"
            );
        }

        if (customer == null) {
            throw new InvalidPolicyDataException(policyNum, "customer is missing");
        }

        this.policyNum = policyNum;
        this.customer = customer;
        this.vehicleType = vehicleType;
        this.previousClaimCount = previousClaimCount;
        this.expiryDate = expiryDate;
        this.status = PolicyStatus.ACTIVE;
    }

    public PolicyDetail(String policyNum,
                        Customer customer,
                        VehicleType vehicleType,
                        LocalDate expiryDate) {

        this(policyNum, customer, vehicleType, 0, expiryDate);
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

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    private void ensureActive(String action) {

        if (status != PolicyStatus.ACTIVE) {
            throw new IllegalStatusChangeException(
                    policyNum,
                    "cannot " + action + " a policy that is " + status
            );
        }
    }

    public void expire() {

        ensureActive("expire");
        status = PolicyStatus.EXPIRED;
    }

    public void renew(LocalDate today) {

        ensureActive("renew");

        if (previousClaimCount >= CLAIMS_CAP) {
            throw new RenewalNotAllowedException(
                    policyNum,
                    previousClaimCount
                            + " claims on record, "
                            + CLAIMS_CAP
                            + " or more must be referred to underwriting"
            );
        }

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


        return Objects.equals(policyNum, other.getPolicyNum());
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