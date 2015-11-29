package insightService;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONArray;
import org.json.JSONObject;


public class DatabaseOperation {
	
	private Connection con=null;
	
	public Connection getConnection() {
		return con;
	}

	public void setConnection(Connection con) {
		this.con = con;
	}
	
	public DatabaseOperation() 
	{
		try {
			Class.forName("com.mysql.jdbc.Driver");
			 this.con= DriverManager.getConnection(CommonData.mysqlUrl,CommonData.mysqlUserName,CommonData.mysqlPassword);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	

	private boolean addJson(JobStorage job)
	{
		try {
			PreparedStatement prepared= con.prepareStatement(CommonData.insertJsonQuery);
			prepared.setString(1, job.getOutput());
			prepared.setInt(2, job.getJobid());
			prepared.executeUpdate();
			prepared.close();
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	public boolean fillFromFile(JobStorage job)
	{
		try {
			
			File file= new File(job.getOutputFile());
			FileInputStream input= new FileInputStream(file);
			byte[] array= new byte[(int) (file.length()+100)];
			input.read(array);
			String temp= new String (array,"UTF-8");
			job.setOutput(temp);
			return addJson(job);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
		
	}
	public boolean fillJobInfo( JobStorage job)
	{
		
		try {
			PreparedStatement prepared= con.prepareStatement(CommonData.getStatusTableQuery);
			prepared.setInt(1, job.getJobid());
		
			ResultSet result =prepared.executeQuery();
			
			if(result==null )
				{prepared.close();return false;}
			
			else{
				result.next();
				job.setCommandLine(result.getString("commandline"));
				job.setType(result.getString("type"));
				job.setFileID(result.getString("fileid"));
				job.setStatus(result.getInt("status"));
				prepared.close();
				return true;
				
			}
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
		
	}
	
	public boolean checkStatus(JobStorage job)
	{
		try {
			PreparedStatement statement= con.prepareStatement(CommonData.checkStatusQuery);
			statement.setString(1, job.getFileID());
			ResultSet result = statement.executeQuery();
			
			if(result.next()) return false;
			statement.close();
			return true;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	public JSONObject getJsonInsight(JobStorage job)
	{
		try {
			PreparedStatement statement = con.prepareStatement(CommonData.getInsightQuery);
			statement.setString(1, job.getFileID());
			ResultSet result = statement.executeQuery();
			JSONArray array= new JSONArray();
			while(result.next())
			{
				String output=result.getString("output");
				String type=result.getString("type");
				JSONObject obj= new JSONObject();
				obj.put("output", output);
				obj.put("type", type);
				array.put(obj);
			}
			JSONObject finalObj = new JSONObject();
			finalObj.put("insight", array);
			statement.close();
			return finalObj;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public void insertJob(JobStorage job)
	{

		try {
			PreparedStatement statement= con.prepareStatement(CommonData.insertStatusTableQuery);
			statement.setInt(1,job.getJobid());
			statement.setString(2, job.getType());
			statement.setString(3, job.getFileID());
			statement.setInt(4, job.getStatus());
			statement.setString(5, job.getOutput());
			statement.setString(6, job.getCommandLine());
			
			statement.executeUpdate();
			statement.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public int getNextJobId()
	{
		try {
			PreparedStatement statement= con.prepareStatement(CommonData.selectMaxQuery);
			ResultSet set=statement.executeQuery();
			set.next();
			int temp=set.getInt(1);	
			set.close();
			statement.close();
			return temp+1;
	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			CommonData.LogFile(e.toString());
			e.printStackTrace();
		}
		return -1;
	}
	
	public void close() 
	{
		try {
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	

}
