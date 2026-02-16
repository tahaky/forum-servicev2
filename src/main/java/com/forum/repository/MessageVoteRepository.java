package com.forum.repository;

import com.forum.entity.MessageVote;
import com.forum.entity.MessageVoteId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageVoteRepository extends JpaRepository<MessageVote, MessageVoteId> {
}
