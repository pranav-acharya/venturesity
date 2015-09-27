package com.tesigners.app.restws;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import com.tesigners.app.model.SecurityManager;
import com.tesigners.app.pojo.AuthenticationResponse;
import com.tesigners.app.pojo.UserLoginModel;
import com.tesigners.app.pojo.UserSignupModel;
 
@Path("loginservice")
public class LoginService {
 
@GET
@Path("/track_ride")
@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
//URL = http://localhost:8080/TesignerRWS/rest/loginservice/login?email=amit@gmail.com&activationcode=XXXXX
public Response updateRide1(@QueryParam("lat") String lat, @QueryParam("lon") String lon) {
	 //ResponseBuilder rb = Response.ok(activateUser(activationCode,email));
     //return rb.build();
	String curVal = lat +","+lon;
	System.out.println("lat="+lat);
	trackRide(lat,lon);
	ResponseBuilder rb = Response.ok("{'status':'success'}");
	return rb.build();
 }	

@GET
@Path("/update_ride")
@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
//URL = http://localhost:8080/TesignerRWS/rest/loginservice/login?email=amit@gmail.com&activationcode=XXXXX
public Response queryUpdateRide() {

	String pos = updateRide();
	System.out.println("pos="+pos);
	ResponseBuilder rb = Response.ok("<p>"+ pos + "</p>");
	return rb.build();
 }

@GET
@Path("/login")
@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
//URL = http://localhost:8080/TesignerRWS/rest/loginservice/login?email=amit@gmail.com&activationcode=XXXXX
public Response userActivationService(@QueryParam("email") String email, @QueryParam("activationcode") String activationCode) {
	 //ResponseBuilder rb = Response.ok(activateUser(activationCode,email));
     //return rb.build();
	ResponseBuilder rb = Response.ok("<status>success</status>");
	return rb.build();
 }

@POST
@Path("/login")
@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
public Response authenticationService(UserLoginModel loginData) {
	 ResponseBuilder rb = Response.ok(authenticateUser(loginData));
     return rb.build();
 }



@Path("/login/create")
@GET
@Produces({MediaType.APPLICATION_XML , MediaType.APPLICATION_JSON})
public Response UserSignupService()        
{
	ResponseBuilder rb = Response.ok(new UserSignupModel());
    return rb.build();
	
}
 

@Path("/login/create")
@POST
@Produces({MediaType.APPLICATION_XML , MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_XML , MediaType.APPLICATION_JSON})
public Response UserSignupService( UserSignupModel userData)        
{
	ResponseBuilder rb = Response.ok(userSignup(userData));
    return rb.build();
	
}


 public AuthenticationResponse authenticateUser(UserLoginModel loginData) {
	 AuthenticationResponse response=null;
	 try {
	    SecurityManager securityManager= new SecurityManager();
		return securityManager.authenticateUserLogin(loginData.getEmail(), loginData.getPassword());
	} catch (Exception e) {
		e.printStackTrace();
	}
	 return response;
 }
 
 public String activateUser(String activationCode,String email) {
	 String response=null;
	 try {
	    SecurityManager securityManager= new SecurityManager();
		return securityManager.activateUser(activationCode,email);
	} catch (Exception e) {
		e.printStackTrace();
	}
	 return response;
 }
 
 public void trackRide(String lat,String lon) {
	 String response=null;
	 try {
	    SecurityManager securityManager= new SecurityManager();
		securityManager.trackRide(lat,lon);
	} catch (Exception e) {
		e.printStackTrace();
	}	 
 }
 
 public String updateRide() {
	 String response=null;
	 try {
	    SecurityManager securityManager= new SecurityManager();
	    response= securityManager.updateRide();
	} catch (Exception e) {
		e.printStackTrace();
	}
	 return response;
 }
 
 public String userSignup(UserSignupModel userData) {
	 String response=null;
	 try {
	    SecurityManager securityManager= new SecurityManager();
		return securityManager.userSignup(userData);
	} catch (Exception e) {
		e.printStackTrace();
	}
	 return response;
 }
 
}