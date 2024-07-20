package com.shellonfire.trackitms.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class UserInfo {
    private String username;
    private List<String> roles;
    private String firstName;
    private String lastName;
    private String email;
    private CompanyDto company;
}
