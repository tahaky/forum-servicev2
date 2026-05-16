package com.forum.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "vehicle_brands")
@Data
public class VehicleBrand {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "UUID")
    private UUID id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(name = "logo_svg", columnDefinition = "TEXT")
    private String logoSvg;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}
