import org.apache.spark.api.java.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.mllib.linalg.Matrix;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.mllib.stat.Statistics;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;




public class CopyOfCorelation {
                
                                
                static SparkConf conf = null;
                static JavaSparkContext sc = null;
                static SQLContext sqlContext = null;
                static DataFrame schema = null;
                static JSONObject obj ;
                static JSONArray dobj;
                static JSONArray mobj;
                static String query;
				private static JSONArray result;
				private static String dimension;
				private static JSONObject dimobj;
				private static String measure;
				private static JSONObject meaobj;
                               
                
                
       static void initialize(String datasetName) throws JSONException{    
         
    	   String path  = "C:\\data_json\\" + datasetName  + ".json";
            conf = new SparkConf().setAppName("JavaDecisionTree").setMaster("local[*]");
            sc = new JavaSparkContext(conf);
                                        
            sqlContext = new org.apache.spark.sql.SQLContext(sc);
                         
            
            schema = sqlContext.read().json(path);
            schema.registerTempTable(datasetName);
            schema.printSchema();
            
            
          
        }
       
       
           static void execute() throws JSONException{    
              
                  final int dimLen  =  1 ; // dobj.length();
                  final int mLen =  mobj.length();
            
             
                  DataFrame data = sqlContext.sql(query);
                     
                     List<Vector> sales = data.javaRDD().map(new Function<Row, Vector>() {
                             public Vector call(Row row) {
                                 
                                 int len =  mLen;
                                 double[] array = new double[len];
                                           
                                    for(int i=0;i< len;i++){
                                    	Object item = row.get(dimLen +i);
                                    	if(item==null)
                                    		continue;
                                        if(item instanceof Long)
                                        {
                                        	array[i]=((Long)item).doubleValue();
                                        }
                                        else
                                        {
                                        	array[i]=(double)item;
                                        }
                                                                                                
                                    }
                                    
                                    Vector currentRow = Vectors.dense(array);
                           
                               return  currentRow;
                             }
                           }).collect();
                     
                     if(sales.size()<2)
                     {
                    	 return;
                     }
                      JavaRDD<Vector> parsedData  =  sc.parallelize(sales);
                     
                     Matrix correlMatrix = Statistics.corr(parsedData.rdd(), "pearson");
                     
                    
                     for(int i=0;i< correlMatrix.numCols();i++){
                                  for(int j=0;j< correlMatrix.numRows();j++){
                                                  
                                                  if(i !=j){
                                                                  double corelation  = correlMatrix.apply(i ,j);
                                                                  
                                                                  if(corelation > 0.9){
                                                                	  JSONObject obj = new JSONObject();
                                                                	  
                                                                	  obj.put("measure", new JSONArray("["+mobj.get(i).toString()+","+mobj.get(j).toString()+"]"));
                                                                	  obj.put("dimension", new JSONArray("["+dimobj.toString()+"]"));
                                                                	  obj.put("type", "CORELATION");
                                                                	   
                                                                	  obj.put("text1", "Co-relation exists between " + mobj.getJSONObject(i).get("name")+  " :: " +  mobj.getJSONObject(j).get("name") + "\nWhen Dimension is "+dimension+"\nCorelation  = " + corelation);
                                                                      obj.put("rank",corelation);
                                                                	  System.out.println( "Co-relation exists between " + mobj.get(i)+  " :: " +  mobj.get(j) + "\nWhen Dimension is "+dimension+"\nCorelation  = " + corelation );
                                                                      result.put(obj);
                                                                  }
                                                                 
                                                                  
                                                  }
                                                
                                                  
                                  }
                                 
                                 
                     }
                     
              
              System.out.println("done" );
}
              
           
              public static void main(String[] args) throws JSONException, FileNotFoundException, UnsupportedEncodingException {   
                  String datasetName = "DVD_RENTAL_ANALYSIS_AFTER_HILO";
                  result = new JSONArray();
                  String[] columns ={};
                  File file = new File("C:\\metadata_json\\" + datasetName  + ".json");
                  FileInputStream input = new FileInputStream(file);
                  byte array[] = new byte [(int) (file.length()+10)];
                  String jsonString;
                  try {
                                  input.read(array);
                                  jsonString= new String(array);
                                  obj=new JSONObject(jsonString);
                           } catch (IOException e) {
                                  // TODO Auto-generated catch block
                                  e.printStackTrace();
                           }
                  
                   //obj = new JSONObject("{\"dimensions\":[{\"id\":\"id_6\",\"name\":\"Year\"},{\"id\":\"id_1\",\"name\":\"Category\"},{\"id\":\"id_5\",\"name\":\"Quarter\"},{\"id\":\"id_4\",\"name\":\"State\"},{\"id\":\"id_2\",\"name\":\"Lines\"},{\"id\":\"id_3\",\"name\":\"City\"}],\"measures\":[{\"aggregation\":\"Sum\",\"id\":\"id_7\",\"name\":\"Margin\"},{\"aggregation\":\"Sum\",\"id\":\"id_8\",\"name\":\"Quantity sold\"},{\"aggregation\":\"Sum\",\"id\":\"id_9\",\"name\":\"Sales revenue\"}]}");
                   dobj = obj.getJSONArray("dimensions");
                   mobj = obj.getJSONArray("measures");
                  
                   CopyOfCorelation.initialize(datasetName); 
                  for(int i=0;i<dobj.length()-1;i++)
                  {
                	  dimension = dobj.getJSONObject(i).getString("name");
                	  String dimeList="`"+dimension+"`";
                       
                       
                       dimobj = dobj.getJSONObject(i);
                       String meaList="";
                       for (int j = 0; j < mobj.length(); j++) {
                    	   String agg = mobj.getJSONObject(j).getString("aggregation").toUpperCase();
                    	   if(agg.equalsIgnoreCase("COUNT(ALL)"))
                    		   agg="COUNT";
                    	   else if(agg.equalsIgnoreCase("Average"))
                    		   agg="AVG";
                    	   meaList+=agg+"(`"+mobj.getJSONObject(j).getString("name")+"`),";
                       } 
                       meaList=meaList.substring(0, meaList.length()-1);
                       System.out.println(dimension);
                         query="SELECT "+dimeList+","+meaList+" FROM "+datasetName+" GROUP BY "+dimeList+"  ORDER BY "+dimeList;
                          
                         // query = "select `Category`, SUM(`Quantity sold`) from eFashionRetailData group by `Category`";
                          
                          //System.out.println(query);
                       CopyOfCorelation.execute();
                         
                  }
                  System.out.println("result is "+result.toString());
                  JSONObject insight = new JSONObject();
                  insight.put("insight", result);
                  File outputfile = new File("C:\\output_json\\"+datasetName+"1.json");
                  PrintWriter writer = new PrintWriter(outputfile, "UTF-8");
                  writer.println(insight.toString());
                  writer.close();
              
       }
          

}





