package com.forum.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "messages_table")
@Data
public class Message {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "UUID")
    private UUID id;
    
    @Column(name = "user_id", nullable = false)
    private String userId;
    
    @Column(nullable = false, columnDefinition = "TEXT")
    private String body;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "upvote_count", nullable = false)
    private Integer upvoteCount = 0;
    
    @Column(nullable = false)
    private Boolean deleted = false;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subthread_id", nullable = false)
    private Subthread subthread;
    
    @OneToMany(mappedBy = "message", cascade = CascadeType.ALL)
    private List<MessageVote> votes = new ArrayList<>();
}
