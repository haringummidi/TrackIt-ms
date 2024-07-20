package com.shellonfire.trackitms.service.impl;

import com.shellonfire.trackitms.dto.ProductDto;
import com.shellonfire.trackitms.dto.mapper.ProductMapper;
import com.shellonfire.trackitms.entity.Company;
import com.shellonfire.trackitms.entity.Product;
import com.shellonfire.trackitms.entity.User;
import com.shellonfire.trackitms.exception.ResourceNotFoundException;
import com.shellonfire.trackitms.repository.CompanyRepository;
import com.shellonfire.trackitms.repository.ProductRepository;
import com.shellonfire.trackitms.repository.UserRepository;
import com.shellonfire.trackitms.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final ProductMapper productMapper;

    @Override
    public ProductDto getProductById(Integer id, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + userId));

        Product product = productRepository.findById(id)
                .filter(p -> p.getCompany().getId().equals(user.getCompany().getId()))
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + id));

        return productMapper.toDto(product);
    }

    @Override
    public List<ProductDto> getAllProducts(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + userId));

        return productRepository.findAllByCompanyOrderByIdDesc(user.getCompany()).stream()
//                .filter(product -> product.getCompany().getId().equals(user.getCompany().getId()))
                .map(productMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ProductDto saveProduct(ProductDto productDto, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + userId));

        Product product = productMapper.toEntity(productDto);
        if (productDto.getBarcode() == null) {
            product.setBarcode(generateBarcode());
        }
        product.setCreatedAt(Instant.now());
        product.setCreatedBy(user.getUsername());
        product.setCompany(user.getCompany());
        Product savedProduct = productRepository.save(product);

        return productMapper.toDto(savedProduct);
    }

    @Override
    public ProductDto updateProduct(ProductDto productDto, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + userId));

        Product existingProduct = productRepository.findById(productDto.getId())
                .filter(p -> p.getCompany().getId().equals(user.getCompany().getId()))
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + productDto.getId()));

        productMapper.partialUpdate(productDto, existingProduct);
        Product updatedProduct = productRepository.save(existingProduct);

        return productMapper.toDto(updatedProduct);
    }

    @Override
    public void deleteProduct(Integer id, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + userId));

        Product product = productRepository.findById(id)
                .filter(p -> p.getCompany().getId().equals(user.getCompany().getId()))
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + id));

        productRepository.delete(product);
    }

    @Override
    public ProductDto getProductByBarcode(String barcode, Integer companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with id " + companyId));
        Product product = productRepository.findProductByCompanyAndBarcode(company, barcode);
        return productMapper.toDto(product);
    }

    @Override
    public long countTotalProducts(Integer companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with id " + companyId));
        return productRepository.countTotalProducts(company);
    }

    @Override
    public List<Object[]> countProductsByDepartment(Integer companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with id " + companyId));
        return productRepository.countProductsByDepartment(company);
    }

    @Override
    public List<Object[]> countProductsByCategory(Integer companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with id " + companyId));
        return productRepository.countProductsByCategory(company);
    }

    @Override
    public List<Object[]> findStockLevelsByProduct(Integer companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with id " + companyId));
        return productRepository.findStockLevelsByProduct(company);
    }

    @Override
    public List<Object[]> findTopMostStockedProducts(Pageable pageable, Integer companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with id " + companyId));
        return productRepository.findTopMostStockedProducts(pageable, company);
    }

    @Override
    public List<Object[]> findTopLeastStockedProducts(Pageable pageable, Integer companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with id " + companyId));
        return productRepository.findTopLeastStockedProducts(pageable, company);
    }

    @Override
    public List<Object[]> findSupplierContributionToStock(Integer companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with id " + companyId));
        return productRepository.findSupplierContributionToStock(company);
    }

    @Override
    public List<Object[]> findTopSuppliersByNumberOfProductsSupplied(Pageable pageable, Integer companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with id " + companyId));
        return productRepository.findTopSuppliersByNumberOfProductsSupplied(pageable, company);
    }

    @Override
    public List<String> findProductsWithNoStock(Integer companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with id " + companyId));
        return null;
    }

    private String generateBarcode() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String dateTimePart = LocalDateTime.now().format(formatter);
        int randomPart = new Random().nextInt(9000) + 1000; // Generate a random 4-digit number

        return dateTimePart + randomPart;
    }
}