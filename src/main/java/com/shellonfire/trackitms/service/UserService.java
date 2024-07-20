package com.shellonfire.trackitms.service;

import com.shellonfire.trackitms.dto.RoleDto;
import com.shellonfire.trackitms.dto.UserDto;
import com.shellonfire.trackitms.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<User> findByUsername(String username);
    List<UserDto> getAllUsersByCompany(Integer companyId);
    UserDto createUser(UserDto userDto, Integer companyId);
    UserDto updateUser(UserDto userDto, Integer companyId);
    void deleteUser(Long userId, Integer companyId);

    List<RoleDto> getRoles();
}