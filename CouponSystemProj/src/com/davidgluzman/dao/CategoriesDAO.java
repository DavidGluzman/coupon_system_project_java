package com.davidgluzman.dao;

import com.davidgluzman.beans.Category;

public interface CategoriesDAO {
	int getCategoryID(Category category);

	Category getCategoryName(int ID);
}
