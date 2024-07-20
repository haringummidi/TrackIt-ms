package com.shellonfire.trackitms.service;

import com.shellonfire.trackitms.dto.CustomerDto;
import com.shellonfire.trackitms.entity.Company;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CustomerService {
    CustomerDto getCustomerById(Integer id, Integer companyId);
    List<CustomerDto> getAllCustomers(Integer companyId);
    CustomerDto saveCustomer(CustomerDto customerDto, Integer companyId);
    CustomerDto updateCustomer(CustomerDto customerDto, Integer companyId);
    void deleteCustomer(Integer id, Integer companyId);
    CustomerDto getCustomerByPhoneNumber(Long phoneNumber, Integer id);
    long countTotalCustomers(Integer companyId);
}