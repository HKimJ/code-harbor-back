package com.example.codeHarbor.user.repository;

import com.example.codeHarbor.user.domain.MessageDomain;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MessageRepository extends JpaRepository<MessageDomain, Long> {
    MessageDomain findByMsgContent(String msgContent);
    MessageDomain findByMsgContentContainingAndMsgContentContainingAndMsgContentContaining(String userId, String groupName, String msgType);
    boolean deleteByMsgId(Long msgId);
    MessageDomain save(MessageDomain message);
}
