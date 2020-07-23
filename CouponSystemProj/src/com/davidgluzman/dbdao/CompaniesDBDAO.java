package com.davidgluzman.dbdao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.davidgluzman.beans.Company;
import com.davidgluzman.beans.Coupon;
import com.davidgluzman.dao.CompaniesDAO;
import com.davidgluzman.db.ConnectionPool;

public class CompaniesDBDAO implements CompaniesDAO {

	private static final String isCompanyExistsEmailPassword = "SELECT * FROM coupon_system.companies where email= ? AND password= ?";
//	private static final String isCompanyExistsID = "SELECT * FROM coupon_system.companies where id=?";
//	private static final String isCompanyExistsEmail = "SELECT * FROM coupon_system.companies where email=?";
//	private static final String isCompanyExistsName = "SELECT * FROM coupon_system.companies where name=?";
	private static final String getCompanyIdbyEmailPassword = "SELECT ID FROM coupon_system.companies where email= ? AND password= ?";
	private static final String addCompany = "INSERT INTO `coupon_system`.`companies` (`ID`, `NAME`, `EMAIL`, `PASSWORD`) VALUES (?,?,?,?)";;
	private static final String deleteCompany = "DELETE FROM `coupon_system`.`companies` WHERE (`ID` = ?)";
	private static final String updateCompany = "UPDATE `coupon_system`.`companies` SET `EMAIL` = ?, `PASSWORD` = ? WHERE (`ID` = ?);";
	private static final String getAllCompanies = "SELECT * FROM coupon_system.companies";
	private static final String getOneCompanyByID = "SELECT * FROM coupon_system.companies WHERE (`ID` = ?)";

	@Override
	public boolean isCompanyExist(String email, String password) {
		Connection connection = null;
		boolean flag = false;
		try {
			// STEP 2 - Open JDBC Connection
			connection = ConnectionPool.getInstance().getConnection();
			// STEP 3 - Execute Statement of SQL
			PreparedStatement statement = connection.prepareStatement(isCompanyExistsEmailPassword);
			statement.setString(1, email);
			statement.setString(2, password);
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
	public void addCompany(Company company) {
		Connection connection = null;

		try {
			// STEP 2 - Open JDBC Connection
			connection = ConnectionPool.getInstance().getConnection();
			// STEP 3 - Execute Statement of SQL
			PreparedStatement statement = connection.prepareStatement(addCompany);
			statement.setInt(1, company.getId());
			statement.setString(2, company.getName());
			statement.setString(3, company.getEmail());
			statement.setString(4, company.getPassword());
			statement.executeUpdate();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			// STEP 5 - Close JDBC Connection

			ConnectionPool.getInstance().returnConnection(connection);

		}
	}

	@Override
	public void updateCompany(Company company) {
		Connection connection = null;

		try {
			// STEP 2 - Open JDBC Connection
			connection = ConnectionPool.getInstance().getConnection();
			// STEP 3 - Execute Statement of SQL
			PreparedStatement statement = connection.prepareStatement(updateCompany);
			statement.setString(1, company.getEmail());
			statement.setString(2, company.getPassword());
			statement.setInt(3, company.getId());
			statement.executeUpdate();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			// STEP 5 - Close JDBC Connection

			ConnectionPool.getInstance().returnConnection(connection);

		}

	}

	@Override
	public void deleteCompany(int companyID) {
		Connection connection = null;

		try {
			// STEP 2 - Open JDBC Connection
			connection = ConnectionPool.getInstance().getConnection();
			//deletes all coupons related to this company
			CouponsDBDAO couponsDBDAO = new CouponsDBDAO();
			List<Coupon> coupons = couponsDBDAO.getAllCoupons(companyID);
			for (Coupon coupon : coupons) {
				couponsDBDAO.deleteCouponPurchase(coupon.getId());
				couponsDBDAO.deleteCoupon(coupon.getId());
			}
			// STEP 3 - Execute Statement of SQL
			PreparedStatement statement = connection.prepareStatement(deleteCompany);
			statement.setInt(1, companyID);
			statement.executeUpdate();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			// STEP 5 - Close JDBC Connection

			ConnectionPool.getInstance().returnConnection(connection);

		}

	}

	@Override
	public List<Company> getAllCompanies() {
		List<Company> companies = new ArrayList<Company>();
		Connection connection = null;

		try {
			// STEP 2 - Open JDBC Connection
			connection = ConnectionPool.getInstance().getConnection();
			// STEP 3 - Execute Statement of SQL
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(getAllCompanies);
			while (resultSet.next()) {
				int id = resultSet.getInt(1);
				String name = resultSet.getString(2);
				String email = resultSet.getString(3);
				String password = resultSet.getString(4);
				companies.add(new Company(id, name, email, password, new CouponsDBDAO().getAllCoupons(id)));
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			// STEP 5 - Close JDBC Connection

			ConnectionPool.getInstance().returnConnection(connection);

		}
		return companies;
	}

	@Override
	public Company getOneCompany(int companyID) {
		Company company = null;
		Connection connection = null;

		try {
			// STEP 2 - Open JDBC Connection
			connection = ConnectionPool.getInstance().getConnection();
			// STEP 3 - Execute Statement of SQL
			PreparedStatement statement = connection.prepareStatement(getOneCompanyByID);
			statement.setInt(1, companyID);
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				String name = resultSet.getString(2);
				String email = resultSet.getString(3);
				String password = resultSet.getString(4);
				company = new Company(companyID, name, email, password, new CouponsDBDAO().getAllCoupons(companyID));
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			// STEP 5 - Close JDBC Connection

			ConnectionPool.getInstance().returnConnection(connection);

		}
		return company;
	}
	//used in CompanyFacade for login purposes
	public int getCompanyIdByEmailAndPassword(String email,String password) {
	Connection connection = null;
	int id=0;
		
	try {
		// STEP 2 - Open JDBC Connection
		connection = ConnectionPool.getInstance().getConnection();
		// STEP 3 - Execute Statement of SQL
		PreparedStatement statement = connection.prepareStatement(getCompanyIdbyEmailPassword);
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
