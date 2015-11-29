import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.spark.api.java.*;
import org.apache.spark.mllib.linalg.distributed.RowMatrix;
import org.apache.spark.mllib.linalg.Matrix;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.rdd.RDD;
import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
public class PCA {

	public static void main(String[] args) {
		SparkConf conf = new SparkConf().setAppName("PCA Example");
		SparkContext sc = new SparkContext(conf);

		double[][] array = { {2.0,3.0},
				{3.9,4.1},
				{1.5,7.9} };


		LinkedList<Vector> rowsList = new LinkedList<Vector>();
		for (int i = 0; i < array.length; i++) {
			Vector currentRow = Vectors.dense(array[i]);
			rowsList.add(currentRow);
		}
		JavaRDD<Vector> rows = JavaSparkContext.fromSparkContext(sc).parallelize(rowsList);

		// Create a RowMatrix from JavaRDD<Vector>.
		RowMatrix mat = new RowMatrix(rows.rdd());

		// Compute the top 3 principal components.
		Matrix pc = mat.computePrincipalComponents(1);
		RowMatrix projected = mat.multiply(pc);
		
		System.out.println("---------------------------------------------------------------");
		System.out.println("Non PCA Matrix is:\n"+mat.toBreeze());
		//System.out.println("PCA Value is:\n"+pc);
		System.out.println("PCA Matrix is:"+projected.toBreeze());
		System.out.println("---------------------------------------------------------------");
		

	}

}


