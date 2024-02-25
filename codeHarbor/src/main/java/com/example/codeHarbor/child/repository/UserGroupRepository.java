package com.example.codeHarbor.child.repository;

import com.example.codeHarbor.child.domain.UserGroupDomain;
import com.example.codeHarbor.group.domain.GroupDomain;
import com.example.codeHarbor.user.domain.UserDomain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserGroupRepository extends JpaRepository<UserGroupDomain, Long> {
    UserGroupDomain findUserGroupByUserAndJoinedGroup(UserDomain user, GroupDomain joinedGroup);
    UserGroupDomain findUserGroupByUser(UserDomain user);
    boolean existsByUserAndJoinedGroup(UserDomain user, GroupDomain joinedGroup);
    boolean deleteAllByUserAndJoinedGroup(UserDomain user, GroupDomain joinedGroup);
    UserGroupDomain save(UserGroupDomain user_Group);
}
