package weatherlogclient.show;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Side;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;




public class InsertGraph 
{
    private final UtilCity UTIL;
    
    
    public InsertGraph()
    {
        UTIL=new UtilCity();
    }
    
    
    /************************************************************************
     * 
     * @param propertyName property name
     * @param propertyUnit unit
     * @param thisCityHandler ref. to the caller
     * @param allCityMeasurements
     * 
     * Called if we have choose to inspect the measures of more than one city.
     * If we have 4 type of measure available, this method is called 4 times;
     * every times it adds only one graph.
     * 
     * 1)create the axis
     * 2)Create the bar chart
     * 3)add bars to the bar chart
     * 
     ************************************************************************/
    protected void createBarGraphTab(String propertyName,String propertyUnit,
                                     HandlerCity thisCityHandler, 
                                     LinkedList<LinkedList> allCityMeasurements)
    {
        LinkedList<HashMap> measurements;
        HashMap<String, String> measure;
        
        //Tab initialization: add a new tab
        thisCityHandler.initTab(propertyName);
 
        //axis
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel(UTIL.onlyFirstUp(propertyUnit));
        
        //BarChart
        StackedBarChart<String,Number> barChart = new StackedBarChart<>(xAxis,yAxis);
        barChart.setTitle(UTIL.onlyFirstUp(propertyName));
        //barChart.setCategoryGap(60);
        barChart.setPrefSize(600,300);
        barChart.setLegendSide(Side.RIGHT);
        
        //for each city
        for (int i=0; i<allCityMeasurements.size(); i++)
        {
            //list of measure for the i-th city
            measurements=allCityMeasurements.get(i);
            //in every city is present only one set of measure
            measure=measurements.get(0);
            
            //insert a bar
            insertABarSerie(barChart,i,measure,propertyName);               
        }
        //append the graph
        thisCityHandler.newPane.getChildren().add(barChart);
        
        //set the max bar width
        setMaxBarWidth(60, 10, barChart,xAxis);
        barChart.widthProperty().addListener((obs)->
        {
            Platform.runLater(()->setMaxBarWidth(60, 10, barChart,xAxis));
        });
    }
    
    
    /**************************************************************************
     * 
     * @param barChart
     * @param i 
     * @param measure
     * @param PropertyName
     * 
     * insert a bar into a BarChart
     * 
     */
    protected void insertABarSerie(StackedBarChart<String, Number> barChart, 
                   int i, HashMap<String,String> measure, String PropertyName)
    {
        String s;
        int index;
        
        //cityName
        s=measure.get("cityName");
        
        
        //add a serie
        XYChart.Series serie = new XYChart.Series();
        serie.setName(s); 
        
        //retrieve the data
        String s2=measure.get(PropertyName);
        s2=UTIL.retrieveBefore(s2, "_");
        

        //add the bar
        serie.getData().add(new XYChart.Data(s, Double.parseDouble(s2.replace(",", "."))));
        //append the bar
        barChart.getData().addAll(serie);
        
    }  
        
    
    /**************************************************************************
     * 
     * @param maxCategoryWidth
     * @param minCategoryGap
     * @param bc
     * @param xAxis 
     * 
     * set the max possible bar width 
     */
    private void setMaxBarWidth(double maxCategoryWidth, double minCategoryGap,
                                StackedBarChart<String,Number> bc,
                                CategoryAxis xAxis )
    {
        double catSpace = xAxis.getCategorySpacing();
        bc.setCategoryGap(catSpace - Math.min(maxCategoryWidth, catSpace - minCategoryGap));
    }
    
    
    /***************************************************************************
     * 
     * @param propertyName
     * @param propertyUnit
     * @param thisCityHandler 
     * @param allCityMeasurements 
     * @param daily 
     */
    protected void createLineGraphTabMultipleCity(String propertyName,
                                 String propertyUnit,HandlerCity thisCityHandler,
                                 LinkedList<LinkedList> allCityMeasurements, String reqType)
    {
        LinkedList<HashMap> measurements;
        HashMap <String, String> singleMeasure;
        String s;
        Integer hour, day, month;
        ObservableList<String> xValuesTemp, xValues;
        Calendar c;
        long millisecond;
        
        
       //Tab initialization
       thisCityHandler.initTab(propertyName);
         
         
       c= Calendar.getInstance();
       c.getTime();
       xValuesTemp= FXCollections.observableArrayList(); 
       xValues= FXCollections.observableArrayList();
       
       switch (reqType)
       { 
           case "d":
           
                for (int i=0; i<24; i++)
                {                


                    hour=c.get(Calendar.HOUR_OF_DAY);
                    day = c.get(Calendar.DAY_OF_MONTH);
                    month = c.get(Calendar.MONTH)+1;

                    xValuesTemp.add(day+"/"+month+" h"+hour);
                    millisecond = c.getTimeInMillis();
                    millisecond-=60*60*1000;
                    c.setTimeInMillis(millisecond);
                }
                break;
                
           case "w":
                millisecond = c.getTimeInMillis();
                
                for (int i=0; i<28; i++)
                {                
                    hour=c.get(Calendar.HOUR_OF_DAY);
                    day = c.get(Calendar.DAY_OF_MONTH);
                    month = c.get(Calendar.MONTH)+1;

                    xValuesTemp.add(day+"/"+month+" h"+hour);
                    millisecond = c.getTimeInMillis();
                    millisecond-=6*60*60*1000;
                    c.setTimeInMillis(millisecond);
                }
               
                break;
              
            case "m":
                for (int i=0; i<30; i++)
                {                
                    hour=c.get(Calendar.HOUR_OF_DAY);
                    day = c.get(Calendar.DAY_OF_MONTH);
                    month = c.get(Calendar.MONTH)+1;

                    xValuesTemp.add(day+"/"+month);
                    millisecond = c.getTimeInMillis();
                    millisecond-=24*60*60*1000;
                    c.setTimeInMillis(millisecond);
                }
        }
        
        for( int i=xValuesTemp.size()-1; i>=0; i--)
        {
            xValues.add(xValuesTemp.get(i));
        }
        
                
        //create x and y axis
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel(propertyUnit);
        xAxis.setLabel(propertyName);
        
        xAxis.setCategories(xValues); 
       
        //creating the chart
        final LineChart<String,Number> lineChart = new LineChart<>(xAxis,yAxis);
        lineChart.setTitle(UTIL.onlyFirstUp(propertyName));
        lineChart.setPrefSize(600,300);
        lineChart.setLegendSide(Side.RIGHT);
        
        
        //for each city
        for (int i=0; i<allCityMeasurements.size(); i++)
        {
            //get the list of measure
            measurements=allCityMeasurements.get(i);
            
            //retrieve the cityName
            singleMeasure=(HashMap <String,String>) allCityMeasurements.get(i).get(0);
            s=singleMeasure.get("cityName");
            
            //draw a single line
            insertASingleLineSerieMultipleCity(lineChart,i, propertyName, s,
                                               allCityMeasurements, reqType);
        }
       
        //append
        thisCityHandler.newPane.getChildren().add(lineChart);
    }
    
    
    protected void insertASingleLineSerieMultipleCity(LineChart<String, Number> lineChart, 
                                     int i, String propertyName, String cityName,
                                     LinkedList<LinkedList> allCityMeasurements, String reqType)
    {
        LinkedList<HashMap> measures;
        HashMap<String,String> measure;
        String value,newPoint;
        SimpleDateFormat  simpleDate;
        Calendar calendar;
        Date d;
        
        //all reguarding the i-th city
        measures=allCityMeasurements.get(i);
        
        //create the serie
        XYChart.Series series = new XYChart.Series();
        series.setName(cityName);
        
        //for each measure 
       for (int j=measures.size()-1;j>=0 ;j--)
        {
            if (!propertyName.equals("sunrise")&&!propertyName.equals("sunset"))
            {
                measure=measures.get(j);

                //retrieve the data
                value=measure.get(propertyName);
                
                //if i don't have thi particular value then i can continue
                if (value==null) continue;
                
                value=UTIL.retrieveBefore(value,"_" );
                value=UTIL.retrieveAfter(value, " ");
                
                //UTC date string
                newPoint=measure.get("date");
                simpleDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                calendar=Calendar.getInstance();
                try 
                {
                    calendar.setTime(simpleDate.parse(newPoint));

                } catch (ParseException ex) 
                {
                    Logger.getLogger(InsertTextTab.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                //x label
                if (reqType!="m")
                {
                    //day, month, hour are required
                    newPoint=calendar.get(Calendar.DAY_OF_MONTH)+"/"
                                   +(calendar.get(Calendar.MONTH)+1)+" h"
                                   +calendar.get(Calendar.HOUR_OF_DAY);
                }
                else
                {
                    //day, month  are required
                    newPoint=calendar.get(Calendar.DAY_OF_MONTH)+"/"
                                   +(calendar.get(Calendar.MONTH)+1);
                }
                 
                //add them to the serie            
                series.getData().add(new XYChart.Data(newPoint, 
                                  Double.parseDouble(value.replace(",", "."))));
            }
        }
        lineChart.getData().add(series);
    }
}