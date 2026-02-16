package com.forum.repository;

import com.forum.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface MessageRepository extends JpaRepository<Message, UUID> {
    List<Message> findBySubthreadId(UUID subthreadId);
    
    @Query("SELECT m FROM Message m WHERE m.subthread.thread.id = :threadId")
    List<Message> findByThreadId(@Param("threadId") UUID threadId);
}
