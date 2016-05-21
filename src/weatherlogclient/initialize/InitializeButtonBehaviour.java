/******************************************************************************
 *
 * @author ChiaraCaiazza
 * @author GionatanGallo
 * 
 *****************************************************************************/
package weatherlogclient.initialize;

import weatherlogclient.analize.AnalizeDocumentReceived;
import weatherlogclient.connections.ServerConnection;
import weatherlogclient.connections.SetRequest;
import weatherlogclient.show.HandlerCity;
import java.util.LinkedList;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.w3c.dom.Document;


public class InitializeButtonBehaviour 
{
    private final ServerConnection CONNECT;
    private final SetRequest CONNECTION_PATH;
    private final AnalizeDocumentReceived ANALIZED_DOCUMENT;
    private final LinkedList<LinkedList> CITY_MEASURE;
    private final Stage PRIMARY_STAGE;
    private HandlerCity handle_city;
    private Document receivedDocument;
    
    
    public InitializeButtonBehaviour(Stage stage)
    {
        CONNECT = new ServerConnection();
        CONNECTION_PATH = new SetRequest();
        ANALIZED_DOCUMENT = new AnalizeDocumentReceived();
        
        CITY_MEASURE = new LinkedList<>();   
        
        PRIMARY_STAGE=stage;
    }
    
    
    /**************************************************************************
     * 
     * @param measurements      list of measurements
     * @param dataGridPane
     * @param who:              list of requested city
     *                          who="city1,city2,city3,city4"
     * @param comboBox          type ComboBox
     * @param typeName          list of possible type name
     * @param typeCode          list of possible tye code
     * 
     * 1)remove all child from dataGridPane()
     * 2)retrieve the type name&code
     * 3)construct the path, make the request and parse the response
     * 4)show thw respponse
     * 
     */
    public void analisysButtonBehaviour(ObservableList<String> measurements, 
                GridPane dataGridPane, String who, ComboBox comboBox,
                ObservableList<String> typeName, ObservableList<String> typeCode)
    {
        String path, typeOfRequestName, typeOfRequestCode;
        int typeOfRequestIndex;
        Pane oldData;
        
        //init an handler city object
        handle_city = new HandlerCity(PRIMARY_STAGE, dataGridPane);
        
        //in pane 9 we can find the tabPane
        oldData= (Pane) dataGridPane.lookup("#pane9");
        //if i making a new req i remove the old one
        oldData.getChildren().clear();
        //i also remove all the old measurements collected
        CITY_MEASURE.clear();
        
        //i obtained the selected option
        typeOfRequestName=(String) comboBox.getSelectionModel().getSelectedItem().toString(); 
        //its index
        typeOfRequestIndex= typeName.indexOf(typeOfRequestName);
        //its code
        typeOfRequestCode=typeCode.get(typeOfRequestIndex);
        
        
        /*set the path:
                typeOfrequestCode=d --->type=daily
                typeOfrequestCode=w --->type=weekly
                typeOfrequestCode=m --->type=monthly
        */
        path = CONNECTION_PATH.constructPath(typeOfRequestCode,who);
        //make the request
        receivedDocument = CONNECT.makeRequest(path);
        //parse the received document
        ANALIZED_DOCUMENT.parseDocument(receivedDocument, measurements, 
                                                          CITY_MEASURE);
        
        //received (i received more than one set of measurements for each city)
        handle_city.pastCityValue(CITY_MEASURE, typeOfRequestCode);   
    }
    
    
    /**************************************************************************
     * 
     * @param measurements
     * @param dataGridPane
     * @param who               list of requested city
     *                          who="city1,city2,city3,city4"
     * 
     * 1)remove all child from dataGridPane() and all the old data collected 
     *   from eventually previous response
     * 2)construct the path, make the request and parse the response
     * 3)show thw respponse
     * 
     */
    public void inspectButtonBehaviour(ObservableList<String> measurements,
                GridPane dataGridPane, String who)
    {
        String path;
        Pane oldData;
        
        
        //init an handler city object
        handle_city = new HandlerCity(PRIMARY_STAGE, dataGridPane);
        
        //in pane 9 we can find the tabPane
        oldData= (Pane) dataGridPane.lookup("#pane9");
        //if i making a new req i remove the old one
        oldData.getChildren().clear();
        //i also remove all the old measurements collected
        CITY_MEASURE.clear();
        
        //set the path
        //h--> type=hourly. In this case is the only possible choice
        path = CONNECTION_PATH.constructPath("h", who);
        //make the request
        receivedDocument = CONNECT.makeRequest(path);
        //parse the received response
        ANALIZED_DOCUMENT.parseDocument(receivedDocument,measurements,
                                                         CITY_MEASURE);
        
        /*i add an actual single city---> i add a textual tab with all the value 
        received (i received one set of measurements for each city)*/
        handle_city.actualCityValue(CITY_MEASURE);
    }
}