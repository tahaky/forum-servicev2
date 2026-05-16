package com.forum.repository;

import com.forum.entity.VehicleBrand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface VehicleBrandRepository extends JpaRepository<VehicleBrand, UUID> {
    boolean existsByName(String name);
}
