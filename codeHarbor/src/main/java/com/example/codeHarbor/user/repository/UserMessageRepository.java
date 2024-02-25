package com.example.codeHarbor.user.repository;

import com.example.codeHarbor.user.domain.MessageDomain;
import com.example.codeHarbor.user.domain.UserDomain;
import com.example.codeHarbor.user.domain.UserMessageDomain;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserMessageRepository extends JpaRepository<UserMessageDomain, Long> {
    boolean existsAllByMessageOwnerAndIsRead(UserDomain messageOwner, boolean isRead); // 유저가 읽지 않은 메시지가 있는지
    List<UserMessageDomain> findAllByMessageOwnerAndIsRead(UserDomain messageOwner, boolean isRead); // 유저가 읽지 않은 메시지 목록 전체 반환

    boolean deleteByMessageOwner(UserDomain messageOwner); // 유저가 보유한 전체 메세지 비우기
    boolean deleteAllByMessageOwnerAndIsRead(UserDomain messageOwner, boolean isRead); // 읽은 메세지 삭제

    UserMessageDomain save(UserMessageDomain userMessageDomain);
}
