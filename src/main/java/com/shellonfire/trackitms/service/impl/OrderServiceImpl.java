package com.shellonfire.trackitms.service.impl;

import com.shellonfire.trackitms.dto.DashBoardInformation;
import com.shellonfire.trackitms.dto.OrderDto;
import com.shellonfire.trackitms.dto.mapper.OrderMapper;
import com.shellonfire.trackitms.entity.Company;
import com.shellonfire.trackitms.entity.Order;
import com.shellonfire.trackitms.exception.ResourceNotFoundException;
import com.shellonfire.trackitms.repository.CompanyRepository;
import com.shellonfire.trackitms.repository.OrderRepository;
import com.shellonfire.trackitms.repository.ProductRepository;
import com.shellonfire.trackitms.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final CompanyRepository companyRepository;
    private final OrderMapper orderMapper;

    @Override
    public OrderDto getOrderById(Integer id, Integer companyId) {
        Order order = orderRepository.findById(id)
                .filter(o -> o.getCompany().getId().equals(companyId))
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id " + id));
        return orderMapper.toDto(order);
    }

    @Override
    public Page<OrderDto> getOrderByPageable(Integer companyId, Pageable page) {
        Page<Order> orders = orderRepository.findAllByCompanyIdOrderByOrderDateDesc(companyId, page);
        return orders.map(orderMapper::toDto);
    }

    @Override
    public DashBoardInformation getDashBoardInfo(Company company) {
        DashBoardInformation dashBoardInformation = new DashBoardInformation();
        dashBoardInformation.setTotalItems(orderRepository.sumQuantityInStockByCompanyId(company.getId()));
        dashBoardInformation.setTotalOrders(orderRepository.countByCompany(company));
        dashBoardInformation.setDailyOrders(orderRepository.countByCompanyAndOrderDate(company, LocalDate.now()));
        dashBoardInformation.setTodaySaleAmount(orderRepository.totalSaleAmountToday(company, LocalDate.now()));
        dashBoardInformation.setNumberOfLowStockItems(productRepository.countByQuantityInStockLessThanEqual(125L));
        dashBoardInformation.setNumberOfAvailableItems(productRepository.countByQuantityInStockGreaterThan(125L));
        dashBoardInformation.setNumberOfOutOfStockItems(productRepository.countByQuantityInStockEquals(0L));
        List<String> alertMessages = Arrays.asList(
                "Stock for Product is running low!",
                "Stock for Amul Milk is running low!",
                "Stock for Stark Nanotech Suit is running low!",
                "Stock for Stark Energy Shield is running low!",
                "Stock for Stark Iron Man Suit is running low!",
                "Stock for Acme Dynamite is running low!",
                "New order received from Customer Harin Gummidi.",
                "Suppliers has updated their prices.",
                "Few Products in inventory are out of stock!",
                "Globex VR Headset out of stock!",
                "Globex Drone is out of stock!",
                "Soylent Meal Replacement is out of stock!",
                "Daily sales target achieved!",
                "Inventory count mismatch detected.",
                "New user registered in the system.",
                "Scheduled maintenance on the server tonight.",
                "New feedback received from Likhitha.",
                "New feedback received from Keerthana."
        );

        // Shuffle the list to ensure random selection each time
        Collections.shuffle(alertMessages);

        // Select the first 5 messages from the shuffled list
        List<String> selectedAlerts = alertMessages.stream().limit(5).collect(Collectors.toList());

        dashBoardInformation.setImportantAlerts(selectedAlerts);

        List<Order> top5ByCompanyOrderByCreatedAtDesc = orderRepository.findTop5ByCompanyOrderByIdDesc(company);

        top5ByCompanyOrderByCreatedAtDesc.stream()
                .map(orderMapper::toDto).collect(Collectors.toList());

        dashBoardInformation.setLatestOrders(top5ByCompanyOrderByCreatedAtDesc.stream()
                .map(orderMapper::toDto).collect(Collectors.toList()));
        return dashBoardInformation;
    }

    @Override
    public List<Object[]> findTotalSalesByMonth(Integer companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with id " + companyId));
        return orderRepository.findTotalSalesByMonth(company.getId());
    }

    @Override
    public List<Object[]> findTotalSalesByYear(Integer companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with id " + companyId));
        return orderRepository.findTotalSalesByYear(company);
    }

    @Override
    public List<Object[]> findCustomerOrderFrequency(Integer companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with id " + companyId));
        return orderRepository.findCustomerOrderFrequency(company);
    }

    @Override
    public List<Object[]> findTopCustomersBySalesVolume(Pageable pageable, Integer companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with id " + companyId));
        return orderRepository.findTopCustomersBySalesVolume(pageable, company);
    }

    @Override
    public List<Object[]> findOrdersByDay(Integer companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with id " + companyId));
        return orderRepository.findOrdersByDay(company);
    }

    @Override
    public Double findAverageOrderValue(Integer companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with id " + companyId));
        return orderRepository.findAverageOrderValue(company);
    }

    @Override
    public List<OrderDto> getAllOrders(Integer companyId) {
        return orderRepository.findAllByCompanyIdOrderByOrderDateDesc(companyId).stream()
//                .filter(order -> order.getCompany().getId().equals(companyId))
                .map(orderMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public OrderDto saveOrder(OrderDto orderDto, Integer companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with id " + companyId));
        Order order = orderMapper.toEntity(orderDto);
        order.setCompany(company);
        order.setOrderDate(LocalDate.now());
        Order savedOrder = orderRepository.save(order);
        return orderMapper.toDto(savedOrder);
    }

    @Override
    public OrderDto updateOrder(OrderDto orderDto, Integer companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with id " + companyId));
        Order existingOrder = orderRepository.findById(orderDto.getId())
                .filter(o -> o.getCompany().getId().equals(companyId))
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id " + orderDto.getId()));

        orderMapper.partialUpdate(orderDto, existingOrder);
        existingOrder.setCompany(company);
        Order updatedOrder = orderRepository.save(existingOrder);
        return orderMapper.toDto(updatedOrder);
    }

    @Override
    public void deleteOrder(Integer id, Integer companyId) {
        Order order = orderRepository.findById(id)
                .filter(o -> o.getCompany().getId().equals(companyId))
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id " + id));
        orderRepository.delete(order);
    }
}
