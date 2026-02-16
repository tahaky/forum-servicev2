package com.forum.repository;

import com.forum.entity.Subthread;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface SubthreadRepository extends JpaRepository<Subthread, UUID> {
    List<Subthread> findByThreadId(UUID threadId);
}
