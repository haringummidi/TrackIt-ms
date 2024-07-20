package com.shellonfire.trackitms.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

/**
 * DTO for {@link com.shellonfire.trackitms.entity.Order}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderDto implements Serializable {
    Integer id;
    CustomerDto customer;
    LocalDate orderDate;
    BigDecimal totalAmount;
    String createdBy;
    String updatedBy;
    Instant createdAt;
    Instant updatedAt;
}