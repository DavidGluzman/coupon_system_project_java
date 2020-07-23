package com.davidgluzman.facade;

import com.davidgluzman.dbdao.CompaniesDBDAO;
import com.davidgluzman.dbdao.CouponsDBDAO;
import com.davidgluzman.dbdao.CustomersDBDAO;
import com.davidgluzman.exceptions.FailedToLogin;

public abstract class ClientFacade {
protected CompaniesDBDAO companiesDBDAO;
protected CustomersDBDAO customersDBDAO;
protected CouponsDBDAO couponsDBDAO;

 public ClientFacade() {
	super();
	this.companiesDBDAO = new CompaniesDBDAO();
	this.customersDBDAO = new CustomersDBDAO();
	this.couponsDBDAO = new CouponsDBDAO();
}

public abstract boolean login(String email,String password) throws FailedToLogin;
}
