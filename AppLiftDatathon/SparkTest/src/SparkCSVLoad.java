import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;

import scala.Tuple2;


public class SparkCSVLoad {

	public static void main(String[] args){

		SparkConf sparkConf = new SparkConf().setAppName("Spark CSV load");
		JavaSparkContext ctx = new JavaSparkContext(sparkConf);
		JavaRDD<String> lines = ctx.textFile(args[0], 1);

		JavaRDD<Record> rdd_records = lines.map(
				new Function<String, Record>() {
					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					public Record call(String line) throws Exception {
						// Here you can use JSON
						// Gson gson = new Gson();
						// gson.fromJson(line, Record.class);
						String[] fields = line.split(",");
						System.out.println("-----------------Out put from CSV file is-------------------------"+fields);
						Record sd = new Record(fields[0], fields[1], fields[2], fields[3]);
						return sd;
					}
				});


		JavaPairRDD<String, Tuple2<Long, Integer>> records_JPRDD = 
				rdd_records.mapToPair(new
						PairFunction<Record, String, Tuple2<Long, Integer>>(){
					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					public Tuple2<String, Tuple2<Long, Integer>> call(Record record){
						Tuple2<String, Tuple2<Long, Integer>> t2 = 
								new Tuple2<String, Tuple2<Long,Integer>>(
										record.department + record.designation + record.state,
										new Tuple2<Long, Integer>(record.costToCompany,1)
										);
						return t2;
					}


				});

		JavaPairRDD<String, Tuple2<Long, Integer>> final_rdd_records = 
				records_JPRDD.reduceByKey(new Function2<Tuple2<Long, Integer>, Tuple2<Long,
						Integer>, Tuple2<Long, Integer>>() {
					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					public Tuple2<Long, Integer> call(Tuple2<Long, Integer> v1,
							Tuple2<Long, Integer> v2) throws Exception {
						return new Tuple2<Long, Integer>(v1._1 + v2._1, v1._2+ v2._2);
					}
				});

		
		final_rdd_records.saveAsTextFile("C:\\Users\\i308124\\Desktop\\DB_Comparision_Output\\output_rdbms.txt");
	}
}
