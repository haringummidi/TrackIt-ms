package com.shellonfire.trackitms.controller;

import com.shellonfire.trackitms.dto.SupplierDto;
import com.shellonfire.trackitms.entity.User;
import com.shellonfire.trackitms.service.SupplierService;
import com.shellonfire.trackitms.util.SecurityProvider;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/suppliers")
@AllArgsConstructor
public class SupplierController {

    private final SupplierService supplierService;
    private final SecurityProvider securityProvider;

    private User getCurrentUser() {
        return securityProvider.getCurrentUser();
    }

    @GetMapping
    public ResponseEntity<List<SupplierDto>> getAllSuppliers() {
        User currentUser = getCurrentUser();
        List<SupplierDto> suppliers = supplierService.getAllSuppliers(currentUser.getCompany().getId());
        return new ResponseEntity<>(suppliers, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SupplierDto> getSupplier(@PathVariable("id") Integer id) {
        try {
            User currentUser = getCurrentUser();
            SupplierDto supplierDto = supplierService.getSupplierById(id, currentUser.getCompany().getId());
            return new ResponseEntity<>(supplierDto, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<SupplierDto> createSupplier(@RequestBody SupplierDto supplierDto) {
        try {
            User currentUser = getCurrentUser();
            SupplierDto createdSupplier = supplierService.saveSupplier(supplierDto, currentUser.getCompany().getId());
            return new ResponseEntity<>(createdSupplier, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<SupplierDto> updateSupplier(@PathVariable("id") Integer id, @RequestBody SupplierDto supplierDto) {
        try {
            User currentUser = getCurrentUser();
            supplierDto.setId(id);
            SupplierDto updatedSupplier = supplierService.updateSupplier(supplierDto, currentUser.getCompany().getId());
            return new ResponseEntity<>(updatedSupplier, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSupplier(@PathVariable("id") Integer id) {
        try {
            User currentUser = getCurrentUser();
            supplierService.deleteSupplier(id, currentUser.getCompany().getId());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
