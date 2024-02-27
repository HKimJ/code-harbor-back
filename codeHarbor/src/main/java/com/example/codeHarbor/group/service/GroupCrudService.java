package com.example.codeHarbor.group.service;

import com.example.codeHarbor.intermediate.domain.UserGroupDomain;
import com.example.codeHarbor.intermediate.repository.UserGroupRepository;
import com.example.codeHarbor.group.domain.GroupDomain;
import com.example.codeHarbor.group.dto.GroupCrudRequestDto;
import com.example.codeHarbor.group.dto.GroupCrudResponseDto;
import com.example.codeHarbor.group.repository.GroupRepository;
import com.example.codeHarbor.tool.javamail.JavaMailService;
import com.example.codeHarbor.tool.redis.RedisService;
import com.example.codeHarbor.user.domain.MessageDomain;
import com.example.codeHarbor.user.domain.UserDomain;
import com.example.codeHarbor.user.domain.UserMessageDomain;
import com.example.codeHarbor.user.repository.MessageRepository;
import com.example.codeHarbor.user.repository.UserMessageRepository;
import com.example.codeHarbor.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GroupCrudService {
    private final UserRepository userRepo;
    private final GroupRepository groupRepo;
    private final MessageRepository messageRepo;
    private final UserGroupRepository userGroupRepo;
    private final UserMessageRepository userMessageRepo;

    private final JavaMailService mailSender;
    private final RedisService redisService;
//    private final String ACCEPT_NEW_URL = "127.0.0.1:8081/GroupCrud/verifyAcceptNew";
//    private final String ACCEPT_EXIST_URL = "/GroupCrud/acceptExistMember";
    private final String REDIRECT_URL = "127.0.0.1:5173/login";

    public GroupCrudResponseDto checkGroupName(GroupCrudRequestDto input) {
        GroupCrudResponseDto response;
        Map<String, Object> data = new HashMap<>();
        try {
            if (!groupRepo.existsByGroupName(input.getGroupName())) {

                data.put("msg", "사용 가능한 그룹명입니다.");
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
        if (!input.isChecked()) {
            data.put("msg", "그룹 이름 중복확인이 되지 않았습니다.");
            response = GroupCrudResponseDto.builder().success(true).data(data).build();
        } else if (groupRepo.existsByGroupCreator(input.getGroupCreator())) {
            data.put("msg", "이미 그룹을 생성한 상태입니다.");
            response = GroupCrudResponseDto.builder().success(false).data(data).build();
        } else if(!userRepo.existsByUserId(input.getGroupCreator())) {
            data.put("msg", "올바르지 않은 접근입니다.");
            response = GroupCrudResponseDto.builder().success(false).data(data).build();
        } else {
            try {
                GroupDomain newGroup = new GroupDomain();
                UserDomain groupCreator = userRepo.findByUserId(input.getGroupCreator());
                groupCreator.setUserGroupJoinStatus(1);
                newGroup.setGroupCreator(input.getGroupCreator());
                newGroup.setGroupName(input.getGroupName());
                UserGroupDomain relation = new UserGroupDomain();
                relation.setUser(groupCreator);
                relation.setJoinedGroup(newGroup);
                relation.setAccepted(true);
                newGroup.setGroupMembers(new ArrayList<>() {{
                    add(relation);
                }});
                groupRepo.save(newGroup);
                userGroupRepo.save(relation);
                groupCreator.setUserGroupJoinStatus(1);
                MessageDomain newMsg = new MessageDomain();
                UserMessageDomain connecting = new UserMessageDomain();
                newMsg.setMsgType("CREATE_GROUP");
                newMsg.setMsgContent("[" + newGroup.getGroupName() +"] 그룹을 생성했습니다.");
                messageRepo.save(newMsg);
                connecting.setMessageOwner(groupCreator);
                connecting.setMessage(newMsg);
                userMessageRepo.save(connecting);
                groupCreator.setHasNewMsg(true);
                userRepo.save(groupCreator);
                data.put("msg", "그룹 생성에 성공했습니다.");
                response = GroupCrudResponseDto.builder().success(true).data(data).build();
            } catch (Exception e) {
                e.printStackTrace();
                data.put("msg", "그룹생성 중 문제가 발생했습니다. 지속시 관리자에게 문의해주세요.");
                response = GroupCrudResponseDto.builder().success(false).data(data).build();
            }
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
                currentGroup = groupRepo.findByGroupName(input.getGroupName());
                if (!userGroupRepo.existsAllByUserAndJoinedGroup(userRepo.findByUserId(input.getGroupInvitee()), currentGroup)) {
                    UserDomain memberCandidate = userRepo.findByUserId(input.getGroupInvitee());
                    if (memberCandidate != null) {
                        memberCandidate.setHasNewMsg(true);
                        memberCandidate.setUserGroupJoinStatus(2);
                        userRepo.save(memberCandidate);

                        List<UserGroupDomain> originalMembers = currentGroup.getGroupMembers();
                        originalMembers.add(userGroupRepo.findAllByUserAndJoinedGroup(memberCandidate, currentGroup));
                        currentGroup.setGroupMembers(originalMembers);
                        groupRepo.save(currentGroup);

                        MessageDomain newMsg = new MessageDomain();
                        newMsg.setMsgType("INVITING_MSG");
                        newMsg.setMsgContent(input.getGroupInvitor() + "님이 사용자를 " + input.getGroupName() + " 그룹으로 초대했습니다." );
                        messageRepo.save(newMsg);

                        UserGroupDomain user_Group = new UserGroupDomain();
                        user_Group.setUser(memberCandidate);
                        user_Group.setJoinedGroup(currentGroup);
                        userGroupRepo.save(user_Group);

                        UserMessageDomain user_Message = new UserMessageDomain();
                        user_Message.setMessageOwner(userRepo.findByUserId(input.getGroupInvitee()));
                        user_Message.setMessage(messageRepo.findByMsgContentContainingAndMsgContentContainingAndMsgContentContaining(input.getGroupInvitee(), input.getGroupName(), "그룹으로 초대했습니다."));
                        userMessageRepo.save(user_Message);

                        // 초대 확인 메일 보내고 알림메세지 전송하는 로직
                        response = mailSender.sendGroupInvitaiondMail(input.getGroupInvitee(), input.getGroupName(), REDIRECT_URL);
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

        try {
            UserDomain acceptingUser = userRepo.findByUserId(input.getGroupInvitee());
            GroupDomain acceptingGroup = groupRepo.findByGroupName(input.getGroupName());
            UserGroupDomain user_Group = userGroupRepo.findAllByUserAndJoinedGroup(acceptingUser, acceptingGroup);
            if (user_Group != null && acceptingUser.getUserGroupJoinStatus() == 2) {
                MessageDomain newMsg = new MessageDomain();
                newMsg.setMsgType("GROUP_ACCEPT");
                newMsg.setMsgContent(acceptingGroup.getGroupName() + " 그룹으로 가입되었습니다.");

                user_Group.setUser(userRepo.findByUserId(input.getGroupInvitee()));
                user_Group.setJoinedGroup(groupRepo.findByGroupName(input.getGroupName()));
                user_Group.setAccepted(true);
                userGroupRepo.save(user_Group);

                List<UserGroupDomain> originMembers = acceptingGroup.getGroupMembers();
                originMembers.add(userGroupRepo.findAllByUserAndJoinedGroup(acceptingUser, acceptingGroup));
                acceptingGroup.setGroupMembers(originMembers);
                groupRepo.save(acceptingGroup);

                acceptingGroup.setGroupMembers(originMembers);
                acceptingUser.setHasNewMsg(true);
                acceptingUser.setUserGroupJoinStatus(1);
                userRepo.save(acceptingUser);

                messageRepo.save(newMsg);
                data.put("msg", "회원을 그룹에 초대했습니다.");
                response = GroupCrudResponseDto.builder().success(true).data(data).build();
            } else if (user_Group != null && acceptingUser.getUserGroupJoinStatus() == 1) {
                data.put("msg", "이미 수락된 요청입니다.");
                response = GroupCrudResponseDto.builder().success(false).data(data).build();
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

    public GroupCrudResponseDto showAllGroups(GroupCrudRequestDto input) {
        GroupCrudResponseDto response;
        Map<String, Object> data = new HashMap<>();
        if (input.getUserId() != null && userRepo.existsByUserId(input.getUserId())) {
            try {
                List<GroupDomain> allGroups = groupRepo.findAll();
                List<String[]> groupData = new ArrayList<>();
                for (GroupDomain group : allGroups) {
                    groupData.add(new String[]{group.getGroupName(), group.getGroupCreator(), group.getGroupCreateDate().toString(), String.valueOf(group.getGroupMembers().size())});
                }
                data.put("msg", "모든 그룹 조회");
                data.put("groupLists", groupData);
                response = GroupCrudResponseDto.builder().success(true).data(data).build();
            } catch (Exception e) {
                e.printStackTrace();
                data.clear();
                data.put("msg", "알 수 없는 오류가 발생했습니다. 지속시 관리자에게 문의해주세요");
                response = GroupCrudResponseDto.builder().success(false).data(data).build();
            }
        } else {
            data.put("msg", "올바르지 않은 접근입니다.");
            response = GroupCrudResponseDto.builder().success(false).data(data).build();
        }
        return response;
    }

    public GroupCrudResponseDto joinNewGroup(GroupCrudRequestDto input) {
        GroupCrudResponseDto response;
        Map<String, Object> data = new HashMap<>();
        if (input.getGroupJoiner() != null && input.getGroupName() != null) {
            try {
                UserDomain joiner = userRepo.findByUserId(input.getGroupJoiner());
                GroupDomain group = groupRepo.findByGroupName(input.getGroupName());
                joiner.setUserGroupJoinStatus(2);
                userRepo.save(joiner);
                UserGroupDomain user_Group = new UserGroupDomain();
                user_Group.setUser(joiner);
                user_Group.setJoinedGroup(group);
                userGroupRepo.save(user_Group);

                List<UserGroupDomain> originMembers = group.getGroupMembers();
                originMembers.add(userGroupRepo.findAllByUserAndJoinedGroup(joiner, group));
                UserDomain groupCreator = userRepo.findByUserId(group.getGroupCreator());


                response = GroupCrudResponseDto.builder().success(true).data(data).build();
            } catch (Exception e) {
                e.printStackTrace();
                data.clear();
                data.put("msg", "그룹 가입신청 중 알 수 없는 문제가 발생했습니다. 문제 지속시 관리자에게 문의해주세요");
                response = GroupCrudResponseDto.builder().success(false).data(data).build();
            }
        } else {
            data.put("msg", "올바르지 않은 요청입니다.");
            response = GroupCrudResponseDto.builder().success(false).data(data).build();
        }
        return response;
    }
}
