package com.example.codeHarbor.user.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Data
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
