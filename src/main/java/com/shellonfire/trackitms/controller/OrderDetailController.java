package com.shellonfire.trackitms.controller;

import com.shellonfire.trackitms.dto.OrderDetailDto;
import com.shellonfire.trackitms.dto.ProductDto;
import com.shellonfire.trackitms.entity.OrderDetailId;
import com.shellonfire.trackitms.entity.User;
import com.shellonfire.trackitms.service.OrderDetailService;
import com.shellonfire.trackitms.util.SecurityProvider;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/order-details")
@AllArgsConstructor
public class OrderDetailController {

    private final OrderDetailService orderDetailService;
    private final SecurityProvider securityProvider;

    private User getCurrentUser() {
        return securityProvider.getCurrentUser();
    }

    @GetMapping
    public ResponseEntity<List<OrderDetailDto>> getAllOrderDetails() {
        User currentUser = getCurrentUser();
        List<OrderDetailDto> orderDetails = orderDetailService.getAllOrderDetails(currentUser.getCompany().getId());
        return new ResponseEntity<>(orderDetails, HttpStatus.OK);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<List<OrderDetailDto>> getAllOrderDetails(@PathVariable("orderId") Integer orderId) {
        User currentUser = getCurrentUser();
        List<OrderDetailDto> orderDetails = orderDetailService.getAllOrderDetailsByOrderId(orderId, currentUser.getCompany().getId());
        return new ResponseEntity<>(orderDetails, HttpStatus.OK);
    }

    @GetMapping("/products/{orderId}")
    public ResponseEntity<List<ProductDto>> getAllProductsByOrderId(@PathVariable("orderId") Integer orderId) {
        User currentUser = getCurrentUser();
        List<ProductDto> products = orderDetailService.getAllProductsByOrderId(orderId, currentUser);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/{orderId}/{productId}")
    public ResponseEntity<OrderDetailDto> getOrderDetail(@PathVariable("orderId") Integer orderId,
                                                         @PathVariable("productId") Integer productId) {
        try {
            User currentUser = getCurrentUser();
            OrderDetailId orderDetailId = new OrderDetailId(orderId, productId);
            OrderDetailDto orderDetailDto = orderDetailService.getOrderDetailById(orderDetailId, currentUser.getCompany().getId());
            return new ResponseEntity<>(orderDetailDto, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<OrderDetailDto> createOrderDetail(@RequestBody OrderDetailDto orderDetailDto) {
        try {
            User currentUser = getCurrentUser();
            OrderDetailDto createdOrderDetail = orderDetailService.saveOrderDetail(orderDetailDto, currentUser.getCompany().getId());
            return new ResponseEntity<>(createdOrderDetail, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/all")
    public ResponseEntity<List<OrderDetailDto>> saveOrderDetailsList(@RequestBody List<OrderDetailDto> orderDetailDtos) {
        try {
            User currentUser = getCurrentUser();
            List<OrderDetailDto> createdOrderDetail = orderDetailService.saveOrderDetailList(orderDetailDtos, currentUser.getCompany().getId());
            return new ResponseEntity<>(createdOrderDetail, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{orderId}/{productId}")
    public ResponseEntity<OrderDetailDto> updateOrderDetail(@PathVariable("orderId") Integer orderId,
                                                            @PathVariable("productId") Integer productId,
                                                            @RequestBody OrderDetailDto orderDetailDto) {
        try {
            User currentUser = getCurrentUser();
            orderDetailDto.getId().setOrderId(orderId);
            orderDetailDto.getId().setProductId(productId);
            OrderDetailDto updatedOrderDetail = orderDetailService.updateOrderDetail(orderDetailDto, currentUser.getCompany().getId());
            return new ResponseEntity<>(updatedOrderDetail, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{orderId}/{productId}")
    public ResponseEntity<Void> deleteOrderDetail(@PathVariable("orderId") Integer orderId,
                                                  @PathVariable("productId") Integer productId) {
        try {
            User currentUser = getCurrentUser();
            OrderDetailId orderDetailId = new OrderDetailId(orderId, productId);
            orderDetailService.deleteOrderDetail(orderDetailId, currentUser.getCompany().getId());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/best-selling-products")
    public ResponseEntity<List<Object[]>> getTopBestSellingProducts(Pageable pageable) {
        User currentUser = getCurrentUser();
        List<Object[]> topProducts = orderDetailService.findTopBestSellingProducts(pageable, currentUser.getCompany().getId());
        return new ResponseEntity<>(topProducts, HttpStatus.OK);
    }

    @GetMapping("/sales/product-category")
    public ResponseEntity<List<Object[]>> getSalesByProductCategory() {
        User currentUser = getCurrentUser();
        List<Object[]> salesByCategory = orderDetailService.findSalesByProductCategory(currentUser.getCompany().getId());
        return new ResponseEntity<>(salesByCategory, HttpStatus.OK);
    }

    @GetMapping("/sales/product-department")
    public ResponseEntity<List<Object[]>> getSalesByProductDepartment() {
        User currentUser = getCurrentUser();
        List<Object[]> salesByDepartment = orderDetailService.findSalesByProductDepartment(currentUser.getCompany().getId());
        return new ResponseEntity<>(salesByDepartment, HttpStatus.OK);
    }
}