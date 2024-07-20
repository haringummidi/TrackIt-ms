package com.shellonfire.trackitms.controller;


import com.shellonfire.trackitms.dto.mapper.CompanyMapper;
import com.shellonfire.trackitms.entity.Role;
import com.shellonfire.trackitms.entity.User;
import com.shellonfire.trackitms.dto.AuthenticationRequest;
import com.shellonfire.trackitms.dto.AuthenticationResponse;
import com.shellonfire.trackitms.dto.UserInfo;
import com.shellonfire.trackitms.entity.WallmartData;
import com.shellonfire.trackitms.repository.WallmartDataRepository;
import com.shellonfire.trackitms.util.JWTTokenHelper;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
public class AuthenticationController {

    private final WallmartDataRepository repository;
    private final AuthenticationManager authenticationManager;

    private final JWTTokenHelper jWTTokenHelper;

    private final UserDetailsService userDetailsService;

    private final CompanyMapper companyMapper;


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequest authenticationRequest) throws InvalidKeySpecException, NoSuchAlgorithmException {

        final Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authenticationRequest.getEmail(), authenticationRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = (User) authentication.getPrincipal();
        String jwtToken = jWTTokenHelper.generateToken(user.getUsername());

        AuthenticationResponse response = new AuthenticationResponse();

        UserInfo userInfo = new UserInfo();

        userInfo.setEmail(user.getUsername());
        userInfo.setRoles(user.getAuthorities().stream().map(role -> ((Role) role).getCode()).collect(Collectors.toList()));
        userInfo.setUsername(user.getUsername());
        userInfo.setFirstName(user.getFirstName());
        userInfo.setLastName(user.getLastName());
        userInfo.setCompany(companyMapper.toDto(user.getCompany()));
        response.setUserinfo(userInfo);
        response.setToken(jwtToken);
        response.setStatus(Boolean.TRUE);


        return ResponseEntity.ok(response);
    }

    @PostMapping("/image")
    public HashMap getImage(@RequestBody HashMap<String, Object> image) {
        System.out.println(image.get("host"));
        String s = ((List<String>) image.get("imageUrls")).stream().filter(e -> e.contains("seo")).findAny().get();
        System.out.println(s);
        WallmartData host = repository.findByProductUrl(image.get("host").toString());
        host.setImageUrl(s);
        repository.save(host);
        HashMap<String, String> res = new HashMap<>();
        res.putIfAbsent("status", "success");
        return res;
    }
}
