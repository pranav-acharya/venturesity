package insightService;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import org.json.JSONException;
import org.json.JSONObject;


public class JobStorage {
	
	private String type;
	private String fileID;
	private String fileName;
	private String output;
	private String commandLine;
	private int status;
	private int jobid;
	private String jarName;
	private String className;
	private String outputFile;
	
	
	
	public JobStorage(int jobid)
	{
		this.jobid=jobid;
	}
	
	
	public String getFileNameFromID(String fileID)
	{
		return fileName;
	}
	public boolean checkStatus()
	{
		DatabaseOperation data= new DatabaseOperation();
		boolean bool=data.checkStatus(this);
		data.close();
		return bool;
	}
	public synchronized void addToDB()
	{
		DatabaseOperation data= new DatabaseOperation();
		setJobid(data.getNextJobId());
		data.insertJob(this);
		data.close();
	}
	 public JobStorage(JSONObject obj) {
		// TODO Auto-generated constructor stub
		try {
			type=obj.getString("type");
			fileID=obj.getString("dataset");
			output="";
			commandLine=obj.getJSONObject("commandLine").toString();
			status=0;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		
	}

	public JobStorage(String fileID) {
		// TODO Auto-generated constructor stub
		this.fileID=fileID;
	}


	public String getJarNameFromType(String type,String pName)
	{
		return getPropery(CommonData.jarMapProp, pName);
	}
	
	public JSONObject getInsight()
	{
		DatabaseOperation db= new DatabaseOperation();
		JSONObject obj=db.getJsonInsight(this);
		db.close();
		return obj;
	}
	public void fillDetails()
	{ 
		DatabaseOperation db = new DatabaseOperation();
		db.fillJobInfo(this);
		fillFromProps();
		db.close();
		
	}
	
	public void fillFromProps()
	{
		String temp=getPropery(CommonData.fileIdToNameProp,getFileID());
		String temp2=getPropery(CommonData.jarMapProp, getType());
		String arr[]=temp2.split(",");
		jarName=arr[0];
		className=arr[1];
		fileName=temp;
		outputFile=fileName+jobid;
	}
	
	public String createAbsolutePath(String file,String pName,String relativePath)
	{
		String temp=getPropery(file, pName);
		return temp+relativePath;
		
	}
	public String getPropery(String file,String pName)
	{
		Properties props= new Properties();
		InputStream input;
		try{
			//input=new FileInputStream(file);
			input=getClass().getClassLoader().getResourceAsStream(file);
			props.load(input);
			return props.getProperty(pName);
			
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	public String generateCommand()
	{
		String jarName2=createAbsolutePath(CommonData.configFile, "JAR_FOLDER", jarName);
		String fileName2=createAbsolutePath(CommonData.configFile, "IN_FILE_FOLDER", fileName);
		return "spark-submit --master local --class "+className+"  --jars /root/bin/mysql-connector-java-5.1.37-bin.jar "+jarName2+" "+jobid+"  "+fileName2;
	}
	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public String getFileID() {
		return fileID;
	}


	public void setFileID(String fileID) {
		this.fileID = fileID;
	}


	public String getFileName() {
		return fileName;
	}


	public void setFileName(String fileName) {
		this.fileName = fileName;
	}


	public String getOutput() {
		return output;
	}


	public void setOutput(String output) {
		this.output = output;
	}


	public String getCommandLine() {
		return commandLine;
	}


	public void setCommandLine(String commandLine) {
		this.commandLine = commandLine;
	}


	public int getStatus() {
		return status;
	}


	public void setStatus(int status) {
		this.status = status;
	}


	public int getJobid() {
		return jobid;
	}


	public void setJobid(int jobid) {
		this.jobid = jobid;
	}

	public String getJarName() {
		return jarName;
	}

	public void setJarName(String jarName) {
		this.jarName = jarName;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getOutputFile() {
		return outputFile;
	}

	public void setOutputFile(String outputFile) {
		this.outputFile = outputFile;
	}


	public String generatResponse() {
		// TODO Auto-generated method stub
		
		String json="{ url: " +CommonData.mysqlUrl+ " , username:" +CommonData.mysqlUserName+ " , password: "+CommonData.mysqlPassword+" }";
		return json;
	}
	
	

}
