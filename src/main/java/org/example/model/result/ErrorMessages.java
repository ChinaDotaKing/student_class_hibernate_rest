package org.example.model.result;

public enum ErrorMessages {
	
	MISSING_REQUIRED_FIELD("Missing required field. Please check documentation for required fields"),
	RECORD_ALREADY_EXIST("Record already exists"),
	INTERNAL_SERVER_ERROR("Internal server error"),
	NO_RECORD_FOUND("Record with provided id is not found"),
	AUTHENTICATION_FAILED("Authentication failed"),
	COULD_NOT_UPDATE_RECORD("Could not update record"),
	COULD_NOT_DELETE_RECORD("Could not delete record"),
	EMAIL_ADDRESS_NOT_VERIFIED("Email address could not be verified"),
	STUDENT_NOT_ACTIVE("Student is not active"),
	CLASS_NOT_ACTIVE("Class is not active"),
	PASS_THE_PREREQUISITE("didn't pass prereq:"),
	WITHDRAWN("you have withdrawn from the class"),
	Time_CONFLICT("you have a time conflict"),
	PASSED("you have passed the course"),



	CLASS_NOT_FOUNT("Class not found");

	private String errorMessage;
	
	ErrorMessages(String errorMessage){
		this.errorMessage = errorMessage;
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}
	
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
}
