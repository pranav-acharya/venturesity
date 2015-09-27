package com.tesigners.app.model;

import java.sql.Connection;
import java.util.ArrayList;

import javax.ws.rs.core.Response;

import com.tesigners.app.pojo.AuthenticationResponse;
import com.tesigners.app.pojo.UserSignupModel;
import com.tesigners.app.dao.DBConnection;
 
import com.tesigners.app.dao.LoginHandler;
import com.tesigners.app.dao.UserInfoHandler;
 
public class SecurityManager {
 
public AuthenticationResponse authenticateUserLogin(String email, String password) throws Exception {
	try {
		 DBConnection database= new DBConnection();
		 Connection connection = database.getConnection();
		 LoginHandler loginHandler= new LoginHandler();
		 return loginHandler.authenticateUserLogin(connection,email,password);
	 } catch (Exception e) {
		 throw e;
	 }	
 }

public String userSignup(UserSignupModel userData) throws Exception {
	try {
		 DBConnection database= new DBConnection();
		 Connection connection = database.getConnection();
		 LoginHandler loginHandler= new LoginHandler();
		 return loginHandler.userSignupProcess(connection,userData);
	 } catch (Exception e) {
		 throw e;
	 }	
	
}

public String activateUser(String activationCode,String email) throws Exception {
	try {
		 DBConnection database= new DBConnection();
		 Connection connection = database.getConnection();
		 LoginHandler loginHandler= new LoginHandler();
		 return loginHandler.activateUser(connection,activationCode,email);
	 } catch (Exception e) {
		 throw e;
	 }	
}

public boolean trackRide(String lat,String lon) throws Exception {
	try {
		 DBConnection database= new DBConnection();
		 Connection connection = database.getConnection();
		 LoginHandler loginHandler= new LoginHandler();
		 return loginHandler.trackRide(connection,lat,lon);
	 } catch (Exception e) {
		 throw e;
	 }	
}

public String updateRide() throws Exception {
	try {
		 DBConnection database= new DBConnection();
		 Connection connection = database.getConnection();
		 LoginHandler loginHandler= new LoginHandler();
		 return loginHandler.updateRide(connection);
	 } catch (Exception e) {
		 throw e;
	 }	
}

/*public Response fetchUserOrders(int id) throws Exception {
	// TODO Auto-generated method stub
	try {
		 DBConnection database= new DBConnection();
		 Connection connection = database.getConnection();
		 UserInfoHandler orderInfoHandler= new UserInfoHandler();
		 return userInfoHandler.fetchUserOrders(connection,id);
	 } catch (Exception e) {
		 throw e;
	 }
}*/

}
 