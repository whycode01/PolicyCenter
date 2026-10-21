package com.wipfli.training.service;

import com.wipfli.training.model.PolicyDetail;

public interface PremiumCalculable {
    double calculatePremium(PolicyDetail policy);
}