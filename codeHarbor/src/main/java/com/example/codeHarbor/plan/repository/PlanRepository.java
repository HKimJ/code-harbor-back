package com.example.codeHarbor.plan.repository;

import com.example.codeHarbor.plan.domain.PlanDomain;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlanRepository extends JpaRepository<PlanDomain, Long> {

    PlanDomain save(PlanDomain plan);
}
