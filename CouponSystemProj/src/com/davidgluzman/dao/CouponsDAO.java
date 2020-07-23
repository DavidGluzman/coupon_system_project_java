package com.davidgluzman.dao;

import java.util.ArrayList;
import java.util.List;

import com.davidgluzman.beans.Coupon;

public interface CouponsDAO {

	public void addCoupon(Coupon coupon);

	public void updateCoupon(Coupon coupon);

	public void deleteCoupon(int couponID);

	public List<Coupon> getAllCoupons(int companyID);

	public Coupon getOneCoupon(int couponID);

	public void addCouponPurchase(int customerID, int couponID);

	public void deleteCouponPurchase(int customerID, int couponID);

	public void deleteCouponPurchase(int couponID); //used in CompaniesDBDAO.deleteCompany()

	public List<Coupon> getAllCouponsByCustomerID(int customerID); //used in CustomersDBDAO.deleteCompany()

	public ArrayList<Coupon> getAllCoupons();//used in test
	
	boolean isCouponExists(int couponID);//used in customer facade
}