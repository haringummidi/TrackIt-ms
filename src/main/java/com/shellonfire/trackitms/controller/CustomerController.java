package com.shellonfire.trackitms.controller;

import com.shellonfire.trackitms.dto.CustomerDto;
import com.shellonfire.trackitms.entity.User;
import com.shellonfire.trackitms.service.CustomerService;
import com.shellonfire.trackitms.util.SecurityProvider;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
@AllArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    private final SecurityProvider securityProvider;

    private User getCurrentUser() {
        return securityProvider.getCurrentUser();
    }

    @GetMapping
    public ResponseEntity<List<CustomerDto>> getAllCustomers() {
        User currentUser = getCurrentUser();
        List<CustomerDto> customers = customerService.getAllCustomers(currentUser.getCompany().getId());
        return new ResponseEntity<>(customers, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDto> getCustomer(@PathVariable("id") Integer id) {
        try {
            User currentUser = getCurrentUser();
            CustomerDto customerDto = customerService.getCustomerById(id, currentUser.getCompany().getId());
            return new ResponseEntity<>(customerDto, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/phone-number/{phoneNumber}")
    public ResponseEntity<CustomerDto> getCustomer(@PathVariable("phoneNumber") Long phoneNumber) {
        try {
            User currentUser = getCurrentUser();
            CustomerDto customerDto = customerService.getCustomerByPhoneNumber(phoneNumber, currentUser.getCompany().getId());
            return new ResponseEntity<>(customerDto, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<CustomerDto> createCustomer(@RequestBody CustomerDto customerDto) {
        try {
            User currentUser = getCurrentUser();
            CustomerDto createdCustomer = customerService.saveCustomer(customerDto, currentUser.getCompany().getId());
            return new ResponseEntity<>(createdCustomer, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerDto> updateCustomer(@PathVariable("id") Integer id, @RequestBody CustomerDto customerDto) {
        try {
            User currentUser = getCurrentUser();
            customerDto.setId(id);
            CustomerDto updatedCustomer = customerService.updateCustomer(customerDto, currentUser.getCompany().getId());
            return new ResponseEntity<>(updatedCustomer, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable("id") Integer id) {
        try {
            User currentUser = getCurrentUser();
            customerService.deleteCustomer(id, currentUser.getCompany().getId());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/count")
    public ResponseEntity<Long> countTotalCustomers() {
        try {
            User currentUser = getCurrentUser();
            return new ResponseEntity<>(customerService.countTotalCustomers(currentUser.getCompany().getId()), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}