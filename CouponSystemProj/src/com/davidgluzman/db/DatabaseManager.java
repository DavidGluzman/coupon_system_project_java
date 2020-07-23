package com.davidgluzman.db;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

import com.davidgluzman.beans.Category;
import com.davidgluzman.beans.Company;
import com.davidgluzman.beans.Coupon;
import com.davidgluzman.beans.Customer;
import com.davidgluzman.dbdao.CompaniesDBDAO;
import com.davidgluzman.dbdao.CouponsDBDAO;
import com.davidgluzman.dbdao.CustomersDBDAO;

public class DatabaseManager {

	private static Connection connection = null;

// JDBC connection strings

	private static final String user = "root";
	private static final String password = "root";
	private static final String url = "jdbc:mysql://localhost:3306/?createDatabaseIfNotExist=TRUE&useTimezone=TRUE&serverTimezone=UTC";

//JDBC connection getters

	public static String getUser() {
		return user;
	}

	public static String getPassword() {
		return password;
	}

	public static String getUrl() {
		return url;
	}

//Create table SQL strings

	private static final String CREATE_TABLE_COMPANIES = "CREATE TABLE `coupon_system`.`companies` (\r\n"
			+ "  `ID` INT NOT NULL AUTO_INCREMENT,\r\n" + "  `NAME` VARCHAR(45) NOT NULL,\r\n"
			+ "  `EMAIL` VARCHAR(45) NOT NULL,\r\n" + "  `PASSWORD` VARCHAR(45) NOT NULL,\r\n"
			+ "  PRIMARY KEY (`ID`));\r\n" + "";

	private static final String CREATE_TABLE_CUSTOMERS = "CREATE TABLE `coupon_system`.`customers` (\r\n"
			+ "  `ID` INT NOT NULL AUTO_INCREMENT,\r\n" + "  `FIRST_NAME` VARCHAR(45) NOT NULL,\r\n"
			+ "  `LAST_NAME` VARCHAR(45) NOT NULL,\r\n" + "  `EMAIL` VARCHAR(45) NOT NULL,\r\n"
			+ "  `PASSWORD` VARCHAR(45) NOT NULL,\r\n" + "  PRIMARY KEY (`ID`));";

	// categories table
	public static void createTableCategories() throws SQLException {
		try {
			connection = ConnectionPool.getInstance().getConnection();
			String sql = "CREATE TABLE `coupon_system`.`categories`" + "(`ID` INT NOT NULL AUTO_INCREMENT,"
					+ "`NAME` VARCHAR(45) NULL," + " PRIMARY KEY (`ID`))";
			PreparedStatement statment = connection.prepareStatement(sql);
			statment.executeUpdate();
			sql = "INSERT INTO `coupon_system`.`categories` (`NAME`) VALUES ('Food');";
			statment = connection.prepareStatement(sql);
			statment.executeUpdate();
			sql = "INSERT INTO `coupon_system`.`categories` (`NAME`) VALUES ('Electricity');";
			statment = connection.prepareStatement(sql);
			statment.executeUpdate();
			sql = "INSERT INTO `coupon_system`.`categories` (`NAME`) VALUES ('Sport');";
			statment = connection.prepareStatement(sql);
			statment.executeUpdate();
			sql = "INSERT INTO `coupon_system`.`categories` (`NAME`) VALUES ('Vacation');";
			statment = connection.prepareStatement(sql);
			statment.executeUpdate();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			ConnectionPool.getInstance().returnConnection(connection);
			connection = null;
		}

	}

	public static void dropTableCategories() throws SQLException {
		try {
			connection = ConnectionPool.getInstance().getConnection();
			String sql = "DROP TABLE `coupon_system`.`categories`";
			PreparedStatement statment = connection.prepareStatement(sql);
			statment.executeUpdate();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			ConnectionPool.getInstance().returnConnection(connection);
			connection = null;
		}

	}

