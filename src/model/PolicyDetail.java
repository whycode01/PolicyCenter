package model;

public class PolicyDetail {
    private int policyNum;
    private String customerName;
    private int customerAge;
    private String VehicleType;
    private int previousClaimCount;

    public PolicyDetail(int policyNum,String customerName,int customerAge,String vehicleType,int previousClaimCount){
        this.policyNum=policyNum;
        this.customerName=customerName;
        this.customerAge=customerAge;
        this.VehicleType=vehicleType;
        this.previousClaimCount=previousClaimCount;

    }

    public int getPolicyNum(){
        return policyNum;
    }

}
