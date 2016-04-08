package initialize;


import java.io.File;
import java.util.LinkedList;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author ChiaraC
 * @author GionatanG
 * 
 */
public class InitializeElements 
{
    private MenuItem contactUs, an ,insp, backToHome;
    GridPane dataGridPane, homeGridPane;
    
    /**************************************************************************
     * 
     * @param homeGridPane 
     * 
     * add to homeGridPane 3 row and 3 column
     * 
    ***************************************************************************/
    public void setGrid(GridPane homeGridPane)
    {
        RowConstraints row1, row2, row3;
        ColumnConstraints col1, col2, col3;
        
        //create 3 Row: nav.bar, title and data.
        row1 = new RowConstraints(28);
        row2 = new RowConstraints(120);
        row3 = new RowConstraints();
        //add them
        homeGridPane.getRowConstraints().add(row1);
        homeGridPane.getRowConstraints().add(row2);
        homeGridPane.getRowConstraints().add(row3);
        row3.setPercentHeight(60);
          
        //create 3 column 
        col1 = new ColumnConstraints();
        col2 = new ColumnConstraints();
        col3 = new ColumnConstraints();
        //add them
        homeGridPane.getColumnConstraints().add(col1);
        homeGridPane.getColumnConstraints().add(col2);
        homeGridPane.getColumnConstraints().add(col3);
        //set percentage
        col1.setPercentWidth(5);
        col2.setPercentWidth(90);
        col3.setPercentWidth(5);
    }
   
    
    /**************************************************************************
     * 
     * @param stage
     * @param homeGridPane
     * @param navigationPane
     * @param titlePane
     * @param dataPane 
     * 
     * Initialize all the elements of the home window
     * 
     */
    public void initializeStartingPane(Stage stage,GridPane gridPane, 
                         Pane navigationPane, Pane titlePane, GridPane dataPane)
    {
        LinkedList<String> strings;
        Image img;
        ImageView  title;
        
        homeGridPane=gridPane;
        
        
        /*******************WORKING ON THE NAVIGATION PANE *******************/
        //add the navigation bar to the navigationPane
        addNavigationToolbar(navigationPane);
        //add the navigation Pane to the GridPane
        homeGridPane.add(navigationPane,0,0,3,1);
        
        
        /**********************WORKING ON THE TITLE PANE **********************/
        //create an image
        img = new Image ("images/title.png");
        //create an imageview
        title=new ImageView();
        //add the image
        title.setImage(img);
        title.setFitWidth(600);
        title.translateXProperty()
            .bind(stage.widthProperty().subtract(title.getFitWidth())
                    .divide(2));
        System.out.println(title.getFitWidth());
        title.setStyle("-fx-background-color: black;");
        //add the ImageView
        titlePane.getChildren().add(title);
        //add the pane to the GridPane
        homeGridPane.add(titlePane,0,1,3,1);
        
        /**********************WORKING ON THE DATA PANE **********************/
        //add the gridpane
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
    
    
    /***************************************************************************
     * 
     * @param homeGridPane
     * @param dataGridPane
     * @param strings 
     * 
     * take a list of string from strings and fill the data pane for the home
     * 
     */
    private void setHomeDataGrid( LinkedList strings)
    {
        double dim1, dim2, dim3,dim4, dim5;
        Text text1, text2, text3,text4,text5; 
        RowConstraints row1, row2, row3;
        ColumnConstraints col1, col2;
        
        
        //create 3 rows
        row1 = new RowConstraints(150);
        row2 = new RowConstraints(70);
        row3 = new RowConstraints(70);
        
        //add them to the data gridpane
        dataGridPane.getRowConstraints().add(row1);
        dataGridPane.getRowConstraints().add(row2);
        dataGridPane.getRowConstraints().add(row3);
          
        //create 2 columns
        col1 = new ColumnConstraints(200);
        col2 = new ColumnConstraints();
        
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
        dataGridPane.add(text1, 0, 0,2,1);
        dataGridPane.add(text2, 0, 1);
        dataGridPane.add(text3, 1, 1);
        dataGridPane.add(text4, 0, 2);
        dataGridPane.add(text5, 1, 2);
        
        dataGridPane.setGridLinesVisible(true);
        
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
     * Implement all the beaviour associate to the menù option
     * 
    **************************************************************************/
    private void addMenuBehaviour()
    {
        contactUs.setOnAction((ActionEvent e) -> 
        {
            String s;
            
            s="\n\n\nWeatherLog is a project of Chiara Caiazza and"
               + " Gionatan Gallo.\nFor any inconvenient or requirements "
               + "write to us at admin@weatherlog.com";
            openPopup("Contact us!",s);
        });
        
        
        an.setOnAction((ActionEvent e) -> 
        {
            initializeRequirements(true);
        });
        
        
        insp.setOnAction((ActionEvent e) -> 
        {
            initializeRequirements(true);
        });
        
        backToHome.setOnAction((ActionEvent e) -> 
        {
            LinkedList<String> strings;
            GridPane dataPane;
            
            /*strings=new LinkedList();
            strings.add("Welcome back to WeatherLog, you can restart from where you left off!\n\nChoose one of your possibility:");
            strings.add("-Inspect:");
            strings.add("see past wheather information about one or more than one city.");
            strings.add("-Analisys:");
            strings.add("Analize past data.");
            
            setHomeDataGrid(strings);
            homeGridPane.add(dataGridPane, 1, 2);*/
            Label l =new Label();
            l.setText("cscdc");
            homeGridPane.add(l, 1, 2);
        });
                

    }
    
    
    /**************************************************************************
     * 
     * @param title
     * @param popupText
     * 
     * create and initialize a popup using the given parameter
     * 
     */
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
        popupFlowPane.setId("backgroundStyle");
            
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
    
    
    /*************************************************************************
     * 
     * @param navigationPane  Pane will contain the navigation bar
     * 
     * Create a navigation bar with some predetermined menù and append them
     * to the NavigationPane 
     * 
     */
    private void addNavigationToolbar(Pane navigationPane)
    {
        MenuBar mainMenu;
        Menu home, help, exit, analisys, inspect;
        
        //Creates our main menu to hold our Sub-Menus.
        mainMenu = new MenuBar();
        
        //create the menù
        home = new Menu ("Home");
        inspect=new Menu("Inspect");
        analisys=new Menu("Analisys");
        help=new Menu("Help");
        exit=new Menu("Exit");
        //add them to the mainMenu
        mainMenu.getMenus().addAll(home, inspect, analisys, help,exit);
        
        //create the submenù options
        contactUs=new MenuItem("Contact us!");
        an=new MenuItem("Make analize request");
        insp=new MenuItem("Make inspect request");
        backToHome = new MenuItem("ComeBackTohome");
        //add them
        help.getItems().add(contactUs);
        analisys.getItems().add(an);
        inspect.getItems().add(insp);
        home.getItems().add(backToHome);
        
        //add the container to the navigation Pane
        navigationPane.getChildren().add(mainMenu);
        addMenuBehaviour();
    }
      
    private void initializeRequirements(boolean analisys)
    {
        dataGridPane.getChildren().clear();
    }
}
