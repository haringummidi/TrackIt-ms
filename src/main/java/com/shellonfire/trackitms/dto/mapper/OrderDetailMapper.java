package com.shellonfire.trackitms.dto.mapper;

import com.shellonfire.trackitms.entity.OrderDetail;
import com.shellonfire.trackitms.dto.OrderDetailDto;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = {OrderDetailIdMapper.class, CompanyMapper.class})
public interface OrderDetailMapper {
    OrderDetail toEntity(OrderDetailDto orderDetailDto);

    OrderDetailDto toDto(OrderDetail orderDetail);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    OrderDetail partialUpdate(OrderDetailDto orderDetailDto, @MappingTarget OrderDetail orderDetail);
}