package com.example.codeHarbor.intermediate.repository;

import com.example.codeHarbor.intermediate.domain.UserGroupDomain;
import com.example.codeHarbor.group.domain.GroupDomain;
import com.example.codeHarbor.user.domain.UserDomain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserGroupRepository extends JpaRepository<UserGroupDomain, Long> {
    UserGroupDomain findAllByUserAndJoinedGroup(UserDomain user, GroupDomain joinedGroup);
    UserGroupDomain findByUser(UserDomain user);
    boolean existsByUser(UserDomain user);
    boolean existsAllByUserAndJoinedGroup(UserDomain user, GroupDomain joinedGroup);
    boolean deleteAllByUserAndJoinedGroup(UserDomain user, GroupDomain joinedGroup);
    UserGroupDomain save(UserGroupDomain user_Group);
}
