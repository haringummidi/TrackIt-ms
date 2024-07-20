package com.shellonfire.trackitms.service;

import com.shellonfire.trackitms.dto.ProductDto;
import com.shellonfire.trackitms.entity.Company;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductService {
    ProductDto getProductById(Integer id, Long userId);
    List<ProductDto> getAllProducts(Long userId);
    ProductDto saveProduct(ProductDto productDto, Long userId);
    ProductDto updateProduct(ProductDto productDto, Long userId);
    void deleteProduct(Integer id, Long userId);
    ProductDto getProductByBarcode(String barcode, Integer id);

    long countTotalProducts(Integer companyId);

    List<Object[]> countProductsByDepartment(Integer companyId);

    List<Object[]> countProductsByCategory(Integer companyId);

    List<Object[]> findStockLevelsByProduct(Integer companyId);

    List<Object[]> findTopMostStockedProducts(Pageable pageable, Integer companyId);

    List<Object[]> findTopLeastStockedProducts(Pageable pageable, Integer companyId);

    List<Object[]> findSupplierContributionToStock(Integer companyId);

    List<Object[]> findTopSuppliersByNumberOfProductsSupplied(Pageable pageable, Integer companyId);

    List<String> findProductsWithNoStock(Integer companyId);
}