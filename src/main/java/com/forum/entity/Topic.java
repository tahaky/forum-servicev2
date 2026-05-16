package com.forum.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "topic")
@Data
public class Topic {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "UUID")
    private UUID id;

    @Column(name = "user_id", nullable = false, columnDefinition = "UUID")
    private UUID userId;

    @Column(name = "topic_name", nullable = false)
    private String topicName;

    @Column(name = "initial_message", nullable = false, columnDefinition = "TEXT")
    private String initialMessage;

    @OneToMany(mappedBy = "topic", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<AttributeList> attributes = new ArrayList<>();

    @OneToMany(mappedBy = "topic", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Comment> comments = new ArrayList<>();
}
