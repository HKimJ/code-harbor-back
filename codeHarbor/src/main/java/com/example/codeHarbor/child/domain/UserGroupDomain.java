package com.example.codeHarbor.child.domain;

import com.example.codeHarbor.group.domain.GroupDomain;
import com.example.codeHarbor.user.domain.UserDomain;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name="Group_Members")
@NoArgsConstructor
@AllArgsConstructor
public class UserGroupDomain {
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long seq;
    @ManyToOne @JoinColumn(name="user_Id", referencedColumnName = "userId")
    private UserDomain user;
    @ManyToOne @JoinColumn(name="group_Name", referencedColumnName = "groupName")
    private GroupDomain group;
}
