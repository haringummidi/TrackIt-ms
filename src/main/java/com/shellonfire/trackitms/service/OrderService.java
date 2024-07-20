package com.shellonfire.trackitms.service;

import com.shellonfire.trackitms.dto.DashBoardInformation;
import com.shellonfire.trackitms.dto.OrderDto;
import com.shellonfire.trackitms.entity.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderService {
    OrderDto getOrderById(Integer id, Integer companyId);
    Page<OrderDto> getOrderByPageable(Integer companyId, Pageable page);
    List<OrderDto> getAllOrders(Integer companyId);
    OrderDto saveOrder(OrderDto orderDto, Integer companyId);
    OrderDto updateOrder(OrderDto orderDto, Integer companyId);
    void deleteOrder(Integer id, Integer companyId);
    DashBoardInformation getDashBoardInfo(Company company);

    List<Object[]> findTotalSalesByMonth(Integer companyId);

    List<Object[]> findTotalSalesByYear(Integer companyId);

    List<Object[]> findCustomerOrderFrequency(Integer companyId);

    List<Object[]> findTopCustomersBySalesVolume(Pageable pageable, Integer companyId);

    List<Object[]> findOrdersByDay(Integer companyId);

    Double findAverageOrderValue(Integer companyId);
}
