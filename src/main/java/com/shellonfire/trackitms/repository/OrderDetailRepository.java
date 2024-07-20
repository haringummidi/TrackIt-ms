package com.shellonfire.trackitms.repository;

import com.shellonfire.trackitms.entity.Company;
import com.shellonfire.trackitms.entity.OrderDetail;
import com.shellonfire.trackitms.entity.OrderDetailId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, OrderDetailId> {
    List<OrderDetail> findAllByIdOrderId(Integer orderId);

    @Query("SELECT p.name, SUM(od.quantity) FROM OrderDetail od JOIN Product p ON od.id.productId = p.id where od.company = :company GROUP BY p.name ORDER BY SUM(od.quantity) DESC")
    List<Object[]> findTopBestSellingProducts(Pageable pageable, Company company);

    @Query("SELECT p.category, SUM(p.price * od.quantity) FROM OrderDetail od JOIN Product p ON od.id.productId = p.id where od.company = :company GROUP BY p.category")
    List<Object[]> findSalesByProductCategory(Company company);

    @Query("SELECT p.department, SUM(p.price * od.quantity) FROM OrderDetail od JOIN Product p ON od.id.productId = p.id where od.company = :company GROUP BY p.department")
    List<Object[]> findSalesByProductDepartment(Company company);
}