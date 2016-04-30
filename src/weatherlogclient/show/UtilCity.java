package weatherlogclient.show;
/**
 *
 * @author ChiaraCaiazza
 * @author GionatanGallo
 * 
 */


import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;



public class UtilCity
{
    public UtilCity(){}
    
    
    /***************************************************************************
     * 
     * @param s  suppose "value_unit"
     * @return "value unit"
     * 
     * (replace "_" with " ")
     *
     **************************************************************************/
    protected String removeUnderscore(String s)
    {
        String result;
        int index=s.indexOf("_");
        
        if (index==-1)
                return s;
        
        result=s.substring(0,index);
        result+=" ";
        result+=s.substring(index+1);
        return result;
    }    
    
    
    /**************************************************************************
     * 
     * @param allCityMeasurements
     * @param propertyList
     * @param unitList
     * 
     * Inspect the data structure and retrieve the list of the measurements and 
     * the correspondent unit stored
     * 
     ***************************************************************************/
   protected void retrievePropertyList(LinkedList<LinkedList> allCityMeasurements, 
                    LinkedList<String> propertyList, LinkedList<String>unitList)
    {
        LinkedList<HashMap> measurements;
        HashMap<String,String> measure;
                
       //for each city
          for( int i=0; i<allCityMeasurements.size();i++)
        {   
            //list of measure of that city
            measurements=allCityMeasurements.get(i);
            
           //for each measure
            for (int j=0;j<measurements.size();j++)
            {
                measure=measurements.get(j);

                //for each element into the HashMap temp
                for (Map.Entry entry : measure.entrySet()) 
                {
                    //if the entry is not a cityID, a city name or a date then 
                    //I add the relative entry to the list
                    if (!"cityID".equals(entry.getKey().toString())
                        && !"date".equals(entry.getKey().toString())
                        && !"cityName".equals(entry.getKey().toString()))
                    {
                        //check if this property is already present
                        int index=propertyList.indexOf(entry.getKey());
                        //if it is not present
                        if (index==-1)
                        {
                            //add the property name
                            propertyList.addLast(entry.getKey().toString());
                            //and its unit
                            if ("sunset".equals(entry.getKey().toString())    ||
                               "sunrise".equals(entry.getKey().toString()))
                            {
                                //if it is a sunset or sunrise measurements its 
                                //unit value is time
                                unitList.addLast("Time");
                            }
                            else
                            {
                                //otherwise I take its unit from the data structure
                                unitList.addLast(retrieveAfter
                                            (entry.getValue().toString(), "_")); 
                            }
                        }
                    }
                }
            }
        }
    }
    
    
    /***************************************************************************
     * 
     * @param base
     * @param what
     * @param s suppose "abcdeFghi"
     * @return  "Abcdefghi"
     * 
     * Take a string and return the same one. The first character is turn to 
     * uppercase, the others are turned to lowercase.
     * 
     *************************************************************************/
    protected String onlyFirstUp(String s)
    {
        String result;
        
        result=s.substring(0, 1).toUpperCase();
        result+=s.substring(1).toLowerCase();
        return result;
    }
    
    
    /*************************************************************************
     * 
     * @param base suppose "string1_string2"
     * @param what "_"
     * @return 
     * 
     * return what comes before who (in this case string1)
     * 
     *************************************************************************/
    public String retrieveBefore (String base, String what)
    {
        String result;
        int index;
        
        result=base;
        index=result.indexOf(what);
        if (index==-1)
            return result;
        
        result=result.substring(0, index);        
        return result;
    }
    
    
    /*************************************************************************
     * 
     * @param base suppose "string1_string2"
     * @param what "_"
     * @return 
     * 
     * return what comes after who (in this case string2)
     * 
     *************************************************************************/
    public String retrieveAfter (String base, String what)
    {
        String result;
        int index;
        
        result=base;
        index=result.indexOf(what);
        if (index==-1)
            return result;
        
        result=result.substring(index+1);        
        return result;
    }
    
    
}
