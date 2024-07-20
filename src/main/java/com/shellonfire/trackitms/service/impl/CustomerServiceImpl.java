package com.shellonfire.trackitms.service.impl;

import com.shellonfire.trackitms.dto.CustomerDto;
import com.shellonfire.trackitms.dto.mapper.CustomerMapper;
import com.shellonfire.trackitms.entity.Company;
import com.shellonfire.trackitms.entity.Customer;
import com.shellonfire.trackitms.exception.ResourceNotFoundException;
import com.shellonfire.trackitms.repository.CompanyRepository;
import com.shellonfire.trackitms.repository.CustomerRepository;
import com.shellonfire.trackitms.service.CustomerService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CompanyRepository companyRepository;
    private final CustomerMapper customerMapper;

    @Override
    public CustomerDto getCustomerById(Integer id, Integer companyId) {
        Customer customer = customerRepository.findById(id)
                .filter(c -> c.getCompany().getId().equals(companyId))
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id " + id));
        return customerMapper.toDto(customer);
    }

    @Override
    public List<CustomerDto> getAllCustomers(Integer companyId) {
        return customerRepository.findAll().stream()
                .filter(customer -> customer.getCompany().getId().equals(companyId))
                .map(customerMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public CustomerDto saveCustomer(CustomerDto customerDto, Integer companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with id " + companyId));
        Customer customer = customerMapper.toEntity(customerDto);
        customer.setCompany(company);
        Customer savedCustomer = customerRepository.save(customer);
        return customerMapper.toDto(savedCustomer);
    }

    @Override
    public CustomerDto updateCustomer(CustomerDto customerDto, Integer companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with id " + companyId));
        Customer existingCustomer = customerRepository.findById(customerDto.getId())
                .filter(c -> c.getCompany().getId().equals(companyId))
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id " + customerDto.getId()));

        customerMapper.partialUpdate(customerDto, existingCustomer);
        existingCustomer.setCompany(company);
        Customer updatedCustomer = customerRepository.save(existingCustomer);
        return customerMapper.toDto(updatedCustomer);
    }

    @Override
    public void deleteCustomer(Integer id, Integer companyId) {
        Customer customer = customerRepository.findById(id)
                .filter(c -> c.getCompany().getId().equals(companyId))
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id " + id));
        customerRepository.delete(customer);
    }

    @Override
    public CustomerDto getCustomerByPhoneNumber(Long phoneNumber, Integer companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with id " + companyId));
        Customer customer = customerRepository.findCustomerByCompanyAndPhoneNumber(company, phoneNumber);
        return customerMapper.toDto(customer);
    }

    @Override
    public long countTotalCustomers(Integer companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with id " + companyId));
        return customerRepository.countTotalCustomers(company);
    }
}