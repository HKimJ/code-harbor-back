package com.example.codeHarbor.user.service;

import com.example.codeHarbor.child.domain.UserGroupDomain;
import com.example.codeHarbor.child.repository.UserGroupRepository;
import com.example.codeHarbor.user.domain.UserDomain;
import com.example.codeHarbor.user.domain.UserMessageDomain;
import com.example.codeHarbor.user.dto.UserAuthRequestDto;
import com.example.codeHarbor.user.dto.UserAuthResponseDto;
import com.example.codeHarbor.user.dto.UserCrudResponseDto;
import com.example.codeHarbor.user.repository.MessageRepository;
import com.example.codeHarbor.user.repository.UserMessageRepository;
import com.example.codeHarbor.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class UserAuthService {
    private final UserRepository userRepo;
    private final MessageRepository messageRepo;
    private final UserGroupRepository userGroupRepo;
    private final UserMessageRepository userMessageRepo;

    public UserAuthResponseDto basicLogin(UserAuthRequestDto input) {
        UserAuthResponseDto response = new UserAuthResponseDto();
        Map<String, Object> data = new HashMap<>();
        if (!isEmail(input.getUserId())) {
            response.setSuccess(false);
            data.put("msg", "올바른 아이디 형태가 아닙니다");
            response.setData(data);
            return response;
        }
        try {
            UserDomain dbUser = userRepo.findUserByUserId(input.getUserId());
            if (dbUser != null) {
                UserDomain checkUser = userRepo.findUserByUserIdAndUserPassword(input.getUserId(), input.getUserPassword());
                if (dbUser.equals(checkUser)) {
                    response.setSuccess(true);
                    data.put("userId", checkUser.getUserId());
                    data.put("userNickname", checkUser.getUserNickname());
                    response.setData(data);
                } else {
                    response.setSuccess(false);
                    data.put("msg", "아이디 혹은 비밀번호가 일치하지 않습니다.");
                    response.setData(data);
                }
            } else {
                response.setSuccess(false);
                data.clear();
                data.put("msg", "아이디 혹은 비밀번호가 일치하지 않습니다.");
                response.setData(data);
            }
        } catch(Exception e) {
            e.printStackTrace();
            data.clear();
            response.setSuccess(false);
            data.put("msg", "로그인 과정에서 문제가 발생했습니다. 문제 지속시 관리자에게 연락해주세요.");
            response.setData(data);
        }
        return response;
    }
    public UserAuthResponseDto latestUserInfo(UserAuthRequestDto input) {
        UserAuthResponseDto response = new UserAuthResponseDto();
        Map<String, Object> data = new HashMap<>();
        try {
            if(input.getUserId() != null) {
                UserDomain refreshingUser = userRepo.findUserByUserId(input.getUserId());
                data.put("userId", refreshingUser.getUserId());
                data.put("userNickname", refreshingUser.getUserNickname());
                if (refreshingUser.getUserGroupJoinStatus() != 0) {
                    UserGroupDomain user_Group = userGroupRepo.findByUser(refreshingUser);
                    data.put("userGroupStatus",
                            new HashMap<String, Object>(){{
                                put("groupStatus", refreshingUser.getUserGroupJoinStatus());
                                put("userGroupName", user_Group.getJoinedGroup().getGroupName());
                    }});
                } else {
                    data.put("userGroupStatus",
                            new HashMap<String, Object>(){{
                                put("groupStatus", refreshingUser.getUserGroupJoinStatus());
                    }});
                }
                if (refreshingUser.isHasNewMsg()) {
                    Map<Long, String[]> newMessageInfo = new HashMap<>();
                    List<UserMessageDomain> newMessageList = userMessageRepo.findAllByMessageOwnerAndIsRead(refreshingUser, false);
                    for (UserMessageDomain msg : newMessageList) {
                        String[] temp = new String[2];
                        temp[0] = msg.getMessage().getMsgType();
                        temp[1] = msg.getMessage().getMsgContent();
                        newMessageInfo.put(msg.getUserMessageId(), temp);
                    }
                    data.put("newMessageList", newMessageInfo);
                    data.put("msg", "새 메세지가 포함된 최신화된 유저정보 조회");
                } else {
                    data.put("newMessageList", null);
                    data.put("msg", "새 메세지가 없는 최신화된 유저정보 조회");
                }
                response.setSuccess(true);
            } else {
                response.setSuccess(false);
                data.put("msg", "올바르지 않은 이용자 요청입니다");
            }
            response.setData(data);
        } catch (Exception e) {
            e.printStackTrace();
            response.setSuccess(false);
            data.clear();
            data.put("msg", "유저정보 최신화 중 알수없는 문제가 발생했습니다. 지속시 관리자에게 연락해주세요.");
            response.setData(data);
        }
        return response;
    }

    public boolean isEmail(String input) {
        final String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }
}
