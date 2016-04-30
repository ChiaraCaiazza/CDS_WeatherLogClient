/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package weatherlogclient.initialize;

import weatherlogclient.connections.ServerConnection;
import weatherlogclient.analize.AnalizeDocumentReceived;
import javafx.collections.ObservableList;
import org.w3c.dom.Document;
/**
 *
 * @author ChiaraCaiazza
 * @author GionatanGallo
 * 
 */
public class InitializeData 
{
    private final ServerConnection CONNECT;
    private final AnalizeDocumentReceived ANALIZED_DOCUMENT;
    private Document receivedDocument;
    private final ObservableList<String> citiesNameList;
    private final ObservableList<String> citiesIdList;
    private final ObservableList<String> measurementsNameList;
    private final ObservableList<String> measurementsIdList;
    
    
    public InitializeData(ObservableList<String>  cityName,ObservableList<String>  cityId, 
                         ObservableList<String>  measurementsName,
                         ObservableList<String>  measurementsId)
    {
        CONNECT = new ServerConnection();
        ANALIZED_DOCUMENT = new AnalizeDocumentReceived();
        citiesNameList=cityName;
        citiesIdList=cityId;
        measurementsNameList=measurementsName;
        measurementsIdList=measurementsId;
    }
    
    
    public void startDataInitialization()
    {
        //retrieve the list of all cities
        receivedDocument = CONNECT.makeRequest("list/city");
        //parsing the response
        ANALIZED_DOCUMENT.analizeReicevedCity(receivedDocument, citiesNameList,
                                              citiesIdList);        
        
        /**********************************************************************/
        System.out.println("\n\n\nQuesto è il documento");
        CONNECT.printDoc(receivedDocument);
        System.out.println("\nQuesto è la lista delle città");
        System.out.println(citiesNameList.toString());
        System.out.println("\n\n\nQuesta la lista degli id");
        System.out.println(citiesIdList.toString());
        /**********************************************************************/
        
        //retrieve the list of all measurements pair name-unit
        receivedDocument = CONNECT.makeRequest("list/measurement");
        //parsing the response
        ANALIZED_DOCUMENT.analizeReicevedMeasurement(receivedDocument, measurementsNameList, measurementsIdList);        
        /**********************************************************************/
        //print
        System.out.println("\n\n\nQuesto è il documento");
        CONNECT.printDoc(receivedDocument);
        System.out.println("\nQuesto è la lista delle città");
        System.out.println(measurementsNameList.toString());
        System.out.println("\n\n\nQuesta la lista degli id");
        System.out.println(measurementsIdList.toString());
        /**********************************************************************/
    }
}
