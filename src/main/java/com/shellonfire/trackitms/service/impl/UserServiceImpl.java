package com.shellonfire.trackitms.service.impl;

import com.shellonfire.trackitms.dto.RoleDto;
import com.shellonfire.trackitms.dto.UserDto;
import com.shellonfire.trackitms.dto.mapper.RoleMapper;
import com.shellonfire.trackitms.dto.mapper.UserMapper;
import com.shellonfire.trackitms.dto.mapper.UserRoleMapper;
import com.shellonfire.trackitms.entity.*;
import com.shellonfire.trackitms.exception.ResourceNotFoundException;
import com.shellonfire.trackitms.repository.CompanyRepository;
import com.shellonfire.trackitms.repository.RoleRepository;
import com.shellonfire.trackitms.repository.UserRepository;
import com.shellonfire.trackitms.repository.UserRoleRepository;
import com.shellonfire.trackitms.service.UserService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final UserRoleRepository userRoleRepository;
    private final UserRoleMapper userRoleMapper;
    private final RoleMapper roleMapper;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;

    private final EntityManager entityManager;

    private final PasswordEncoder passwordEncoder;

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getAllUsersByCompany(Integer companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with id " + companyId));
        List<UserDto> userDtos = userRepository.findAllByCompany(company).stream()
                .map(userMapper::toDto)
                .peek(u -> {
                    u.setRoleDtos(userRoleRepository.findAllByUserId(u.getId()).stream()
                            .map(UserRole::getRole)
                            .map(roleMapper::toDto)
                            .collect(Collectors.toList()));
                }).collect(Collectors.toList());
        return userDtos;
    }
    @Override
    @Transactional
    public UserDto createUser(UserDto userDto, Integer companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with id " + companyId));
        User user = userMapper.toEntity(userDto);
        user.setCompany(company);
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user = userRepository.save(user);

        UserRole userRole = new UserRole();
        userRole.setId(new UserRoleId(user.getId(), userDto.getRoleDtos().get(0).getId()));
        userRole.setUser(user);
        userRole.setRole(roleRepository.findById(userDto.getRoleDtos().get(0).getId())
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with id " + userDto.getRoleDtos().get(0).getId())));

        userRoleRepository.save(userRole);

        return userMapper.toDto(user);
    }

    @Override
    @Transactional
    public UserDto updateUser(UserDto userDto, Integer companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with id " + companyId));
        User user = userRepository.findByIdAndCompany(userDto.getId(), company);
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()
        ));
        userMapper.partialUpdate(userDto, user);
        user = userRepository.save(user);
        return userMapper.toDto(user);
    }

    @Override
    @Transactional
    public void deleteUser(Long userId, Integer companyId) {
        // Delete all user roles associated with the user in a single bulk operation
        Query query = entityManager.createNativeQuery("DELETE FROM USER_ROLE WHERE USER_ID = :userId");
        query.setParameter("userId", userId);
        query.executeUpdate();

        Query query1 = entityManager.createNativeQuery("DELETE FROM USER WHERE USER_ID = :userId");
        query1.setParameter("userId", userId);
        query1.executeUpdate();
    }

    @Override
    public List<RoleDto> getRoles() {
        return roleRepository.findAll().stream().map(roleMapper::toDto).collect(Collectors.toList());
    }
}
