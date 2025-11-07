package com.employeeApplication.auth.refreshtoken;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Integer> {
     Optional<RefreshToken> findByToken(String token);
     @Modifying
     @Transactional
     @Query(value = "delete from refresh_token where user_name = :userName",nativeQuery = true)
     void deleteByUsername(@Param("userName") String userName);

     @Query(value = "select * from refresh_token where user_name = :userName",nativeQuery = true)
     RefreshToken getRefreshTokenByName(@Param("userName") String userName);
}
