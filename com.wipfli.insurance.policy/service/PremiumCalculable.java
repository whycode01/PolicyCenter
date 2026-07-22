package service;

import model.PolicyDetail;

public interface PremiumCalculable {
    double calculatePremium(
            PolicyDetail policy
    );
}