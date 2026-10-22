package com.wipfli.training.controller;

import com.wipfli.training.dto.BikePolicyRequestDTO;
import com.wipfli.training.dto.CarPolicyRequestDTO;
import com.wipfli.training.dto.PolicyResponseDTO;
import com.wipfli.training.dto.PremiumResponseDTO;
import com.wipfli.training.dto.TruckPolicyRequestDTO;
import com.wipfli.training.exception.PolicyNotFoundException;
import com.wipfli.training.model.Customer;
import com.wipfli.training.model.PolicyDetail;
import com.wipfli.training.service.PolicyFactory;
import com.wipfli.training.service.PolicyRegister;
import com.wipfli.training.service.PremiumCalculable;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/policies")
public class PolicyController {

    private final PolicyRegister policyRegister;
    private final PremiumCalculable standardPremiumCalculator;
    private final PremiumCalculable noClaimBonusCalculator;

    public PolicyController(
            PolicyRegister policyRegister,
            @Qualifier("standardPremiumCalculator") PremiumCalculable standardPremiumCalculator,
            @Qualifier("noClaimBonusCalculator") PremiumCalculable noClaimBonusCalculator) {

        this.policyRegister = policyRegister;
        this.standardPremiumCalculator = standardPremiumCalculator;
        this.noClaimBonusCalculator = noClaimBonusCalculator;
    }

    @PostMapping("/car")
    public ResponseEntity<PolicyResponseDTO> createCarPolicy(
            @RequestBody CarPolicyRequestDTO request) {

        PolicyDetail policy = PolicyFactory.createCarPolicy(
                request.getPolicyNum(),
                new Customer(request.getCustomerId(), request.getCustomerName(), request.getCustomerAge()),
                request.getPreviousClaimCount(),
                request.getExpiryDate(),
                request.getRegistrationNumber()
        );

        policyRegister.add(policy);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(toResponse(policy));
    }

    @PostMapping("/bike")
    public ResponseEntity<PolicyResponseDTO> createBikePolicy(
            @RequestBody BikePolicyRequestDTO request) {

        PolicyDetail policy = PolicyFactory.createBikePolicy(
                request.getPolicyNum(),
                new Customer(request.getCustomerId(), request.getCustomerName(), request.getCustomerAge()),
                request.getPreviousClaimCount(),
                request.getExpiryDate(),
                request.getEngineCC()
        );

        policyRegister.add(policy);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(toResponse(policy));
    }

    @PostMapping("/truck")
    public ResponseEntity<PolicyResponseDTO> createTruckPolicy(
            @RequestBody TruckPolicyRequestDTO request) {

        PolicyDetail policy = PolicyFactory.createTruckPolicy(
                request.getPolicyNum(),
                new Customer(request.getCustomerId(), request.getCustomerName(), request.getCustomerAge()),
                request.getPreviousClaimCount(),
                request.getExpiryDate(),
                request.getLoadCapacityTons()
        );

        policyRegister.add(policy);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(toResponse(policy));
    }

    @GetMapping("/{policyNumber}")
    public PolicyResponseDTO getPolicy(
            @PathVariable String policyNumber)
            throws PolicyNotFoundException {

        return toResponse(policyRegister.findByNumber(policyNumber));
    }

    @GetMapping
    public List<PolicyResponseDTO> listPolicies() {
        return policyRegister.getAllPolicies().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @PostMapping("/{policyNumber}/claims")
    public PolicyResponseDTO recordClaim(
            @PathVariable String policyNumber)
            throws PolicyNotFoundException {

        return toResponse(policyRegister.recordClaim(policyNumber));
    }

    @PostMapping("/{policyNumber}/expire")
    public PolicyResponseDTO expirePolicy(
            @PathVariable String policyNumber)
            throws PolicyNotFoundException {

        return toResponse(policyRegister.expire(policyNumber));
    }

    @PostMapping("/{policyNumber}/renew")
    public PolicyResponseDTO renewPolicy(
            @PathVariable String policyNumber,
            @RequestParam("on") @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate on)
            throws PolicyNotFoundException {

        return toResponse(policyRegister.renew(policyNumber, on));
    }

    @GetMapping("/{policyNumber}/premium")
    public PremiumResponseDTO calculatePremium(
            @PathVariable String policyNumber)
            throws PolicyNotFoundException {

        PolicyDetail policy = policyRegister.findByNumber(policyNumber);

        double standardPremium = standardPremiumCalculator.calculatePremium(policy);
        double discountedPremium = noClaimBonusCalculator.calculatePremium(policy);

        return new PremiumResponseDTO(standardPremium, discountedPremium);
    }

    private PolicyResponseDTO toResponse(PolicyDetail policy) {
        return new PolicyResponseDTO(
                policy.getPolicyNum(),
                policy.getCustomer().getCustomerName(),
                policy.getCustomer().getCustomerAge(),
                policy.getVehicleType(),
                policy.getPreviousClaimCount(),
                policy.getStatus(),
                policy.getExpiryDate()
        );
    }
}
