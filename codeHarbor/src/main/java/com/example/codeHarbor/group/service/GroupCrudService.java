package com.example.codeHarbor.group.service;

import com.example.codeHarbor.group.domain.GroupDomain;
import com.example.codeHarbor.group.dto.GroupCrudRequestDto;
import com.example.codeHarbor.group.dto.GroupCrudResponseDto;
import com.example.codeHarbor.group.repository.GroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GroupCrudService {
    private final GroupRepository groupRepo;
    public GroupCrudResponseDto createGroup(GroupCrudRequestDto input) {
        GroupCrudResponseDto response;
        Map<String, Object> data = new HashMap<>();
        try {
            if(!groupRepo.existsByGroupName(input.getGroupName())) {
                GroupDomain newGroup = new GroupDomain();
                newGroup.setGroupCreator(input.getGroupCreator());
                newGroup.setGroupName(input.getGroupName());
                groupRepo.save(newGroup);
                data.put("mst", "그룹 생성에 성공했습니다.");
                response = GroupCrudResponseDto.builder().success(true).data(data).build();
            } else {
                data.put("msg", "이미 존재하는 그룹 이름입니다.");
                response = GroupCrudResponseDto.builder().success(false).data(data).build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            data.put("msg", "그룹생성중 문제가 발생했습니다. 지속시 관리자에게 문의해주세요.");
            response = GroupCrudResponseDto.builder().success(false).data(data).build();
        }
        return response;
    }

    public GroupCrudResponseDto inviteMemeber(GroupCrudRequestDto input) {
        GroupCrudResponseDto response;
        GroupDomain currentGroup;
        Map<String, Object> data = new HashMap<>();
        try {
            if () {

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
