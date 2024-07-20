package com.shellonfire.trackitms.repository;

import com.shellonfire.trackitms.entity.Company;
import com.shellonfire.trackitms.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    User findByIdAndCompany(Long id, Company company);
    List<User> findAllByCompany(Company company);
    Integer deleteUserByIdAndCompany(Long id, Company company);
}