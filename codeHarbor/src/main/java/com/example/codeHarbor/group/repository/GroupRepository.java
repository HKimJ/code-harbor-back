package com.example.codeHarbor.group.repository;

import com.example.codeHarbor.group.domain.GroupDomain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepository extends JpaRepository<GroupDomain, Long> {
    GroupDomain findGroupByGroupCreator(String groupCreator);
    GroupDomain findGroupByGroupName(String groupName);
    boolean existsByGroupName(String groupName);
    GroupDomain save(GroupDomain groupDomain);

}
