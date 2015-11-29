import org.apache.spark.api.java.*;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.mllib.clustering.KMeans;
import org.apache.spark.mllib.clustering.KMeansModel;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.SparkConf;

public class KMeansExample {
  public static void main(String[] args) {
    SparkConf conf = new SparkConf().setAppName("K-means Example").setMaster("local[*]");
    JavaSparkContext sc = new JavaSparkContext(conf);

    // Load and parse data
    String path = "C:/Users/i302913/Desktop/spark/test.txt";
    JavaRDD<String> data = sc.textFile(path);
    JavaRDD<Vector> parsedData = data.map(
      new Function<String, Vector>() {
        public Vector call(String s) {
          String[] sarray = s.split(" ");
          double[] values = new double[sarray.length];
          for (int i = 0; i < sarray.length; i++)
            values[i] = Double.parseDouble(sarray[i]);
          return Vectors.dense(values);
        }
      }
    );
    parsedData.cache();

    // Cluster the data into two classes using KMeans
    int numClusters = 2;
    int numIterations = 20;
    KMeansModel clusters = KMeans.train(parsedData.rdd(), numClusters, numIterations);

    // Evaluate clustering by computing Within Set Sum of Squared Errors
    double WSSSE = clusters.computeCost(parsedData.rdd());
    System.out.println("Within Set Sum of Squared Errors = " + WSSSE);

    // Save and load model
    clusters.save(sc.sc(), "myModelPath");
    KMeansModel sameModel = KMeansModel.load(sc.sc(), "myModelPath");
    Vector[] v = sameModel.clusterCenters();
    for(int i=0;i<v.length;i++)
    {
    	double d []=v[i].toArray();
    	for(int j=0;j<d.length;j++)
    	{
    		System.out.print(d[j]+"\t");
    	}
    	System.out.println();
    }
    System.out.println("Learned k-means model:\n" + sameModel.clusterCenters());
  }
}