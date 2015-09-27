package com.tesigners.app.dao;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriBuilder;

import com.tesigners.app.pojo.AuthenticationResponse;
import com.tesigners.app.pojo.UserSignupModel;
import com.tesigners.app.util.SendMailTLS;

public class LoginHandler {
	
	public AuthenticationResponse authenticateUserLogin(Connection connection,String email, String password) {
		AuthenticationResponse response = new AuthenticationResponse();
		
		String query = "select EMAIL, REGISTERED from USER_INFO where EMAIL = ?";
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			 ps = connection.prepareStatement(query);
			 ps.setString(1, email);
			  rs = ps.executeQuery();
			 if (rs.next()) {
				 int register = rs.getInt(2);
				 if(register == 1){
					 query = "Select NAME,EMAIL from USER_INFO where EMAIL= ? and PASSWORD = ?";
					 ps = connection.prepareStatement(query);
					 ps.setString(1,email);
					 ps.setString(2,password);
	                 rs=ps.executeQuery();
	                 
	                 if(rs.next())
	                 {
	                     String name = rs.getString(1);
	                     String uid = rs.getString(2);
	                     UUID uuid = UUID.randomUUID();
	                     String sysUUIDString = uuid.toString();
	                     
	                     query = "update USER_INFO set SESSION_ID = ? where EMAIL = ?";
                         ps= connection.prepareStatement(query);
                         ps.setString(1, sysUUIDString);
                         ps.setString(2, email);
                         int updateInt=ps.executeUpdate();
                         
                         //connection.commit();
                         if(updateInt == 1) {
                        	 response.setName(name);
                        	 response.setEmail(uid);
                        	 response.setSessionID(sysUUIDString);
                        	 response.setStatus("success");
                        	 response.setMsg(null);
                       }
	                 }else{
	                	 response.setStatus("fail");
                    	 response.setMsg("Unable to Authenticate");
	                	
	                 }
				 } else if(register == 0) {
					 response.setStatus("fail");
                	 response.setMsg("Account not activated");
			        }
			  }else{
				  response.setStatus("fail");
				  response.setMsg("User not signed up");  
			  }
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally
			  {
					try {
						 if(rs!= null)
							 rs.close();
						if(ps!=null)
					        ps.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			    
			      
			  }
		
		return response;
	}

	public String userSignupProcess(Connection connection, UserSignupModel userData) {
		String status = "";
		String msg = "";
		String query = "select EMAIL, REGISTERED from USER_INFO where EMAIL = ?";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try{
			stmt = connection.prepareStatement(query);
		    stmt.setString(1, userData.getEmail());
		    rs = stmt.executeQuery();
			
		    if(rs.next()) {
		        int register = rs.getInt(2);
		        if(register == 1) {
		        	status = "fail";
		        	msg = "User already Registered";
		        } else if(register == 0) {
		        	status = "fail";
		        	msg = "Users exist but account not activated";
		        }
		    }    else {
				        try
				        { 
				        	String activationCode = makeid();
				          // sendMail(activationCode,'signup',userid, url);
				            
				           query = "insert into USER_INFO ( EMAIL, PASSWORD, ACTIVATION_CODE, NAME, PHONE) values(?,?,?,?,?)";
				           msg=null;
		
				        stmt = connection.prepareStatement(query);
				        stmt.setString(1,userData.getEmail());
				        stmt.setString(2,userData.getPassword());
				        stmt.setString(3,activationCode);
				        stmt.setString(4,userData.getName());
				        stmt.setString(5,userData.getPhone());
				       
				        int rs1;
				        rs1 = stmt.executeUpdate();
				        //connection.commit();
				        if(rs1 == 1)
				      	   {
				      	      status = "success";
				      	      msg="user is successfully registered";
				      	      triggerSendMail(userData.getName(),userData.getEmail(),"signup",activationCode);
					      	  				      	      
				        }else{
				        	 status = "fail";
				      	      msg="user registration failed!";
				           }
				      	 }
				      	catch(SQLException e)
				      	{
				      		e.printStackTrace();
				      		msg = e.toString();
				      		status = "fail";
				      	} 
				      	finally {
				      		if (stmt != null) {
				      			stmt.close();
				      		}
				      	}
		      }
		    
		    
		}catch(SQLException e){
			
		}
		
		return "<UserSignup><status>"+status+"</status><msg>"+msg+"</msg></UserSignup>";
	}
	

	public String activateUser(Connection conn,
			String activationCode,String email) {
		
		String status = "";
		String msg = "";
		String query = "Select * from USER_INFO where EMAIL= ? AND ACTIVATION_CODE =?";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try{
			stmt= conn.prepareStatement(query);
		    stmt.setString(1,email);
		    stmt.setString(2,activationCode);
		    rs=stmt.executeQuery();
		    
		    if(rs.next())
		      {
		         boolean res = registerUser(conn, email);
		         	status = res ? "success":"fail" ;
		            msg="User activated successfully!";
		      }
		}catch(SQLException e){
			e.printStackTrace();
			status = "fail";
			msg="User activation failed!";
		}
		
		return "<UserActivation><status>"+status+"</status><msg>"+msg+"</msg></UserActivation>";
	}
	
	
	public boolean registerUser(Connection conn,String email)
			{   PreparedStatement stmt = null;
			    int rs ;
			    String query = "UPDATE USER_INFO set REGISTERED = 1 where EMAIL=?";
			    try
			    {
			        stmt = conn.prepareStatement(query);
			        stmt.setString(1,email);
			        rs = stmt.executeUpdate();
			        //conn.commit();
			        if(rs == 1){
			            return true;
			        }else{
			            return false;
			        }
			  
			    }
			    catch(Exception e)
			    {	e.printStackTrace();
			        return false;
			    }
			}
	
	public String makeid() {
		String text = "";
		String possible = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		for (int i = 0; i < 5; i++)
			text += possible.charAt((int) Math.floor(Math.random() * possible.length()));
		return text;
	}
	
	public void triggerSendMail(String name,String email,String loginAction,String activationCode){
		String[] toList = {email};
		String subject = "";
		String activationLink = "http://localhost:8080/TesignerRWS/rest/webservice/login?email="+email+"&activationcode="+activationCode;
		
		if(loginAction.equals("signup")){
			subject = "New user signup at Tesigners.com";
		}
		
		SendMailTLS.sendMail( toList,subject,SendMailTLS.constructMessageBody(name,loginAction,activationLink));
	}
	
	public boolean trackRide(Connection conn,String lat,String lon)
	{   PreparedStatement stmt = null;
	    boolean rs ;
	    String query = "insert into track_ride(lat,lon) values(?,?)";
	    try
	    {
	        stmt = conn.prepareStatement(query);
	        stmt.setString(1,lat);
	        stmt.setString(2,lon);
	        rs = stmt.execute();
	        
	        return true;
	      	  
	    }
	    catch(Exception e)
	    {	e.printStackTrace();
	        return false;
	    }
	}

	public String updateRide(Connection conn) {
		String lat=null,lon=null;
		String query = "SELECT lat,lon FROM track_ride ORDER BY id DESC LIMIT 1";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try{
			stmt= conn.prepareStatement(query);
		    rs=stmt.executeQuery();
		    
		    if(rs.next())
		      {
		         lat = rs.getString("lat");
		         lon = rs.getString("lon");
		         System.out.println("lat="+lat+"--long="+lon);
		      }
		    
		}catch(SQLException e){
			e.printStackTrace();
			
		}
		 return lat+","+lon;
	}
	
}
