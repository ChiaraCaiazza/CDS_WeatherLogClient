/**
 *
 * @author ChiaraCaiazza
 * @author GionatanGallo
 * 
 */
package weatherlogclient.connections;


import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;


public class ServerConnection 
{
    private final String PATH;
    private static HttpURLConnection connection;
    
    public ServerConnection()
    {
        PATH="http://weatherlogserver-weatherlog.rhcloud.com/rest/";        
    }
    
    
    public Document makeRequest(String req)
    {
        Document doc= null;
        
        try 
        {
            URL url = new URL(PATH+req);
            connection = (HttpURLConnection) url.openConnection();
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            doc=builder.parse( connection.getInputStream());
        } 
        
        catch (MalformedURLException ex) {
            Logger.getLogger(ServerConnection.class.getName()).log(Level.SEVERE, null, ex);
        } 
        catch (ParserConfigurationException | SAXException | IOException ex) {
            Logger.getLogger(ServerConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
      
        return doc;
    }
    
    public static void closeConnection()
    {
        connection.disconnect();
    }
}
