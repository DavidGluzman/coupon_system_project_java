package com.davidgluzman.dao;

import java.util.List;

import com.davidgluzman.beans.Customer;

public interface CustomersDAO {
	public boolean isCustomerExists(String email, String Password);

	public void addCustomer(Customer customer);

	public void updateCustomer(Customer customer);

	public void deleteCustomer(int customerID);

	public List<Customer> getAllCustomers();

	public Customer getOneCustomer(int customerID);
}