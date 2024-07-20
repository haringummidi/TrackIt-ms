package com.shellonfire.trackitms.service;

import com.shellonfire.trackitms.dto.OrderDetailDto;
import com.shellonfire.trackitms.dto.ProductDto;
import com.shellonfire.trackitms.entity.Company;
import com.shellonfire.trackitms.entity.OrderDetailId;
import com.shellonfire.trackitms.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderDetailService {
    OrderDetailDto getOrderDetailById(OrderDetailId orderDetailId, Integer companyId);
    List<OrderDetailDto> getAllOrderDetails(Integer companyId);
    OrderDetailDto saveOrderDetail(OrderDetailDto orderDetailDto, Integer companyId);
    OrderDetailDto updateOrderDetail(OrderDetailDto orderDetailDto, Integer companyId);
    void deleteOrderDetail(OrderDetailId orderDetailId, Integer companyId);
    List<OrderDetailDto> getAllOrderDetailsByOrderId(Integer orderId, Integer id);
    List<OrderDetailDto> saveOrderDetailList(List<OrderDetailDto> orderDetailDtos, Integer companyId);
    List<ProductDto> getAllProductsByOrderId(Integer orderId, User id);
    List<Object[]> findTopBestSellingProducts(Pageable pageable, Integer companyId);
    List<Object[]> findSalesByProductCategory(Integer companyId);
    List<Object[]> findSalesByProductDepartment(Integer companyId);
}
