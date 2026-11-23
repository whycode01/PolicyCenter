package com.wipfli.training.service;

import com.wipfli.training.exception.DuplicatePolicyNumberException;
import com.wipfli.training.exception.PolicyNotFoundException;
import com.wipfli.training.model.PolicyDetail;
import com.wipfli.training.model.VehicleType;
import com.wipfli.training.repository.PolicyRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class PolicyRegister {

    public static final LocalDate REFERENCE_DATE = LocalDate.of(2026, 8, 9);

    private final PremiumCalculatorResolver premiumCalculatorResolver;
    private final PolicyRepository policyRepository;

    public PolicyRegister(PremiumCalculatorResolver premiumCalculatorResolver,
                          PolicyRepository policyRepository) {
        this.premiumCalculatorResolver = premiumCalculatorResolver;
        this.policyRepository = policyRepository;
    }


    @Transactional
    public void add(PolicyDetail policy) {
        String number = policy.getPolicyNum();

        if (policyRepository.existsById(number)) {
            throw new DuplicatePolicyNumberException(
                    number, "a policy with this number is already registered");
        }

        policyRepository.save(policy);
    }

    public PolicyDetail findByNumber(String number) throws PolicyNotFoundException {
        return policyRepository.findById(number)
                .orElseThrow(() -> new PolicyNotFoundException(
                        number, "no policy with this number"));
    }



    @Transactional
    public PolicyDetail expire(String number) throws PolicyNotFoundException {
        PolicyDetail policy = findByNumber(number);
        policy.expire();
        return policyRepository.save(policy);
    }

    @Transactional
    public PolicyDetail renew(String number, LocalDate on) throws PolicyNotFoundException {
        PolicyDetail policy = findByNumber(number);
        policy.renew(on);
        return policyRepository.save(policy);
    }

    @Transactional
    public PolicyDetail recordClaim(String number) throws PolicyNotFoundException {
        PolicyDetail policy = findByNumber(number);
        policy.recordClaim();
        return policyRepository.save(policy);
    }

    public Optional<PolicyDetail> findByPolicyNumber(String number) {
        return policyRepository.findById(number);
    }



    public List<PolicyDetail> findByCustomer(String customerName) {
        return policyRepository.findByCustomerCustomerName(customerName);
    }

    public List<PolicyDetail> findByCustomerName(String customerName) {
        return policyRepository.findByCustomerCustomerName(customerName);
    }



    public List<PolicyDetail> findByVehicleType(VehicleType vehicleType) {
        return policyRepository.findByVehicleType(vehicleType);
    }


    public double totalPremiumForVehicleType(VehicleType vehicleType) {
        return policyRepository.findByVehicleType(vehicleType)
                .stream()
                .mapToDouble(this::premiumOf)
                .sum();
    }

    public Map<VehicleType, Double> totalPremiumByVehicleType() {
        return policyRepository.findAll()
                .stream()
                .collect(Collectors.groupingBy(
                        PolicyDetail::getVehicleType,
                        Collectors.summingDouble(this::premiumOf)));
    }

    public Map<VehicleType, Long> countPoliciesByVehicleType() {
        Map<VehicleType, Long> counts = new EnumMap<>(VehicleType.class);
        for (Object[] row : policyRepository.countGroupedByVehicleType()) {
            counts.put((VehicleType) row[0], (Long) row[1]);
        }
        return counts;
    }



    public List<PolicyDetail> findExpiringWithin(int days, LocalDate today) {
        return policyRepository.findExpiringBetween(today, today.plusDays(days));
    }

    public List<PolicyDetail> policiesExpiringWithin(int days, LocalDate referenceDate) {
        return policyRepository.findExpiringBetween(referenceDate, referenceDate.plusDays(days));
    }


    public List<PolicyDetail> policiesExpiringWithin(int days) {
        return policiesExpiringWithin(days, REFERENCE_DATE);
    }



    public List<PolicyDetail> findSimilarPolicyHolders(String policyNumber)
            throws PolicyNotFoundException {

        PolicyDetail referencePolicy = findByNumber(policyNumber);
        String referenceCustomer = referencePolicy.getCustomer().getCustomerName();

        Set<VehicleType> referencePattern =
                policyRepository.findByCustomerCustomerName(referenceCustomer)
                        .stream()
                        .map(PolicyDetail::getVehicleType)
                        .collect(Collectors.toSet());

        Map<String, List<PolicyDetail>> policiesByCustomer =
                policyRepository.findAll()
                        .stream()
                        .filter(p -> !referenceCustomer.equals(p.getCustomer().getCustomerName()))
                        .collect(Collectors.groupingBy(p -> p.getCustomer().getCustomerName()));

        List<PolicyDetail> similarPolicies = new ArrayList<>();

        for (Map.Entry<String, List<PolicyDetail>> entry : policiesByCustomer.entrySet()) {
            Set<VehicleType> customerPattern = new HashSet<>();
            for (PolicyDetail policy : entry.getValue()) {
                customerPattern.add(policy.getVehicleType());
            }
            if (customerPattern.equals(referencePattern)) {
                similarPolicies.addAll(entry.getValue());
            }
        }

        return similarPolicies;
    }



    public Optional<String> customerWithMostPolicies() {
        return policyRepository.findAll()
                .stream()
                .collect(Collectors.groupingBy(
                        p -> p.getCustomer().getCustomerName(),
                        Collectors.counting()))
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey);
    }

    public List<PolicyDetail> top5ByPremium() {
        Comparator<PolicyDetail> byPremium = Comparator.comparingDouble(this::premiumOf);

        return policyRepository.findAll()
                .stream()
                .sorted(byPremium.reversed())
                .limit(5)
                .collect(Collectors.toList());
    }



    public Collection<PolicyDetail> getAllPolicies() {
        return policyRepository.findAll();
    }

    public int size() {
        return (int) policyRepository.count();   // SELECT COUNT(*), no rows loaded
    }

    private double premiumOf(PolicyDetail policy) {
        return premiumCalculatorResolver.resolve(policy).calculatePremium(policy);
    }
}