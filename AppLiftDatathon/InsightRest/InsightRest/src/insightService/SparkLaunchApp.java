package insightService;

import java.io.IOException;

import org.apache.spark.launcher.SparkLauncher;

public class SparkLaunchApp {
	
	
	public void runSpark(JobStorage job)
	{
		SparkLauncher launch = new SparkLauncher();
		//launch.setJavaHome("");
		launch.setAppName(job.getFileID());
		launch.addJar("/root/jar/"+job.getJarName());
		launch.setMainClass(job.getClassName());
		launch.setVerbose(true);
		//launch.setMaster("local[*]");
		launch.addAppArgs(job.getJobid()+" "+job.getFileName());
		launch.setSparkHome("/etc/spark/");
		try {
			launch.launch();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			CommonData.LogFile(e.toString());
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
