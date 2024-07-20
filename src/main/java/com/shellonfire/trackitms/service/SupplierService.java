package com.shellonfire.trackitms.service;

import com.shellonfire.trackitms.dto.SupplierDto;

import java.util.List;

public interface SupplierService {
    SupplierDto getSupplierById(Integer id, Integer companyId);
    List<SupplierDto> getAllSuppliers(Integer companyId);
    SupplierDto saveSupplier(SupplierDto supplierDto, Integer companyId);
    SupplierDto updateSupplier(SupplierDto supplierDto, Integer companyId);
    void deleteSupplier(Integer id, Integer companyId);
}
