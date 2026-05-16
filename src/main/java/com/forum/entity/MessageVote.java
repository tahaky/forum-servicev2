package com.forum.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "message_votes")
@Data
public class MessageVote {
    
    @EmbeddedId
    private MessageVoteId id;
    
    @Column(nullable = false)
    private Boolean upvoted;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("messageId")
    @JoinColumn(name = "message_id", nullable = false)
    private Message message;
}
