package com.example.codeHarbor.intermediate.domain;

import com.example.codeHarbor.group.domain.GroupDomain;
import com.example.codeHarbor.plan.domain.PlanDomain;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "group_plans")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GroupPlanDomain {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long groupPlanId;
    @ManyToOne
    private GroupDomain parentGroup;
    @ManyToOne
    private PlanDomain plans;
}
