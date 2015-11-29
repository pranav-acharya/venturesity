import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.apache.spark.mllib.util.MLUtils;



public class SparkMLIBExmp {

	public static void main(String[] args) {
		Vector sv = Vectors.sparse(3, new int[] {0, 2}, new double[] {1.0, 3.0});
		

		

		// Create a labeled point with a positive label and a dense feature vector.
		LabeledPoint pos = new LabeledPoint(1.0, Vectors.dense(1.0, 0.0, 3.0));

		// Create a labeled point with a negative label and a sparse feature vector.
		LabeledPoint neg = new LabeledPoint(0.0, Vectors.sparse(3, new int[] {0, 2}, new double[] {1.0, 3.0}));
		
//		JavaRDD<LabeledPoint> examples = 
//				  MLUtils.loadLibSVMFile(jsc.sc(), "data/mllib/sample_libsvm_data.txt").toJavaRDD();

	}

}
