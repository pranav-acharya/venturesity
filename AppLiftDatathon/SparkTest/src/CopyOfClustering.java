

import org.apache.spark.api.java.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.mllib.clustering.KMeans;
import org.apache.spark.mllib.clustering.KMeansModel;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.mllib.stat.MultivariateStatisticalSummary;
import org.apache.spark.mllib.stat.Statistics;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;




public class CopyOfClustering {
       
              
       static SparkConf conf = null;
       static JavaSparkContext sc = null;
       static SQLContext sqlContext = null;
       static DataFrame schema = null;
       static JSONObject obj ;
       static JSONArray dobj;
       static JSONArray mobj;
       static String query;
       static JSONArray result;    
       static String dimension;
       static String dimension2;
       static String measure;
       static JSONObject dimobj;
       static JSONObject meaobj;
	private static JSONObject dimobj2;
        static void initialize(String datasetName) throws JSONException{    
         
                 
         String path  = "C:\\data_json\\" + datasetName  + ".json";
             
            conf = new SparkConf().setAppName("JavaDecisionTree").setMaster("local[*]");
            sc = new JavaSparkContext(conf);
                                        
            sqlContext = new org.apache.spark.sql.SQLContext(sc);
                         
            
            schema = sqlContext.read().json(path);
            schema.registerTempTable(datasetName);
            schema.printSchema();
            
            
          
        }
       
       
        static void execute(final int dimLen) throws JSONException{    
            
            //final int dimLen  = 1;//  dobj.length();
            final int mLen = mobj.length();
                          
        
          DataFrame data = sqlContext.sql(query);  
          
          List<Vector> sales = data.javaRDD().map(new Function<Row, Vector>() {
            
            public Vector call(Row row) {
                  
                  int len =  1;
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
          
          
          List<List<String>> dimensionValuesList =  data.javaRDD().map(new Function<Row, List<String>>() {
              public  List<String> call(Row row) {
                                       
                   int len =  dimLen;
                  List<String> values = new ArrayList<String>();
                  for(int i=0;i< len;i++){
                  values.add(row.getString(i));
                                                                 
                     }       
                 
                return values;
              }  
            }
          ).collect();
       
          
       
       JavaRDD<Vector> parsedData  =  sc.parallelize(sales);
       

       // Cluster the data into two classes using KMeans
    int x= (int) Math.sqrt(sales.size())  /2;
    
    int numClusters = Math.max(1,x);
    numClusters = Math.min(5,numClusters);
    int numIterations = 20;
    KMeansModel clusters = KMeans.train(parsedData.rdd(), numClusters, numIterations);
    
    MultivariateStatisticalSummary summary = Statistics.colStats(parsedData.rdd()); 
    double varaince  =summary.variance().apply(0);
    double mean  =summary.mean().apply(0);
    double max  =summary.max().apply(0);
    double min  =summary.min().apply(0);
    double sd=  Math.sqrt(varaince);
     

    System.out.println("Cluster centers:");
    
    for (Vector center : clusters.clusterCenters()) {
                  
           System.out.println(" center " + center);
        
      }
    if(clusters.clusterCenters().length<2)
    {
    	return;
    }
    Map<Integer,Integer> clusterDesnsityMap = new HashMap<Integer,Integer>();
    Map<Integer,Double> absoluteDistanceFromCentriod = new HashMap<Integer,Double>();
    Map<Integer,List<Integer>> clusterValueMap = new HashMap<Integer,List<Integer>>();
    Map<Integer,Double> ampdThreshhold = new HashMap<Integer,Double>();
    Map<Integer,Boolean> outlierMap = new LinkedHashMap<Integer,Boolean>();
    Map<Integer,Double> outlierRankMap = new LinkedHashMap<Integer,Double>();
    
    Set<Integer> candidateCluster = new HashSet<Integer>();
    
    for(int i=0;i<sales.size();i++){
          
           int clusterIndex = clusters.predict(sales.get(i));
          
           Integer density = clusterDesnsityMap.get(clusterIndex);
           if(density == null){
                  density=1;
           }
           else
           {
                  density++;
           }
           clusterDesnsityMap.put(clusterIndex, density);
           
           List<Integer> values = clusterValueMap.get(clusterIndex);
           if(values== null){
                  values = new ArrayList<Integer>();
                  clusterValueMap.put(clusterIndex, values);
           }
          
           values.add(i);
    
           
    }        
   
    
    int cIndex=0;
    for (Integer  key : clusterValueMap.keySet() ) {
          
          double avdADMP = 0;
          
          
          List<Integer> values = clusterValueMap.get(key);
          Vector centeroidV = clusters.clusterCenters()[key];
          double centeroid  = centeroidV.apply(0);
                  for(Integer index : values){
                         
                         double value = sales.get(index).apply(0);
                   double diff = Math.abs(centeroid - value );
                         absoluteDistanceFromCentriod.put(index,diff);
                         avdADMP += diff;
                         
                  }
            
         avdADMP = (avdADMP / values.size()) * 2.5;
         
         ampdThreshhold.put(cIndex++, avdADMP);
        
    }
    
 

    double clusterAvg=sales.size() / clusters.clusterCenters().length;
    double desityVariance = 0;
    int minDensityInex=0;  
    int mindensity = 1000000;
    for(Integer key : clusterDesnsityMap.keySet()){
          
          int density = clusterDesnsityMap.get(key);
          System.out.println("  cluster no : " + key + " :: density : " +  density);
          desityVariance +=  Math.pow(Math.abs( density - clusterAvg ) ,2);
          
          if(mindensity > density){
                 mindensity  =density; 
                 minDensityInex = key;
          }
        
    }
    
    desityVariance = desityVariance / clusters.clusterCenters().length;
    double desitySD  = Math.sqrt(desityVariance);
        
  
    
      if(sales.size() > 0 && mindensity <= 5){
            
            Integer key  = minDensityInex;
            List<Integer> values = clusterValueMap.get(key);
            for(Integer index : values){
                        
                  boolean positive =true;
                  if(sales.get(index).apply(0) >=  mean){
                               positive = true;
                  }
                  else
                  {
                               positive = false;
                  }
                  outlierMap.put(index,positive);
                  double rank =  Math.abs(mean- sales.get(index).apply(0)) / sd;
                  outlierRankMap.put(index, rank );
                        
           }
            
      }
              
          
  
      int sminDensityInex=0;  
      int smindensity = 1000000;
         for(Integer key : clusterDesnsityMap.keySet()){
             
             int density = clusterDesnsityMap.get(key);
             if(minDensityInex  != key){
                  
                  if( Math.abs( mindensity -  density)  <  desitySD )
                 { 
                  candidateCluster.add(key); 
                 }
                  
                  if(smindensity > density){
                        
                        smindensity = density;
                        sminDensityInex = key;
                  }
             }
                           
             
         }
                 
        if(candidateCluster.size()  ==0){
          
          candidateCluster.add(sminDensityInex);
        }
          
    
    
    for(Integer key : candidateCluster){
    
          List<Integer> values = clusterValueMap.get(key);
          double threshold = ampdThreshhold.get(key);
                        
           for(Integer index : values){
                  
                  double ampd = absoluteDistanceFromCentriod.get(index);
                  if(ampd > threshold){
                        
                        boolean positive =true;
                        if(sales.get(index).apply(0) >=  mean){
                               positive = true;
                        }
                        else
                        {
                               positive = false;
                        }
                        outlierMap.put(index,positive);   
                        double rank =  Math.abs(mean- sales.get(index).apply(0)) / sd;
                        outlierRankMap.put(index, rank );
                       
                        
                  }
                  
                 
           }
          
          
    } 
    
    
    Set<Entry<Integer, Double>> set = outlierRankMap.entrySet();
    List<Entry<Integer, Double>> list = new ArrayList<Entry<Integer, Double>>(set);
    Collections.sort( list, new Comparator<Map.Entry<Integer, Double>>()
    {
        public int compare( Map.Entry<Integer, Double> o1, Map.Entry<Integer, Double> o2 )
        {
            return (o2.getValue()).compareTo( o1.getValue() );
        }
    } );
    
    
    Map<Integer,Integer> positiveRank = new LinkedHashMap<Integer,Integer>();
    Map<Integer,Integer> negativeRank = new LinkedHashMap<Integer,Integer>();
    Map<Integer,Integer> globalRank = new LinkedHashMap<Integer,Integer>();
    
    
    int i=0;
    
    for(Map.Entry<Integer, Double> entry:list){
          
           int index= entry.getKey();
           boolean positive = outlierMap.get(index);
          
           
          if(positive == true){
                           
                 if(positiveRank.size()  < 2){
                       positiveRank.put(i,index);
                       
                 }
                 
                  }
                 else
                 { 
                        if(negativeRank.size()  < 2){
                              
                               negativeRank.put(i,index);
                       }
                       
                  }
           
          i++;
          
    }
    
    int globalIndex=0;
   int negKey;
   int posKey;
   if(negativeRank.size() > 0){
      negKey=(int) negativeRank.keySet().toArray()[0];
      if(positiveRank.size() > 0)
      {
    	  posKey = (int) positiveRank.keySet().toArray()[0];
             if( positiveRank.get(posKey) >  negativeRank.get(negKey) )
             {
                    globalRank.put(0, positiveRank.get(posKey));
                    globalRank.put(1, negativeRank.get(negKey));
             }
             else
             {
                    globalRank.put(1, positiveRank.get(0));
                    globalRank.put(0, negativeRank.get(0));
             }
      }
      else
      {
             for(Integer index : negativeRank.values()){
                    
                    globalRank.put(globalIndex++, index);
             } 
      }
      
      
             
     
      
   }
   else{
             
      for(Integer index : positiveRank.values()){
             
             globalRank.put(globalIndex++, index);
      }
      
            
             
      }
   JSONObject tempobj = new JSONObject();
   tempobj.put("measure", new JSONArray("["+meaobj.toString()+"]"));
   tempobj.put("dimension", new JSONArray("["+dimobj.toString()+"]"));
   tempobj.put("type", "CLUSTERING");
   String text="";    
    
   double maxrank = 0;
   int flag=0;
   int ue=0;
   JSONObject textobj1 = new JSONObject();
   JSONObject textobj2 = new JSONObject();
   JSONArray highlight = new JSONArray();
    for(Integer index : globalRank.values() ){
          //System.out.println(measure+":"+dimension+" index "+index);
    		if( outlierRankMap.get(index)==null)
    			continue;
           double rank =   outlierRankMap.get(index).doubleValue();
           highlight.put(index);
           if(flag==0)
           {
                 maxrank = rank;
                 flag=1;
           }
           else if(rank>maxrank)
           {
                 maxrank=rank;
           }
           
           List<String> dimensionValues = dimensionValuesList.get(index);
           
           boolean positive = outlierMap.get(index);
                 String dimStr = "";
                 
                 for(String dim : dimensionValues){
                       
                        dimStr += dim + ",";
                       
                  }
                 String prefix = "";
                 String insight = "";
                  if(positive == true){
                            
                        prefix += "High";
                       
                  }
                 else
                 { 
                        prefix += "Low";
                        
                 }
                  insight = dimStr.substring(0, dimStr.length()-1);
                  if(ue==0)
                  {
                       ue=1;
                       textobj1.put("outliertype", positive);
                       textobj1.put("text", insight);
                       textobj1.put("typetext", prefix);
                  }
                  else
                  {
                       textobj2.put("outliertype", positive);
                       textobj2.put("text", insight);
                       textobj2.put("typetext", prefix);
                  }
                  
                  System.out.println(insight);
    }
    
    if(globalRank.values().size()>0)
    {
	    if(textobj2.has("outliertype") && textobj1.getBoolean("outliertype")==textobj2.getBoolean("outliertype"))
	    {
	       tempobj.put("text1",measure +" for " +textobj1.get("text")+","+textobj2.get("text")+ " are " + textobj1.get("typetext"));
	       tempobj.put("outliertype1", textobj1.getBoolean("outliertype"));
	    }
	    else if(textobj2.has("outliertype"))
	    {
	       tempobj.put("text1",measure +" for " +textobj1.get("text") + " is " + textobj1.get("typetext"));
	       tempobj.put("outliertype1", textobj1.getBoolean("outliertype"));
	       tempobj.put("text2",measure + " for "+textobj2.get("text") + " is " + textobj2.get("typetext"));
	       tempobj.put("outliertype2", textobj2.getBoolean("outliertype"));
	    }
	    else
	    {
	       tempobj.put("text1",measure+" for " + textobj1.get("text") + " is " + textobj1.get("typetext"));
	       tempobj.put("outliertype1", textobj1.getBoolean("outliertype"));
	    }
	    tempobj.put("rank", maxrank);
	    tempobj.put("highlight", highlight);
	    result.put(tempobj);
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
                  
                   CopyOfClustering.initialize(datasetName); 
                  for(int i=0;i<dobj.length()-1;i++)
                  {
                	  dimension = dobj.getJSONObject(i).getString("name");
                	  dimobj = dobj.getJSONObject(i);
                	  for(int l=i;l<=i;l++) // for 2D1M change condition as l<dobj.length() && for 1D1M change condition as l<=i
                	  {
                		String dimeList="`"+dimension+"`";
                       dimension2= "";
                       if(l!=i)
                       {
                    	   dimension2=dobj.getJSONObject(l).getString("name");
                    	   dimobj2 = dobj.getJSONObject(l); 
                    	   dimeList +=",`"+dimension2+"`";
                       }
                       
                       
                       
                       for (int j = 0; j < mobj.length(); j++) {
                             measure = mobj.getJSONObject(j).getString("name");
                             meaobj = mobj.getJSONObject(j);
                             query="SELECT ";
                          
                           String agg = mobj.getJSONObject(j).getString("aggregation").toUpperCase();
                      	   if(agg.equalsIgnoreCase("COUNT(ALL)"))
                      		   agg="COUNT";
                      	   else if(agg.equalsIgnoreCase("Average"))
                      		   agg="AVG";
                          query+=dimeList+","+agg+"(`"+measure+"`)"+" FROM "+datasetName+" GROUP BY "+dimeList+"  ORDER BY "+dimeList;
                          
                         // query = "select `Category`, SUM(`Quantity sold`) from eFashionRetailData group by `Category`";
                          
                          //System.out.println(query);
                          if(i!=l)
                        	  CopyOfClustering.execute(2);
                          else
                        	  CopyOfClustering.execute(1);
                         }
                	  }
                  }
                  System.out.println("result is "+result.toString());
                  JSONObject insight = new JSONObject();
                  insight.put("insight", result);
                  File outputfile = new File("C:\\output_json\\"+datasetName+".json");
                  PrintWriter writer = new PrintWriter(outputfile, "UTF-8");
                  writer.println(insight.toString());
                  writer.close();
              
       }

}







