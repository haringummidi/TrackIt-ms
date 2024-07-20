package com.shellonfire.trackitms.service.impl;

import com.shellonfire.trackitms.dto.OrderDetailDto;
import com.shellonfire.trackitms.dto.ProductDto;
import com.shellonfire.trackitms.dto.mapper.OrderDetailMapper;
import com.shellonfire.trackitms.entity.*;
import com.shellonfire.trackitms.exception.ResourceNotFoundException;
import com.shellonfire.trackitms.repository.CompanyRepository;
import com.shellonfire.trackitms.repository.OrderDetailRepository;
import com.shellonfire.trackitms.repository.OrderRepository;
import com.shellonfire.trackitms.repository.ProductRepository;
import com.shellonfire.trackitms.service.OrderDetailService;
import com.shellonfire.trackitms.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
public class OrderDetailServiceImpl implements OrderDetailService {

    private final OrderDetailRepository orderDetailRepository;
    private final OrderRepository orderRepository;
    private final CompanyRepository companyRepository;
    private final OrderDetailMapper orderDetailMapper;
    private final ProductRepository productRepository;
    private final ProductService productService;

    @Override
    public OrderDetailDto getOrderDetailById(OrderDetailId orderDetailId, Integer companyId) {
        Order order = orderRepository.findById(orderDetailId.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id " + orderDetailId.getOrderId()));
        if (!order.getCompany().getId().equals(companyId)) {
            throw new ResourceNotFoundException("Order not found in the company with id " + companyId);
        }

        OrderDetail orderDetail = orderDetailRepository.findById(orderDetailId)
                .orElseThrow(() -> new ResourceNotFoundException("OrderDetail not found with id " + orderDetailId));
        return orderDetailMapper.toDto(orderDetail);
    }

    @Override
    public List<OrderDetailDto> getAllOrderDetails(Integer companyId) {
        return orderDetailRepository.findAll().stream()
                .filter(orderDetail -> {
                    Order order = orderRepository.findById(orderDetail.getId().getOrderId())
                            .orElseThrow(() -> new ResourceNotFoundException("Order not found with id " + orderDetail.getId().getOrderId()));
                    return order.getCompany().getId().equals(companyId);
                })
                .map(orderDetailMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public OrderDetailDto saveOrderDetail(OrderDetailDto orderDetailDto, Integer companyId) {
        Order order = orderRepository.findById(orderDetailDto.getId().getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id " + orderDetailDto.getId().getOrderId()));
        if (!order.getCompany().getId().equals(companyId)) {
            throw new ResourceNotFoundException("Order not found in the company with id " + companyId);
        }

        OrderDetail orderDetail = orderDetailMapper.toEntity(orderDetailDto);
        orderDetail.setId(new OrderDetailId(orderDetailDto.getId().getOrderId(), orderDetailDto.getId().getProductId()));
        OrderDetail savedOrderDetail = orderDetailRepository.save(orderDetail);
        return orderDetailMapper.toDto(savedOrderDetail);
    }

    @Override
    public OrderDetailDto updateOrderDetail(OrderDetailDto orderDetailDto, Integer companyId) {
        Order order = orderRepository.findById(orderDetailDto.getId().getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id " + orderDetailDto.getId().getOrderId()));
        if (!order.getCompany().getId().equals(companyId)) {
            throw new ResourceNotFoundException("Order not found in the company with id " + companyId);
        }

        OrderDetailId orderDetailId = new OrderDetailId(orderDetailDto.getId().getOrderId(), orderDetailDto.getId().getProductId());
        OrderDetail existingOrderDetail = orderDetailRepository.findById(orderDetailId)
                .orElseThrow(() -> new ResourceNotFoundException("OrderDetail not found with id " + orderDetailId));

        orderDetailMapper.partialUpdate(orderDetailDto, existingOrderDetail);
        OrderDetail updatedOrderDetail = orderDetailRepository.save(existingOrderDetail);
        return orderDetailMapper.toDto(updatedOrderDetail);
    }

    @Override
    public void deleteOrderDetail(OrderDetailId orderDetailId, Integer companyId) {
        Order order = orderRepository.findById(orderDetailId.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id " + orderDetailId.getOrderId()));
        if (!order.getCompany().getId().equals(companyId)) {
            throw new ResourceNotFoundException("Order not found in the company with id " + companyId);
        }

        OrderDetail orderDetail = orderDetailRepository.findById(orderDetailId)
                .orElseThrow(() -> new ResourceNotFoundException("OrderDetail not found with id " + orderDetailId));
        orderDetailRepository.delete(orderDetail);
    }

    @Override
    public List<OrderDetailDto> getAllOrderDetailsByOrderId(Integer orderId, Integer id) {
        return orderDetailRepository.findAllByIdOrderId(orderId).stream()
                .map(orderDetailMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderDetailDto> saveOrderDetailList(List<OrderDetailDto> orderDetailDtos, Integer companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with id " + companyId));
        List<OrderDetail> orderDetails = orderDetailDtos.stream().map(orderDetailMapper::toEntity).peek(orderDetail -> orderDetail.setCompany(company)).toList();
        List<OrderDetail> savedOrderDetails = orderDetailRepository.saveAllAndFlush(orderDetails);
        return savedOrderDetails.stream().map(orderDetailMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<ProductDto> getAllProductsByOrderId(Integer orderId, User user) {
        List<OrderDetailDto> orderDetailDtos = getAllOrderDetailsByOrderId(orderId, user.getCompany().getId());

        Stream<ProductDto> products = orderDetailDtos.stream().map(orderDetailDto -> productService.getProductById(orderDetailDto.getId().getProductId(), user.getId()));
        return products.collect(Collectors.toList());
    }

    @Override
    public List<Object[]> findTopBestSellingProducts(Pageable pageable, Integer companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with id " + companyId));
        return orderDetailRepository.findTopBestSellingProducts(pageable, company);
    }

    @Override
    public List<Object[]> findSalesByProductCategory(Integer companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with id " + companyId));
        return orderDetailRepository.findSalesByProductCategory(company);
    }

    @Override
    public List<Object[]> findSalesByProductDepartment(Integer companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with id " + companyId));
        return orderDetailRepository.findSalesByProductDepartment(company);
    }

}
