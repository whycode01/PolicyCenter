package com.wipfli.training.repository;

import com.wipfli.training.model.PolicyDetail;
import com.wipfli.training.model.VehicleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;


public interface PolicyRepository extends JpaRepository<PolicyDetail, String> {


    List<PolicyDetail> findByCustomerCustomerName(String customerName);

    // DERIVED FROM THE NAME: WHERE vehicle_type = ?
    List<PolicyDetail> findByVehicleType(VehicleType vehicleType);


    @Query("""
           SELECT p FROM PolicyDetail p
           WHERE p.expiryDate > :from
             AND p.expiryDate <= :to
           ORDER BY p.expiryDate
           """)
    List<PolicyDetail> findExpiringBetween(@Param("from") LocalDate from,
                                           @Param("to") LocalDate to);

    // WRITTEN BY HAND: a GROUP BY count can't be expressed as a method name.
    @Query("SELECT p.vehicleType, COUNT(p) FROM PolicyDetail p GROUP BY p.vehicleType")
    List<Object[]> countGroupedByVehicleType();
}