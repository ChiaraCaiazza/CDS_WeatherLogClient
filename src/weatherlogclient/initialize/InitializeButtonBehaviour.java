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
    private  HandlerCity HANDLE_CITY;
    private Document receivedDocument;
    private final LinkedList<LinkedList> cityMeasure;
    
    private final Stage primaryStage;
    
    
    public InitializeButtonBehaviour(Stage stage)
    {
        CONNECT = new ServerConnection();
        CONNECTION_PATH = new SetRequest();
        ANALIZED_DOCUMENT = new AnalizeDocumentReceived();
        
        cityMeasure = new LinkedList<>();   
        
        primaryStage=stage;
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
        HANDLE_CITY = new HandlerCity(primaryStage, dataGridPane);
        
        //in pane 9 we can find the tabPane
        oldData= (Pane) dataGridPane.lookup("#pane9");
        //if i making a new req i remove the old one
        oldData.getChildren().clear();
        //i also remove all the old measurements collected
        cityMeasure.clear();
        
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
                                                          cityMeasure);
        
        //received (i received more than one set of measurements for each city)
        HANDLE_CITY.pastCityValue(cityMeasure, typeOfRequestCode);   
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
        HANDLE_CITY = new HandlerCity(primaryStage, dataGridPane);
        
        //in pane 9 we can find the tabPane
        oldData= (Pane) dataGridPane.lookup("#pane9");
        //if i making a new req i remove the old one
        oldData.getChildren().clear();
        //i also remove all the old measurements collected
        cityMeasure.clear();
        
        //set the path
        //h--> type=hourly. In this case is the only possible choice
        path = CONNECTION_PATH.constructPath("h", who);
        //make the request
        receivedDocument = CONNECT.makeRequest(path);
        //parse the received response
        ANALIZED_DOCUMENT.parseDocument(receivedDocument,measurements,
                                                         cityMeasure);
        
        //i add an actual single city---> i add a textual tab with all the value 
        //received (i received one set of measurements for each city)
        HANDLE_CITY.actualCityValue(cityMeasure);
    }
}
