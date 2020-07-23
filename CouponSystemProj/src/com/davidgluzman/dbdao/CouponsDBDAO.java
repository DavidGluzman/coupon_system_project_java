package com.davidgluzman.dbdao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.davidgluzman.beans.Category;

import com.davidgluzman.beans.Coupon;
import com.davidgluzman.dao.CouponsDAO;
import com.davidgluzman.db.ConnectionPool;
import com.davidgluzman.utils.Utils;

public class CouponsDBDAO implements CouponsDAO {

	private static final String isCouponExists = "SELECT * FROM coupon_system.coupons where id=?";
	private static final String addCoupon = "INSERT INTO `coupon_system`.`coupons`"
			+ " (`COMPANY_ID`, `CATEGORY_ID`, `TITLE`, `DESCRIPTION`, `START_DATE`, `END_DATE`, `AMOUNT`, `PRICE`, `IMAGE`)"
			+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";
	private static final String updateCoupon = "	UPDATE `coupon_system`.`coupons` SET "
			+ "`CATEGORY_ID` = ? ,`TITLE` = ? ,`DESCRIPTION` = ? ,`START_DATE` = ? ,`END_DATE` = ?, `AMOUNT` = ? ,`PRICE` = ? ,`IMAGE` = ?"
			+ " WHERE (`ID` = ?);";
	private static final String deleteCoupon = "DELETE FROM `coupon_system`.`coupons` WHERE (`ID` = ?);";
	private static final String getAllCoupons = "SELECT * FROM coupon_system.coupons";
	private static final String getOneCoupon = "SELECT * FROM coupon_system.coupons  WHERE (`ID` = ?);";
	private static final String addCouponPurchase = "INSERT INTO `coupon_system`.`customers_vs_coupons` (`CUSTOMER_ID`, `COUPON_ID`) VALUES (?, ?);";
	private static final String deleteCouponPurchaseByCouponCustomer = "delete from coupon_system.customers_vs_coupons where CUSTOMER_ID= ? and COUPON_ID= ?;";
	private static final String getAllCouponsByCompanyID = "SELECT * FROM coupon_system.coupons where COMPANY_ID = ?";
	private static final String getAllCouponsByCustomerID = "SELECT COUPON_ID FROM coupon_system.customers_vs_coupons where CUSTOMER_ID = ?";
	private static final String deleteCouponPurchaseByCouponID = "delete from coupon_system.customers_vs_coupons where COUPON_ID= ?;";
	private static final String getAllCouponsByCustomerID2 = "SELECT * FROM coupon_system.coupons where id= ?;";

	@Override
	public void addCoupon(Coupon coupon) {
		Connection connection = null;
		CategoriesDBDAO CategoryDBDAO = new CategoriesDBDAO();
		try {
			// STEP 2 - Open JDBC Connection
			connection = ConnectionPool.getInstance().getConnection();
			// STEP 3 - Execute Statement of SQL
			PreparedStatement statement = connection.prepareStatement(addCoupon);
			statement.setInt(1, coupon.getCompanyID());
			statement.setInt(2, CategoryDBDAO.getCategoryID(coupon.getCategory()));
			statement.setString(3, coupon.getTitle());
			statement.setString(4, coupon.getDescription());
			statement.setDate(5, Utils.convertUtilDateToSQL(coupon.getStartDate()));
			statement.setDate(6, Utils.convertUtilDateToSQL(coupon.getEndDate()));
			statement.setInt(7, coupon.getAmount());
			statement.setDouble(8, coupon.getPrice());
			statement.setString(9, coupon.getImage());
			statement.executeUpdate();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			// STEP 5 - Close JDBC Connection

			ConnectionPool.getInstance().returnConnection(connection);

		}
	}

	@Override
	public void updateCoupon(Coupon coupon) {
		Connection connection = null;
		CategoriesDBDAO CategoryDBDAO = new CategoriesDBDAO();
		try {
			// STEP 2 - Open JDBC Connection
			connection = ConnectionPool.getInstance().getConnection();
			// STEP 3 - Execute Statement of SQL
			PreparedStatement statement = connection.prepareStatement(updateCoupon);
			statement.setInt(1, CategoryDBDAO.getCategoryID(coupon.getCategory()));
			statement.setString(2, coupon.getTitle());
			statement.setString(3, coupon.getDescription());
			statement.setDate(4, Utils.convertUtilDateToSQL(coupon.getStartDate()));
			statement.setDate(5, Utils.convertUtilDateToSQL(coupon.getEndDate()));
			statement.setInt(6, coupon.getAmount());
			statement.setDouble(7, coupon.getPrice());
			statement.setString(8, coupon.getImage());
			statement.setInt(9, coupon.getId());
			statement.executeUpdate();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			// STEP 5 - Close JDBC Connection

			ConnectionPool.getInstance().returnConnection(connection);

		}
	}

