package insightService;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;

public class CommonData {
	
	public static String hostip="";
	public static String dbName="sparkdb";
	public static int mysqlPort=3306;
	public static String hostName="localhost";
	public static String mysqlUrl="jdbc:mysql://localhost:"+mysqlPort+"/"+dbName;
	public static String statusTable="statustable";
	public static String mysqlUserName="root";
	public static String mysqlPassword="";
	public static String getStatusTableQuery=" select * from "+ statusTable + " where jobid =? ";  
	public static String insertJsonQuery=" modify  "+statusTable+" set status=1 , output= ? where jobid= ?";
	public static String jarMapProp="jarmap.properties";
	public static String fileIdToNameProp="filemap.properties";
	public static String configFile="config.properties";
	public static String logFile="/root/file/log.txt";
	public static String selectMaxQuery="select max(jobid) from "+statusTable;
	public static String insertStatusTableQuery="insert into "+statusTable+" values ( ? , ? , ? , ? , ? ,? )";
	public static String getInsightQuery= " select type , output where fileid=? and status=1";
	public static String checkStatusQuery="  select type , output where fileid=? and status=0";
	public static void LogFile(String msg)
	{
		try {
		
			BufferedWriter writer = new BufferedWriter(new FileWriter(logFile,true));
			writer.write(msg+"/n");
			writer.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

}
