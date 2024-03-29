package com.example.codeHarbor.user.service;

import com.example.codeHarbor.intermediate.domain.UserGroupDomain;
import com.example.codeHarbor.intermediate.repository.UserGroupRepository;
import com.example.codeHarbor.group.repository.GroupRepository;
import com.example.codeHarbor.tool.javamail.JavaMailService;
import com.example.codeHarbor.user.domain.MessageDomain;
import com.example.codeHarbor.user.domain.UserDomain;
import com.example.codeHarbor.user.domain.UserMessageDomain;
import com.example.codeHarbor.user.dto.UserCrudRequestDto;
import com.example.codeHarbor.user.dto.UserCrudResponseDto;
import com.example.codeHarbor.user.repository.MessageRepository;
import com.example.codeHarbor.user.repository.UserMessageRepository;
import com.example.codeHarbor.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class UserCrudService {
    private final UserRepository userRepo;
    private final GroupRepository groupRepo;
    private final MessageRepository messageRepo;
    private final UserGroupRepository userGroupRepo;
    private final UserMessageRepository userMessageRepo;
    private final JavaMailService mailSender;
    public UserCrudResponseDto checkId(UserCrudRequestDto input) {
        String id = input.getUserId();
        UserCrudResponseDto response = new UserCrudResponseDto();
        Map<String, Object> data = new HashMap<>();
        if (!isEmail(id)) {
            response.setSuccess(false);
            data.put("msg", "올바른 아이디 형태가 아닙니다.");
            response.setData(data);
            return response;
        }
        try {
            if (userRepo.existsByUserId(id)) {
                response.setSuccess(false);
                data.put("msg", "이미 가입된 이메일입니다.");
                response.setData(data);
            } else {
                response = mailSender.sendVerificationMail(id, new UserCrudResponseDto());
            }
        } catch(Exception e) {
            e.printStackTrace();
            response.setSuccess(false);
            data.put("msg", "인증 메일 발송 과정에서 문제가 발생했습니다.");
            response.setData(data);
        }
        return response;
    }

    public UserCrudResponseDto verifyCode(UserCrudRequestDto input) {
        UserCrudResponseDto response = new UserCrudResponseDto();
        Map<String, Object> data = new HashMap<>();
        try {
            response = mailSender.confirmVerificationMail(input.getUserId(), input.getVerifyCode());
        } catch (Exception e) {
            e.printStackTrace();
            response.setSuccess(false);
            data.put("msg", "인증 코드 검증 과정에서 문제가 발생했습니다.");
            response.setData(data);
        }
        return response;
    }

    public UserCrudResponseDto checkExistNick(UserCrudRequestDto input) {
        UserCrudResponseDto response = new UserCrudResponseDto();
        Map<String, Object> data = new HashMap<>();
        try {
            if (userRepo.existsByUserNickname(input.getUserNickname())) {
                response.setSuccess(false);
                data.put("msg", "이미 존재하는 닉네임입니다.");
                response.setData(data);
            } else {
                response.setSuccess(true);
                data.put("msg", "가입 가능한 닉네임입니다.");
                response.setData(data);
            }
        } catch(Exception e) {
            e.printStackTrace();
            response.setSuccess(false);
            data.put("msg", "닉네임 유효성 검증 중 문제가 발생했습니다.");
            response.setData(data);
        }
        return response;
    }
    public UserCrudResponseDto basicSignin(UserCrudRequestDto input) {
        UserCrudResponseDto response = new UserCrudResponseDto();
        Map<String, Object> data = new HashMap<>();
        try {
            UserDomain newUser = new UserDomain();
            newUser.setUserId(input.getUserId());
            newUser.setUserNickname(input.getUserNickname());
            newUser.setUserPassword(input.getUserPassword());
            newUser.setHasNewMsg(true);
            userRepo.save(newUser);

            MessageDomain newMsg = new MessageDomain();
            newMsg.setMsgType("WELCOME_MSG");
            newMsg.setMsgContent(newUser.getUserNickname() + "님의 CodeHarbor 가입을 축하드립니다!");
            messageRepo.save(newMsg);

            UserMessageDomain connecting = new UserMessageDomain();
            connecting.setMessageOwner(userRepo.findByUserId(newUser.getUserId()));
            connecting.setMessage(messageRepo.findByMsgContent(newMsg.getMsgContent()));
            userMessageRepo.save(connecting);

            response.setSuccess(true);
            data.put("msg", "회원 가입에 성공했습니다.");
            response.setData(data);
        } catch(Exception e) {
            e.printStackTrace();
            response.setSuccess(false);
            data.put("msg", "회원가입 중 문제가 발생했습니다. 문제 지속시 관리자에게 문의하세요.");
            response.setData(data);
        }
        return response;
    }

    public UserCrudResponseDto findPassword(UserCrudRequestDto input) {
        UserCrudResponseDto response = new UserCrudResponseDto();
        Map<String, Object> data = new HashMap<>();
        if(!isEmail(input.getUserId())) {
            response.setSuccess(false);
            data.put("msg", "올바른 아이디 형태가 아닙니다.");
            response.setData(data);
            return response;
        }
        try {
            UserDomain lostPw = userRepo.findByUserId(input.getUserId());
            if (lostPw != null) {
                response = mailSender.sendLostPasswordMail(lostPw.getUserId(), lostPw.getUserPassword());
            } else {
                response.setSuccess(false);
                data.put("msg", "존재하지 않는 이메일입니다.");
                response.setData(data);
            }
        } catch(Exception e) {
            e.printStackTrace();
            response.setSuccess(false);
            data.put("msg", "비밀번호 찾기 도중 문제가 발생했습니다. 문제가 지속되면 관리자에게 연락해주세요.");
            response.setData(data);
        }
        return response;
    }


    public UserCrudResponseDto modifyUserInfo(UserCrudRequestDto input) {
        UserCrudResponseDto response = new UserCrudResponseDto();
        Map<String, Object> data = new HashMap<>();
        try {
            if(input.getUserId() != null) {
                UserGroupDomain refreshingUser = userGroupRepo.findAllByUserAndJoinedGroup(userRepo.findByUserId(input.getUserId()), groupRepo.findByGroupName(input.getUserGroupName()));
                if (!input.getUserGroupName().equals(refreshingUser.getJoinedGroup().getGroupName())) {
                    refreshingUser.setJoinedGroup(groupRepo.findByGroupName(input.getUserGroupName()));
                }
                if (!input.getUserNickname().equals(refreshingUser.getUser().getUserNickname())) {
                    refreshingUser.setUser(userRepo.findByUserNickname(input.getUserNickname()));
                }
                if (!input.getUserPassword().equals(refreshingUser.getUser().getUserPassword())) {
                    refreshingUser.getUser().setUserPassword(input.getUserPassword()); // 체크 요망
                }
                userRepo.save(refreshingUser.getUser());
                response.setSuccess(true);
                data.put("userNickname", refreshingUser.getUser().getUserNickname());
                data.put("userGroupname", refreshingUser.getJoinedGroup().getGroupName());
                data.put("msg", "유저정보 최신화 성공");
                response.setData(data);
            } else {
                response.setSuccess(false);
                data.put("msg", "올바르지 않은 이용자 요청입니다");
                response.setData(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setSuccess(false);
            data.put("msg", "유저정보 변경 중 알수없는 문제가 발생했습니다. 지속시 관리자에게 연락해주세요.");
            response.setData(data);
        }
        return response;
    }

    public UserCrudResponseDto showNewMessages(UserCrudRequestDto input) {
        UserCrudResponseDto response = new UserCrudResponseDto();
        Map<String, Object> data = new HashMap<>();
        try {
            if(input.getUserId() != null) {
                List<UserMessageDomain> newMessageList = userMessageRepo.findAllByMessageOwnerAndIsRead(userRepo.findByUserId(input.getUserId()), false);
                Map<Long, String[]> newMessageMap = new HashMap<>();
                for (UserMessageDomain newMessage : newMessageList) {
                    newMessageMap.put(
                            newMessage.getMessage().getMsgId(),
                            new String[]{newMessage.getMessage().getMsgType(), newMessage.getMessage().getMsgContent()}
                    );
                }
                data.put("newMessageMap", newMessageMap);
                data.put("msg", "읽지 않은 메세지 목록 Map<id, String[type, content]>로 전달");
                response.setData(data);
            } else {
                response.setSuccess(false);
                data.put("msg", "올바르지 않은 이용자 요청입니다");
                response.setData(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setSuccess(false);
            data.put("msg", "새 메세지 목록을 조회하는 과정에서 알수없는 문제가 발생했습니다. 지속시 관리자에게 연락해주세요.");
            response.setData(data);
        }
        return response;
    }

    public UserCrudResponseDto readAllMessages(UserCrudRequestDto input) {
        UserCrudResponseDto response = new UserCrudResponseDto();
        Map<String, Object> data = new HashMap<>();
        try {
            if(input.getUserId() != null) {
                UserDomain currentUser = userRepo.findByUserId(input.getUserId());
                List<UserMessageDomain> newMessageList = userMessageRepo.findAllByMessageOwnerAndIsRead(userRepo.findByUserId(input.getUserId()), false);
                List<UserMessageDomain> resetList = new ArrayList<>();
                for (UserMessageDomain newMessage : newMessageList) {
                    newMessage.setRead(true);
                    userMessageRepo.save(newMessage); // 이 부분 성능문제 해결해야함
                }
                currentUser.setHasNewMsg(false);
                userRepo.save(currentUser);

                data.put("msg", "읽지 않은 메세지들을 읽음처리 했습니다.");
                response.setData(data);
            } else {
                response.setSuccess(false);
                data.put("msg", "올바르지 않은 이용자 요청입니다");
                response.setData(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setSuccess(false);
            data.put("msg", "새 메세지 목록을 조회하는 과정에서 알수없는 문제가 발생했습니다. 지속시 관리자에게 연락해주세요.");
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