	@Override
	public void deleteCoupon(int couponID) {
		Connection connection = null;

		try {
			// STEP 2 - Open JDBC Connection
			connection = ConnectionPool.getInstance().getConnection();
			// STEP 3 - Execute Statement of SQL
			PreparedStatement statement = connection.prepareStatement(deleteCoupon);
			statement.setInt(1, couponID);
			statement.executeUpdate();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			// STEP 5 - Close JDBC Connection

			ConnectionPool.getInstance().returnConnection(connection);
		}
	}

	@Override
	public List<Coupon> getAllCoupons(int companyID) {
		List<Coupon> coupons = new ArrayList<Coupon>();
		CategoriesDBDAO categoryDBDAO = new CategoriesDBDAO();
		Connection connection = null;
		try {
			// STEP 2 - Open JDBC Connection
			connection = ConnectionPool.getInstance().getConnection();
			// STEP 3 - Execute Statement of SQL
			PreparedStatement statement = connection.prepareStatement(getAllCouponsByCompanyID);
			statement.setInt(1, companyID);
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				int id = resultSet.getInt(1);
				Category category = categoryDBDAO.getCategoryName(resultSet.getInt(3));
				String title = resultSet.getString(4);
				String description = resultSet.getString(5);
				Date startDate = resultSet.getDate(6);
				Date endDate = resultSet.getDate(7);
				int amount = resultSet.getInt(8);
				double price = resultSet.getDouble(9);
				String image = resultSet.getString(10);
				coupons.add(new Coupon(id, companyID, category, title, description, startDate, endDate, amount, price,
						image));
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			// STEP 5 - Close JDBC Connection

			ConnectionPool.getInstance().returnConnection(connection);

		}
		return coupons;
	}

	@Override
	public Coupon getOneCoupon(int couponID) {
		Coupon coupon = null;
		CategoriesDBDAO categoryDBDAO = new CategoriesDBDAO();
		Connection connection = null;
		try {
			// STEP 2 - Open JDBC Connection
			connection = ConnectionPool.getInstance().getConnection();
			// STEP 3 - Execute Statement of SQL
			PreparedStatement statement = connection.prepareStatement(getOneCoupon);
			statement.setInt(1, couponID);
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				int id = resultSet.getInt(1);
				int companyID = resultSet.getInt(2);
				Category category = categoryDBDAO.getCategoryName(resultSet.getInt(3));
				String title = resultSet.getString(4);
				String description = resultSet.getString(5);
				Date startDate = resultSet.getDate(6);
				Date endDate = resultSet.getDate(7);
				int amount = resultSet.getInt(8);
				double price = resultSet.getDouble(9);
				String image = resultSet.getString(10);
				coupon = new Coupon(id, companyID, category, title, description, startDate, endDate, amount, price,
						image);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			// STEP 5 - Close JDBC Connection

			ConnectionPool.getInstance().returnConnection(connection);

		}
		return coupon;
	}

	@Override
	public void addCouponPurchase(int customerID, int couponID) {
		Connection connection = null;
		try {
			// STEP 2 - Open JDBC Connection
			connection = ConnectionPool.getInstance().getConnection();
			// STEP 3 - Execute Statement of SQL
			PreparedStatement statement = connection.prepareStatement(addCouponPurchase);
			statement.setInt(1, customerID);
			statement.setInt(2, couponID);
			statement.executeUpdate();
			Coupon coupon = this.getOneCoupon(couponID);
			coupon.setAmount(coupon.getAmount() - 1);
			this.updateCoupon(coupon);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			// STEP 5 - Close JDBC Connection

			ConnectionPool.getInstance().returnConnection(connection);

		}
	}

