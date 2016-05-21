/******************************************************************************
 *
 * @author ChiaraCaiazza
 * @author GionatanGallo
 * 
 *****************************************************************************/
package weatherlogclient.show;

import java.util.HashMap;
import java.util.LinkedList;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;



public class HandlerCity 
{
    protected GridPane dataGridPane;
    private final Stage PRIMARY_STAGE;
    private final InsertTextTab INSERT_TEXT_TAB;
    private final InsertGraph INSERTGRAPH;
    private final UtilCity UTIL;
    
    protected Pane elementRootPane;
    protected TabPane root;
    protected Pane newPane;
    
    private LinkedList<LinkedList> allData;
    
    
    public HandlerCity(Stage stage, GridPane gridPane)
    {
        INSERT_TEXT_TAB=new InsertTextTab();
        INSERTGRAPH=new InsertGraph(stage);
        UTIL=new UtilCity();
        
        PRIMARY_STAGE=stage;
        dataGridPane=gridPane;
    }
    
    
    /**************************************************************************
     * 
     * @param list:   list of every measure recept from the server
     * 
     * Show the response recept from the server.
     * If we have queried only one city he show a text tab, otherwse this 
     * function show a tex tab plus a tab (containing a bar graph) for each
     * measure received.
     * 
     **************************************************************************/
    public void actualCityValue(LinkedList<LinkedList> list)
    {
        LinkedList<String> propertyList, unitList;
        String s;
        
        //int the LinkedList
        propertyList= new LinkedList<>();
        unitList= new LinkedList<>();
        
        //init the data structure
        allData=list;
        
        //new rootPane
        root=new TabPane();
        
        //search&add
        elementRootPane= (Pane)dataGridPane.lookup("#pane9");
        elementRootPane.getChildren().add(root);
        
        //set height and width
        root.prefWidthProperty().bind(elementRootPane.widthProperty());
        root.prefHeightProperty().bind(elementRootPane.heightProperty());
        
        //create the textual tab
        INSERT_TEXT_TAB.createTextTab(this, "", allData);
        
        //if we have more than one city we have to show the graph tabs too
        if (allData.size()>1)
        {
            //retrieve te list of property & the list of unit
            UTIL.retrievePropertyList(allData, propertyList, unitList);
            //for each property create a graph Tab
            while(!propertyList.isEmpty())            
            {
                //extract the first element from propertyList
                s=propertyList.pollFirst();
                if (s.equals("sunrise")||s.equals("sunset"))
                {
                    //I manage sunrise and sunset differently
                    unitList.pollFirst();
                }
                else
                {
                    //insert the bar and the graph
                    INSERTGRAPH.createBarGraphTab(s,unitList.pollFirst(),this, allData);
                }
            }
        }
    }
    
    
    public void pastCityValue(LinkedList<LinkedList> allDataList, String reqType)
    {
        LinkedList<LinkedList> temp;
        LinkedList<String> propertyList, unitList;
        HashMap<String,String> h;
        String s, property, unit;
        
        allData=allDataList;
        
        //new rootPane
        root=new TabPane();
        //new LinkedList
        propertyList= new LinkedList<>();
        unitList = new LinkedList<>();
        
        //search&add
        elementRootPane= (Pane)dataGridPane.lookup("#pane9");
        elementRootPane.getChildren().add(root);
        
        //set height and width
        root.prefWidthProperty().bind(elementRootPane.widthProperty());
        root.prefHeightProperty().bind(elementRootPane.heightProperty());
        
        temp=(LinkedList<LinkedList>)allData.clone();
        
        //creo il tab testuale
        for (int i=0; i<temp.size();i++)
        {
            allData.clear();
            allData.add(temp.get(i));

            //retrieve the name of this city
            h=(HashMap<String, String>)allData.get(0).get(0);
            s=h.get("cityName");
            
            //insert the tab
            INSERT_TEXT_TAB.createTextTab(this, s, allData);
        }
        
        //update HandlerSessionData.cityMeasure
        allData=(LinkedList<LinkedList>) temp.clone();
        
        //retrieve te list of property & the list of unit
        UTIL.retrievePropertyList(allData, propertyList, unitList);
        
        //for each property we create a graph
        while(!propertyList.isEmpty())
        {
            property=propertyList.pollFirst();
            unit=unitList.pollFirst();
            
            //even if we have a sunrise/sunset measure we don't make a graph
            if (!property.equals("sunrise")&&!property.equals("sunset"))
                INSERTGRAPH.createLineGraphTabMultipleCity(property,unit,this,
                                                           allData, reqType);
        }
    }
    
    
    /**************************************************************************
     * 
     * @param subject  Tab title
     * 
     * Create a new tab with a new ScrollPane.
     * The scrollPane have a single child newPane. This Pane will contain the
     * shown response.
     * 
     *************************************************************************/
    protected void initTab(String subject)
    {
        ScrollPane scrollPane;
        Tab newTab;
        
        //new scrollPane
        scrollPane=new ScrollPane();
        //new Tab
        newTab=new Tab();
        newTab.setId("backgroundStyle_Tab");
        //set title
        newTab.setText(subject);
        
        //new Pane
        newPane= new Pane();
         
        //append
        root.getTabs().add(newTab);
        newTab.setContent(scrollPane);
        scrollPane.setContent(newPane);
        
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
    }
    
    
    /***************************************************************************
     * 
     * @param measurementPane father pane
     * @param index cityIndex
     * @param list list of HashMap. each HashMap contain one measure for that
     *             city
     * 
     * 1) create a new Pane
     * 2)retrieve the information about the city name and append it
     * 
     **************************************************************************/
    protected void insertCityName(Pane measurementPane, int index, LinkedList<HashMap> list)
    {
        String s;
        Text t = new Text();
       
        //i take the first hashmap (all the hash map have the same city)
        HashMap<String,String> map=list.getFirst();
                
        //reposition
        measurementPane.relocate(30 , 60+(index*240));
        
        //retrieve the cityName
        s=map.get("cityName");
        t.setText(s);
        t.setId("cityTitle");
            
        //Append
        measurementPane.getChildren().add(t);
    } 
}