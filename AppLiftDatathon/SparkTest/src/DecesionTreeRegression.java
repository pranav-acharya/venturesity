import java.util.HashMap;
import java.util.HashMap;



import scala.Tuple2;
import scala.collection.Iterator;

import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.apache.spark.mllib.tree.DecisionTree;
import org.apache.spark.mllib.tree.model.DecisionTreeModel;
import org.apache.spark.mllib.tree.model.Node;
import org.apache.spark.mllib.util.MLUtils;
import org.apache.spark.SparkConf;

public class DecesionTreeRegression {

	public static void main(String[] args) {

		SparkConf sparkConf = new SparkConf().setAppName("JavaDecisionTree").setMaster("local[*]");
		JavaSparkContext sc = new JavaSparkContext(sparkConf);	
		// Load and parse the data file.
		// Cache the data since we will use it again to compute training error.
		String datapath = "C:/Users/i302913/Desktop/spark/libsvm.data";
		//String datapath="C:/HCP_POC_Setup/MLib.txt";
		JavaRDD<LabeledPoint> data = MLUtils.loadLibSVMFile(sc.sc(), datapath).toJavaRDD().cache();




		// Set parameters.
		//  Empty categoricalFeaturesInfo indicates all features are continuous.
		HashMap<Integer, Integer> categoricalFeaturesInfo = new HashMap<Integer, Integer>();
		String impurity = "variance";
		Integer maxDepth = 20;
		Integer maxBins = 100;

		// Train a DecisionTree model.
		final DecisionTreeModel model = DecisionTree.trainClassifier(data,2,
				categoricalFeaturesInfo, impurity, maxDepth, maxBins);


//		int a=1;
//		int b=3;
//		int c=940;
//		int d=12000;
//		for(int i=0;i<10;i++){
//			System.out.println("Training Mean Squared Error:::" + model.predict(Vectors.dense(a,b,c,d)));
//			if(a<=2){
//				a=a+2;
//			}else{
//				a=a-1;
//			}
//
//			if(b<=1){
//				b=b+3;
//			}else{
//				b=b-2;
//			}
//
//			c=c+300;
//			d=d+1000;
//		}
		
		/*		

		// Evaluate model on training instances and compute training error
		JavaPairRDD<Double, Double> predictionAndLabel =
		  data.mapToPair(new PairFunction<LabeledPoint, Double, Double>() {
		    @Override public Tuple2<Double, Double> call(LabeledPoint p) {
		      return new Tuple2<Double, Double>(model.predict(Vectors.dense(2,1,836,59222)), p.label());
		    }
		  });
		Double trainMSE =
		  predictionAndLabel.map(new Function<Tuple2<Double, Double>, Double>() {
		    @Override public Double call(Tuple2<Double, Double> pl) {
		      Double diff = pl._1() - pl._2();
		      return diff * diff;
		    }
		  }).reduce(new Function2<Double, Double, Double>() {
		    @Override public Double call(Double a, Double b) {
		      return a + b;
		    }
		  }) ;*/
		//System.out.println("Training Mean Squared Error:::" + model.predict(Vectors.dense(a,b,c,d)));
		System.out.println("Learned regression tree model:\n" + model.toDebugString());
		//		Iterator<Node> i=  model.topNode().rightNode().iterator();
		//		while(i.hasNext()){
		//			System.out.println("Values are like that:::"+i.next());
		//		}


	}

}
