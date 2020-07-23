package com.davidgluzman.facade;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.davidgluzman.beans.Category;
import com.davidgluzman.beans.Coupon;
import com.davidgluzman.beans.Customer;
import com.davidgluzman.exceptions.FailedToLogin;
import com.davidgluzman.exceptions.InvalidAction;
import com.davidgluzman.exceptions.ItemAlreadyExists;

public class CustomerFacade extends ClientFacade {
	private int customerID;

	@Override
	public boolean login(String email, String password) throws FailedToLogin {
		if (customersDBDAO.isCustomerExists(email, password) == false) {
			throw new FailedToLogin("Customers doesnt exist");
		}
		this.customerID = customersDBDAO.getCustomersIdByEmailAndPassword(email, password);
		return true;
	}

	// it gets a bit tricky - thats why I should start using comments
	// adding coupon purchase with coupon in args because I already have the
	// customersID from login
	public void addCouponPurchase(Coupon coupon) throws InvalidAction {
		// checking if the customer already bought this coupon (by coupon id)
		boolean flag = false;
		List<Coupon> coupons = couponsDBDAO.getAllCouponsByCustomerID(customerID);
		for (Coupon coupon2 : coupons) {
			if (coupon2.getId() == coupon.getId())
				flag = true;
		}
		// throwing exception
		if (flag == true) {
			throw new InvalidAction("you can only buy this coupon once");
		}
		// if amount == 0 throws exception
		if (couponsDBDAO.getOneCoupon(coupon.getId()).getAmount() == 0) {
			throw new InvalidAction("no coupons left, amount=0");
			// if expiration date is today and the daily job didnt reach him yet
		} else if (couponsDBDAO.getOneCoupon(coupon.getId()).getEndDate()
				.compareTo(java.sql.Date.valueOf(LocalDate.now())) == 0) {
			throw new InvalidAction("expired coupon, not deleted by daily job yet");
			// adding coupon purchase, amount-- is done in the method in the DBDAO
		} else {
			couponsDBDAO.addCouponPurchase(this.customerID, coupon.getId());
		}

	}

	public List<Coupon> getAllCoupons() {
		return couponsDBDAO.getAllCouponsByCustomerID(customerID);
	}

	public List<Coupon> getAllCouponsByCategory(Category category) {
		List<Coupon> coupons = new ArrayList<Coupon>();
		coupons = couponsDBDAO.getAllCouponsByCustomerID(customerID);
		List<Coupon> filteredCoupons = new ArrayList<Coupon>();
		for (Coupon coupon : coupons) {
			if (coupon.getCategory() == category) {
				filteredCoupons.add(coupon);
			}
		}
		return filteredCoupons;
	}

	public List<Coupon> getAllCouponsByMaxPrice(double maxPrice) {
		List<Coupon> coupons = couponsDBDAO.getAllCoupons(this.customerID);
		List<Coupon> filteredCoupons = new ArrayList<Coupon>();
		for (Coupon coupon : coupons) {
			if (coupon.getPrice() <= maxPrice) {
				filteredCoupons.add(coupon);
			}
		}
		return filteredCoupons;
	}

	public Customer getCustomer() {

		Customer customer = customersDBDAO.getOneCustomer(this.customerID);
		customer.setCoupons(getAllCoupons());
		return customer;
	}
}
