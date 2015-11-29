import java.io.Serializable;

public class Record implements Serializable {
	//  public Record(String string, String string2, String trim, String string3) {
	//	  this. department= department;
	//	  this.designation=designation;
	//	  this.costToCompany=costToCompany;
	//	  this.state=state;
	//	}
	///**

	private static final long serialVersionUID = 1L;
	public Record(String department, String designation, String costToCompany,
			String state) {
		super();
		this.department = department;
		this.designation = designation;
		this.costToCompany = Long.parseLong(costToCompany);
		this.state = state;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getDesignation() {
		return designation;
	}
	public void setDesignation(String designation) {
		this.designation = designation;
	}
	public Long getCostToCompany() {
		return costToCompany;
	}
	public void setCostToCompany(Long costToCompany) {
		this.costToCompany = costToCompany;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	String department;
	String designation;
	Long costToCompany;
	String state;
	// constructor , getters and setters  
}