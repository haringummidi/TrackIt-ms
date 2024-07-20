package com.shellonfire.trackitms.dto.mapper;

import com.shellonfire.trackitms.entity.UserRoleId;
import com.shellonfire.trackitms.dto.UserRoleIdDto;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserRoleIdMapper {
    UserRoleId toEntity(UserRoleIdDto userRoleIdDto);

    UserRoleIdDto toDto(UserRoleId userRoleId);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    UserRoleId partialUpdate(UserRoleIdDto userRoleIdDto, @MappingTarget UserRoleId userRoleId);
}