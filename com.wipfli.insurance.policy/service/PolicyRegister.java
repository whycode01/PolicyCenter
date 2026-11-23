package service;

import model.PolicyDetail;
import model.VehicleType;
import exception.DuplicatePolicyNumberException;
import exception.PolicyNotFoundException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class PolicyRegister {


    private final Map<String, PolicyDetail> policiesByNumber = new HashMap<>();
    private final Map<String, List<PolicyDetail>> policiesByCustomer = new HashMap<>();
    private final Map<VehicleType, List<PolicyDetail>> policiesByVehicleType = new EnumMap<>(VehicleType.class);
    private final TreeMap<LocalDate, List<PolicyDetail>> policiesByExpiryDate = new TreeMap<>();

    private final PremiumCalculable premiumCalculator;

    public PolicyRegister(PremiumCalculable premiumCalculator) {
        this.premiumCalculator = premiumCalculator;
    }

    //policy numbers are unique
    public void add(PolicyDetail policy) {

        String number = policy.getPolicyNum();

        if (policiesByNumber.containsKey(number)) {
            throw new DuplicatePolicyNumberException(
                    number,
                    "a policy with this number is already registered"
            );
        }

        policiesByNumber.put(number, policy);

        String customerName = policy.getCustomer().getCustomerName();
        policiesByCustomer
                .computeIfAbsent(customerName, key -> new ArrayList<>())
                .add(policy);

        VehicleType vehicleType = policy.getVehicleType();
        policiesByVehicleType
                .computeIfAbsent(vehicleType, key -> new ArrayList<>())
                .add(policy);

        LocalDate expiryDate = policy.getExpiryDate();
        policiesByExpiryDate
                .computeIfAbsent(expiryDate, key -> new ArrayList<>())
                .add(policy);
    }

    // missing lookup is an error, NEVER returns null
    public PolicyDetail findByNumber(String number)
            throws PolicyNotFoundException {

        PolicyDetail policy = policiesByNumber.get(number);

        if (policy == null) {
            throw new PolicyNotFoundException(
                    number,
                    "no policy with this number"
            );
        }

        return policy;
    }

    public List<PolicyDetail> findByCustomer(String customerName) {

        List<PolicyDetail> found = policiesByCustomer.get(customerName);

        if (found == null) {
            return new ArrayList<>();
        }

        return new ArrayList<>(found);
    }

    public List<PolicyDetail> findByVehicleType(VehicleType vehicleType) {

        List<PolicyDetail> found = policiesByVehicleType.get(vehicleType);

        if (found == null) {
            return new ArrayList<>();
        }

        return new ArrayList<>(found);
    }

    public List<PolicyDetail> findExpiringWithin(int days, LocalDate today) {

        List<PolicyDetail> expiringSoon = new ArrayList<>();

        LocalDate cutoff = today.plusDays(days);

        for (Map.Entry<LocalDate, List<PolicyDetail>> entry : policiesByExpiryDate.entrySet()) {

            LocalDate expiryDate = entry.getKey();

            if (expiryDate.isAfter(today) && !expiryDate.isAfter(cutoff)) {
                expiringSoon.addAll(entry.getValue());
            }
        }

        return expiringSoon;
    }



    public List<PolicyDetail> findSimilarPolicyHolders(String policyNumber)
            throws PolicyNotFoundException {

        PolicyDetail referencePolicy = findByNumber(policyNumber);
        String referenceCustomer = referencePolicy.getCustomer().getCustomerName();

        Set<VehicleType> referencePattern = new HashSet<>();
        for (PolicyDetail policy : policiesByCustomer.get(referenceCustomer)) {
            referencePattern.add(policy.getVehicleType());
        }

        List<PolicyDetail> similarPolicies = new ArrayList<>();

        for (Map.Entry<String, List<PolicyDetail>> entry : policiesByCustomer.entrySet()) {

            String customerName = entry.getKey();

            if (customerName.equals(referenceCustomer)) {
                continue;
            }

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


    public Map<VehicleType, Double> totalPremiumByVehicleType() {

        Map<VehicleType, Double> totals = new EnumMap<>(VehicleType.class);

        for (Map.Entry<VehicleType, List<PolicyDetail>> entry : policiesByVehicleType.entrySet()) {

            double total = 0.0;

            for (PolicyDetail policy : entry.getValue()) {
                total += premiumCalculator.calculatePremium(policy);
            }

            totals.put(entry.getKey(), total);
        }

        return totals;
    }


    // Stream-based queries for Assignment 8

    public Optional<PolicyDetail> findByPolicyNumber(String number) {
        return Optional.ofNullable(policiesByNumber.get(number));
    }

    public List<PolicyDetail> findByCustomerName(String customerName) {
        return policiesByNumber.values().stream()
                .filter(policy -> policy.getCustomer().getCustomerName().equals(customerName))
                .collect(Collectors.toList());
    }

    public double totalPremiumForVehicleType(VehicleType vehicleType) {
        return policiesByNumber.values().stream()
                .filter(policy -> policy.getVehicleType() == vehicleType)
                .mapToDouble(premiumCalculator::calculatePremium)
                .sum();
    }

    public Map<VehicleType, Long> countPoliciesByVehicleType() {
        return policiesByNumber.values().stream()
                .collect(Collectors.groupingBy(PolicyDetail::getVehicleType, Collectors.counting()));
    }

    public List<PolicyDetail> policiesExpiringWithin(int days, LocalDate referenceDate) {
        LocalDate cutoff = referenceDate.plusDays(days);
        return policiesByExpiryDate.entrySet().stream()
                .filter(entry -> {
                    LocalDate expiryDate = entry.getKey();
                    return expiryDate.isAfter(referenceDate) && !expiryDate.isAfter(cutoff);
                })
                .flatMap(entry -> entry.getValue().stream())
                .sorted(Comparator.comparing(PolicyDetail::getExpiryDate))
                .collect(Collectors.toList());
    }

    public Optional<String> customerWithMostPolicies() {
        return policiesByCustomer.entrySet().stream()
                .max(Comparator.comparingLong(entry -> entry.getValue().size()))
                .map(Map.Entry::getKey);
    }

    public List<PolicyDetail> top5ByPremium() {
        return policiesByNumber.values().stream()
                .sorted(Comparator.comparingDouble(premiumCalculator::calculatePremium).reversed())
                .limit(5)
                .collect(Collectors.toList());
    }

    // Handy for looping over all stored policies
    public Collection<PolicyDetail> getAllPolicies() {
        return policiesByNumber.values();
    }

    public int size() {
        return policiesByNumber.size();
    }
}
