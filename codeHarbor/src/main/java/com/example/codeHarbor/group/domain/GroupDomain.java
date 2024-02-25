package com.example.codeHarbor.group.domain;

import com.example.codeHarbor.child.domain.UserGroupDomain;
import com.example.codeHarbor.user.domain.UserMessageDomain;
import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Entity
@Data
@Table(name="group_info")
@NoArgsConstructor
@AllArgsConstructor
public class GroupDomain {
//    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Integer groupId;
    @Id
    @Column(columnDefinition = "VARCHAR(30)", unique = true)
    private String groupName;
    @Column(columnDefinition = "VARCHAR(50)", updatable = false)
    private String groupCreator;
    @Temporal(value = TemporalType.DATE) @Column(columnDefinition = "DATE DEFAULT (CURRENT_DATE)", insertable = false, updatable = false)
    private Date groupCreatedate;

    @OneToMany(mappedBy = "joinedGroup")
    private List<UserGroupDomain> groupMembers;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if(obj == null || getClass() != obj.getClass()) return false;
        GroupDomain group = (GroupDomain) obj;
        return Objects.equals(groupName, group.groupName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupName);
    }

}
