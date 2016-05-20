package weatherlogclient.connections;
/**
 *
 * @author ChiaraCaiazza
 * @author GionatanGallo
 * 
 */

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;


public class ServerConnection 
{
    private final String PATH;
    
    public ServerConnection()
    {
        PATH="http://weatherlogserver-weatherlog.rhcloud.com/rest/";
    }
    
    
    public Document makeRequest(String req)
    {
        Document doc;
        doc = null;
        try 
        {
            URL url = new URL(PATH+req);
            System.out.println("URL         :"+url);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            
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
    
    
    /**************************************************************************
     * 
     * @param doc XML document
     * 
     * print the param xmldocument
     * 
     *************************************************************************/
    public void printDoc(Document doc)
    {
        try 
        {
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            
            transformer.transform(new DOMSource(doc),
                    new StreamResult(new OutputStreamWriter(System.out, "UTF-8")));
        } 
        catch (Exception e) {}
    }
}
