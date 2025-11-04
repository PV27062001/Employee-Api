package com.sampleDataBase.auth;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDetailProviderRepository extends JpaRepository<Users,Integer> {
    @Query(value = "select * from users where user_name = :userName",nativeQuery = true)
    Users findUserByUserName(@Param("userName") String userName);
}
