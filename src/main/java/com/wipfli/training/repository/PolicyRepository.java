package com.wipfli.training.repository;

import com.wipfli.training.model.PolicyDetail;

import java.util.Collection;
import java.util.Optional;

public interface PolicyRepository {

    void save(PolicyDetail policy);

    Optional<PolicyDetail> findByNumber(String number);

    boolean existsByNumber(String number);

    Collection<PolicyDetail> findAll();
}