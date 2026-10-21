package com.wipfli.training.service;

import com.wipfli.training.exception.DuplicatePolicyNumberException;
import com.wipfli.training.exception.PolicyNotFoundException;
import com.wipfli.training.model.PolicyDetail;
import com.wipfli.training.model.VehicleType;
import com.wipfli.training.repository.PolicyRepository;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PolicyRegister {

    private final PremiumCalculatorResolver premiumCalculatorResolver;
    private final PolicyRepository policyRepository;

    public PolicyRegister(
            PremiumCalculatorResolver premiumCalculatorResolver,
            PolicyRepository policyRepository) {

        this.premiumCalculatorResolver = premiumCalculatorResolver;
        this.policyRepository = policyRepository;
    }


    // ---------------------------------------------------------
    // BASIC POLICY OPERATIONS
    // ---------------------------------------------------------

    public void add(PolicyDetail policy) {

        String number = policy.getPolicyNum();

        if (policyRepository.existsByNumber(number)) {

            throw new DuplicatePolicyNumberException(
                    number,
                    "a policy with this number is already registered"
            );
        }

        policyRepository.save(policy);
    }


    public PolicyDetail findByNumber(String number)
            throws PolicyNotFoundException {

        return policyRepository.findByNumber(number)
                .orElseThrow(() ->
                        new PolicyNotFoundException(
                                number,
                                "no policy with this number"
                        )
                );
    }


    public Optional<PolicyDetail> findByPolicyNumber(
            String number) {

        return policyRepository.findByNumber(number);
    }


    // ---------------------------------------------------------
    // CUSTOMER QUERIES
    // ---------------------------------------------------------

    public List<PolicyDetail> findByCustomer(
            String customerName) {

        return policyRepository.findAll()
                .stream()
                .filter(policy ->
                        policy.getCustomer() != null)
                .filter(policy ->
                        customerName.equals(
                                policy.getCustomer()
                                        .getCustomerName()))
                .collect(Collectors.toList());
    }


    public List<PolicyDetail> findByCustomerName(
            String customerName) {

        return policyRepository.findAll()
                .stream()
                .filter(policy ->
                        policy.getCustomer() != null)
                .filter(policy ->
                        customerName.equals(
                                policy.getCustomer()
                                        .getCustomerName()))
                .collect(Collectors.toList());
    }


    // ---------------------------------------------------------
    // VEHICLE TYPE QUERIES
    // ---------------------------------------------------------

    public List<PolicyDetail> findByVehicleType(
            VehicleType vehicleType) {

        return policyRepository.findAll()
                .stream()
                .filter(policy ->
                        vehicleType ==
                                policy.getVehicleType())
                .collect(Collectors.toList());
    }


    public double totalPremiumForVehicleType(
            VehicleType vehicleType) {

        return policyRepository.findAll()
                .stream()
                .filter(policy ->
                        policy.getVehicleType()
                                == vehicleType)
                .mapToDouble(policy ->
                        premiumCalculatorResolver
                                .resolve(policy)
                                .calculatePremium(policy))
                .sum();
    }


    public Map<VehicleType, Double>
    totalPremiumByVehicleType() {

        return policyRepository.findAll()
                .stream()
                .collect(
                        Collectors.groupingBy(
                                PolicyDetail::getVehicleType,
                                Collectors.summingDouble(
                                        policy -> premiumCalculatorResolver
                                                .resolve(policy)
                                                .calculatePremium(policy)
                                )
                        )
                );
    }


    public Map<VehicleType, Long>
    countPoliciesByVehicleType() {

        return policyRepository.findAll()
                .stream()
                .collect(
                        Collectors.groupingBy(
                                PolicyDetail::getVehicleType,
                                Collectors.counting()
                        )
                );
    }


    // ---------------------------------------------------------
    // EXPIRY QUERIES
    // ---------------------------------------------------------

    public List<PolicyDetail> findExpiringWithin(
            int days,
            LocalDate today) {

        LocalDate cutoff =
                today.plusDays(days);

        return policyRepository.findAll()
                .stream()
                .filter(policy ->
                        policy.getExpiryDate() != null)
                .filter(policy ->
                        policy.getExpiryDate()
                                .isAfter(today))
                .filter(policy ->
                        !policy.getExpiryDate()
                                .isAfter(cutoff))
                .sorted(
                        Comparator.comparing(
                                PolicyDetail::getExpiryDate))
                .collect(Collectors.toList());
    }


    public List<PolicyDetail> policiesExpiringWithin(
            int days,
            LocalDate referenceDate) {

        LocalDate cutoff =
                referenceDate.plusDays(days);

        return policyRepository.findAll()
                .stream()
                .filter(policy ->
                        policy.getExpiryDate() != null)
                .filter(policy ->
                        policy.getExpiryDate()
                                .isAfter(referenceDate))
                .filter(policy ->
                        !policy.getExpiryDate()
                                .isAfter(cutoff))
                .sorted(
                        Comparator.comparing(
                                PolicyDetail::getExpiryDate))
                .collect(Collectors.toList());
    }


    // ---------------------------------------------------------
    // SIMILAR POLICY HOLDERS
    // ---------------------------------------------------------

    public List<PolicyDetail> findSimilarPolicyHolders(
            String policyNumber)
            throws PolicyNotFoundException {

        PolicyDetail referencePolicy =
                findByNumber(policyNumber);

        String referenceCustomer =
                referencePolicy
                        .getCustomer()
                        .getCustomerName();

        Set<VehicleType> referencePattern =
                policyRepository.findAll()
                        .stream()
                        .filter(policy ->
                                policy.getCustomer() != null)
                        .filter(policy ->
                                referenceCustomer.equals(
                                        policy.getCustomer()
                                                .getCustomerName()))
                        .map(PolicyDetail::getVehicleType)
                        .collect(Collectors.toSet());

        Map<String, List<PolicyDetail>> policiesByCustomer =
                policyRepository.findAll()
                        .stream()
                        .filter(policy ->
                                policy.getCustomer() != null)
                        .filter(policy ->
                                !referenceCustomer.equals(
                                        policy.getCustomer()
                                                .getCustomerName()))
                        .collect(
                                Collectors.groupingBy(
                                        policy ->
                                                policy.getCustomer()
                                                        .getCustomerName()
                                )
                        );

        List<PolicyDetail> similarPolicies =
                new ArrayList<>();

        for (Map.Entry<String, List<PolicyDetail>> entry
                : policiesByCustomer.entrySet()) {

            Set<VehicleType> customerPattern =
                    new HashSet<>();

            for (PolicyDetail policy
                    : entry.getValue()) {

                customerPattern.add(
                        policy.getVehicleType());
            }

            if (customerPattern.equals(
                    referencePattern)) {

                similarPolicies.addAll(
                        entry.getValue());
            }
        }

        return similarPolicies;
    }


    // ---------------------------------------------------------
    // STREAM QUERIES
    // ---------------------------------------------------------

    public Optional<String> customerWithMostPolicies() {

        return policyRepository.findAll()
                .stream()
                .filter(policy ->
                        policy.getCustomer() != null)
                .collect(
                        Collectors.groupingBy(
                                policy ->
                                        policy.getCustomer()
                                                .getCustomerName(),
                                Collectors.counting()
                        )
                )
                .entrySet()
                .stream()
                .max(
                        Map.Entry.comparingByValue())
                .map(
                        Map.Entry::getKey);
    }


    public List<PolicyDetail> top5ByPremium() {

        Comparator<PolicyDetail> byPremium = Comparator.comparingDouble(
                policy -> premiumCalculatorResolver
                        .resolve(policy)
                        .calculatePremium(policy)
        );

        return policyRepository.findAll()
                .stream()
                .sorted(byPremium.reversed())
                .limit(5)
                .collect(Collectors.toList());
    }


    // ---------------------------------------------------------
    // ALL POLICIES
    // ---------------------------------------------------------

    public Collection<PolicyDetail> getAllPolicies() {

        return policyRepository.findAll();
    }


    public int size() {

        return policyRepository
                .findAll()
                .size();
    }
}