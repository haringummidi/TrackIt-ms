package com.shellonfire.trackitms.dto;

import com.shellonfire.trackitms.entity.Role;
import lombok.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

/**
 * DTO for {@link com.shellonfire.trackitms.entity.User}
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class UserDto implements Serializable {
    Long id;
    CompanyDto company;
    String username;
    String password;
    String firstName;
    String lastName;
    String email;
    Boolean isSuperUser;
    List<RoleDto> roleDtos;
}