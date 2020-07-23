package com.davidgluzman.exceptions;

public class FailedToLogin extends Exception {
public FailedToLogin(String s) {
	super("Login Failed"+s);
}
}
