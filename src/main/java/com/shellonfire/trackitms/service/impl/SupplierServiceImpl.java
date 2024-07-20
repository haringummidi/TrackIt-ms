package com.shellonfire.trackitms.service.impl;

import com.shellonfire.trackitms.dto.SupplierDto;
import com.shellonfire.trackitms.dto.mapper.SupplierMapper;
import com.shellonfire.trackitms.entity.Company;
import com.shellonfire.trackitms.entity.Supplier;
import com.shellonfire.trackitms.exception.ResourceNotFoundException;
import com.shellonfire.trackitms.repository.CompanyRepository;
import com.shellonfire.trackitms.repository.SupplierRepository;
import com.shellonfire.trackitms.service.SupplierService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepository supplierRepository;
    private final CompanyRepository companyRepository;
    private final SupplierMapper supplierMapper;

    @Override
    public SupplierDto getSupplierById(Integer id, Integer companyId) {
        Supplier supplier = supplierRepository.findById(id)
                .filter(s -> s.getCompany().getId().equals(companyId))
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with id " + id));
        return supplierMapper.toDto(supplier);
    }

    @Override
    public List<SupplierDto> getAllSuppliers(Integer companyId) {
        return supplierRepository.findAll().stream()
                .filter(supplier -> supplier.getCompany().getId().equals(companyId))
                .map(supplierMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public SupplierDto saveSupplier(SupplierDto supplierDto, Integer companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with id " + companyId));
        Supplier supplier = supplierMapper.toEntity(supplierDto);
        supplier.setCompany(company);
        Supplier savedSupplier = supplierRepository.save(supplier);
        return supplierMapper.toDto(savedSupplier);
    }

    @Override
    public SupplierDto updateSupplier(SupplierDto supplierDto, Integer companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with id " + companyId));
        Supplier existingSupplier = supplierRepository.findById(supplierDto.getId())
                .filter(s -> s.getCompany().getId().equals(companyId))
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with id " + supplierDto.getId()));

        supplierMapper.partialUpdate(supplierDto, existingSupplier);
        existingSupplier.setCompany(company);
        Supplier updatedSupplier = supplierRepository.save(existingSupplier);
        return supplierMapper.toDto(updatedSupplier);
    }

    @Override
    public void deleteSupplier(Integer id, Integer companyId) {
        Supplier supplier = supplierRepository.findById(id)
                .filter(s -> s.getCompany().getId().equals(companyId))
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with id " + id));
        supplierRepository.delete(supplier);
    }
}
