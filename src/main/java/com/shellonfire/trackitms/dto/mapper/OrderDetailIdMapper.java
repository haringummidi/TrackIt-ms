package com.shellonfire.trackitms.dto.mapper;

import com.shellonfire.trackitms.entity.OrderDetailId;
import com.shellonfire.trackitms.dto.OrderDetailIdDto;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrderDetailIdMapper {
    OrderDetailId toEntity(OrderDetailIdDto orderDetailIdDto);

    OrderDetailIdDto toDto(OrderDetailId orderDetailId);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    OrderDetailId partialUpdate(OrderDetailIdDto orderDetailIdDto, @MappingTarget OrderDetailId orderDetailId);
}