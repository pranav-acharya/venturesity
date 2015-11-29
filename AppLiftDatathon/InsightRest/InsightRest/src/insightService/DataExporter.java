package insightService;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.opencsv.CSVReader;


public class DataExporter {

	
	
	public  Connection con ;
	public  PreparedStatement statement ;
	public static String dataTable="datatable";
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	   static final String DB_URL = "jdbc:mysql://localhost/sparkdb";
	private static final String USER = "root";
	private static final String PASS = "root";
	private static String csvPath="C:\\Users\\I309660\\Desktop\\dataset_1gb.csv";
	private static String queryString="insert into datatable  values ( ? , ? , ?,? , ? , ?,? , ? , ?,? , ? , ?,? , ? , ?,?,? , ? , ?,? , ? , ?,?,? , ? , ?,? , ? , ? ) ";
	public  void initialize()
	{
		try {
			Class.forName(JDBC_DRIVER);
			con= DriverManager.getConnection(DB_URL,USER,PASS);
			statement=con.prepareStatement(queryString);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	
	public  void insertToDb(String args[])
	{
		for(int i=0;i<args.length;i++)
		{
			try {
				statement.setString(i+1, args[i]);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			statement.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public  void readCSVAndInsert()
	{
		CSVReader reader=null;
		try {
			reader = new CSVReader(new FileReader(csvPath), ',' , '"' , 1);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	       
	      //Read CSV line by line and use the string array as you want
	      String[] nextLine;
	      try {
			reader.readNext();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	      try {
			while ((nextLine = reader.readNext()) != null) {
			     if (nextLine != null) {
			        //Verifying the read data here
			    	 insertToDb(nextLine);
			     
			     }
			   }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public JSONObject generateJson(long start , long end , String type)
	{
		HashMap<String, String> countryMap = new HashMap<String,String>();
		countryMap.put("IDN","Indonesia");
		countryMap.put("IND","India");
		countryMap.put("TWN","Taiwan");
		countryMap.put("AUS","Australia");
		countryMap.put("USA","USA");
		countryMap.put("SVN","Slovenia");
		countryMap.put("MYS","Malaysia");
		countryMap.put("ESP","Spain");
		countryMap.put("ROU","Romania");
		countryMap.put("SAU","Saudi Arabia");
		countryMap.put("ZAF","South Africa");
		countryMap.put("SGP","Singapore");
		countryMap.put("NGA","Nigeria");
		countryMap.put("KOR","Korea");
		countryMap.put("THA","Thailand");
		countryMap.put("RUS","RUS");
		countryMap.put("JPN","Japan");
		countryMap.put("GHA","Ghana");
		countryMap.put("TUR","Turkey");
		countryMap.put("DEU","Germany");
		countryMap.put("HKG","Hong Kong");
		countryMap.put("KWT","Kuwait" );
		countryMap.put("GBR","United Kingdom");
		countryMap.put("CAN","Canada");
		countryMap.put("PHL","Philippines");
		countryMap.put("IRN","Iran");
		countryMap.put("BLR","Belarus");
		countryMap.put("BRA","Brazil");
		countryMap.put("QAT","Qatar");
		countryMap.put("VNM","Vietnam");
		countryMap.put("GTM","Guatemala");
		countryMap.put("PER","Peru");
		countryMap.put("MEX","Mexico");
		countryMap.put("UKR","Ukraine");
		countryMap.put("DNK","Denmark");
		countryMap.put("AUT","Austria");
		countryMap.put("EST","Estonia");
		countryMap.put("CHE","Switzerland");
		String query="";
		JSONObject obj= new JSONObject();
		ResultSet result=null;
		if(type.equals("outputW"))
		{
			query="Select Country , count(*) , FLOOR(Timestamp/100) as t  from datatable  where outcome <> '0' and Timestamp>=? and Timestamp<=?  group by Country , FLOOR(Timestamp/100) order by FLOOR(Timestamp/100) ;";
		}
		else
		{
			query="Select Country , count(*) , FLOOR(Timestamp/100) as t from datatable  where outcome = 'c' and Timestamp>=? and Timestamp<=?  group by Country , FLOOR(Timestamp/100) order by FLOOR(Timestamp/100) ;";;
		}
		PreparedStatement st;
		try {
			
			
			st = con.prepareStatement(query);
			st.setLong(1, start);
			st.setLong(2, end);
			result=st.executeQuery();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		JSONArray array= new JSONArray();
		if(type.equals("outputW"))
		{
			try {
				for(long start2=start/100 ; start2<end/100+1;start2++)
				{
					JSONArray array2= new JSONArray();
					JSONArray temp=new JSONArray();
					temp.put("Country");
					temp.put("Bids Won");
					array2.put(temp);
				while(result.next())
				{
					int timestamp = result.getInt(3);
					if(timestamp == start2)
					{
						temp = new JSONArray();
						temp.put(countryMap.get(result.getString(1)));
						temp.put(result.getFloat(2));
						array2.put(temp);
					}
					else
						break;
				}
				/*JSONObject obj2= new JSONObject();
				obj2.put("time", start2);
				obj2.put("array", rray2);*/
				array.put(array2);
				if(result==null ) break;
				else result.previous();
				}
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else{
		try {
			for(long start2=start/100 ; start2<end/100+1;start2++)
			{
				JSONArray array2= new JSONArray();
				JSONArray temp=new JSONArray();
				temp.put("Country");
				temp.put("Ads Clicked");
				array2.put(temp);
			while(result.next())
			{
				int timestamp = result.getInt(3);
				if(timestamp == start2)
				{
					temp = new JSONArray();
					temp.put(countryMap.get(result.getString(1)));
					temp.put(result.getFloat(2));
					array2.put(temp);
				}
				else
					break;
			}
			/*JSONObject obj2= new JSONObject();
			obj2.put("time", start2);
			obj2.put("array", array2);*/
			array.put(array2);
			if(result==null ) break;
			else result.previous();
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
		try {
			obj.put("result", array);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return obj;
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DataExporter ex = new DataExporter();
		
		ex.initialize();
		ex.readCSVAndInsert();

	}

}
