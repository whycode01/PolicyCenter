package com.wipfli.training.repository;

import com.wipfli.training.model.PolicyDetail;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class InMemoryPolicyRepository implements PolicyRepository {

    private final Map<String, PolicyDetail> policiesByNumber =
            new HashMap<>();

    @Override
    public void save(PolicyDetail policy) {

        policiesByNumber.put(
                policy.getPolicyNum(),
                policy
        );
    }

    @Override
    public Optional<PolicyDetail> findByNumber(String number) {

        return Optional.ofNullable(
                policiesByNumber.get(number)
        );
    }

    @Override
    public boolean existsByNumber(String number) {

        return policiesByNumber.containsKey(number);
    }

    @Override
    public Collection<PolicyDetail> findAll() {

        return new ArrayList<>(
                policiesByNumber.values()
        );
    }
}