	private static final String CREATE_TABLE_COUPONS = "CREATE TABLE `coupon_system`.`coupons` (\r\n"
			+ "  `ID` INT NOT NULL AUTO_INCREMENT,\r\n" + "  `COMPANY_ID` INT NOT NULL,\r\n"
			+ "  `CATEGORY_ID` INT NOT NULL,\r\n" + "  `TITLE` VARCHAR(45) NOT NULL,\r\n"
			+ "  `DESCRIPTION` VARCHAR(45) NOT NULL,\r\n" + "  `START_DATE` DATE NOT NULL,\r\n"
			+ "  `END_DATE` DATE NOT NULL,\r\n" + "  `AMOUNT` INT NOT NULL,\r\n" + "  `PRICE` DOUBLE NOT NULL,\r\n"
			+ "  `IMAGE` VARCHAR(45) NOT NULL,\r\n" + "  PRIMARY KEY (`ID`),\r\n"
			+ "  INDEX `COMPANY_ID _idx` (`COMPANY_ID` ASC) VISIBLE,\r\n"
			+ "  INDEX `CATEGORY_ID _idx` (`CATEGORY_ID` ASC) VISIBLE,\r\n" + "  CONSTRAINT `COMPANY_ID `\r\n"
			+ "    FOREIGN KEY (`COMPANY_ID`)\r\n" + "    REFERENCES `coupon_system`.`companies` (`ID`)\r\n"
			+ "    ON DELETE NO ACTION\r\n" + "    ON UPDATE NO ACTION,\r\n" + "  CONSTRAINT `CATEGORY_ID `\r\n"
			+ "    FOREIGN KEY (`CATEGORY_ID`)\r\n" + "    REFERENCES `coupon_system`.`categories` (`ID`)\r\n"
			+ "    ON DELETE NO ACTION\r\n" + "    ON UPDATE NO ACTION);";

	private static final String CREATE_TABLE_CUSTOMERS_VS_COUPONS = "    CREATE TABLE `coupon_system`.`customers_vs_coupons` (\r\n"
			+ "  `CUSTOMER_ID` INT NOT NULL,\r\n" + "  `COUPON_ID` INT NOT NULL,\r\n"
			+ "  PRIMARY KEY (`CUSTOMER_ID`, `COUPON_ID`),\r\n"
			+ "  INDEX `COUPON_ID _idx` (`COUPON_ID` ASC) VISIBLE,\r\n" + "  CONSTRAINT `CUSTOMER_ID `\r\n"
			+ "    FOREIGN KEY (`CUSTOMER_ID`)\r\n" + "    REFERENCES `coupon_system`.`customers` (`ID`)\r\n"
			+ "    ON DELETE NO ACTION\r\n" + "    ON UPDATE NO ACTION,\r\n" + "  CONSTRAINT `COUPON_ID `\r\n"
			+ "    FOREIGN KEY (`COUPON_ID`)\r\n" + "    REFERENCES `coupon_system`.`coupons` (`ID`)\r\n"
			+ "    ON DELETE NO ACTION\r\n" + "    ON UPDATE NO ACTION);\r\n" + "";

//Drop table SQL strings

	private static final String DROP_TABLE_COMPANIES = "DROP TABLE `coupon_system`.`companies`;";
	private static final String DROP_TABLE_CUSTOMERS = "DROP TABLE `coupon_system`.`customers`;";
	private static final String DROP_TABLE_CATEGORIES = "DROP TABLE `coupon_system`.`categories`;";
	private static final String DROP_TABLE_COUPONS = "DROP TABLE `coupon_system`.`coupons`;";
	private static final String DROP_TABLE_CUSTOMERS_VS_COUPONS = "DROP TABLE `coupon_system`.`customers_vs_coupons`;";

//This method is running SQL query  

	public static void runSQLQuery(String sql) {
		try {
			// STEP 2 - Open JDBC Connection
			connection = ConnectionPool.getInstance().getConnection();
			// STEP 3 - Execute Statement of SQL
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.executeUpdate();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			// STEP 5 - Close JDBC Connection

			ConnectionPool.getInstance().returnConnection(connection);

			connection = null;
		}
	}

//This method is dropping and creating all tables	

	public static void DropAndCreateDatabase() throws SQLException {

		runSQLQuery(DROP_TABLE_CUSTOMERS_VS_COUPONS);
		runSQLQuery(DROP_TABLE_COUPONS);
		runSQLQuery(DROP_TABLE_COMPANIES);
		runSQLQuery(DROP_TABLE_CUSTOMERS);
		runSQLQuery(DROP_TABLE_CATEGORIES);
		runSQLQuery(CREATE_TABLE_COMPANIES);
		runSQLQuery(CREATE_TABLE_CUSTOMERS);
		createTableCategories();
		runSQLQuery(CREATE_TABLE_COUPONS);
		runSQLQuery(CREATE_TABLE_CUSTOMERS_VS_COUPONS);
FillDatabase();

	}

