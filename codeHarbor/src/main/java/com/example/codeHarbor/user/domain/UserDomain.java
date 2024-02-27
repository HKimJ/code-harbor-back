package com.example.codeHarbor.user.domain;

//import com.example.codeHarbor.child.domain.UserPlanDomain;
import com.example.codeHarbor.child.domain.UserGroupDomain;
import io.micrometer.common.lang.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.*;

@Entity
@Data
@Table(name="user_Info")
@NoArgsConstructor
@AllArgsConstructor
public class UserDomain {
    @Id
    @Column(columnDefinition = "VARCHAR(50)", unique = true, updatable = false) @NotBlank
    private String userId;
    @Column(columnDefinition = "VARCHAR(20)", unique = true, nullable = false) @NotBlank // 닉네임을 빈값으로 넣을 수 있으면 문제 발생
    private String userNickname;
    @Column(columnDefinition = "VARCHAR(20)", nullable = false) @NotBlank
    private String userPassword;
    @Temporal(value = TemporalType.DATE) @Column(columnDefinition = "DATE DEFAULT (CURRENT_DATE)", insertable = false, updatable = false, nullable = false) @NotBlank
    private LocalDate userSignUpDate;
    @Column(columnDefinition = "TINYINT(1) DEFAULT 0", nullable = false) @NotBlank
    private int userGroupJoinStatus; // 0: 미가입, 1: 가입, 2: 임시가입(가입 신청중, 미가입 상태 때문에 중간테이블이 아니라 여기서 관리)
    @Column(columnDefinition = "TINYINT(1)")
    private boolean hasNewMsg;

    @OneToMany(mappedBy = "messageOwner", cascade = CascadeType.ALL)
    private List<UserMessageDomain> messages;

//    @OneToMany(mappedBy = "planOwner")
//    private Set<UserPlanDomain> userPlans;



    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if(obj == null || getClass() != obj.getClass()) return false;
        UserDomain user = (UserDomain) obj;
        return Objects.equals(userId, user.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }
}
