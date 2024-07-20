package com.shellonfire.trackitms.service.impl;

import com.shellonfire.trackitms.dto.CompanyDto;
import com.shellonfire.trackitms.dto.mapper.CompanyMapper;
import com.shellonfire.trackitms.entity.Company;
import com.shellonfire.trackitms.exception.ResourceNotFoundException;
import com.shellonfire.trackitms.repository.CompanyRepository;
import com.shellonfire.trackitms.service.CompanyService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;
    private final CompanyMapper companyMapper;

    @Override
    public CompanyDto getCompanyById(Integer id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with id " + id));
        return companyMapper.toDto(company);
    }

    @Override
    public List<CompanyDto> getAllCompanies() {
        return companyRepository.findAll().stream()
                .map(companyMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public CompanyDto saveCompany(CompanyDto companyDto) {
        Company company = companyMapper.toEntity(companyDto);
        Company savedCompany = companyRepository.save(company);
        return companyMapper.toDto(savedCompany);
    }

    @Override
    public CompanyDto updateCompany(CompanyDto companyDto) {
        Company existingCompany = companyRepository.findById(companyDto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with id " + companyDto.getId()));

        companyMapper.partialUpdate(companyDto, existingCompany);
        Company updatedCompany = companyRepository.save(existingCompany);
        return companyMapper.toDto(updatedCompany);
    }

    @Override
    public void deleteCompany(Integer id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with id " + id));
        companyRepository.delete(company);
    }
}