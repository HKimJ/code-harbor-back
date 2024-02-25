package com.example.codeHarbor.user.repository;

import com.example.codeHarbor.user.domain.MessageDomain;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MessageRepository extends JpaRepository<MessageDomain, Long> {
    MessageDomain save(MessageDomain message);

    boolean deleteByMsgId(Long msgId);

}
