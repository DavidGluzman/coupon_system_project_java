package com.davidgluzman.facade;

import java.util.List;

import com.davidgluzman.beans.Company;
import com.davidgluzman.beans.Customer;
import com.davidgluzman.exceptions.FailedToLogin;
import com.davidgluzman.exceptions.InvalidAction;
import com.davidgluzman.exceptions.ItemAlreadyExists;

public class AdminFacade extends ClientFacade {

	public AdminFacade() {
		super();
	}

	@Override
	public boolean login(String email, String password) throws FailedToLogin {

		if (email == "admin" && password == "admin") {
			return true;
		}
		throw new FailedToLogin("wrong email or password");

	}

	public void addCompany(Company company) throws ItemAlreadyExists {
		List<Company> companies = companiesDBDAO.getAllCompanies();
		for (Company c : companies) {
			if (c.getName().equals(company.getName())  || c.getEmail().equals(company.getEmail())) {
				throw new ItemAlreadyExists(company.getName());
			}
		}

		companiesDBDAO.addCompany(company);
	}

	public void updateCompany(Company company) throws InvalidAction {
		Company company2 = companiesDBDAO.getOneCompany(company.getId());
		if (!company2.getName().equals(company.getName())) {
			
			throw new InvalidAction(" cannot update company name");
		} 
			
			companiesDBDAO.updateCompany(company);
		

	}

	public void deleteCompany(int companyID) {

		companiesDBDAO.deleteCompany(companyID);
	}

	public List<Company> getAllCompanies() {
		return companiesDBDAO.getAllCompanies();

	}

	public Company getOneCompany(int companyId) {
		return companiesDBDAO.getOneCompany(companyId);
	}

	public void AddCustomer(Customer customer) throws ItemAlreadyExists {
		List<Customer> customers = customersDBDAO.getAllCustomers();
		for (Customer customer2 : customers) {
			if (customer.getEmail().equals(customer2.getEmail()) ) {
				throw new ItemAlreadyExists("email");
			}

		}
		customersDBDAO.addCustomer(customer);

	}

	public void updateCustomer(Customer customer) {
		customersDBDAO.updateCustomer(customer);
	}

	public void deleteCustomer(int customerID) {
		customersDBDAO.deleteCustomer(customerID);
	}

	public List<Customer> getAllCustomers() {
		return customersDBDAO.getAllCustomers();
	}

	public Customer getOneCustomer(int customerID) {
		return customersDBDAO.getOneCustomer(customerID);
	}
}
