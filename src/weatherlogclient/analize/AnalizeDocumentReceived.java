/******************************************************************************
 *
 * @author ChiaraCaiazza
 * @author GionatanGallo
 * 
 *****************************************************************************/
package weatherlogclient.analize;


import java.util.HashMap;
import java.util.LinkedList;
import javafx.collections.ObservableList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import weatherlogclient.connections.ServerConnection;


public class AnalizeDocumentReceived 
{
    private final ServerConnection CONNECT;
     
    private ObservableList<String> measurementsNameList;
    private ObservableList<String> measurementsIdList;
    private ObservableList<String> cityNameList;
    private ObservableList<String> cityIdList;
     
    public static LinkedList<LinkedList> cityMeasure;
     
    public AnalizeDocumentReceived()
    {
         CONNECT = new ServerConnection();
    }
     
     
    /**************************************************************************
     * 
     * @param receivedDocument
     * @param citiesName
     * @param citiesId
     * 
     * At the initialization, set the environment in a proper way.
     * Analize an xml string contained the list of available cities
     * 
     ***************************************************************************/
    public void analizeReicevedCity(Document receivedDocument, ObservableList<String> citiesName, ObservableList<String> citiesId)
    {       
        cityIdList=citiesId;
        cityNameList=citiesName;
        
        //list of city elements
        NodeList nList = receivedDocument.getElementsByTagName("city");
        
        //for each city
        for (int temp = 0; temp < nList.getLength(); temp++)
        {
            Node nNode;
            Element el;
            
            // this is a single city
            nNode = nList.item(temp);
            //city elements
            el= (Element) nNode;
            
            //add its name
            cityNameList.add(el.getAttribute("name"));
            //add its id
            cityIdList.add(el.getAttribute("id"));
        }        
    }
    
    
    /**************************************************************************
     * 
     * @param receivedDocument
     * @param measurementsName
     * @param measurementsId
     * 
     * At the initialization, set the environment in a proper way.
     * analize an xml string contained the list of measurements
     * 
     ***************************************************************************/
    public void analizeReicevedMeasurement (Document receivedDocument, 
                                    ObservableList<String> measurementsName, 
                                    ObservableList<String> measurementsId)
    {        
        measurementsNameList=measurementsName;
        measurementsIdList=measurementsId;
        
        //list of measurement elements
        NodeList nList = receivedDocument.getElementsByTagName("measurement");
        
        //for measurement node
        for (int temp = 0; temp < nList.getLength(); temp++)
        {
            Node nNode;
            Element el;
            
            // this is a single name-unit pair
            nNode = nList.item(temp);
            //name-unit elements
            el = (Element) nNode;
            
            //add its name
            measurementsNameList.add(el.getElementsByTagName("name").item(0)
                                                        .getTextContent());
            //add its id
            measurementsIdList.add(el.getElementsByTagName("unit").item(0)
                                                        .getTextContent());
        }        
    }
    
    
    /***************************************************************************
     *
     * @param receivedDocument
     * @param mesurements
     * @param rootList
     * 
     * take the document, make the parse of the xml string, save the response 
     * into some data structures
     * 
    ***************************************************************************/
    public void parseDocument(Document receivedDocument, 
             ObservableList<String> mesurements, LinkedList<LinkedList> rootList)
    {   
        
        NodeList cities, citiesMeasurement_group, citiesMeasurement;
        Element city, cityMeasurement_group, cityMeasurement;
        String cityID, cityName, updateTime;
        
        LinkedList<HashMap> cityD;
        HashMap<String, String> singleMeasure;
        
        
        measurementsNameList=mesurements;
        cityMeasure=rootList;
        
        //list of city
        cities = receivedDocument.getElementsByTagName("cityLog");
        
        //  for each city 
        for(int i = 0; i < cities.getLength(); i++)
        {            
            //for the i-st city
            city = (Element) cities.item(i);
            //its id
            cityID = city.getAttribute("id");
            cityName= city.getAttribute("name");
            
            cityD =new LinkedList<>();
            rootList.add(cityD);
            
            //all the related measurement available for this city
            citiesMeasurement_group=city.getElementsByTagName("measurement_group");
            
            //for each measurement_group
            for (int j = 0;j < citiesMeasurement_group.getLength(); j++)
            {    
                Element main, value, unit;  
                
                cityMeasurement_group = (Element) citiesMeasurement_group.item(j);
                
                updateTime=cityMeasurement_group.getAttribute("update");
                
                if (!cityMeasurement_group.hasChildNodes()) 
                    continue;
                
                citiesMeasurement=cityMeasurement_group.getElementsByTagName("measurement");
                //for each measurement in a measurement group
                singleMeasure = new HashMap<>();
                for (int k = 0;k < citiesMeasurement.getLength(); k++)
                { 
                    // this mesurement
                    cityMeasurement=(Element)citiesMeasurement.item(k);

                    main=(Element) cityMeasurement.getChildNodes().item(0);
                    value=(Element) cityMeasurement.getChildNodes().item(1);
                    unit=(Element) cityMeasurement.getChildNodes().item(2);

                    if (k==0)
                    {
                        //insert the cityID, the date and the hour
                        singleMeasure.put("cityID", cityID);
                        singleMeasure.put("cityName", cityName);
                        singleMeasure.put("date", updateTime);
                        cityD.add(singleMeasure);
                    } 

                    singleMeasure.put(main.getAttribute("name"),value.getTextContent()
                                                        + '_'+unit.getTextContent() );
                    
                }
            }
        } 
    }
}