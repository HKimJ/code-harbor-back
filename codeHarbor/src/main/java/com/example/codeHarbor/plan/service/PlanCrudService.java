package com.example.codeHarbor.plan.service;

import com.example.codeHarbor.plan.domain.PlanDomain;
import com.example.codeHarbor.plan.dto.PlanCrudRequestDto;
import com.example.codeHarbor.plan.dto.PlanCrudResponseDto;
import com.example.codeHarbor.plan.repository.PlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PlanCrudService {
    private final PlanRepository planRepo;

    public PlanCrudResponseDto createPlan(PlanCrudRequestDto input) {
        PlanCrudResponseDto response;
        Map<String, Object> data = new HashMap<>();
        PlanDomain newPlan;
        try {
            if (input.getUserId() != null && input.getGroupName() != null) {
                data.put("msg", "계획이 생성되었습니다.");
                response = PlanCrudResponseDto.builder().success(true).data(data).build();
            } else {
                data.put("msg", "올바르지 않은 형태의 요청 및 접근입니다.");
                response = PlanCrudResponseDto.builder().success(false).data(data).build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            data.put("msg", "계획 생성도중 문제가 발생했습니다. 지속시 관리자에게 문의해주세요.");
            response = PlanCrudResponseDto.builder().success(false).data(data).build();
        }

        return response;
    }
}
