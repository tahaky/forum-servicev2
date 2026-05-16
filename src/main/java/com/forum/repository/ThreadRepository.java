package com.forum.repository;

import com.forum.entity.ForumThread;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface ThreadRepository extends JpaRepository<ForumThread, UUID> {
    List<ForumThread> findByVehicleBrandId(UUID vehicleBrandId);
    List<ForumThread> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
