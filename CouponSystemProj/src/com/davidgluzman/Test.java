package com.davidgluzman;

import java.sql.SQLException;
import java.util.Date;

import com.davidgluzman.beans.Category;
import com.davidgluzman.beans.Company;
import com.davidgluzman.beans.Coupon;
import com.davidgluzman.beans.Customer;
import com.davidgluzman.db.ConnectionPool;
import com.davidgluzman.db.DatabaseManager;
import com.davidgluzman.dbdao.CompaniesDBDAO;
import com.davidgluzman.dbdao.CouponsDBDAO;
import com.davidgluzman.dbdao.CustomersDBDAO;
import com.davidgluzman.dbdao.DailyJob;
import com.davidgluzman.exceptions.FailedToLogin;
import com.davidgluzman.exceptions.InvalidAction;
import com.davidgluzman.exceptions.ItemAlreadyExists;
import com.davidgluzman.facade.AdminFacade;
import com.davidgluzman.facade.ClientType;
import com.davidgluzman.facade.CompanyFacade;
import com.davidgluzman.facade.CustomerFacade;
import com.davidgluzman.facade.LoginManager;
import com.davidgluzman.utils.Utils;

public class Test {

	public static void main(String[] args) throws SQLException, ClassNotFoundException, InterruptedException {
		testAll();

	}

