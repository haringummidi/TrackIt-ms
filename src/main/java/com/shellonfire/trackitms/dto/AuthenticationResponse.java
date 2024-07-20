package com.shellonfire.trackitms.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class AuthenticationResponse {
    private String token;
    private boolean status;
    @JsonProperty("user_data")
    private UserInfo userinfo;

}
