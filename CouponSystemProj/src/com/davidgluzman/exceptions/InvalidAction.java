package com.davidgluzman.exceptions;

public class InvalidAction extends Exception {
public InvalidAction(String s) {
	super("Invalid Action "+s);
}
}
