package com.shellonfire.trackitms.controller;

import com.shellonfire.trackitms.dto.DashBoardInformation;
import com.shellonfire.trackitms.dto.OrderDto;
import com.shellonfire.trackitms.entity.User;
import com.shellonfire.trackitms.service.OrderService;
import com.shellonfire.trackitms.util.SecurityProvider;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@CrossOrigin
@AllArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final SecurityProvider securityProvider;

    private User getCurrentUser() {
        return securityProvider.getCurrentUser();
    }

    @GetMapping
    public ResponseEntity<List<OrderDto>> getAllOrders() {
        User currentUser = getCurrentUser();
        List<OrderDto> orders = orderService.getAllOrders(currentUser.getCompany().getId());
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @GetMapping("/pageable")
    public ResponseEntity<Page<OrderDto>> getAllOrdersByPageable(Pageable pageable) {
        User currentUser = getCurrentUser();
        Page<OrderDto> orders = orderService.getOrderByPageable(currentUser.getCompany().getId(), pageable);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @GetMapping("/count")
    public ResponseEntity<DashBoardInformation> getDashBoardInfo() {
        User currentUser = getCurrentUser();
        DashBoardInformation dashBoardInfo = orderService.getDashBoardInfo(currentUser.getCompany());
        return new ResponseEntity<>(dashBoardInfo, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> getOrder(@PathVariable("id") Integer id) {
        try {
            User currentUser = getCurrentUser();
            OrderDto orderDto = orderService.getOrderById(id, currentUser.getCompany().getId());
            return new ResponseEntity<>(orderDto, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<OrderDto> createOrder(@RequestBody OrderDto orderDto) {
        try {
            User currentUser = getCurrentUser();
            OrderDto createdOrder = orderService.saveOrder(orderDto, currentUser.getCompany().getId());
            return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderDto> updateOrder(@PathVariable("id") Integer id, @RequestBody OrderDto orderDto) {
        try {
            User currentUser = getCurrentUser();
            orderDto.setId(id);
            OrderDto updatedOrder = orderService.updateOrder(orderDto, currentUser.getCompany().getId());
            return new ResponseEntity<>(updatedOrder, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable("id") Integer id) {
        try {
            User currentUser = getCurrentUser();
            orderService.deleteOrder(id, currentUser.getCompany().getId());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/sales/month")
    public ResponseEntity<List<Object[]>> getTotalSalesByMonth() {
        User currentUser = getCurrentUser();
        List<Object[]> sales = orderService.findTotalSalesByMonth(currentUser.getCompany().getId());
        return new ResponseEntity<>(sales, HttpStatus.OK);
    }

    @GetMapping("/sales/year")
    public ResponseEntity<List<Object[]>> getTotalSalesByYear() {
        User currentUser = getCurrentUser();
        List<Object[]> sales = orderService.findTotalSalesByYear(currentUser.getCompany().getId());
        return new ResponseEntity<>(sales, HttpStatus.OK);
    }

    @GetMapping("/customer/frequency")
    public ResponseEntity<List<Object[]>> getCustomerOrderFrequency() {
        User currentUser = getCurrentUser();
        List<Object[]> frequency = orderService.findCustomerOrderFrequency(currentUser.getCompany().getId());
        return new ResponseEntity<>(frequency, HttpStatus.OK);
    }

    @GetMapping("/customer/top")
    public ResponseEntity<List<Object[]>> getTopCustomersBySalesVolume(Pageable pageable) {
        User currentUser = getCurrentUser();
        List<Object[]> topCustomers = orderService.findTopCustomersBySalesVolume(pageable, currentUser.getCompany().getId());
        return new ResponseEntity<>(topCustomers, HttpStatus.OK);
    }

    @GetMapping("/orders/day")
    public ResponseEntity<List<Object[]>> getOrdersByDay() {
        User currentUser = getCurrentUser();
        List<Object[]> ordersByDay = orderService.findOrdersByDay(currentUser.getCompany().getId());
        return new ResponseEntity<>(ordersByDay, HttpStatus.OK);
    }

    @GetMapping("/orders/average")
    public ResponseEntity<Double> getAverageOrderValue() {
        User currentUser = getCurrentUser();
        Double averageOrderValue = orderService.findAverageOrderValue(currentUser.getCompany().getId());
        return new ResponseEntity<>(averageOrderValue, HttpStatus.OK);
    }
}