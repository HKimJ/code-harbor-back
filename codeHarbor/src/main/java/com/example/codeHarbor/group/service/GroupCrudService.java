package com.example.codeHarbor.group.service;

import com.example.codeHarbor.child.domain.UserGroupDomain;
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
//    private final String ACCEPT_NEW_URL = "127.0.0.1:8081/GroupCrud/verifyAcceptNew";
    private final String ACCEPT_EXIST_URL = "/GroupCrud/acceptExistMember";
    private final String REDIRECT_URL = "127.0.0.1:5173/GroupCrud/signUp";

    public GroupCrudResponseDto checkGroupName(GroupCrudRequestDto input) {
        GroupCrudResponseDto response;
        Map<String, Object> data = new HashMap<>();
        try {
            if (!groupRepo.existsByGroupName(input.getGroupName())) {

                data.put("mst", "사용 가능한 그룹명입니다.");
                response = GroupCrudResponseDto.builder().success(true).data(data).build();
            } else {
                data.put("msg", "이미 존재하는 그룹 이름입니다.");
                response = GroupCrudResponseDto.builder().success(false).data(data).build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            data.put("msg", "그룹명 중복 확인 중 문제가 발생했습니다. 지속시 관리자에게 문의해주세요.");
            response = GroupCrudResponseDto.builder().success(false).data(data).build();
        }
        return response;
    }

    public GroupCrudResponseDto createGroup(GroupCrudRequestDto input) {
        GroupCrudResponseDto response;
        Map<String, Object> data = new HashMap<>();
        try {
            GroupDomain newGroup = new GroupDomain();
            UserGroupDomain relation = new UserGroupDomain();
            newGroup.setGroupCreator(input.getGroupCreator());
            newGroup.setGroupName(input.getGroupName());

            relation.setUser(userRepo.findUserByUserId(input.getGroupCreator()));
            relation.setGroup(newGroup);
            groupRepo.save(newGroup);
            userGroupRepo.save(relation);
            data.put("mst", "그룹 생성에 성공했습니다.");
            response = GroupCrudResponseDto.builder().success(true).data(data).build();

        } catch (Exception e) {
            e.printStackTrace();
            data.put("msg", "그룹생성 중 문제가 발생했습니다. 지속시 관리자에게 문의해주세요.");
            response = GroupCrudResponseDto.builder().success(false).data(data).build();
        }
        return response;
    }

    public GroupCrudResponseDto inviteMember(GroupCrudRequestDto input) {
        GroupCrudResponseDto response;
        GroupDomain currentGroup;
        Map<String, Object> data = new HashMap<>();
        if (input.getGroupInvitor().equals(input.getGroupInvitee())) {
            data.put("msg", "본인을 초대할 수 없습니다.");
            response = GroupCrudResponseDto.builder().success(false).data(data).build();
        } else {
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
                    data.put("msg", "이미 그룹에 가입된 이용자입니다.");
                    response = GroupCrudResponseDto.builder().success(false).data(data).build();
                }

            } catch (Exception e) {
                e.printStackTrace();
                data.put("msg", "그룹 초대과정에서 문제가 발생했습니다. 지속시 관리자에게 문의해주세요");
                response = GroupCrudResponseDto.builder().success(false).data(data).build();
            }
        }
        return response;
    }

    public GroupCrudResponseDto acceptExistUserAsMember(GroupCrudRequestDto input) {
        GroupCrudResponseDto response;
        Map<String, Object> data = new HashMap<>();
        UserGroupDomain user_Group = new UserGroupDomain();
        try {
            String s0 = input.getGroupInviteVerify();
            System.out.println(s0);
            String s1 = redisService.retrieveDataFromRedis(s0);
            System.out.println(s1);

            if (input.getGroupInviteVerify().equals(redisService.retrieveDataFromRedis(input.getGroupInvitee()))) {
                user_Group.setUser(userRepo.findUserByUserId(input.getGroupInvitee()));
                user_Group.setGroup(groupRepo.findGroupByGroupName(input.getGroupName()));
                userGroupRepo.save(user_Group);
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
