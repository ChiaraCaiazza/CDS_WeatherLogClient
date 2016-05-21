/**
 *
 * @author ChiaraCaiazza
 * @author GionatanGallo
 * 
 */
package weatherlogclient.initialize;


import javafx.collections.ObservableList;
import org.w3c.dom.Document;

import weatherlogclient.connections.ServerConnection;
import weatherlogclient.analize.AnalizeDocumentReceived;


public class InitializeData 
{
    private final ServerConnection CONNECT;
    private final AnalizeDocumentReceived ANALIZED_DOCUMENT;
    private final ObservableList<String> CITIES_NAME_LIST;
    private final ObservableList<String> CITIES_ID_LIST;
    private final ObservableList<String> MEASUREMENTS_NAME_LIST;
    private final ObservableList<String> MEASUREMENT_ID_LIST;
    private Document receivedDocument;
    
    
    public InitializeData(ObservableList<String>  cityName,ObservableList<String>  cityId, 
                         ObservableList<String>  measurementsName,
                         ObservableList<String>  measurementsId)
    {
        CONNECT = new ServerConnection();
        ANALIZED_DOCUMENT = new AnalizeDocumentReceived();
        CITIES_NAME_LIST=cityName;
        CITIES_ID_LIST=cityId;
        MEASUREMENTS_NAME_LIST=measurementsName;
        MEASUREMENT_ID_LIST=measurementsId;
    }
    
    
    public void startDataInitialization()
    {
        //retrieve the list of all cities
        receivedDocument = CONNECT.makeRequest("list/city");
        //parsing the response
        ANALIZED_DOCUMENT.analizeReicevedCity(receivedDocument, CITIES_NAME_LIST,
                                              CITIES_ID_LIST);        
        
        //retrieve the list of all measurements pair name-unit
        receivedDocument = CONNECT.makeRequest("list/measurement");
        //parsing the response
        ANALIZED_DOCUMENT.analizeReicevedMeasurement(receivedDocument, MEASUREMENTS_NAME_LIST, MEASUREMENT_ID_LIST); 
    }
}
