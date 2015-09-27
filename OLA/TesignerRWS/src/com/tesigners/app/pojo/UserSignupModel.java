package com.tesigners.app.pojo;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class UserSignupModel {

	private String email;
	private String name;
	private String phone;
	private String password;
	private String confirmPassword;
	private String activationCode;
	
	public UserSignupModel(){
		this.email="";
		this.name="";
		this.phone="";
		this.password = "";
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	/*public String getConfirmPassword() {
		return confirmPassword;
	}
	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}*/
	public String getActivationCode() {
		return activationCode;
	}
	public void setActivationCode(String activationCode) {
		this.activationCode = activationCode;
	}
	
}
