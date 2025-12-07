package com.global.devices.devicesapi.adaptor.outadaptor.repository.model;

import com.global.devices.devicesapi.domain.model.DeviceState;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.UUID;

@Builder
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "device")
public class DeviceEntity {
    @Id
    @Column(columnDefinition = "uuid")
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String brand;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private DeviceState state;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant creationTime;

}
