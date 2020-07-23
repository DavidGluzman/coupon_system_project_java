package com.davidgluzman.dbdao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.davidgluzman.beans.Category;
import com.davidgluzman.dao.CategoriesDAO;
import com.davidgluzman.db.ConnectionPool;

public class CategoriesDBDAO implements CategoriesDAO {

	private static final String getCategoryID = "SELECT ID FROM coupon_system.categories where NAME=?;";
	private static final String getCategoryName = "SELECT NAME FROM coupon_system.categories where ID=?;";

	@Override
	public int getCategoryID(Category category) {
		Connection connection = null;
		int x = 0;
		try {
			// STEP 2 - Open JDBC Connection
			connection = ConnectionPool.getInstance().getConnection();
			// STEP 3 - Execute Statement of SQL
			PreparedStatement statement = connection.prepareStatement(getCategoryID);
			statement.setString(1, category.toString());
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				x = resultSet.getInt(1);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			// STEP 5 - Close JDBC Connection

			ConnectionPool.getInstance().returnConnection(connection);

			connection = null;
		}
		return x;
	}

	@Override
	public Category getCategoryName(int ID) {
		Connection connection = null;
		String category = null;
		try {
			// STEP 2 - Open JDBC Connection
			connection = ConnectionPool.getInstance().getConnection();
			// STEP 3 - Execute Statement of SQL
			PreparedStatement statement = connection.prepareStatement(getCategoryName);
			statement.setInt(1, ID);
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				category = resultSet.getString(1);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			// STEP 5 - Close JDBC Connection

			ConnectionPool.getInstance().returnConnection(connection);

		}
		return Category.valueOf(category);
	}

}
