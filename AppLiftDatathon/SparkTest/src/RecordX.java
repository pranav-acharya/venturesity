import java.io.Serializable;

public class  RecordX implements Serializable {
       
       

       public String getCategory() {
              return category;
       }



       public void setCategory(String category) {
              this.category = category;
       }



       public String getLines() {
              return lines;
       }



       public void setLines(String lines) {
              this.lines = lines;
       }



       public String getCity() {
              return city;
       }



       public void setCity(String city) {
              this.city = city;
       }



       public String getState() {
              return state;
       }



       public void setState(String state) {
              this.state = state;
       }



       public String getQuarter() {
              return quarter;
       }



       public void setQuarter(String quarter) {
              this.quarter = quarter;
       }



       public int getYear() {
              return year;
       }



       public void setYear(int year) {
              this.year = year;
       }



       public double getMargin() {
              return margin;
       }



       public void setMargin(double margin) {
              this.margin = margin;
       }



       public int getQuantitySold() {
              return quantitySold;
       }



       public void setQuantitySold(int quantitySold) {
              this.quantitySold = quantitySold;
       }



       public double getSalesRevenue() {
              return salesRevenue;
       }



       public void setSalesRevenue(double salesRevenue) {
              this.salesRevenue = salesRevenue;
       }



       private String category;
       private String lines;
       private String city;
       private String state;
       private String quarter;
       private int year;
       private double margin;
       private int quantitySold;
       private double salesRevenue;
       
       
         
         public RecordX(  String category,String lines, String city, String state,String quarter,String year, String margin, String quantitySold,String salesRevenue){
                
                try
                {
                       this.category = category;
                       this.lines= lines;
                       this.city = city;
                       this.state = state;
                       this.quarter=quarter;
                       this.year =  Integer.parseInt(year);
                       this.margin = Double.parseDouble(margin);
                       this.quantitySold=  Integer.parseInt(quantitySold);
                       this.salesRevenue= Double.parseDouble(salesRevenue);
                }
                catch(Exception e){
                       
                }
                
                
         }
       

         
         // constructor , getters and setters  
       }
