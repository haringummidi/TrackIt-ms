package com.shellonfire.trackitms.service;

import com.shellonfire.trackitms.dto.CompanyDto;

import java.util.List;

public interface CompanyService {
    CompanyDto getCompanyById(Integer id);
    List<CompanyDto> getAllCompanies();
    CompanyDto saveCompany(CompanyDto companyDto);
    CompanyDto updateCompany(CompanyDto companyDto);
    void deleteCompany(Integer id);
}