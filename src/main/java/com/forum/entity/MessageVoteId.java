package com.forum.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.UUID;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageVoteId implements Serializable {
    
    @Column(name = "message_id", columnDefinition = "UUID")
    private UUID messageId;
    
    @Column(name = "user_id")
    private String userId;
}
