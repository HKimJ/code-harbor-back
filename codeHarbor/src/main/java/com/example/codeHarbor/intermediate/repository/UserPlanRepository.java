package com.example.codeHarbor.intermediate.repository;

import com.example.codeHarbor.intermediate.domain.UserPlanDomain;
import com.example.codeHarbor.user.domain.UserDomain;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserPlanRepository extends JpaRepository<UserPlanDomain, Long> {

    boolean existsByPlanOwner(UserDomain planOwner);
    boolean existsAllByPlanOwnerAndIsDone(UserDomain planOwner, boolean isDone);
    UserPlanDomain save(UserPlanDomain userPlan);

}
