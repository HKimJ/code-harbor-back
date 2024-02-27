package com.example.codeHarbor.user.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Getter
@Setter
@Table(name = "message_info")
@NoArgsConstructor
@AllArgsConstructor
public class MessageDomain {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long msgId;

    @Column(columnDefinition = "VARCHAR(127) DEFAULT 'COMMON'", nullable = false)
    private String msgType;

    @Column(columnDefinition = "VARCHAR(255)", nullable = false) @NotNull
    private String msgContent;

}
