package com.davidgluzman.utils;
import java.sql.Date;
import java.util.List;

import com.davidgluzman.beans.*;

public class Utils {
	public static final void hr() {
		System.out.println("--------------------------------------------------------------------------------------------");
	}
	// Converting java.util.Date to java.sql.Date
		public static Date convertUtilDateToSQL(java.util.Date date) {
			return new java.sql.Date(date.getYear() - 1900, date.getMonth() - 1, date.getDate() + 1);
		}
		public static void printTestLine(String s) {
			System.out.println();
			System.out.println(String.format("*************************%s*************************", s));
			System.out.println();
		}
public static void br() {
	for (int i = 0; i < 5; i++) {
		System.out.println();
	}
}
	public static void printCompaniesTable(List<Company> arrayList) {
		System.out.println();
		System.out.printf("%10s %10s %20s %10s %10s", "id", "name", "email", "password", "coupons");
		System.out.println();
		Utils.hr();
		for (int i = 0; i < arrayList.size(); i++) {
			System.out.printf("%10s %10s %20s %10s %10s", (arrayList.get(i)).getId(), (arrayList.get(i)).getName(),
					(arrayList.get(i)).getEmail(), (arrayList.get(i)).getPassword(), (arrayList.get(i)).getCoupons());
			System.out.println();
		}
		System.out.println();

	}

	public static void printCustomersTable(List<Customer> customers) {
		System.out.println();
		System.out.printf("%10s %10s %10s %20s %10s %10s", "id", "first", "last", "email", "password", "coupons");
		System.out.println();
		Utils.hr();
		for (int i = 0; i < customers.size(); i++) {
			System.out.printf("%10s %10s %10s %20s %10s %10s", (customers.get(i)).getId(),
					(customers.get(i)).getFirstName(), (customers.get(i)).getLastName(), (customers.get(i)).getEmail(),
					(customers.get(i)).getPassword(), (customers.get(i)).getCoupons());
			System.out.println();
		}
	}

	public static void printCouponsTable(List<Coupon> coupons) {
		System.out.println();
		System.out.printf("%3s %10s %15s %15s %17s %11s %11s %7s %7s %10s", "id", "companyID", "category", "title",
				"description", "startDate", "endDate", "amount", "price", "img");
		System.out.println();
		System.out.print("----------------------------------");
		Utils.hr();
		for (int i = 0; i < coupons.size(); i++) {
			System.out.printf("%3s %10s %15s %15s %17s %11s %11s %7s %7s %10s", (coupons.get(i)).getId(),
					(coupons.get(i)).getCompanyID(), (coupons.get(i)).getCategory(), (coupons.get(i)).getTitle(),
					(coupons.get(i)).getDescription(), (coupons.get(i)).getStartDate(), (coupons.get(i)).getEndDate(),
					(coupons.get(i)).getAmount(), (coupons.get(i)).getPrice(), (coupons.get(i)).getImage());
			System.out.println();
		}
		System.out.println();
	}
}
