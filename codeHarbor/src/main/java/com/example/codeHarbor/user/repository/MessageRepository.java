package com.example.codeHarbor.user.repository;

import com.example.codeHarbor.user.domain.MessageDomain;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<MessageDomain, Long> {
    MessageDomain findByMsgContent(String msgContent);
    MessageDomain findByMsgContentStartsWithAndMsgContentContainsAndMsgContentEndsWith(String groupInvitor, String groupInvitee, String inviteMsg);
    boolean deleteByMsgId(Long msgId);
    MessageDomain save(MessageDomain message);
}
