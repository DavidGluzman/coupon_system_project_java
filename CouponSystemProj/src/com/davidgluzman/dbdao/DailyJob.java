package com.davidgluzman.dbdao;

import java.util.ArrayList;
import java.util.Date;

import com.davidgluzman.beans.Coupon;
import com.davidgluzman.dao.CouponsDAO;

public class DailyJob extends Thread {
	private static final int sleep = 24 * 60 * 60 * 100; // 24 hours
	private CouponsDAO couponsDBDAO;
	private boolean quit;

	public DailyJob() {
		this.couponsDBDAO = new CouponsDBDAO();
		this.quit = false;
	}

	public void run() {
		while (!quit) {
			System.out.println("Daily jobs shift has been started");

			ArrayList<Coupon> coupons = couponsDBDAO.getAllCoupons();
			for (Coupon coupon : coupons) {
				if (coupon.getEndDate().before(new Date())) {
					couponsDBDAO.deleteCouponPurchase(coupon.getId());
					couponsDBDAO.deleteCoupon(coupon.getId());
					System.out.println("Daily Job erased coupon #"+coupon.getId()+" because it has been expired.");
				}
			}

			try {

				System.out.println("Daily job is going to sleep " + sleep);
				Thread.sleep(sleep);

			} catch (Exception e) {
				e.getMessage();
			}
		}

	}

	public void pause() {
		this.quit = true;
		System.out.println("DailyJob Has Been Stopped!");
	}

}
