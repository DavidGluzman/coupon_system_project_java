package com.davidgluzman.dbdao;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.davidgluzman.beans.Coupon;
import com.davidgluzman.beans.Customer;
import com.davidgluzman.dao.CustomersDAO;
import com.davidgluzman.db.ConnectionPool;

public class CustomersDBDAO implements CustomersDAO {
	private static final String isCustomerExistsEmailPassword = "SELECT * FROM coupon_system.customers where email= ? AND password= ?";
	// private static final String isCustomerExistsID = "SELECT * FROM
	// coupon_system.customers where id=?";
//	private static final String isCustomerExistsEmail = "SELECT * FROM coupon_system.customers where email=?";
	 private static final String getCustomerIdbyEmailPassword = "SELECT ID FROM coupon_system.customers where email= ? AND password= ?";
	private static final String addCustomer = "INSERT INTO `coupon_system`.`customers` (`ID`,`FIRST_NAME`, `LAST_NAME`, `EMAIL`, `PASSWORD`) VALUES (?,?,?,?,?)";
	private static final String deleteCustomer = "DELETE FROM `coupon_system`.`customers` WHERE (`ID` = ?)";
	private static final String updateCustomer = "UPDATE `coupon_system`.`customers` SET `FIRST_NAME` = ?, `LAST_NAME` = ?, `EMAIL` = ?, `PASSWORD` = ? WHERE (`ID` = ?)";
	private static final String getAllCustomers = "SELECT * FROM coupon_system.customers";
	private static final String getCustomerByID = "SELECT * FROM coupon_system.customers WHERE (`ID` = ?)";

	@Override
	public boolean isCustomerExists(String email, String Password) {
		Connection connection = null;
		boolean flag = false;
		try {
			// STEP 2 - Open JDBC Connection
			connection = ConnectionPool.getInstance().getConnection();
			// STEP 3 - Execute Statement of SQL
			PreparedStatement statement = connection.prepareStatement(isCustomerExistsEmailPassword);
			statement.setString(1, email);
			statement.setString(2, Password);
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

	@Override
	public void addCustomer(Customer customer) {
		Connection connection = null;

		try {
			// STEP 2 - Open JDBC Connection
			connection = ConnectionPool.getInstance().getConnection();
			// STEP 3 - Execute Statement of SQL
			PreparedStatement statement = connection.prepareStatement(addCustomer);
			statement.setInt(1, customer.getId());
			statement.setString(2, customer.getFirstName());
			statement.setString(3, customer.getLastName());
			statement.setString(4, customer.getEmail());
			statement.setString(5, customer.getPassword());
			statement.executeUpdate();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			// STEP 5 - Close JDBC Connection

			ConnectionPool.getInstance().returnConnection(connection);

		}
	}

	@Override
	public void updateCustomer(Customer customer) {
		Connection connection = null;

		try {
			// STEP 2 - Open JDBC Connection
			connection = ConnectionPool.getInstance().getConnection();
			// STEP 3 - Execute Statement of SQL
			PreparedStatement statement = connection.prepareStatement(updateCustomer);
			statement.setString(1, customer.getFirstName());
			statement.setString(2, customer.getLastName());
			statement.setString(3, customer.getEmail());
			statement.setString(4, customer.getPassword());
			statement.setInt(5, customer.getId());
			statement.executeUpdate();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			// STEP 5 - Close JDBC Connection

			ConnectionPool.getInstance().returnConnection(connection);

		}
	}

	@Override
	public void deleteCustomer(int customerID) {
		Connection connection = null;

		try {
			CouponsDBDAO couponsDBDAO = new CouponsDBDAO();
			List<Coupon> coupons = couponsDBDAO.getAllCouponsByCustomerID(customerID);
			for (Coupon coupon : coupons) {
				couponsDBDAO.deleteCouponPurchase(coupon.getId());
			}
			// STEP 2 - Open JDBC Connection
			connection = ConnectionPool.getInstance().getConnection();
			// STEP 3 - Execute Statement of SQL
			PreparedStatement statement = connection.prepareStatement(deleteCustomer);
			statement.setInt(1, customerID);
			statement.executeUpdate();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			// STEP 5 - Close JDBC Connection

			ConnectionPool.getInstance().returnConnection(connection);

		}
	}

	@Override
	public List<Customer> getAllCustomers() {
		List<Customer> customers = new ArrayList<Customer>();
		Connection connection = null;

		try {
			// STEP 2 - Open JDBC Connection
			connection = ConnectionPool.getInstance().getConnection();
			// STEP 3 - Execute Statement of SQL
			java.sql.Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(getAllCustomers);
			while (resultSet.next()) {
				int id = resultSet.getInt(1);
				String firstName = resultSet.getString(2);
				String lastName = resultSet.getString(3);
				String email = resultSet.getString(4);
				String password = resultSet.getString(5);
				customers.add(new Customer(id, firstName, lastName, email, password, null));
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			// STEP 5 - Close JDBC Connection

			ConnectionPool.getInstance().returnConnection(connection);

		}
		return customers;
	}

	@Override
	public Customer getOneCustomer(int customerID) {
		Customer customer = null;
		Connection connection = null;

		try {
			// STEP 2 - Open JDBC Connection
			connection = ConnectionPool.getInstance().getConnection();
			// STEP 3 - Execute Statement of SQL
			PreparedStatement statement = connection.prepareStatement(getCustomerByID);
			statement.setInt(1, customerID);
			ResultSet resultSet = statement.executeQuery();

			while (resultSet.next()) {
				String firstName = resultSet.getString(2);
				String lastName = resultSet.getString(3);
				String email = resultSet.getString(4);
				String password = resultSet.getString(5);
				customer = new Customer(customerID, firstName, lastName, email, password, null);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			// STEP 5 - Close JDBC Connection

			ConnectionPool.getInstance().returnConnection(connection);

		}
		return customer;
	}
	//used in CustomerFacade for login purposes
		public int getCustomersIdByEmailAndPassword(String email,String password) {
		Connection connection = null;
		int id=0;
			
		try {
			// STEP 2 - Open JDBC Connection
			connection = ConnectionPool.getInstance().getConnection();
			// STEP 3 - Execute Statement of SQL
			PreparedStatement statement = connection.prepareStatement(getCustomerIdbyEmailPassword);
			statement.setString(1, email);
			statement.setString(2, password);
			ResultSet resultSet=statement.executeQuery();
		if (resultSet.next()) {
			id=resultSet.getInt(1);
		}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			// STEP 5 - Close JDBC Connection
			
			ConnectionPool.getInstance().returnConnection(connection);
			
			
		}return id ;
	}

}