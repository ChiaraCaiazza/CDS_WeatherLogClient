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
     * call at the initialization in order to set the environment in a proper
     * way.
     * analize an xml string contained the list of available citis
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
     * call at the initialization in order to set the environment in a proper
     * way.
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
        
        /**************************************************************************************/
        CONNECT.printDoc(receivedDocument);
        /**************************************************************************************/
        
        measurementsNameList=mesurements;
        cityMeasure=rootList;
        
        //list of city
        cities = receivedDocument.getElementsByTagName("cityLog");
        //how much of them?
        System.out.println("# of city:  " + cities.getLength());
        
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
            //create a LinkedList and an HashMap 
            //singleMeasure=retrieveList(cityID);
            
            //all the related measurement available for this city
            citiesMeasurement_group=city.getElementsByTagName("measurement_group");
            
            //for each measurement_group
            int len1=citiesMeasurement_group.getLength();
            for (int j = 0;j < citiesMeasurement_group.getLength(); j++)
            {    
                //Node measurement;
                Element main, value, unit;  
                
                cityMeasurement_group = (Element) citiesMeasurement_group.item(j);
                
                updateTime=cityMeasurement_group.getAttribute("update");
                
                //create a LinkedList and an HashMap 
                //singleMeasure=retrieveList(cityID);
            
                if (!cityMeasurement_group.hasChildNodes()) 
                    continue;
                
                citiesMeasurement=cityMeasurement_group.getElementsByTagName("measurement");
                int len2=citiesMeasurement.getLength();
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

                    String s;
                    singleMeasure.put(main.getAttribute("name"),value.getTextContent()
                                                        + '_'+unit.getTextContent() );
                    
                }
            }
            
        } 
    }
    
    
    /***********************************************************************
     * 
     * @param  CityID String containing the ID of the city under analisys.
     * @return to the caller an HashMap that will contain the data related to 
     *          the current measure under analisys.
     * 
     * If we don't have a list of hashmap measure for this particular city we 
     * create a new one. Otherwise we return tan hash map.
     * 
     */
    /*private HashMap<String,String> retrieveList(String CityID)
    {
        HashMap<String,String> singleMeasure;
        LinkedList<HashMap> listOfMeasures;
        
        //for each city
        for(int i=0; i<cityMeasure.size();i++)
        {
            //for each city
            listOfMeasures=cityMeasure.get(i);
            //retrieve (but don't delete) the first element
            singleMeasure=listOfMeasures.element();
        
            if (singleMeasure.get("cityID").equals(CityID))
            {
                //this city is already present!. We create a new HashMap for 
                //this result and we return it.
                singleMeasure=new HashMap<>();
                listOfMeasures.add(singleMeasure);
                
                return singleMeasure;
            }
        }
        
        //this city is a new one and we don't have a linked list for it.
        //We create a new LinkedList for this city and a new HashMap for this measure.
        listOfMeasures=new LinkedList<>();
        singleMeasure=new HashMap<>();
        //add both the structures
        cityMeasure.add(listOfMeasures);
        listOfMeasures.add(singleMeasure);
        
        //return the HashMap
        return singleMeasure;
    }*/
}