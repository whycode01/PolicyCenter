package model;

import java.util.Objects;

public class PolicyDetail {

    private String policyNum;
    private String customerName;
    private int customerAge;
    private VehicleType vehicleType;
    private int previousClaimCount;

    public PolicyDetail(String policyNum,
                        String customerName,
                        int customerAge,
                        VehicleType vehicleType,
                        int previousClaimCount) {

        this.policyNum = policyNum;
        this.customerName = customerName;
        this.customerAge = customerAge;
        this.vehicleType = vehicleType;
        this.previousClaimCount = previousClaimCount;
    }

    public String getPolicyNum() {
        return policyNum;
    }

    public String getCustomerName() {
        return customerName;
    }

    public int getCustomerAge() {
        return customerAge;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public int getPreviousClaimCount() {
        return previousClaimCount;
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
                "policyNum=" + policyNum +
                ", customerName='" + customerName + '\'' +
                ", customerAge=" + customerAge +
                ", vehicleType=" + vehicleType +
                ", previousClaimCount=" + previousClaimCount +
                '}';
    }
}