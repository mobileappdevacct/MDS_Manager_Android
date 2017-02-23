package com.mymdsmanager.task;

public class ErrorObj {

	
	private String message;
	
	public ErrorObj(String pMessage)
	{
		message = pMessage;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
