package com.davidgluzman.facade;

import com.davidgluzman.exceptions.FailedToLogin;

public class LoginManager {

	private static LoginManager instance = null;

	public static LoginManager getInstance() {
		if (instance == null)
			synchronized (LoginManager.class) {
				if (instance == null)
					instance = new LoginManager();
			}

		return instance;
	}

	public ClientFacade login(String email, String password, ClientType clientType) throws FailedToLogin {
		AdminFacade adminFacade = new AdminFacade();
		CompanyFacade companyFacade = new CompanyFacade();
		CustomerFacade customerFacade = new CustomerFacade();
		if (clientType == ClientType.Admin && adminFacade.login(email, password) == true) {
			return adminFacade;
		} else if (clientType == ClientType.Customer && customerFacade.login(email, password) == true) {
			return customerFacade;
		} else if (clientType == ClientType.Company && companyFacade.login(email, password) == true) {
			return companyFacade;
		}else {
		return null;
		}
	}
}
