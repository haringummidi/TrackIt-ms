package com.shellonfire.trackitms.repository;

import com.shellonfire.trackitms.entity.UserRole;
import com.shellonfire.trackitms.entity.UserRoleId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.Native;
import java.util.List;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, UserRoleId> {
    List<UserRole> findAllByUserId(Long id);

    Integer deleteAllByUserId(Long user_id);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM USER_ROLE WHERE USER_ID = ?1 AND ROLE_ID = ?2", nativeQuery = true)
    void deleteAllUserRoleRecords(List<UserRoleId> userRoleIds);
}