	@Override
	public void deleteCouponPurchase(int customerID, int couponID) {
		Connection connection = null;

		try {
			// STEP 2 - Open JDBC Connection
			connection = ConnectionPool.getInstance().getConnection();
			// STEP 3 - Execute Statement of SQL
			PreparedStatement statement = connection.prepareStatement(deleteCouponPurchaseByCouponCustomer);
			statement.setInt(1, customerID);
			statement.setInt(2, couponID);
			statement.executeUpdate();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			// STEP 5 - Close JDBC Connection

			ConnectionPool.getInstance().returnConnection(connection);

		}
	}

	// used in CompaniesDBDAO.deleteCompany()
	public void deleteCouponPurchase(int couponID) {
		Connection connection = null;
		try {
			// STEP 2 - Open JDBC Connection
			connection = ConnectionPool.getInstance().getConnection();

			// STEP 3 - Execute Statement of SQL
			PreparedStatement statement = connection.prepareStatement(deleteCouponPurchaseByCouponID);
			statement.setInt(1, couponID);
			statement.executeUpdate();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			// STEP 5 - Close JDBC Connection
			ConnectionPool.getInstance().returnConnection(connection);
		}

	}

	// used in CustomersDBDAO.deleteCompany()
	@Override
	public List<Coupon> getAllCouponsByCustomerID(int customerID) {
		List<Coupon> coupons = new ArrayList<Coupon>();
		List<Integer> filterCoupons = new ArrayList<Integer>(); // to get all coupons ID from C_vs_C for the ID
		CategoriesDBDAO categoryDBDAO = new CategoriesDBDAO();
		Connection connection = null;

		try {
			// STEP 2 - Open JDBC Connection
			connection = ConnectionPool.getInstance().getConnection();
			// STEP 3 - Execute Statement of SQL
			PreparedStatement statement = connection.prepareStatement(getAllCouponsByCustomerID);// checks in cvc table
			statement.setInt(1, customerID);
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				filterCoupons.add(resultSet.getInt(1));
			}
			for (int i = 0; i < filterCoupons.size(); i++) {
				statement = connection.prepareStatement(getAllCouponsByCustomerID2);// checks in coupon table
				statement.setInt(1, filterCoupons.get(i));
				resultSet = statement.executeQuery();
				while (resultSet.next()) {
					int id = resultSet.getInt(1);
					int companyID = resultSet.getInt(2);
					Category category = categoryDBDAO.getCategoryName(resultSet.getInt(3));
					String title = resultSet.getString(4);
					String description = resultSet.getString(5);
					Date startDate = resultSet.getDate(6);
					Date endDate = resultSet.getDate(7);
					int amount = resultSet.getInt(8);
					double price = resultSet.getDouble(9);
					String image = resultSet.getString(10);
					coupons.add(new Coupon(id, companyID, category, title, description, startDate, endDate, amount,
							price, image));
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			// STEP 5 - Close JDBC Connection

			ConnectionPool.getInstance().returnConnection(connection);

		}
		return coupons;
	}

	@Override
	public ArrayList<Coupon> getAllCoupons() {
		ArrayList<Coupon> coupons = new ArrayList<Coupon>();
		CategoriesDBDAO categoryDBDAO = new CategoriesDBDAO();
		Connection connection = null;
		try {
			connection = ConnectionPool.getInstance().getConnection();
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(getAllCoupons);
			while (resultSet.next()) {
				int id = resultSet.getInt(1);
				int companyID = resultSet.getInt(2);
				Category category = categoryDBDAO.getCategoryName(resultSet.getInt(3));
				String title = resultSet.getString(4);
				String description = resultSet.getString(5);
				Date startDate = resultSet.getDate(6);
				Date endDate = resultSet.getDate(7);
				int amount = resultSet.getInt(8);
				double price = resultSet.getDouble(9);
				String image = resultSet.getString(10);
				coupons.add(new Coupon(id, companyID, category, title, description, startDate, endDate, amount, price,
						image));
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			ConnectionPool.getInstance().returnConnection(connection);
		}

		return coupons;
	}

	@Override
	public boolean isCouponExists(int couponID) {
		Connection connection = null;
		boolean flag = false;
		try {
			// STEP 2 - Open JDBC Connection
			connection = ConnectionPool.getInstance().getConnection();
			// STEP 3 - Execute Statement of SQL
			PreparedStatement statement = connection.prepareStatement(isCouponExists);
			statement.setInt(1, couponID);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				flag = true;
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			// STEP 5 - Close JDBC Connection

			ConnectionPool.getInstance().returnConnection(connection);

			
		}
		return flag;
	}
}
