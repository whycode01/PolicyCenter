package com.wipfli.training.dto;

public class PremiumResponseDTO {

    private double standardPremium;
    private double discountedPremium;

    public PremiumResponseDTO(
            double standardPremium,
            double discountedPremium) {

        this.standardPremium = standardPremium;
        this.discountedPremium = discountedPremium;
    }

    public double getStandardPremium() {
        return standardPremium;
    }

    public double getDiscountedPremium() {
        return discountedPremium;
    }
}