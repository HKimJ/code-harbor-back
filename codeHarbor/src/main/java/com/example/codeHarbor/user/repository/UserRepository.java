package com.example.codeHarbor.user.repository;

import com.example.codeHarbor.user.domain.UserDomain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;
@Repository
public interface UserRepository extends JpaRepository<UserDomain, Long> {
//    UserDomain findUserByUserSeq(Long userSeq); 필요시 추가
    UserDomain findUserByUserId(String userId);
    boolean existsByUserId(String userId);
    boolean existsByUserNick(String userNick);
    UserDomain findUserByUserIdAndUserPassword(String userId, String userPassword);
    UserDomain save(UserDomain userDomain);
}
