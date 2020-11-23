package com.jaycoder.web.model;

public class Result {
	private boolean valid;
	
	private String errorMessage;
	
	private Integer count;
	
	private Result(boolean valid,  Integer count, String errorMessage) {
		this.valid = valid;
		this.count = count;
		this.errorMessage = errorMessage;
	}
	
	public boolean isValid() {
			return valid;
	}
	
	public Integer getCount() {
			return count;
	}
	
	public String getErrorMessage() {
			return errorMessage;
	}
	
	public static Result ok(Integer count) {
			return new Result(true, count, null);
	}
	
	public static Result fail(Integer count, String errorMessage) {
			return new Result(false, count, errorMessage);
	}
}