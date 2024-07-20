package com.shellonfire.trackitms.controller;

import com.shellonfire.trackitms.dto.ProductDto;
import com.shellonfire.trackitms.entity.User;
import com.shellonfire.trackitms.service.ProductService;
import com.shellonfire.trackitms.util.SecurityProvider;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@AllArgsConstructor
public class ProductsController {

    private final ProductService productService;
    private final SecurityProvider securityProvider;

    private User getCurrentUser() {
        return securityProvider.getCurrentUser();
    }

    @GetMapping
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        User currentUser = getCurrentUser();
        List<ProductDto> productDtos = productService.getAllProducts(currentUser.getId());
        return new ResponseEntity<>(productDtos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProduct(@PathVariable("id") Integer id) {
        try {
            User currentUser = getCurrentUser();
            ProductDto productDto = productService.getProductById(id, currentUser.getId());
            return new ResponseEntity<>(productDto, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/barcode/{barcode}")
    public ResponseEntity<ProductDto> getProduct(@PathVariable("barcode") String barcode) {
        try {
            User currentUser = getCurrentUser();
            ProductDto productDto = productService.getProductByBarcode(barcode, currentUser.getCompany().getId());
            return new ResponseEntity<>(productDto, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDto) {
        try {
            User currentUser = getCurrentUser();
            ProductDto createdProduct = productService.saveProduct(productDto, currentUser.getId());
            return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable("id") Integer id, @RequestBody ProductDto productDto) {
        try {
            User currentUser = getCurrentUser();
            productDto.setId(id);
            ProductDto updatedProduct = productService.updateProduct(productDto, currentUser.getId());
            return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable("id") Integer id) {
        try {
            User currentUser = getCurrentUser();
            productService.deleteProduct(id, currentUser.getId());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/count")
    public ResponseEntity<Long> countTotalProducts() {
        User currentUser = getCurrentUser();
        long totalProducts = productService.countTotalProducts(currentUser.getCompany().getId());
        return new ResponseEntity<>(totalProducts, HttpStatus.OK);
    }

    @GetMapping("/count-by-department")
    public ResponseEntity<List<Object[]>> countProductsByDepartment() {
        User currentUser = getCurrentUser();
        List<Object[]> countByDepartment = productService.countProductsByDepartment(currentUser.getCompany().getId());
        return new ResponseEntity<>(countByDepartment, HttpStatus.OK);
    }

    @GetMapping("/count-by-category")
    public ResponseEntity<List<Object[]>> countProductsByCategory() {
        User currentUser = getCurrentUser();
        List<Object[]> countByCategory = productService.countProductsByCategory(currentUser.getCompany().getId());
        return new ResponseEntity<>(countByCategory, HttpStatus.OK);
    }

    @GetMapping("/stock-levels")
    public ResponseEntity<List<Object[]>> findStockLevelsByProduct() {
        User currentUser = getCurrentUser();
        List<Object[]> stockLevels = productService.findStockLevelsByProduct(currentUser.getCompany().getId());
        return new ResponseEntity<>(stockLevels, HttpStatus.OK);
    }

    @GetMapping("/most-stocked-products")
    public ResponseEntity<List<Object[]>> findTopMostStockedProducts(Pageable pageable) {
        User currentUser = getCurrentUser();
        List<Object[]> mostStockedProducts = productService.findTopMostStockedProducts(pageable, currentUser.getCompany().getId());
        return new ResponseEntity<>(mostStockedProducts, HttpStatus.OK);
    }

    @GetMapping("/least-stocked-products")
    public ResponseEntity<List<Object[]>> findTopLeastStockedProducts(Pageable pageable) {
        User currentUser = getCurrentUser();
        List<Object[]> leastStockedProducts = productService.findTopLeastStockedProducts(pageable, currentUser.getCompany().getId());
        return new ResponseEntity<>(leastStockedProducts, HttpStatus.OK);
    }

    @GetMapping("/supplier-contribution")
    public ResponseEntity<List<Object[]>> findSupplierContributionToStock() {
        User currentUser = getCurrentUser();
        List<Object[]> supplierContribution = productService.findSupplierContributionToStock(currentUser.getCompany().getId());
        return new ResponseEntity<>(supplierContribution, HttpStatus.OK);
    }

    @GetMapping("/top-suppliers")
    public ResponseEntity<List<Object[]>> findTopSuppliersByNumberOfProductsSupplied(Pageable pageable) {
        User currentUser = getCurrentUser();
        List<Object[]> topSuppliers = productService.findTopSuppliersByNumberOfProductsSupplied(pageable, currentUser.getCompany().getId());
        return new ResponseEntity<>(topSuppliers, HttpStatus.OK);
    }

    @GetMapping("/no-stock")
    public ResponseEntity<List<String>> findProductsWithNoStock() {
        User currentUser = getCurrentUser();
        List<String> productsWithNoStock = productService.findProductsWithNoStock(currentUser.getCompany().getId());
        return new ResponseEntity<>(productsWithNoStock, HttpStatus.OK);
    }
}