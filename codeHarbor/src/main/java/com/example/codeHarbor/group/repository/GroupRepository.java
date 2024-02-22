package com.example.codeHarbor.group.repository;

import com.example.codeHarbor.group.domain.GroupDomain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupRepository extends JpaRepository<GroupDomain, Long> {
    GroupDomain findGroupByGroupCreator(String groupCreator);
    boolean existsByGroupName(String groupName);
//    List<GroupDomain> findBySerialzable
    GroupDomain save(GroupDomain groupDomain);

}
