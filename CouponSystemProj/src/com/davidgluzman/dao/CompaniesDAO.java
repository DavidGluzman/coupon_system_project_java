package com.davidgluzman.dao;

import java.util.List;

import com.davidgluzman.beans.Company;

public interface CompaniesDAO {
	public boolean isCompanyExist(String email, String password);

	public void addCompany(Company company);

	public void updateCompany(Company company);

	public void deleteCompany(int companyID);

	public List<Company> getAllCompanies();

	public Company getOneCompany(int companyID);

}