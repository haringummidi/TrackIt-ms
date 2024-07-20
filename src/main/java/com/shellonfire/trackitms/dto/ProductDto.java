package com.shellonfire.trackitms.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

/**
 * DTO for {@link com.shellonfire.trackitms.entity.Product}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductDto implements Serializable {
    Integer id;
    String barcode;
    String name;
    String imageLocation;
    String description;
    BigDecimal price;
    Integer quantityInStock;
    String createdBy;
    String updatedBy;
    Instant createdAt;
    Instant updatedAt;
    SupplierDto supplier;
    CompanyDto company;
}