package com.shellonfire.trackitms.repository;

import com.shellonfire.trackitms.entity.Company;
import com.shellonfire.trackitms.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    Customer findCustomerByCompanyAndPhoneNumber(Company company, Long phoneNumber);

    @Query("SELECT COUNT(c) FROM Customer c where c.company = :company")
    long countTotalCustomers(Company company);

    @Query("SELECT c.company.id, COUNT(c) FROM Customer c where c.company = :company GROUP BY c.company.id")
    List<Object[]> countCustomersByCompany(Company company);


}