package com.davidgluzman.exceptions;

public class ItemAlreadyExists extends Exception {
public ItemAlreadyExists(String s) {
	super("Item already exists: "+ s);
}
}
