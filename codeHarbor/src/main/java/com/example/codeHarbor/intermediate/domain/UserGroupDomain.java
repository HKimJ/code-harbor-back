package com.example.codeHarbor.intermediate.domain;

import com.example.codeHarbor.group.domain.GroupDomain;
import com.example.codeHarbor.user.domain.UserDomain;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@Table(name="Group_Members")
@NoArgsConstructor
public class UserGroupDomain {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userGroupId;
    @ManyToOne
    private UserDomain user;
    @ManyToOne
    private GroupDomain joinedGroup;
    @Column(columnDefinition = "TINYINT(1) DEFAULT 0", nullable = false) @NotBlank
    private boolean accepted;
    @Temporal(value = TemporalType.DATE) @Column(columnDefinition = "DATE DEFAULT (CURRENT_DATE)", insertable = false) // 이러면 임시로 가입된 정보를 넣은 시점이 저장되어버림, 수락 시점에 쿼리를 한번 더 날려야할듯
    private LocalDate userGroupJoinDate;

}
