package com.davidgluzman.facade;

import java.util.ArrayList;
import java.util.List;

import com.davidgluzman.beans.Category;
import com.davidgluzman.beans.Company;
import com.davidgluzman.beans.Coupon;
import com.davidgluzman.exceptions.FailedToLogin;
import com.davidgluzman.exceptions.InvalidAction;
import com.davidgluzman.exceptions.ItemAlreadyExists;

public class CompanyFacade extends ClientFacade{

	private int companyID;
	@Override
	public boolean login(String email, String password) throws FailedToLogin {
		if(!companiesDBDAO.isCompanyExist(email, password)) {
			throw new FailedToLogin("Company doesnt exist");}
		this.companyID=companiesDBDAO.getCompanyIdByEmailAndPassword(email, password);
			return true;
			
		}
		
	
	public void addCoupon(Coupon coupon) throws ItemAlreadyExists {
		List<Coupon>coupons=couponsDBDAO.getAllCoupons(companyID);
	for (Coupon coupon2 : coupons) {
		if (coupon.getTitle().equals(coupon2.getTitle())) {
			throw new ItemAlreadyExists("Title");
		}
		coupon.setId(this.companyID);//to make sure that a company only adds her own coupons
	}
	couponsDBDAO.addCoupon(coupon);
	}
	public void updateCoupon(Coupon coupon) throws InvalidAction {
		if(coupon.getCompanyID()!=couponsDBDAO.getOneCoupon(coupon.getId()).getCompanyID()) {
			throw new InvalidAction("cannot change coupons company ID");
		}
		couponsDBDAO.updateCoupon(coupon);
	}
	public void deleteCoupon(Coupon coupon) throws InvalidAction {
		
		if (!couponsDBDAO.isCouponExists(coupon.getId()))
			throw new InvalidAction("id doesnt exist");
		couponsDBDAO.deleteCouponPurchase(coupon.getId());
		couponsDBDAO.deleteCoupon(coupon.getId());
	}
	public List<Coupon> getAllCouponsbyCompany(){
		return couponsDBDAO.getAllCoupons(this.companyID);
		
	}
	public List<Coupon> getAllCouponsByCategory(Category category){
		List<Coupon>coupons=couponsDBDAO.getAllCoupons(this.companyID);
	List<Coupon>filteredCoupons=new ArrayList<Coupon>(); 
	for (Coupon coupon : coupons) {
		if(coupon.getCategory()==category) {
			filteredCoupons.add(coupon);
		}
	}return filteredCoupons;
	}
	public List<Coupon> getAllCouponsByMaxPrice(double maxPrice){
		List<Coupon>coupons=couponsDBDAO.getAllCoupons(this.companyID);
	List<Coupon>filteredCoupons=new ArrayList<Coupon>(); 
	for (Coupon coupon : coupons) {
		if(coupon.getPrice()<=maxPrice) {
			filteredCoupons.add(coupon);
		}
	}return filteredCoupons;
	}
	public Company getOneCompany() {
		return companiesDBDAO.getOneCompany(this.companyID);
	}
}
