package com.dpatrones.proyecto.domain.model;

import java.sql.Timestamp;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_roles")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRole {

    @EmbeddedId
    private UserRoleId id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("roleId")
    @JoinColumn(name = "role_id")
    private Role role;

    @Column(name = "assigned_at", nullable = false)
    private Timestamp assignedAt;

    @PrePersist
    protected void onCreate() {
        if (this.assignedAt == null) {
            this.assignedAt = new Timestamp(System.currentTimeMillis());
        }
    }
}
