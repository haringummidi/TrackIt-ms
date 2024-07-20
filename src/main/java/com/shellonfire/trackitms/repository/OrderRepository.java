package com.shellonfire.trackitms.repository;

import com.shellonfire.trackitms.entity.Company;
import com.shellonfire.trackitms.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    @Query("SELECT SUM(p.quantityInStock) FROM Product p WHERE p.company.id = :companyId")
    Long sumQuantityInStockByCompanyId(@Param("companyId") Integer companyId);
    Page<Order> findAllByCompanyId(Integer companyId,
                                   Pageable pageable);

    Page<Order> findAllByCompanyIdOrderByOrderDateDesc(Integer companyId,
                                   Pageable pageable);
    List<Order> findTop5ByCompanyOrderByIdDesc(Company company);

    Long countByCompany(Company company);
    Long countByCompanyAndOrderDate(Company company, LocalDate date);

    List<Order> findAllByCompanyIdOrderByOrderDateDesc(Integer id);

    @Query("SELECT SUM(o.totalAmount) FROM Order o WHERE o.orderDate = :orderDate and o.company = :company")
    Long totalSaleAmountToday(Company company, LocalDate orderDate);

    @Query(value = "SELECT CONCAT(YEAR(o.ORDER_DATE), '-', LPAD(MONTH(o.ORDER_DATE), 2, '0')) AS month_year, SUM(o.TOTAL_AMOUNT) AS total_amount " +
            "FROM `ORDER` o " +
            "WHERE o.COMPANY_ID = :companyId " +
            "GROUP BY month_year " +
            "ORDER BY month_year",
            nativeQuery = true)
    List<Object[]> findTotalSalesByMonth(@Param("companyId") int companyId);

    @Query("SELECT FUNCTION('YEAR', o.orderDate), SUM(o.totalAmount) FROM Order o WHERE o.company = :company GROUP BY FUNCTION('YEAR', o.orderDate)")
    List<Object[]> findTotalSalesByYear(Company company);

    @Query("SELECT c.name, COUNT(o) FROM Order o JOIN Customer c ON o.customer.id = c.id where o.company = :company GROUP BY c.name")
    List<Object[]> findCustomerOrderFrequency(Company company);

    @Query("SELECT c.name, SUM(o.totalAmount) FROM Order o JOIN Customer c ON o.customer.id = c.id where o.company = :company GROUP BY c.name ORDER BY SUM(o.totalAmount) DESC")
    List<Object[]> findTopCustomersBySalesVolume(Pageable pageable, Company company);

    @Query("SELECT o.orderDate, COUNT(o) FROM Order o where o.company = :company GROUP BY o.orderDate")
    List<Object[]> findOrdersByDay(Company company);

    @Query("SELECT AVG(o.totalAmount) FROM Order o where o.company = :company")
    Double findAverageOrderValue(Company company);
}