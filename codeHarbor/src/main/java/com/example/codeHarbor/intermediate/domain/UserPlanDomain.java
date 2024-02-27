package com.example.codeHarbor.intermediate.domain;

import com.example.codeHarbor.plan.domain.PlanDomain;
import com.example.codeHarbor.user.domain.UserDomain;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_olan")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserPlanDomain {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userPlanId;

    @ManyToOne
    private UserDomain planOwner;

    @ManyToOne
    private PlanDomain planOfUser;

    @Temporal(TemporalType.TIMESTAMP) @Column(columnDefinition = "DATETIME DEFAULT (CURRENT_TIMESTAMP)", insertable = false, updatable = false)
    private LocalDateTime planCreatedDateTime;

    @Column(columnDefinition = "TINYINT(1) DEFAULT 0", insertable = false)
    private boolean isDone;
}
