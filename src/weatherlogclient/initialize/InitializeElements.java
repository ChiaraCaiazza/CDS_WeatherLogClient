/******************************************************************************
 *
 * @author ChiaraCaiazza
 * @author GionatanGallo
 * 
 *****************************************************************************/
package weatherlogclient.initialize;


import java.io.File;
import java.util.LinkedList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.cell.ComboBoxListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class InitializeElements 
{
    GridPane dataGridPane, homeGridPane;
    Stage primaryStage;
    
    private InitializeButtonBehaviour INIT_BUTTON;
    private MenuItem contactUs, an ,insp, backToHome, exitApplication;
    private ObservableList<String> citiesNameList;
    private ObservableList<String> measuresNameList;

    
    public InitializeElements()
    {
    }
    
    
    /**************************************************************************
     * 
     * @param stage
     * @param root 
     * 
     * add to the root GridPane 3 row and 3 column
     * 
    ***************************************************************************/
    public void setGrid(Stage stage, GridPane root)
    {
        RowConstraints row1, row2, row3;
        ColumnConstraints col1, col2, col3;
        
        primaryStage=stage;
        INIT_BUTTON= new InitializeButtonBehaviour(primaryStage);
        /*create 3 Row:
            -   row1: navigation bar, 28px height;
            -   row2: title, 120 px height;
            -   row3: data content;
        */
        row1 = new RowConstraints(28);
        row2 = new RowConstraints(120);
        row3 = new RowConstraints();
        //add them
        root.getRowConstraints().add(row1);
        root.getRowConstraints().add(row2);
        root.getRowConstraints().add(row3);
        //stage height - 28 - 120 - 25 (bottom white space)
        row3.prefHeightProperty().bind(root.heightProperty().subtract(28).subtract(120).subtract(25));
          
        //create 3 column 
        col1 = new ColumnConstraints();
        col2 = new ColumnConstraints();
        col3 = new ColumnConstraints();
        //add them
        root.getColumnConstraints().add(col1);
        root.getColumnConstraints().add(col2);
        root.getColumnConstraints().add(col3);
        //set percentage width
        col1.setPercentWidth(5);
        col2.setPercentWidth(90);
        col3.setPercentWidth(5);
    }
   
    
    /**************************************************************************
     * 
     * @param gridPane
     * @param navigationPane
     * @param titlePane
     * @param dataPane 
     * @param citiesName
     * 
     * Initialize all the elements of the main home window
     * @param measurementsName
     * 
    */
    public void initializeStartingPane(GridPane gridPane,Pane navigationPane, 
            Pane titlePane, GridPane dataPane, ObservableList<String>citiesName,ObservableList<String>measurementsName )
    {
        LinkedList<String> strings;
        Image img;
        ImageView  title;
        
        
        homeGridPane=gridPane;
        citiesNameList=citiesName;
        measuresNameList = measurementsName;
        
        /*******************WORKING ON THE NAVIGATION PANE *******************/
        
        //add the navigation bar to the navigationPane
        addNavigationToolbar(navigationPane);
        //add the navigation Pane to the GridPane:
        // -------------------------------------
        // |xxxxxxxxxxx|xxxxxxxxxxx|xxxxxxxxxxx|
        // -------------------------------------
        // |           |           |           |
        // -------------------------------------
        // |           |           |           |
        // -------------------------------------
        homeGridPane.add(navigationPane,0,0,3,1);
        
        
        /**********************WORKING ON THE TITLE PANE **********************/
        //create an image
        img = new Image ("style/images/title.png");
        //create an imageview
        title=new ImageView();
        //add the image
        title.setImage(img);
        title.setFitWidth(600);
        title.translateXProperty()
            .bind(primaryStage.widthProperty().subtract(title.getFitWidth())
                    .divide(2));
        title.setStyle("-fx-background-color: black;");
        //add the ImageView
        titlePane.getChildren().add(title);
        //add the pane to the GridPane
        homeGridPane.add(titlePane,0,1,3,1);
        
        // -------------------------------------
        // |           |           |           |
        // -------------------------------------
        // |xxxxxxxxxxx|xxxxxxxxxxx|xxxxxxxxxxx|
        // -------------------------------------
        // |           |           |           |
        // -------------------------------------
        
        /**********************WORKING ON THE DATA PANE **********************/
        //add the content gridpane
        // -------------------------------------
        // |           |           |           |
        // -------------------------------------
        // |           |           |           |
        // -------------------------------------
        // |           |xxxxxxxxxxx|           |
        // -------------------------------------
        homeGridPane.add(dataPane,1,2);
        
        //create and set the string that I want to print
        strings=new LinkedList();
        strings.add("Welcome to WeatherLog!\n\nChoose one of your possibility:");
        strings.add("-Inspect:");
        strings.add("see past wheather information about one or more than one city.");
        strings.add("-Analisys:");
        strings.add("Analize past data.");
        
        dataGridPane=dataPane;
        setHomeDataGrid(strings);
    }
    
    
    /*************************************************************************
     * 
     * @param navigationPane  Pane will contain the navigation bar
     * 
     * Create a navigation bar with 3 menù (hoe, request, help) menù and append 
     * them to the NavigationPane 
     * 
     */
    private void addNavigationToolbar(Pane navigationPane)
    {
        MenuBar mainMenu;
        Menu home, help, request;
        
        //Creates our main menu to hold our Sub-Menus.
        mainMenu = new MenuBar();
        //set the width of the nevigation tollbar
        mainMenu.prefWidthProperty().bind(primaryStage.widthProperty());
        
        //create the menù
        home = new Menu ("Home");
        request = new Menu("Request");
        help=new Menu("Help");
        //add them to the mainMenu
        mainMenu.getMenus().addAll(home, request, help);
        
        //create the submenù options
        contactUs=new MenuItem("Contact us!");
        an=new MenuItem("Make analize request");
        insp=new MenuItem("Make inspect request");
        backToHome = new MenuItem("Come back to the home");
        exitApplication = new MenuItem("Close application");
        //add them
        help.getItems().add(contactUs);
        request.getItems().addAll(an, insp);
        home.getItems().addAll(backToHome, exitApplication);
        
        //add the container to the navigation Pane
        navigationPane.getChildren().add(mainMenu);
        addMenuBehaviour();
    }
    
    
    /**************************************************************************
     * 
     * Implement all the actions associate to the menù option
     * 
    **************************************************************************/
    private void addMenuBehaviour()
    {
        /***********************************************************************
                                 Contact us!
        ************************************************************************/
        contactUs.setOnAction((ActionEvent e) -> 
        {
            String s;
            
            s="\n\n\nWeatherLog is a project of Chiara Caiazza and"
               + " Gionatan Gallo.\nFor any inconvenient or requirements "
               + "write to us at admin@weatherlog.com";
            openPopup("Contact us!",s);
        });
        
        /***********************************************************************
                                Make analize request
        ************************************************************************/
        an.setOnAction((ActionEvent e) -> 
        {
            //add the elements for an analize request
            setRequirementsPane(true);
        });
        
        /***********************************************************************
                                Make inspect request
        ************************************************************************/
        insp.setOnAction((ActionEvent e) -> 
        {
            //add the elements for an inspect request
            setRequirementsPane(false);
        });
        
        /***********************************************************************
                                Exit application
        ************************************************************************/
        exitApplication.setOnAction((ActionEvent e) -> 
        {
            //close the application window
            primaryStage.close();
        });
        
        
        /***********************************************************************
                             Come back to the home
        ************************************************************************/
        backToHome.setOnAction((ActionEvent e) -> 
        {
            LinkedList<String> strings;
            GridPane dataPane;
            
            //contains the message that I want display
            strings=new LinkedList();
            strings.add("Welcome back to WeatherLog, you can restart from where you left off!\n\nChoose one of your possibility:");
            strings.add("-Inspect:");
            strings.add("see past wheather information about one or more than one city.");
            strings.add("-Analisys:");
            strings.add("Analize past data.");
            
            //remove the old dataGridPane
            homeGridPane.getChildren().remove(dataGridPane);
            //create a new one
            dataGridPane= new GridPane();
            //add it
            homeGridPane.add(dataGridPane, 1, 2);
            
            
            //refill the new dataGridPane
            setHomeDataGrid(strings);
        });
    }
    
    
    /**************************************************************************
     * 
     * @param title
     * @param popupText
     * 
     * create and initialize a popup using the given parameter
     * 
    **************************************************************************/
    private void openPopup(String title, String popupText)
    {
        FlowPane popupFlowPane;
        Scene popupScene;
        Stage popupStage;
        File f;
        Text text;
        
        //create a new FlowPane
        popupFlowPane = new FlowPane();
        //set the CSS id
        popupFlowPane.setId("backgroundStyle_popup");
            
        //create a new Scene
        popupScene = new Scene(popupFlowPane, 400, 250);
            
        //create a new stage
        popupStage = new Stage();
        //init
        popupStage.setScene(popupScene);
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle(title);
        popupStage.setMinWidth(350);
        popupStage.show();
            
        //add a file.css
        f = new File("src/style/myStyle.css");
        popupScene.getStylesheets().clear();
        popupScene.getStylesheets().add("file:///" + f.getAbsolutePath().replace("\\", "/"));
            
        //Create a new Text
        text=new Text();
        //set the ID
        text.setId("popupText");
        //Width of the text box.
        text.setWrappingWidth(360);
        //add the Text
        text.setText(popupText);
            
        //add the text Element
        popupFlowPane.getChildren().add(text);
    }
      
    
    /**************************************************************************
     * 
     * @param analisys 
     * 
     * manage the data grid pane for the analisys and inspect funtionality.
     * analisys=true--> the user want to perform an analisys operation
     * analisys=false--> the user want to perform an inspect operation
     * 
    ****************************************************************************/
    private void setRequirementsPane(boolean analisys)
    {
        Pane temp1, temp2;
        Label label;
        ListView listView;
        ComboBox comboBox;
        Button cityButton1, cityButton2, requestButton ;
        ObservableList firstElements,tempCityNameList;
        ObservableList<String> typeName, typeCode;
        
        //i remove the old dataGridPane
        homeGridPane.getChildren().remove(dataGridPane);
        //create a new one 
        dataGridPane= new GridPane();
        //add it to the home GridPane
        homeGridPane.add(dataGridPane, 1, 2);
        
        firstElements= FXCollections.observableArrayList();
        typeName = FXCollections.observableArrayList("Last day","Last week",
                                                     "Last month");
        typeCode = FXCollections.observableArrayList( "d", "w","m");
        
        comboBox=new ComboBox(typeName);
        //the first element is the default one
        comboBox.getSelectionModel().selectFirst();
                
        //initialize this new dataGridPane
        initializeRequirementsPane();
        
        //add city label
        temp1= (Pane) dataGridPane.lookup("#pane2");
        label=new Label("City");
        label.getStyleClass().add("cityText");
        temp1.getChildren().add(label);
        
        //add city ListView
        temp1= (Pane) dataGridPane.lookup("#pane3");
        tempCityNameList=FXCollections.observableArrayList(citiesNameList);
        listView= new ListView();
        listView.setId("cityListViewStyle");
        listView.relocate(10, 0);        
        listView.setEditable(true);
        //add the first elements
        firstElements.add("---");
        listView.setItems(firstElements);
        listView.setCellFactory(ComboBoxListCell.forListView(tempCityNameList));
        //add the list view
        temp1.getChildren().add(listView);
        
        //add city button
        temp1= (Pane) dataGridPane.lookup("#pane4");
        cityButton1= new Button();
        cityButton1.setText("New city");
        cityButton1.setId("smallAddButtonStyle");
        cityButton1.relocate(20, 0);
        temp1.getChildren().add(cityButton1);
        
        //remove city button
        temp1= (Pane) dataGridPane.lookup("#pane4");
        cityButton2= new Button();
        cityButton2.setText("Remove city");
        cityButton2.setId("smallRemoveButtonStyle");
        cityButton2.relocate(105, 0);
        temp1.getChildren().add(cityButton2);
        
        //add type label if required
        if (analisys==true)
        {
            //add a label "Type"
            temp1= (Pane) dataGridPane.lookup("#pane6");
            label=new Label("Type");
            temp1.getChildren().add(label);
            //add a class
            label.getStyleClass().add("cityText");
            
            //add a choicebox
            temp1= (Pane) dataGridPane.lookup("#pane7");
            temp1.getChildren().add(comboBox);
            //set its ID
            comboBox.setId("typeComboBoxStyle");            
        }
        
        //retrieve the pane8
        temp1= (Pane) dataGridPane.lookup("#pane8");
        //create a new button
        requestButton=new Button();
        requestButton.setId("buttonStyle");
        //set its text
        if (analisys==true)
        {
            requestButton.setText("Make Analisys");
            requestButton.setOnMouseClicked((MouseEvent e) -> 
            {
                String who;
                
                who = setCities(listView);
                
                //one (or more) cities were selected
                if (!"".equals(who)) 
                {
                    INIT_BUTTON.analisysButtonBehaviour(measuresNameList, 
                                     dataGridPane, who, comboBox, typeName, typeCode);
                }
            });
        }
        else
        {
            requestButton.setText("Make inspect");
            
            requestButton.setOnMouseClicked((MouseEvent e) -> 
            {
                String who;
                
                who = setCities(listView);
                
                //one (or more) cities were selected
                if (!"".equals(who)) 
                {
                    INIT_BUTTON.inspectButtonBehaviour(measuresNameList,
                               dataGridPane, who);
                }
            });
        }
        //sets its width and relocate it
        requestButton.relocate(50,20);
        //add the button
        temp1.getChildren().add(requestButton);
        
        //AddACity behaviour
        cityButton1.setOnMouseClicked((MouseEvent e) -> 
        {
            ObservableList<String> items;
                
            
            items= listView.getItems();
        
            //the choosed elements is deleted from the list
            tempCityNameList.remove(listView.getSelectionModel().getSelectedItem());
            
            //we can have no more than 5 elements
            if(items.size() <= 4 && (items.isEmpty() || !items.get(items.size() - 1).equals("---")))
            {
                items.add("---");
            }
        });
        
        
        //remove city bahaviour
        cityButton2.setOnMouseClicked((MouseEvent e) -> 
        {
            ObservableList<String> items;
            
            items= listView.getItems();
            
            if(items.size() > 0 && listView.getSelectionModel().getSelectedItem()!= "---"&& "---".equals(items.get(items.size()-1)))
            {
                //i add the removed item to the tempCityListName
                tempCityNameList.add(listView.getSelectionModel().getSelectedItem());
                //i remove the elements from the list view chosed items
                items.remove(listView.getSelectionModel().getSelectedItem());
                //if there is no city I add a possible empty choice
                if(items.isEmpty())
                {
                    items.add("---");
                }
            }
        });
        
    }
    
    private String setCities(ListView listView)
    {
        ObservableList<String> items;
        int quanti;
        String who;
                
        who="";                
        items= listView.getItems();
        quanti=items.size();
                
        for (int k=0; k<quanti; k++)
        {
            if ("---".equals(items.get(k)))
            {
                return "";
            }
            
            who+=items.get(k);
            if  (k< (quanti-2))
            { 
                
                who +=",";
            }
            else
            {
                // the next !="---"
                if (k+1!= quanti && !"---".equals(items.get(k+1)))
                {
                    who +=",";
                } 
                else 
                {
                    //the last elements is the --- one. i increment k
                    k++;
                }
            }
        }
        
        return who;
    }
    
    
    /***************************************************************************
     * 
     * @param homeGridPane
     * @param dataGridPane
     * @param strings 
     * 
     * take a list of string from strings and fill the data pane for the home
     * 
     **************************************************************************/
    private void setHomeDataGrid( LinkedList strings)
    {
        double dim1, dim2, dim3,dim4, dim5;
        Text text1, text2, text3,text4,text5; 
        RowConstraints row1, row2, row3, row4;
        ColumnConstraints col1, col2;
        
        
        //create 4 rows
        row1 = new RowConstraints(75);
        row2 = new RowConstraints(75);
        row3 = new RowConstraints(75);
        row4 = new RowConstraints(75);
        
        //add them to the data gridpane
        dataGridPane.getRowConstraints().add(row1);
        dataGridPane.getRowConstraints().add(row2);
        dataGridPane.getRowConstraints().add(row3);
        dataGridPane.getRowConstraints().add(row4);
          
        //create 2 columns
        col1 = new ColumnConstraints(200);
        col2 = new ColumnConstraints();
        //set the width of the second column
        col2.prefWidthProperty().bind(primaryStage.widthProperty().subtract(primaryStage.getWidth()*10/100));
        
        //add them to the data gridPane
        dataGridPane.getColumnConstraints().add(col1);
        dataGridPane.getColumnConstraints().add(col2);
        
        //Create the new Texts
        text1=new Text();
        text2=new Text();
        text3=new Text();
        text4=new Text();
        text5=new Text();
        
        //set the class
        text1.getStyleClass().add("homeText");
        text2.getStyleClass().add("homeText");
        text2.getStyleClass().add("homeTextBold");
        text3.getStyleClass().add("homeText");
        text4.getStyleClass().add("homeText");
        text4.getStyleClass().add("homeTextBold");
        text5.getStyleClass().add("homeText");
       
        //add the Text
        text1.setText(strings.removeFirst().toString());
        text2.setText(strings.removeFirst().toString());
        text3.setText(strings.removeFirst().toString());
        text4.setText(strings.removeFirst().toString());
        text5.setText(strings.removeFirst().toString());
        
        //add al the elements
        dataGridPane.add(text1, 0, 0,2,2);
        dataGridPane.add(text2, 0, 2);
        dataGridPane.add(text3, 1, 2);
        dataGridPane.add(text4, 0, 3);
        dataGridPane.add(text5, 1, 3);
        
        
        //add a css class
        dataGridPane.getStyleClass().add("backgroundStyle_data");
        
        //retrieve the width of the text box.
        dim1=homeGridPane.widthProperty().doubleValue();
        //I remove the two empty column 
        dim1=dim1-(dim1*10/100);
        dim2=200;
        dim3=dim1-200;
        dim4=200;
        dim5=dim1-200;
        
        //set the width
        text1.setWrappingWidth(dim1);
        text2.setWrappingWidth(dim2);
        text3.setWrappingWidth(dim3);
        text4.setWrappingWidth(dim4);
        text5.setWrappingWidth(dim5);
    }
    
    
    /**************************************************************************
     * 
     * create the grid for the analize, or inspect, grid pane
    ***************************************************************************/
    private void initializeRequirementsPane()
    {
        RowConstraints row1, row2, row3, row4, row5, row6, row7, row8;
        ColumnConstraints col1, col2;
        Pane pane1, pane2, pane3, pane4, pane5, pane6, pane7, pane8, pane9;                
        
        //create 8 new row
        row1 = new RowConstraints();
        row2 = new RowConstraints();
        row3 = new RowConstraints();
        row4 = new RowConstraints();
        row5 = new RowConstraints();
        row6 = new RowConstraints();
        row7 = new RowConstraints();
        row8 = new RowConstraints();
        //add them to the data grid pane
        dataGridPane.getRowConstraints().add(row1);
        dataGridPane.getRowConstraints().add(row2);
        dataGridPane.getRowConstraints().add(row3);
        dataGridPane.getRowConstraints().add(row4);
        dataGridPane.getRowConstraints().add(row5);
        dataGridPane.getRowConstraints().add(row6);
        dataGridPane.getRowConstraints().add(row7);
        dataGridPane.getRowConstraints().add(row8);
        //set height
        row2.setPercentHeight(10);
        row3.setMinHeight(120);
        row3.setMaxHeight(120);
        row4.setPercentHeight(10);
        row5.setPercentHeight(5);
        row6.setPercentHeight(10);
        row7.setPercentHeight(10);
        row8.setPercentHeight(15);
        row1.prefHeightProperty().bind(primaryStage.heightProperty().subtract(120).subtract(primaryStage.getHeight()*60/100));
        
        //create 2 new colmns
        col1 = new ColumnConstraints(205);
        col2 = new ColumnConstraints();
        //add them to the dataGridPane
        dataGridPane.getColumnConstraints().add(col1);
        dataGridPane.getColumnConstraints().add(col2);
        //set width
        col1.setMinWidth(205);
        col1.setMaxWidth(205);
        col2.prefWidthProperty().bind(primaryStage.widthProperty().subtract(205));
        
        //create 16 new pane
        pane1 = new Pane();
        pane2 = new Pane();
        pane3 = new Pane();
        pane4 = new Pane();
        pane5 = new Pane();
        pane6 = new Pane();
        pane7 = new Pane();
        pane8 = new Pane();
        pane9 = new Pane();
        //add them to the grid
        dataGridPane.add(pane1, 0, 0);
        dataGridPane.add(pane2, 0, 1);
        dataGridPane.add(pane3, 0, 2);
        dataGridPane.add(pane4, 0, 3);
        dataGridPane.add(pane5, 0, 4);
        dataGridPane.add(pane6, 0, 5);
        dataGridPane.add(pane7, 0, 6);
        dataGridPane.add(pane8, 0, 7);
        dataGridPane.add(pane9, 1, 0, 1, 8);
        
        //add css class
        pane1.getStyleClass().add("backgroundStyle_cityColumn");
        pane2.getStyleClass().add("backgroundStyle_cityColumn");
        pane3.getStyleClass().add("backgroundStyle_cityColumn");
        pane4.getStyleClass().add("backgroundStyle_cityColumn");
        pane5.getStyleClass().add("backgroundStyle_cityColumn");
        pane6.getStyleClass().add("backgroundStyle_cityColumn");
        pane7.getStyleClass().add("backgroundStyle_cityColumn");
        pane8.getStyleClass().add("backgroundStyle_cityColumn");    
        pane9.getStyleClass().add("backgroundStyle_responseColumn"); 
       
        //set an id for each pane
        pane2.setId("pane2");
        pane3.setId("pane3");
        pane4.setId("pane4");
        pane5.setId("pane5");
        pane6.setId("pane6");
        pane7.setId("pane7");
        pane8.setId("pane8"); 
        pane9.setId("pane9");
    }
}