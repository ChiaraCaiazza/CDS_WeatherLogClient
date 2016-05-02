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
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;




public class InsertGraph 
{
    private final UtilCity UTIL;
    private final Stage root;
    
    public InsertGraph(Stage stage)
    {
        UTIL=new UtilCity();
        root=stage;
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
        barChart.getStyleClass().add("chartStyle");
        barChart.setLegendSide(Side.RIGHT);
        
        //optimize the translateX property
        updateTranslateX(null, barChart);
        //optimize the translateY property
        updateTranslateY(null, barChart);
        
        //when the window width change optimize the translateX property
        root.widthProperty().addListener((obs)->
        {
            Platform.runLater(()->updateTranslateX(null, barChart));
        });
        //when the window height change optimize the translateY property
        root.heightProperty().addListener((obs)->
        {
            Platform.runLater(()->updateTranslateY(null, barChart));
        });
        
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
                                 LinkedList<LinkedList> allCityMeasurements, 
                                 String reqType)
    {
        LinkedList<HashMap> measurements;
        HashMap <String, String> singleMeasure;
        String s;
        Integer hour, day, month;
        double interval;
        HashMap<String,Double> bounds;
        ObservableList<String> xValuesTemp, xValues;
        Calendar c;
        long millisecond;
        
        
        bounds=new HashMap<>();
        searchMinMax(propertyName, allCityMeasurements, bounds);
        interval=(bounds.get("upperBound")+(bounds.get("upperBound")*10/100))-
                (bounds.get("lowerBound")-(bounds.get("lowerBound")*10/100));
        
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

                    xValuesTemp.add(hour+":00");
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
        final NumberAxis yAxis = new NumberAxis(
                     bounds.get("lowerBound")-(bounds.get("lowerBound")*10/100),
                     bounds.get("upperBound")+(bounds.get("upperBound")*10/100),
                     interval/10);
        
        yAxis.setLabel(propertyUnit);
        xAxis.setLabel(propertyName);
        xAxis.setCategories(xValues); 
        
       
        //creating the chart
        final LineChart<String,Number> lineChart = new LineChart<>(xAxis,yAxis);
        lineChart.getStyleClass().add("chartStyle");
        
        //optimize the translateX property
        updateTranslateX(lineChart, null);
        //optimize the translateY property
        updateTranslateY(lineChart, null);
        
        //when the window width change optimize the translateX property
        root.widthProperty().addListener((obs)->
        {
            Platform.runLater(()->updateTranslateX(lineChart, null));
        });
        //when the window height change optimize the translateY property
        root.heightProperty().addListener((obs)->
        {
            Platform.runLater(()->updateTranslateY(lineChart, null));
        });
       
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
    
    private void updateTranslateX(LineChart lineChart, StackedBarChart barChart)
    {
        double xSpace;
        
        //the available white space
        xSpace=(root.getWidth()*90/100)-1000;
        
        // if some white space is available translate the graph
        if (xSpace>0)
        {
            if (lineChart!=null)
                lineChart.setTranslateX(xSpace/2);
            else
                barChart.setTranslateX(xSpace/2);
        }
        else
        {
            if (lineChart!=null)
                lineChart.setTranslateX(0);   
            else
                barChart.setTranslateX(0);
        }
    }
    
    private void updateTranslateY(LineChart lineChart, StackedBarChart barChart)
    {
        double ySpace; 
        
        //the available white space
        ySpace = (root.getHeight()*95/100) - 180 - 350;
               
        // if some white space is available translate the graph
        if (ySpace>0)
        {
            if (lineChart!=null)
                lineChart.setTranslateY(ySpace/2);
            else
                barChart.setTranslateY(ySpace/2);
        }
        else
        {
            if (lineChart!=null)
                lineChart.setTranslateY(0);
            else
                barChart.setTranslateY(0);
        }
    }
    
    private void searchMinMax(String propertyName,
                              LinkedList<LinkedList> allCityMeasurements, 
                              HashMap<String,Double> bounds)
    {
        LinkedList<HashMap> measures;
        HashMap<String,String> measure;
        String value;
        
        for(int i=0; i<allCityMeasurements.size();i++)
        {
            //all reguarding the i-th city
            measures=allCityMeasurements.get(i);
        
        
            //for each measure of this city 
           for (int j=measures.size()-1;j>=0 ;j--)
           {
               //take the measurement
               measure=measures.get(j);

               //retrieve the data
               value=measure.get(propertyName);
               
               //if i don't search for this particular value then i can continue
               if (value==null) continue;
                
                value=UTIL.retrieveBefore(value,"_" );
                value=UTIL.retrieveAfter(value, " ");
                
                double thisValue=Double.parseDouble(value.replace(",", "."));
                
                if (bounds.size() ==0)
                {
                    bounds.put("lowerBound", thisValue);
                    bounds.put("upperBound", thisValue);
                }
                if (bounds.get("lowerBound")> thisValue )
                    bounds.put("lowerBound", thisValue);
                if ( bounds.get("upperBound") < thisValue )
                    bounds.put("upperBound", thisValue);
                
            }
        
        }
   
    }
    
    protected void insertASingleLineSerieMultipleCity(LineChart<String, Number> lineChart, 
                                     int i, String propertyName, String cityName,
                                     LinkedList<LinkedList> allCityMeasurements, String reqType
                                     )
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
                switch (reqType)
                {
                    case "d":
                        //only hour and minutes are required
                        newPoint=calendar.get(Calendar.HOUR_OF_DAY)+":00"; 
                        break;
                        
                    case "w":
                        //day, month, hour are required
                        newPoint=calendar.get(Calendar.DAY_OF_MONTH)+"/"
                                       +(calendar.get(Calendar.MONTH)+1)+" h"
                                       +calendar.get(Calendar.HOUR_OF_DAY);
                        break;
                        
                    case "m":
                        //day, month  are required
                        newPoint=calendar.get(Calendar.DAY_OF_MONTH)+"/"
                                   +(calendar.get(Calendar.MONTH)+1);
                        break;
                        
                    default:
                }
                double thisValue=Double.parseDouble(value.replace(",", "."));
                
                
                //add them to the serie            
                series.getData().add(new XYChart.Data(newPoint, 
                                 thisValue));
                System.out.println("Punto - "+ newPoint + "-" +thisValue);
            }
        }
        lineChart.getData().add(series);
    }
}