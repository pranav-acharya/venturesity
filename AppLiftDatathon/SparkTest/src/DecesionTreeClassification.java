import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.ml.Pipeline;
import org.apache.spark.ml.PipelineModel;
import org.apache.spark.ml.PipelineStage;
import org.apache.spark.ml.classification.DecisionTreeClassificationModel;
import org.apache.spark.ml.classification.DecisionTreeClassifier;
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator;
import org.apache.spark.ml.feature.IndexToString;
import org.apache.spark.ml.feature.StringIndexer;
import org.apache.spark.ml.feature.StringIndexerModel;
import org.apache.spark.ml.feature.VectorIndexer;
import org.apache.spark.ml.feature.VectorIndexerModel;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.apache.spark.mllib.util.MLUtils;
import org.apache.spark.rdd.RDD;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.SQLContext;
public class DecesionTreeClassification {

	public static void main(String[] args) {

		SparkConf sparkConf = new SparkConf().setAppName("JavaDecisionTree").setMaster("local[*]");
		JavaSparkContext sc = new JavaSparkContext(sparkConf);

		// Load and parse the data file.
		String datapath = "C:/Users/i302913/Desktop/spark/libsvm.data";
		
		//JavaRDD<String> lines = sc.textFile(args[0], 1).;

		// Load and parse the data file, converting it to a DataFrame.
		RDD<LabeledPoint> rdd = MLUtils.loadLibSVMFile(sc.sc(), datapath);
		SQLContext sqlContext = new org.apache.spark.sql.SQLContext(sc);
		DataFrame data = sqlContext.createDataFrame(rdd, LabeledPoint.class);

		// Index labels, adding metadata to the label column.
		// Fit on whole dataset to include all labels in index.
		StringIndexerModel labelIndexer = new StringIndexer()
		.setInputCol("label")
		.setOutputCol("indexedLabel")
		.fit(data);
		// Automatically identify categorical features, and index them.
		VectorIndexerModel featureIndexer = new VectorIndexer()
		.setInputCol("features")
		.setOutputCol("indexedFeatures")
		.setMaxCategories(4) // features with > 4 distinct values are treated as continuous
		.fit(data);

		// Split the data into training and test sets (30% held out for testing)
		DataFrame[] splits = data.randomSplit(new double[] {0.7, 0.3});
		DataFrame trainingData = splits[0];
		DataFrame testData = splits[1];

		// Train a DecisionTree model.
		DecisionTreeClassifier dt = new DecisionTreeClassifier()
		.setLabelCol("indexedLabel")
		.setFeaturesCol("indexedFeatures");

		// Convert indexed labels back to original labels.
		IndexToString labelConverter = new IndexToString()
		.setInputCol("prediction")
		.setOutputCol("predictedLabel")
		.setLabels(labelIndexer.labels());

		// Chain indexers and tree in a Pipeline
		Pipeline pipeline = new Pipeline()
		.setStages(new PipelineStage[] {labelIndexer, featureIndexer, dt, labelConverter});

		// Train model.  This also runs the indexers.
		PipelineModel model = pipeline.fit(trainingData);

		// Make predictions.
		DataFrame predictions = model.transform(testData);

		// Select example rows to display.
		predictions.select("predictedLabel", "label", "features").show();

		// Select (prediction, true label) and compute test error
		MulticlassClassificationEvaluator evaluator = new MulticlassClassificationEvaluator()
		.setLabelCol("indexedLabel")
		.setPredictionCol("prediction")
		.setMetricName("precision");
		double accuracy = evaluator.evaluate(predictions);
		System.out.println("Test Error = " + (1.0 - accuracy));

		DecisionTreeClassificationModel treeModel =
				(DecisionTreeClassificationModel)(model.stages()[2]);
		System.out.println("Learned classification tree model:\n" + treeModel.toDebugString());
		

	}

}
