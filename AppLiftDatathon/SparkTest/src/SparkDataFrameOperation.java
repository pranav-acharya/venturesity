import java.util.HashMap;
import java.util.Map;

import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.DataFrame;
//import org.apache.spark.sql.SapSQLContext;
import org.apache.spark.sql.SQLContext;



public class SparkDataFrameOperation {

	public static void main(String[] args) {

		if (args.length < 1) {
			System.err.println("Usage: Data Frame Operation <file>");
			System.exit(1);
		}

		System.out.println("Entry here");
		SparkConf sparkConf = new SparkConf().setAppName("Data Frame Operation");
		JavaSparkContext ctx = new JavaSparkContext(sparkConf);		
		//
		//		JavaRDD<String> lines = ctx.textFile(args[0], 2);
		//
		SQLContext   sqlContext = new SQLContext(ctx);

		//SapSQLContext sapSQLContext1=new SapSQLContext(new SparkContext(sparkConf));


		Map<String, String> options = new HashMap<String, String>();
		options.put("url", "jdbc:mysql://localhost:3306/testnow");
		options.put("dbtable", "emp");
		options.put("user","root");
		options.put("password","root");

		DataFrame jdbcDF = sqlContext.read().format("jdbc"). options(options).load().select("name","id");
		
		

		//DataFrame df=sqlContext.sql("select name from emp");


		jdbcDF.show();
	}

}
