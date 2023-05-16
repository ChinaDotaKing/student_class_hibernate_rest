package org.example.exceptions;

import java.util.Date;

import io.jsonwebtoken.SignatureException;
import org.example.model.result.ErrorMessage;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;



@ControllerAdvice
public class AppExceptionsHandler {

	@ExceptionHandler(value = {BadCredentialsException.class} )
	public ResponseEntity<Object> handleBadCredentials
			(BadCredentialsException ex,WebRequest request)
	{

		ErrorMessage errorMessage = new ErrorMessage(new Date(),ex.getMessage());
		return new ResponseEntity<>(errorMessage,new HttpHeaders(),HttpStatus.UNAUTHORIZED);
	}



	@ExceptionHandler(value = {SignatureException.class} )
	public ResponseEntity<Object> handleWrongJwt
			(SignatureException ex,WebRequest request)
	{

		ErrorMessage errorMessage = new ErrorMessage(new Date(),"Wrong jwt");
		return new ResponseEntity<>(errorMessage,new HttpHeaders(),HttpStatus.UNAUTHORIZED);
	}


	@ExceptionHandler(value = {UserServiceException.class} )
	public ResponseEntity<Object> handleUserServiceException
	(UserServiceException ex,WebRequest request)
	{
		 
		ErrorMessage errorMessage = new ErrorMessage(new Date(),ex.getMessage());
		return new ResponseEntity<>(errorMessage,new HttpHeaders(),HttpStatus.INTERNAL_SERVER_ERROR);
	 }
	
	@ExceptionHandler(value = {MethodArgumentNotValidException.class} )
	public ResponseEntity<Object> handleUserServiceException
	(MethodArgumentNotValidException ex,WebRequest request){

		ErrorMessage errorMessage = new ErrorMessage(new Date(),ex.getMessage());
		return new ResponseEntity<>(errorMessage,new HttpHeaders(),HttpStatus.BAD_REQUEST);
	 }
}
