package com.shellonfire.trackitms.repository;

import com.shellonfire.trackitms.entity.Company;
import com.shellonfire.trackitms.entity.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    Long countByQuantityInStockLessThanEqual(Long quantity);
    Long countByQuantityInStockGreaterThan(Long quantity);
    Long countByQuantityInStockEquals(Long quantity);
    Product findProductByCompanyAndBarcode(Company company, String barcode);

    List<Product> findAllByCompanyOrderByIdDesc(Company company);
    @Query("SELECT COUNT(p) FROM Product p where p.company = :company")
    long countTotalProducts(Company company);

    @Query("SELECT p.department, COUNT(p) FROM Product p where p.company = :company GROUP BY p.department")
    List<Object[]> countProductsByDepartment(Company company);

    @Query("SELECT p.category, COUNT(p) FROM Product p where p.company = :company GROUP BY p.category")
    List<Object[]> countProductsByCategory(Company company);

    @Query("SELECT p.name, p.quantityInStock FROM Product p where p.company = :company")
    List<Object[]> findStockLevelsByProduct(Company company);

    @Query("SELECT p.name, p.quantityInStock FROM Product p where p.company = :company ORDER BY p.quantityInStock DESC")
    List<Object[]> findTopMostStockedProducts(Pageable pageable, Company company);

    @Query("SELECT p.name, p.quantityInStock FROM Product p where p.company = :company ORDER BY p.quantityInStock ASC")
    List<Object[]> findTopLeastStockedProducts(Pageable pageable, Company company);

    @Query("SELECT s.name, COUNT(p) FROM Product p JOIN Supplier s ON p.supplier.id = s.id where p.company = :company GROUP BY s.name")
    List<Object[]> findSupplierContributionToStock(Company company);

    @Query("SELECT s.name, COUNT(p) FROM Product p JOIN Supplier s ON p.supplier.id = s.id where p.company = :company GROUP BY s.name ORDER BY COUNT(p) DESC")
    List<Object[]> findTopSuppliersByNumberOfProductsSupplied(Pageable pageable, Company company);

    @Query("SELECT p.name FROM Product p WHERE p.quantityInStock = 0 and p.company = :company")
    List<String> findProductsWithNoStock(Company company);
}