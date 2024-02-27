package com.example.codeHarbor.user.repository;

import com.example.codeHarbor.user.domain.UserDomain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserDomain, String> {
//    UserDomain findUserByUserSeq(Long userSeq); 필요시 추가
    UserDomain findByUserId(String userId);
    UserDomain findByUserNickname(String userNickname);
    boolean existsByUserId(String userId);

    boolean existsByUserNickname(String userNickname);

    boolean existsAllByUserIdAndHasNewMsg(String userId, boolean hasNewMsg);
    UserDomain findByUserIdAndUserPassword(String userId, String userPassword);
    UserDomain save(UserDomain userDomain);
}
