package weatherlogclient.show;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;



public class InsertTextTab 
{
    private final UtilCity UTIL;
    
    public InsertTextTab()
    {
        UTIL=new UtilCity();
    }
    
    
    /**************************************************************************
     * 
     * @param thisCityHandler reference to an HandlerCity object
     * @param title if !="" contains the name of city
     * @param allData contain the response received from the server
     * 
     * 
     * 1)Create and initialize the text Tab
     * 2)For each city show the related measurement
     * 
     *************************************************************************/
    protected void createTextTab(HandlerCity thisCityHandler, String title, 
                                 LinkedList<LinkedList> allData)
    {
        int j;
        int f;
        Pane measurementPane;
        LinkedList<HashMap> tempMeasurements;
        HashMap<String,String> tempSinglemeasure;
        Text measurement1;
        Text measurement2;
        
        
        //create the tab
        if (title.equals(""))
            thisCityHandler.initTab("Data");
        else
            thisCityHandler.initTab(title);
       
        //for each city
        for( int i=0; i<allData.size();i++)
        {       
            j=0;
            //retrieve the data about this city
            tempMeasurements=(LinkedList)allData.get(i);
            
            //new pane for the i° city
            measurementPane=new Pane();
            thisCityHandler.newPane.getChildren().add(measurementPane);
            
            //Insert the name of this particular city
            thisCityHandler.insertCityName(measurementPane, i, tempMeasurements);
            
            //for each measurement of this city
            f=0;
            for( int k=0; k<tempMeasurements.size();k++)
            {  
                //this HashMap contain the value related to the k-th measure of
                //the i-th city
                tempSinglemeasure=tempMeasurements.get(k);
                
                //for each element into the HashMap                
                for (Map.Entry entry : tempSinglemeasure.entrySet()) 
                {
                    // a new element
                    if (!"cityID".equals(entry.getKey().toString())
                            &&!"hour".equals(entry.getKey().toString())
                            &&!"cityName".equals(entry.getKey().toString())) 
                    {
                        measurement1=new Text();
                        measurement2=new Text();
                        
                        //reposition
                        measurement1.relocate(30,10+(j*20)+f);
                        measurement2.relocate(200,10+(j*20)+f);
                        
                        applyLabelText(measurement1, measurement2, 
                                       entry.getKey().toString(),
                                       entry.getValue().toString());
                        
                        applyLabelStyle(measurement1,measurement2,
                                        entry.getKey().toString());
                        
                        //append
                        measurementPane.getChildren().add(measurement1);
                        measurementPane.getChildren().add(measurement2);
                        j++;
                    }
                }
                f=f+30;
            }
        }        
    }    

    
    /**************************************************************************
     * 
     * @param text1 label containig measure name
     * @param text2 label containig measure value
     * @param who   measure name
     * 
     * Apply at the two lable the correct style
     * 
     */
    private void applyLabelStyle(Text text1, Text text2, String who)
    {
        String name;
        
        //cancel initial blank space and _ if presnt
        name=UTIL.retrieveAfter(who, " ");
        
        if ("date".equals(name))
        {
            text1.setFont(Font.font("Calibri", FontWeight.BOLD, 22));
            text2.setFont(Font.font("Calibri", FontWeight.BOLD, 22));
            text1.setWrappingWidth(500);
            
            return;
        }
        
        text1.setFont(Font.font("Calibri", FontWeight.NORMAL, 18));
        text2.setFont(Font.font("Calibri", FontWeight.NORMAL, 18));
    }
    
    
    /**************************************************************************
     * 
     * @param text1 label containig measure name 
     * @param text2 label containig measure value 
     * @param who   measure name  
     * @param what  measure value
     * 
     * Apply at every lable the correct string
     * 
     **************************************************************************/
    private void applyLabelText(Text text1, Text text2, String who, String what)
    {
        String name;
        String value;
        SimpleDateFormat  simpleDate;
        Calendar calendar;
        Date d;
        
        //cancel initial blank space and _ if presnt
        name=UTIL.retrieveAfter(who, " ");
        value=UTIL.retrieveAfter(what, " ");
        value= UTIL.removeUnderscore(value);
        
        if ("date".equals(name))
        {
            simpleDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            calendar=Calendar.getInstance();
            try 
            {
                calendar.setTime(simpleDate.parse(value));
               
            } catch (ParseException ex) 
            {
                Logger.getLogger(InsertTextTab.class.getName()).log(Level.SEVERE, 
                                                                    null, ex);
            }
            
            
            text1.setText(name+" "+calendar.get(Calendar.DAY_OF_MONTH)+"/"+
                    (calendar.get(Calendar.MONTH)+1)+"/"+calendar.get(Calendar.YEAR)
                    +"-"+calendar.get(Calendar.HOUR_OF_DAY)+":00:00");
            text2.setVisible(false);
        }
        else
        {
            text1.setText(name+":     ");
            text2.setText(value);
        }
    }
}