	public static void testAll() throws InterruptedException, ClassNotFoundException, SQLException {
		// Connecting to driver
		Class.forName("com.mysql.cj.jdbc.Driver");
		DatabaseManager.DropAndCreateDatabase();

		CompaniesDBDAO companiesDBDAO = new CompaniesDBDAO();
		CustomersDBDAO customersDBDAO = new CustomersDBDAO();
		CouponsDBDAO couponsDBDAO = new CouponsDBDAO();
		

		Utils.br();

		Utils.printTestLine("Empty Tables Before Tests");
		Utils.printCompaniesTable(companiesDBDAO.getAllCompanies());
		Utils.printCustomersTable(customersDBDAO.getAllCustomers());
		Utils.printCouponsTable(couponsDBDAO.getAllCoupons());

		Utils.br();
		DailyJob dailyJob = new DailyJob();
		dailyJob.start();
		Thread.sleep(1000);
		Utils.br();
		Utils.printTestLine("Testing adminFacade");
		AdminFacade adminFacade = null;
		try {
			adminFacade = (AdminFacade) LoginManager.getInstance().login("admin", "admin", ClientType.Admin);
			System.out.println("Admin - logged in");
		} catch (FailedToLogin e) {

			e.printStackTrace();
		}

		Company company = new Company("AdminTest", "AdminTest", "AdminTest", null);

		try {
			adminFacade.addCompany(company);
		} catch (ItemAlreadyExists e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Company added - AdminTest");

		Company company2 = new Company("AdminTest", "AdminTest", "AdminTest", null);
		System.out.println("Initiated Exception - company already exist");
		try {
			adminFacade.addCompany(company2);
		} catch (ItemAlreadyExists e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		company.setEmail("Updated");

		try {
			adminFacade.updateCompany(company);
		} catch (InvalidAction e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Company updated - Email Updated");

		company.setName("Initiated");
		System.out.println("Initiated Exception - cannot update name");

		try {
			adminFacade.updateCompany(company);
		} catch (InvalidAction e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Company By Id - " + adminFacade.getOneCompany(company.getId()));

		Customer customer = new Customer("AdminTest", "AdminTest", "AdminTest", "AdminTest", null);

		try {
			adminFacade.AddCustomer(customer);
		} catch (ItemAlreadyExists e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Customer added - AdminTest");

		System.out.println("Initiated Exception - customer already exist");
		Customer customer2 = new Customer("AdminTest", "AdminTest", "AdminTest", "AdminTest", null);
		try {
			adminFacade.AddCustomer(customer2);
		} catch (ItemAlreadyExists e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		customer.setEmail("updated");
		System.out.println("Customer updated - Email Updated");

		adminFacade.updateCustomer(customer);

		System.out.println("Customer Info - " + adminFacade.getOneCustomer(customer.getId()));

		Utils.hr();
		Utils.printCompaniesTable(adminFacade.getAllCompanies());
		Utils.printCustomersTable(adminFacade.getAllCustomers());

		Utils.br();
		Utils.printTestLine("Testing CompanyFacade");
		CompanyFacade companyFacade = null;
		try {
			companyFacade = (CompanyFacade) LoginManager.getInstance().login(company.getEmail(), company.getPassword(),
					ClientType.Company);
			System.out.println("Company - logged in");
		} catch (FailedToLogin e) {

			e.printStackTrace();
		}
		Coupon coupon = new Coupon(company.getId(), Category.Vacation, "CompanyTest", "CompanyTest",
				new Date(2020, 1, 1), new Date(2020, 12, 1), 10, 10, "CompanyTest");
		try {
			companyFacade.addCoupon(coupon);
		} catch (ItemAlreadyExists e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Coupon Added - CompanyTest");
		System.out.println("Initiated Exception - Title already Exist");
		Coupon coupon2 = new Coupon(company.getId(), Category.Electricity, "CompanyTest", "CompanyTest",
				new Date(2020, 2, 11), new Date(2020, 8, 12), 20, 5, "CompanyTest2");
		try {
			companyFacade.addCoupon(coupon2);
		} catch (ItemAlreadyExists e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		coupon.setDescription("updated");
		try {
			companyFacade.updateCoupon(coupon);
		} catch (InvalidAction e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Coupon updated - description updated");
		coupon.setCompanyID(3);
		System.out.println("Initiated Exception - Cannot change coupons Company ID");
		try {
			companyFacade.updateCoupon(coupon);
		} catch (InvalidAction e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Coupons by Category - " + companyFacade.getAllCouponsByCategory(Category.Vacation));
		System.out.println("Coupons by MaxPrice=20 - " + companyFacade.getAllCouponsByMaxPrice(20));
		System.out.println("Company info - " + companyFacade.getOneCompany());
		Utils.printCouponsTable(companyFacade.getAllCouponsbyCompany());
		Utils.br();
		Utils.printTestLine("Testing CustomerFacade");
		CustomerFacade customerFacade = null;
		try {
			customerFacade = (CustomerFacade) LoginManager.getInstance().login(customer.getEmail(),
					customer.getPassword(), ClientType.Customer);
			System.out.println("Customer - logged in");
		} catch (FailedToLogin e) {

			e.printStackTrace();
		}
		System.out.println("Coupon added");
		try {
			customerFacade.addCouponPurchase(coupon);
		} catch (InvalidAction e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Initiated Exception - customer can buy coupon only once");
		try {
			customerFacade.addCouponPurchase(coupon);
		} catch (InvalidAction e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("coupons purchased by this customer - "+customerFacade.getAllCoupons());
		System.out.println("coupons from a specific category - "+customerFacade.getAllCouponsByCategory(Category.Vacation));
		System.out.println("coupons with Maxprice=20 - "+customerFacade.getAllCouponsByMaxPrice(20));
		System.out.println("Customer Info - "+customerFacade.getCustomer());
		Utils.printCouponsTable(customerFacade.getAllCoupons());
Utils.br();
Utils.printTestLine("Tables Before Erasing");
Utils.printCompaniesTable(companiesDBDAO.getAllCompanies());
Utils.printCustomersTable(customersDBDAO.getAllCustomers());
Utils.printCouponsTable(couponsDBDAO.getAllCoupons());
Utils.br();
	adminFacade.deleteCompany(company.getId());

	Utils.printTestLine("Tables After Company Erasing");
Utils.printCompaniesTable(companiesDBDAO.getAllCompanies());
Utils.printCustomersTable(customersDBDAO.getAllCustomers());
Utils.printCouponsTable(couponsDBDAO.getAllCoupons());
		ConnectionPool.closeAllConnection();
		dailyJob.pause();

	}

}
