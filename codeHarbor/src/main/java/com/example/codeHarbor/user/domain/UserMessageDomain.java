package com.example.codeHarbor.user.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
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

    @Temporal(value = TemporalType.DATE) @Column(columnDefinition = "DATE DEFAULT (CURRENT_DATE)", insertable = false, updatable = false)
    private Date userMessageCreatedDate;

    @Column(columnDefinition = "TINYINT(1) DEFAULT 0", nullable = false)
    private boolean isRead;
}
