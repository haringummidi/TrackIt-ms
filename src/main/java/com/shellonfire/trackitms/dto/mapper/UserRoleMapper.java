package com.shellonfire.trackitms.dto.mapper;

import com.shellonfire.trackitms.entity.UserRole;
import com.shellonfire.trackitms.dto.UserRoleDto;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = {UserRoleIdMapper.class, UserMapper.class, RoleMapper.class})
public interface UserRoleMapper {
    UserRole toEntity(UserRoleDto userRoleDto);

    UserRoleDto toDto(UserRole userRole);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    UserRole partialUpdate(UserRoleDto userRoleDto, @MappingTarget UserRole userRole);
}