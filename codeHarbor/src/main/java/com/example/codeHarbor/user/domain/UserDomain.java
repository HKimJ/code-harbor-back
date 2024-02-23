package com.example.codeHarbor.user.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Objects;

@Entity
@Data
@Table(name="user_Info")
@NoArgsConstructor
@AllArgsConstructor
public class UserDomain {
    @Id
    @Column(columnDefinition = "VARCHAR(50)",unique = true, updatable = false)
    private String userId;
//    @Column(columnDefinition = "INT AUTO_INCREMENT", unique = true)
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long userNum;
    @Column(columnDefinition = "VARCHAR(20)", unique = true)
    private String userNickname;
    @Column(columnDefinition = "VARCHAR(20)")
    private String userPassword;
    @Temporal(value = TemporalType.DATE) @Column(columnDefinition = "DATE DEFAULT (CURRENT_DATE)", insertable = false, updatable = false)
    private Date userJoindate;

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
