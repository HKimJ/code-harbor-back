package com.example.codeHarbor.user.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "user_message")
@NoArgsConstructor
@AllArgsConstructor
public class UserMessageDomain {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userMessageId;

    @ManyToOne
    private UserDomain messageOwner;

    @ManyToOne
    private MessageDomain message;

    @Temporal(value = TemporalType.TIMESTAMP) @Column(columnDefinition = "DATETIME DEFAULT (CURRENT_TIMESTAMP)", insertable = false, updatable = false)
    private LocalDateTime userMessageCreatedDateTime;

    @Column(columnDefinition = "TINYINT(1) DEFAULT 0", nullable = false)
    private boolean isRead;
}
