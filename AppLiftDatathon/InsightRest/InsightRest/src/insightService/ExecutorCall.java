package insightService;

import org.json.JSONObject;

public class ExecutorCall {
	
	public String execute( int jobid)
	{
		JobStorage job= new JobStorage(jobid);
		job.fillDetails();
		ToRun run= new ToRun(job);
		Thread th= new Thread(run);
		th.start();
		return job.generatResponse();
	}
	
	public JSONObject execute( String fileID)
	{
		JobStorage job= new JobStorage(fileID);
		
		return job.getInsight();
	}
	public String execute( JSONObject  obj)
	{
		JobStorage job= new JobStorage(obj);
		job.addToDB();
		job.fillFromProps();;
		ToRun run= new ToRun(job);
		Thread th= new Thread(run);
		th.start();
		return job.generatResponse();
	}

}
