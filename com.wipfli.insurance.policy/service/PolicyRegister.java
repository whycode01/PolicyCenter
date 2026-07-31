package service;

import model.PolicyDetail;
import exception.DuplicatePolicyNumberException;
import exception.PolicyNotFoundException;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class PolicyRegister {

    // Collections Framework: key = policy number, value = the policy
    private final Map<String, PolicyDetail> policies = new HashMap<>();

    // Rule 5: policy numbers are unique
    public void add(PolicyDetail policy) {

        String number = policy.getPolicyNum();

        if (policies.containsKey(number)) {
            throw new DuplicatePolicyNumberException(
                    number,
                    "a policy with this number is already registered"
            );
        }

        policies.put(number, policy);
    }

    // Rule 6: missing lookup is an error, NEVER returns null
    public PolicyDetail findByNumber(String number)
            throws PolicyNotFoundException {

        PolicyDetail policy = policies.get(number);

        if (policy == null) {
            throw new PolicyNotFoundException(
                    number,
                    "no policy with this number"
            );
        }

        return policy;
    }

    // Handy for looping over all stored policies (premium summary, etc.)
    public Collection<PolicyDetail> getAllPolicies() {
        return policies.values();
    }

    public int size() {
        return policies.size();
    }
}