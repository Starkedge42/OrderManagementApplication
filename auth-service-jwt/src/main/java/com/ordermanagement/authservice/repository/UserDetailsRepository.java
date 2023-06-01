package com.ordermanagement.authservice.repository;

import com.ordermanagement.authservice.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserDetailsRepository extends JpaRepository<UserInfo, Integer> {

    Optional<UserInfo> findByName(String userName);
}
