package com.example.codeHarbor.plan.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="plan_info")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlanDomain {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long planId;
    private String planCreator;
    private String planGroup;
    private String planContent;



}
