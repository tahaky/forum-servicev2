package com.forum.repository;

import com.forum.entity.AttributeList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AttributeListRepository extends JpaRepository<AttributeList, UUID> {

    List<AttributeList> findByTopicId(UUID topicId);
}
