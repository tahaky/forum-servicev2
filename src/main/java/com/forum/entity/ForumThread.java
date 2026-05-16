package com.forum.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "threads")
@Data
public class ForumThread {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "UUID")
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(nullable = false)
    private String type;

    @Column(name = "vehicle_brand_id", insertable = false, updatable = false)
    private UUID vehicleBrandId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_brand_id", nullable = false)
    private VehicleBrand vehicleBrand;

    @Column(nullable = false)
    private String title;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "thread", cascade = CascadeType.ALL)
    private List<Subthread> subthreads = new ArrayList<>();
}
