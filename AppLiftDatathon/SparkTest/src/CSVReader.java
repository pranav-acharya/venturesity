
import org.apache.spark.api.java.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.mllib.clustering.KMeans;
import org.apache.spark.mllib.clustering.KMeansModel;
import org.apache.spark.mllib.fpm.AssociationRules;
import org.apache.spark.mllib.fpm.FPGrowth;
import org.apache.spark.mllib.fpm.FPGrowthModel;
import org.apache.spark.mllib.linalg.Matrix;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.mllib.linalg.distributed.RowMatrix;
import org.apache.spark.mllib.stat.Statistics;
import org.apache.spark.rdd.RDD;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import breeze.linalg.dim;




import org.apache.spark.api.java.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.mllib.clustering.KMeans;
import org.apache.spark.mllib.clustering.KMeansModel;
import org.apache.spark.mllib.fpm.AssociationRules;
import org.apache.spark.mllib.fpm.FPGrowth;
import org.apache.spark.mllib.fpm.FPGrowthModel;
import org.apache.spark.mllib.linalg.Matrix;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.mllib.linalg.distributed.RowMatrix;
import org.apache.spark.mllib.stat.Statistics;
import org.apache.spark.rdd.RDD;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;




public class CSVReader {
       
              
       

       public static void main(String[] args) throws JSONException {
              
              
    	   String datasetName = "eFashionRetailData";
           
           String[] columns ={};
           JSONObject obj = new JSONObject("{\"dimensions\":[{\"id\":\"id_6\",\"name\":\"Year\"},{\"id\":\"id_1\",\"name\":\"Category\"},{\"id\":\"id_5\",\"name\":\"Quarter\"},{\"id\":\"id_4\",\"name\":\"State\"},{\"id\":\"id_2\",\"name\":\"Lines\"},{\"id\":\"id_3\",\"name\":\"City\"}],\"measures\":[{\"aggregation\":\"Sum\",\"id\":\"id_7\",\"name\":\"Margin\"},{\"aggregation\":\"Sum\",\"id\":\"id_8\",\"name\":\"Quantity sold\"},{\"aggregation\":\"Sum\",\"id\":\"id_9\",\"name\":\"Sales revenue\"}]}");
           final JSONArray dobj = obj.getJSONArray("dimensions");
           final JSONArray mobj = obj.getJSONArray("measures");
           
           String query="SELECT ";
           String dimeList="";
           String mesList="";

           for(int i=0;i<dobj.length();i++)
           {
         	  JSONObject tempobj = dobj.getJSONObject(i);
         	  dimeList+= "`"+tempobj.getString("name")+"`,";
           }
           dimeList=dimeList.substring(0, dimeList.length()-1);
           for(int i=0;i<mobj.length();i++)
           {
         	  JSONObject tempobj = mobj.getJSONObject(i);
         	  mesList+= tempobj.getString("aggregation").toUpperCase()+"(`"+tempobj.getString("name")+"`),";
           }
           mesList=mesList.substring(0, mesList.length()-1);
           
           query+=dimeList+","+mesList+" FROM "+datasetName+" GROUP BY "+dimeList;
           
           System.out.println(query);
              
              
              //String path  = "C:\\Raghu\\work\\innovation\\spark\\eFashionRetailData.csv";
              
              String path  = "C:/Users/i302913/Desktop/spark/" + datasetName  + ".json";
              
              SparkConf conf = new SparkConf().setAppName("JavaDecisionTree").setMaster("local[*]");
              JavaSparkContext sc = new JavaSparkContext(conf);
              
              
              
              SQLContext sqlContext = new org.apache.spark.sql.SQLContext(sc);
                           
              
              DataFrame schemafashion = sqlContext.read().json(path);
              schemafashion.registerTempTable(datasetName);
              schemafashion.printSchema();
              
              
              // ----------------------- Clustering
                           
              
              /*
              * 
                 DataFrame data = sqlContext.sql("SELECT storeName,state, city ,sum(quantitySold),lines,year,month,quarter FROM fashion group by storeName,state,city,lines,year,month,quarter ");  
                 List<Vector> sales = data.javaRDD().map(new Function<Row, Vector>() {
                       public Vector call(Row row) {
                             
                     //       Vector currentRow = Vectors.dense((double) row.getLong(3));
                             
                             Vector currentRow = Vectors.dense((double) row.getLong(5));
                           //  System.out.println(" str :  " + row.getString(4));
                         return  currentRow;
                       }
                     }).collect();
              
                     
              
              JavaRDD<Vector> parsedData  =  sc.parallelize(sales);
              
       
              // Cluster the data into two classes using KMeans
           int numClusters = 3;
           int numIterations = 20;
           KMeansModel clusters = KMeans.train(parsedData.rdd(), numClusters, numIterations);
       
           System.out.println("Cluster centers:");
           for (Vector center : clusters.clusterCenters()) {
             System.out.println(" " + center);
           }
           double cost = clusters.computeCost(parsedData.rdd());
           System.out.println("Cost: " + cost);
       
           */
              
              // ----------------------- FP growth
              
              /*
              
              DataFrame data = sqlContext.sql("SELECT category,city,lines,state FROM fashion  ");
              
              
              JavaRDD<List<String>> transactions =  data.javaRDD().map(new Function<Row, List<String>>() {
                               public  List<String> call(Row row) {
                                  
                                   List<String> values = new ArrayList<String>();
                                   values.add( row.getString(0));
                                   values.add( row.getString(1));
                                   values.add( row.getString(2));
                                   values.add( row.getString(3));                                
                                  
                                 return values;
                               }  
                             }
                           );
           
         
               
               FPGrowth fpg = new FPGrowth()
                             .setMinSupport(0.2)
                             .setNumPartitions(10);
                           FPGrowthModel<String> model = fpg.run(transactions);
                           
                  
                           for (FPGrowth.FreqItemset<String> itemset: model.freqItemsets().toJavaRDD().collect()) {
                                    System.out.println("[" + itemset.javaItems() + "], " + itemset.freq());
                                  }
                           
                           JavaRDD<FPGrowth.FreqItemset<String>> freqItemsets =model.freqItemsets().toJavaRDD();
                           
                           AssociationRules arules = new AssociationRules()
                                           .setMinConfidence(0.8);
                                         JavaRDD<AssociationRules.Rule<String>> results = arules.run(freqItemsets);

                                         for (AssociationRules.Rule<String> rule: results.collect()) {
                                           System.out.println(
                                             rule.javaAntecedent() + " => " + rule.javaConsequent() + ", " + rule.confidence());
                                         }
              
              */
              
       
       
                   //  DataFrame data = sqlContext.sql("SELECT `Quantity sold`,Margin FROM fashion");
             
                DataFrame data = sqlContext.sql(query);
              
              
                     
                     List<Vector> sales = data.javaRDD().map(new Function<Row, Vector>() {
                             public Vector call(Row row) {
                                    Vector currentRow = Vectors.dense(row.getDouble(6),row.getDouble(7));
                           
                               return  currentRow;
                             }
                           }).collect();
                     
                     
                      JavaRDD<Vector> parsedData  =  sc.parallelize(sales);
                     
                     Matrix correlMatrix = Statistics.corr(parsedData.rdd(), "pearson");
                     
                     System.out.println( "co-relation - > "  + correlMatrix.apply(0, 1));
       
              
              //JavaDoubleRDD seriesX = ... // a series
              
              //JavaDoubleRDD seriesY = ... // must have the same number of partitions and cardinality as seriesX
                           
              
              //Double correlation = Statistics.corr(seriesX.srdd(), seriesY.srdd(), "pearson");
              
              
              
              System.out.println("done" );
              
              
            
       }

}

