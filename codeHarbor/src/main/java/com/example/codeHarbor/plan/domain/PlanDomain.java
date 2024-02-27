package com.example.codeHarbor.plan.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="plan_info")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlanDomain {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long planId;

    private String planContent;

    @Column(columnDefinition = "TINYINT(1) DEFAULT 0", nullable = false)
    private boolean isDone;


}
