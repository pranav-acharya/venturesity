

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.json.JSONObject;

import com.opencsv.CSVReader;

public class GenericCSVToJsonConversion {
	public static void main(String[] args) throws Exception {

		String files;
		File folder = new File("D:\\CSV");

		File jsonFolder=new File("C:\\data_json");
		File[] listOfFiles = folder.listFiles(); 

		for (int j = 0; j < listOfFiles.length; j++) {

			String[] fileData=listOfFiles[j].getName().split("\\.");
			String fileName=fileData[0];
			String fileExt=fileData[1];

			if (listOfFiles[j].isFile() && fileExt.equalsIgnoreCase("csv"))  {

				CSVReader reader = new CSVReader(new FileReader(listOfFiles[j].getAbsolutePath()));
				String [] nextLine;

				BufferedReader br = null;
				int count=0;

				String outputDir=jsonFolder+"\\"+fileName+".json";
				System.out.println("File path:::"+outputDir);
				PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(outputDir, false)));   


				JSONObject jsonObject = null;
				String[] csvHeader = null;
				try {
					while ((nextLine = reader.readNext()) != null) {
						if(count==0){
							csvHeader = nextLine;
							String header = "BidId,TrafficType,PublisherId,AppSiteId,AppSiteCategory,Position,BidFloor,Timestamp,Age,Gender,OS,OSVersion,Model,Manufacturer,Carrier,DeviceType,DeviceId,DeviceIP,Country,Latitude,Longitude,Zipcode,GeoType,CampaignId,CreativeId,CreativeType,CreativeCategory,ExchangeBid,Outcome";
							csvHeader = header.split(",");
							count++;
						}else{					
							jsonObject = new JSONObject();					
							for(int i=0;i<nextLine.length;i++){
								jsonObject.put(csvHeader[i],nextLine[i]);
							}

							out.println(jsonObject.toString());
							//System.out.println(jsonObject.toString());

						}				
					}
					out.close();
					System.out.println("Operation done");
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					if (br != null) {
						try {

							br.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}				
			}
		}
	}
}
