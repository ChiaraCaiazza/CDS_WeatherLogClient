/******************************************************************************
 *
 * @author ChiaraCaiazza
 * @author GionatanGallo
 * 
 *****************************************************************************/
package weatherlogclient.connections;


public class SetRequest 
{
    /***************************************************************************
     * 
     * @param command   type of request performed [actual(a), hourly(h)
     *                                        daily (d), weekly(w), monthly(m)]
     * @param who       list of requested city [city1,city2,....,cityn]
     * @return          sub-path of weatherlog server address
     * 
     * return the final part of the path needed to start the connection with 
     * WeatherLogServer
     * 
     */
    public String constructPath(String command, String who)
    {
        return "weather/analysis?name="+who+"&type="+command;
       
    }
    
}