	public static void DropDatabase() {
		runSQLQuery(DROP_TABLE_CUSTOMERS_VS_COUPONS);
		runSQLQuery(DROP_TABLE_COUPONS);
		runSQLQuery(DROP_TABLE_COMPANIES);
		runSQLQuery(DROP_TABLE_CUSTOMERS);
		runSQLQuery(DROP_TABLE_CATEGORIES);
	}

	public static void CreateDatabase() throws SQLException {
		runSQLQuery(CREATE_TABLE_COMPANIES);
		runSQLQuery(CREATE_TABLE_CUSTOMERS);
		createTableCategories();
		runSQLQuery(CREATE_TABLE_COUPONS);
		runSQLQuery(CREATE_TABLE_CUSTOMERS_VS_COUPONS);
		FillDatabase();
	}

	public static void FillDatabase() {

		CompaniesDBDAO companiesDBDAO = new CompaniesDBDAO();
		Company company = new Company("Coca-Cola", "Cola@gmail.com", "1111", null);
		Company company1 = new Company("Pepsi", "Pepso@gmail.com", "2222", null);
		Company company2 = new Company("Spring", "Spring@gmail.com", "3333", null);
		Company company3 = new Company("Prigat", "Prigat@gmail.com", "4444", null);
		companiesDBDAO.addCompany(company);
		companiesDBDAO.addCompany(company1);
		companiesDBDAO.addCompany(company2);
		companiesDBDAO.addCompany(company3);
		CustomersDBDAO customersDBDAO = new CustomersDBDAO();
		Customer customer = new Customer("Yossi", "Shemi", "yossi@gmail.com", "111", null);
		Customer customer1 = new Customer("Kobi", "Shshsa", "Kobi@gmail.com", "222", null);
		Customer customer2 = new Customer("Noam", "Marciano", "Noam@gmail.com", "333", null);
		Customer customer3 = new Customer("Eden", "Gal", "Eden@gmail.com", "444", null);
		customersDBDAO.addCustomer(customer);
		customersDBDAO.addCustomer(customer1);
		customersDBDAO.addCustomer(customer2);
		customersDBDAO.addCustomer(customer3);

		CouponsDBDAO couponsDBDAO = new CouponsDBDAO();
		Coupon coupon = new Coupon(1, Category.Food, "couponOne", "50% discount", new Date(2020, 01, 01),
				new Date(2020, 02, 01), 10, 50, "imageURL");
		Coupon coupon1 = new Coupon(1, Category.Electricity, "couponTwo", "50% discount", new Date(2020, 02, 01),
				new Date(2020, 03, 01), 10, 50, "imageURL");
		Coupon coupon2 = new Coupon(2, Category.Vacation, "couponThree", "50% discount", new Date(2020, 03, 01),
				new Date(2020, 04, 01), 10, 50, "imageURL");
		Coupon coupon3 = new Coupon(2, Category.Sport, "couponFour", "50% discount", new Date(2020, 04, 01),
				new Date(2020, 05, 01), 10, 50, "imageURL");
		Coupon coupon4 = new Coupon(3, Category.Food, "couponFive", "1+1", new Date(2020, 05, 01),
				new Date(2020, 06, 01), 10, 50, "imageURL");
		Coupon coupon5 = new Coupon(2, Category.Electricity, "couponSix", "1+1", new Date(2020, 8, 01),
				new Date(2020, 9, 01), 10, 50, "imageURL");
		Coupon coupon6 = new Coupon(2, Category.Vacation, "couponSeven", "1+1", new Date(2020, 10, 01),
				new Date(2020, 11, 01), 10, 50, "imageURL");
		Coupon coupon7 = new Coupon(3, Category.Vacation, "couponEight", "1+1", new Date(2020, 10, 01),
				new Date(2020, 11, 01), 10, 50, "imageURL");
		couponsDBDAO.addCoupon(coupon);
		couponsDBDAO.addCoupon(coupon1);
		couponsDBDAO.addCoupon(coupon2);
		couponsDBDAO.addCoupon(coupon3);
		couponsDBDAO.addCoupon(coupon4);
		couponsDBDAO.addCoupon(coupon5);
		couponsDBDAO.addCoupon(coupon6);
		couponsDBDAO.addCoupon(coupon7);

	}

}
