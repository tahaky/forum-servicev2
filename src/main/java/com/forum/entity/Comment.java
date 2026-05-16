package com.forum.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Table(name = "comment")
@Data
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "UUID")
    private UUID id;

    @Column(name = "topic_id", insertable = false, updatable = false)
    private UUID topicId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topic_id", nullable = false)
    private Topic topic;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String comment;

    @Column(name = "user_id", nullable = false, columnDefinition = "UUID")
    private UUID userId;
}
