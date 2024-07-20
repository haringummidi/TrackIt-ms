package com.shellonfire.trackitms.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;

/**
 * DTO for {@link com.shellonfire.trackitms.entity.UserRoleId}
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserRoleIdDto implements Serializable {
    Long userId;
    Long roleId;
}