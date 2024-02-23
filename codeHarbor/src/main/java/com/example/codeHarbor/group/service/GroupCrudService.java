package com.example.codeHarbor.group.service;

import com.example.codeHarbor.child.repository.UserGroupRepository;
import com.example.codeHarbor.group.domain.GroupDomain;
import com.example.codeHarbor.group.dto.GroupCrudRequestDto;
import com.example.codeHarbor.group.dto.GroupCrudResponseDto;
import com.example.codeHarbor.group.repository.GroupRepository;
import com.example.codeHarbor.tool.javamail.JavaMailService;
import com.example.codeHarbor.tool.redis.RedisService;
import com.example.codeHarbor.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GroupCrudService {
    private final UserRepository userRepo;
    private final GroupRepository groupRepo;
    private final UserGroupRepository userGroupRepo;
    private final JavaMailService mailSender;
    private final RedisService redisService;
    private final String ACCEPT_NEW_URL = "127.0.0.1:8081/GroupCrud/verifyAcceptNew";
    private final String ACCEPT_EXIST_URL = "127.0.0.1:8081/GroupCrud/verifyAcceptExist";
    private final String REDIRECT_URL = "127.0.0.1:5173/GroupCrud/signUp";

    public GroupCrudResponseDto createGroup(GroupCrudRequestDto input) {
        GroupCrudResponseDto response;
        Map<String, Object> data = new HashMap<>();
        try {
            if (!groupRepo.existsByGroupName(input.getGroupName())) {
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

    public GroupCrudResponseDto inviteMember(GroupCrudRequestDto input) {
        GroupCrudResponseDto response;
        GroupDomain currentGroup;
        Map<String, Object> data = new HashMap<>();
        try {
            currentGroup = groupRepo.findGroupByGroupName(input.getGroupName());
            if (!userGroupRepo.existsByUserAndGroup(userRepo.findUserByUserId(input.getGroupInvitee()), currentGroup)) {
                if (userRepo.findUserByUserId(input.getGroupInvitee()) != null) {
                    String verifyValue = redisService.generateCode("temp");
                    // 초대 메일 보내는 로직
                    response = mailSender.sendGroupInvitaiondMail(input.getGroupInvitee(), input.getGroupName(), ACCEPT_EXIST_URL, verifyValue);
                } else {
                    // 가입 메일 보내는 로직 + 가입 후 자동으로 그룹에 포함시키는 로직(추가 예정)
                    response = mailSender.sendSignUpMail(input.getGroupInvitee(), input.getGroupName(), REDIRECT_URL);
                }
            } else {
                data.put("msg", "이미 초대된 그룹원입니다.");
                response = GroupCrudResponseDto.builder().success(false).data(data).build();
            }

        } catch (Exception e) {
            e.printStackTrace();
            data.put("msg", "그룹 초대과정에서 문제가 발생했습니다. 지속시 관리자에게 문의해주세요");
            response = GroupCrudResponseDto.builder().success(false).data(data).build();
        }

        return response;
    }

    public GroupCrudResponseDto acceptExistUserAsMember(GroupCrudRequestDto input) {
        GroupCrudResponseDto response;
        Map<String, Object> data = new HashMap<>();
        try {
            if (redisService.retrieveDataFromRedis(input.getGroupInvitee()) == input.getGroupInviteVerify()) {
                userGroupRepo.save(userRepo.findUserByUserId(input.getGroupInvitee()), groupRepo.findGroupByGroupName(input.getGroupName()));
                data.put("msg", "회원을 그룹에 초대했습니다.");
                response = GroupCrudResponseDto.builder().success(true).data(data).build();
            } else {
                data.put("msg", "정상적이지 않은 수락 요청입니다.");
                response = GroupCrudResponseDto.builder().success(false).data(data).build();
            }

        } catch (Exception e) {
            e.printStackTrace();
            data.put("msg", "그룹 초대과정에서 문제가 발생했습니다. 지속시 관리자에게 문의해주세요");
            response = GroupCrudResponseDto.builder().success(false).data(data).build();
        }
        return response;
    }

//    public GroupCrudResponseDto acceptNewUserAsMember(GroupCrudRequestDto input) {
//        GroupCrudResponseDto response;
//        Map<String, Object> data = new HashMap<>();
//        // 가입신청 메일에 미리 parameter를 던져줘서 그거 기준으로 가입시키기
//        return response;
//    }
}
