package com.shellonfire.trackitms.controller;

import com.shellonfire.trackitms.dto.RoleDto;
import com.shellonfire.trackitms.dto.SupplierDto;
import com.shellonfire.trackitms.dto.UserDto;
import com.shellonfire.trackitms.entity.User;
import com.shellonfire.trackitms.service.SupplierService;
import com.shellonfire.trackitms.service.UserService;
import com.shellonfire.trackitms.util.SecurityProvider;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;
    private final SecurityProvider securityProvider;

    private User getCurrentUser() {
        return securityProvider.getCurrentUser();
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        User currentUser = getCurrentUser();
        List<UserDto> users = userService.getAllUsersByCompany(currentUser.getCompany().getId());
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/roles")
    public ResponseEntity<List<RoleDto>> getRoles() {
        User currentUser = getCurrentUser();
        List<RoleDto> roles = userService.getRoles();
        return new ResponseEntity<>(roles, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
        try {
            User currentUser = getCurrentUser();
            UserDto user = userService.createUser(userDto, currentUser.getCompany().getId());
            return new ResponseEntity<>(user, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable("id") Long id, @RequestBody UserDto userDto) {
        try {
            User currentUser = getCurrentUser();
            userDto.setId(id);
            userDto = userService.updateUser(userDto, currentUser.getCompany().getId());
            return new ResponseEntity<>(userDto, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") Long id) {
        try {
            User currentUser = getCurrentUser();
            userService.deleteUser(id, currentUser.getCompany().getId());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
