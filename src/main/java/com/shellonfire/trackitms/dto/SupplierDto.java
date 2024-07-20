package com.shellonfire.trackitms.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.io.Serializable;
import java.time.Instant;

/**
 * DTO for {@link com.shellonfire.trackitms.entity.Supplier}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SupplierDto implements Serializable {
    Integer id;
    String name;
    String contactDetails;
    String address;
    CompanyDto company;
    String createdBy;
    String updatedBy;
    Instant createdAt;
    Instant updatedAt;
}