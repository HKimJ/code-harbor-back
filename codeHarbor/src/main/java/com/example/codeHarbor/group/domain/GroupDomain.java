package com.example.codeHarbor.group.domain;

import jakarta.persistence.*;
import lombok.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Data
@Table(name="group_info")
@NoArgsConstructor
@AllArgsConstructor
public class GroupDomain {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long groupSeq;
    @Column(columnDefinition = "VARCHAR(50)", updatable = false)
    private String groupCreator;
    @Column(columnDefinition = "VARCHAR(30)", unique = true)
    private String groupName;
    @Temporal(value = TemporalType.DATE) @Column(columnDefinition = "DATE DEFAULT (CURRENT_DATE)", insertable = false, updatable = false)
    private Date groupCreatedate;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if(obj == null || getClass() != obj.getClass()) return false;
        GroupDomain group = (GroupDomain) obj;
        return Objects.equals(groupSeq, group.groupSeq);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupSeq);
    }

}
