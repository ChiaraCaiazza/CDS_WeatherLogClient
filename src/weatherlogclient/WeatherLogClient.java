/******************************************************************************
 *
 * @author ChiaraCaiazza
 * @author GionatanGallo
 * 
 *****************************************************************************/
package weatherlogclient;


import java.io.File;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import weatherlogclient.connections.ServerConnection;

import weatherlogclient.initialize.InitializeData;
import weatherlogclient.initialize.InitializeElements;



public class WeatherLogClient extends Application 
{
    //GridPane
    private GridPane homeGridPane;
    private GridPane dataPane;
    //Pane
    private Pane navigationPane;
    private Pane titlePane;
    
    private final InitializeElements INIT_ELEMENTS;
    private final InitializeData INIT_DATA;
    private final ObservableList<String> citiesNameList;
    private final ObservableList<String> citiesIdList;
    private final ObservableList<String> measurementsNameList;
    private final ObservableList<String> measurementsIdList;
    
    
    public WeatherLogClient()
    {
        //initialize the observableList
        citiesNameList = FXCollections.observableArrayList();
        citiesIdList = FXCollections.observableArrayList();
        measurementsNameList = FXCollections.observableArrayList();        
        measurementsIdList = FXCollections.observableArrayList();

        // create a new initialize Element
        INIT_ELEMENTS=new InitializeElements();
        //crete a new initializeData
        INIT_DATA = new InitializeData(citiesNameList, citiesIdList, 
                                      measurementsNameList, measurementsIdList);
        
        
        //start to retrieve the fist data about:
        // - list of cities 
        // - list of measures
        INIT_DATA.startDataInitialization();  
    }
    
    
    @Override
    public void start(Stage primaryStage) throws Exception 
    {
        Scene scene;
        
        
        //Create a newPane ( this is the root element)
        homeGridPane= new GridPane();       
        
        //set all the scene stuff
        scene = new Scene(homeGridPane, 750, 550);
        primaryStage.setScene(scene);
        //set the minimum windows size allowed
        primaryStage.setMinWidth(750);
        primaryStage.setMinHeight(550);
        primaryStage.setWidth(1200);
        primaryStage.show();
        
         primaryStage.setOnCloseRequest((WindowEvent we) -> {
            ServerConnection.closeConnection();
        });
        
        //initialize the content of the root grid adding 3 row and 3 column
        INIT_ELEMENTS.setGrid(primaryStage, homeGridPane);
        
        //create a navigation bar, a pane for the title and a pane for the 
        //window's content
        navigationPane= new Pane();
        titlePane=new Pane();
        dataPane=new GridPane();
        
        //initialize all the elements into the homeGridPane
        INIT_ELEMENTS.initializeStartingPane(homeGridPane, navigationPane,
                                             titlePane,dataPane, citiesNameList,
                                             measurementsNameList);        
        //add a file.css
        File f = new File("src/style/myStyle.css");
        scene.getStylesheets().clear();
        scene.getStylesheets().add("file:///" + f.getAbsolutePath().replace("\\", "/"));

        applycss();
    }
    

    /**************************************************************************
     * 
     * @param args the command line arguments
     * 
     **************************************************************************/
    public static void main(String[] args) 
    {
        launch(args);
    }
    

    private void applycss()
    {        
        homeGridPane.setId("backgroundStyle_root");
        
        navigationPane.getStyleClass().add("navigationBar");
        titlePane.getStyleClass().add("applicationTitle");
        dataPane.getStyleClass().add("dataGridPane");
        
        titlePane.relocate(0, 80);        
    }
}