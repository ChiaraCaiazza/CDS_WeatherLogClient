package style;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 *
 * @author kia
 */
public class AddElement 
{
    
    /*************************************************************************
     * 
     * @param navigationPane  Pane will contain the navigation bar
     * 
     * Create a navigation bar and append it to the parameter
     * 
     */
    public void addNavigationToolbar(Pane navigationPane)
    {
        //Creates a container to hold all Menu Objects.
        VBox topContainer = new VBox();  
        //Creates our main menu to hold our Sub-Menus.
        MenuBar mainMenu = new MenuBar();  
        //Creates our tool-bar to hold the buttons.
        //ToolBar toolBar = new ToolBar(); 
        
        
        topContainer.getChildren().add(mainMenu);
        //topContainer.getChildren().add(toolBar);
        topContainer.setPrefSize(682, 25);
        navigationPane.getChildren().add(topContainer);
        
        //Create and add the "File" sub-menu options. 
        Menu file = new Menu("File");
        MenuItem openFile = new MenuItem("Open File");
        MenuItem exitApp = new MenuItem("Exit");
        file.getItems().addAll(openFile,exitApp);

        //Create and add the "Edit" sub-menu options.
        Menu edit = new Menu("Edit");
        MenuItem properties = new MenuItem("Properties");
        edit.getItems().add(properties);

        //Create and add the "Help" sub-menu options.
        Menu help = new Menu("Help");
        MenuItem visitWebsite = new MenuItem("Visit Website");
        help.getItems().add(visitWebsite);

        mainMenu.getMenus().addAll(file, edit, help);
    }
}
