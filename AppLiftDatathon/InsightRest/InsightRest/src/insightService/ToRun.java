package insightService;

public class ToRun implements Runnable{
	
	JobStorage job=null;
	public ToRun(JobStorage job) {
		// TODO Auto-generated constructor stub
		this.job=job;
		
				}
	public void init(String temp)
	{
		
	}
	@Override
	public void run() {
		
		Runtime runtime=Runtime.getRuntime();
		CommonData.LogFile(job.generateCommand());
		try {
			//runtime.exec("export PATH=$PATH:/usr/NX/bin:/sbin:/usr/sbin:/usr/local/sbin:/root/bin:/usr/local/bin:/usr/bin:/bin:/usr/bin/X11:/usr/X11R6/bin:/usr/games:/opt/kde3/bin:/usr/lib/mit/bin:/usr/lib/mit/sbin");
			Process p=runtime.exec(job.generateCommand());
			SparkLaunchApp sp= new SparkLaunchApp();
			sp.runSpark(job);
			CommonData.LogFile("start" +job.generateCommand()+"   end");
			//p.waitFor();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			CommonData.LogFile(e.toString());
			e.printStackTrace();
		}
		//DatabaseOperation db = new DatabaseOperation();
		//db.fillFromFile(job);
	
	}
}