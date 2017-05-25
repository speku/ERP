package connector;

import java.util.Properties;
import java.util.*;

import com.sap.conn.jco.AbapException;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoField;
import com.sap.conn.jco.JCoFieldIterator;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoParameterList;
import com.sap.conn.jco.JCoStructure;
import com.sap.conn.jco.JCoTable;
import com.sap.conn.jco.ext.DestinationDataEventListener;
import com.sap.conn.jco.ext.DestinationDataProvider;


public class Connector
{
	
	private JCoDestination destination;
	
    static class MyDestinationDataProvider implements DestinationDataProvider
    {
        private DestinationDataEventListener eL;

        private Properties ABAP_AS_properties; 
        
        public Properties getDestinationProperties(String destinationName)
        {
            if(destinationName.equals("ABAP_AS") && ABAP_AS_properties!=null)
                return ABAP_AS_properties;
            
            return null;
            //alternatively throw runtime exception
            //throw new RuntimeException("Destination " + destinationName + " is not available");
        }

        public void setDestinationDataEventListener(DestinationDataEventListener eventListener)
        {
            this.eL = eventListener;
        }

        public boolean supportsEvents()
        {
            return true;
        }
        
        void changePropertiesForABAP_AS(Properties properties)
        {
            if(properties==null)
            {
                ABAP_AS_properties = null;
                eL.deleted("ABAP_AS");
            }
            else 
            {
                if(ABAP_AS_properties==null || !ABAP_AS_properties.equals(properties))
                {
	                ABAP_AS_properties = properties;
                    eL.updated("ABAP_AS");
                }
            }
        }   
    }
    
    public Connector() throws Exception
    {
    	 Properties connectProperties = new Properties();
         connectProperties.setProperty(DestinationDataProvider.JCO_ASHOST, "/H/saprouter.hcc.in.tum.de/S/3299/H/131.159.9.153");
         connectProperties.setProperty(DestinationDataProvider.JCO_SYSNR,  "19");
         connectProperties.setProperty(DestinationDataProvider.JCO_CLIENT, "904");
         connectProperties.setProperty(DestinationDataProvider.JCO_USER,   "mroeck");
         connectProperties.setProperty(DestinationDataProvider.JCO_PASSWD, "pelzbaum559");
         connectProperties.setProperty(DestinationDataProvider.JCO_LANG,   "en");

         MyDestinationDataProvider myProvider = new MyDestinationDataProvider();
         
         com.sap.conn.jco.ext.Environment.registerDestinationDataProvider(myProvider);
         myProvider.changePropertiesForABAP_AS(connectProperties);
         
         destination = JCoDestinationManager.getDestination("ABAP_AS");
    }
    
    public ArrayList<String> GetSalesAreas()
    {	ArrayList<String> areas = new ArrayList<String>();
    	try{
    		JCoFunction f = destination.getRepository().getFunction("BAPI_SALES_AREAS_GET");
    		f.execute(destination);
    		JCoTable table = f.getTableParameterList().getTable("SALES_AREAS");
    		for (JCoFieldIterator fI = table.getFieldIterator(); fI.hasNextField();)
    		{
    			areas.add(fI.nextField().getString());
    		}
    	} catch(Exception e){}
    	return areas;
    }
    
    public ArrayList<String> GetCustomers()
    {	ArrayList<String> areas = new ArrayList<String>();
    	try{
    		JCoFunction f = destination.getRepository().getFunction("BAPI_CUSTOMER_GETLIST");
    		f.execute(destination);
    		JCoTable table = f.getTableParameterList().getTable("IDRANGE");
    		for (JCoFieldIterator fI = table.getFieldIterator(); fI.hasNextField();)
    		{
    			areas.add(fI.nextField().getString());
    		}
    	} catch(Exception e){}
    	return areas;
    }
    
    
    
    
    public ArrayList<String> GetSalesOrders()
    {	ArrayList<String> areas = new ArrayList<String>();
    	try{
    		JCoFunction f = destination.getRepository().getFunction("BAPI_SALESORDER_GETLIST");
    		JCoParameterList inputs = f.getImportParameterList();
    		inputs.setValue("CUSTOMER_NUMBER", "0000001390"); // 10 characters long - pad with leading zeros
    		inputs.setValue("SALES_ORGANIZATION", "1000"); // 4 characters long - pad with leading zeros
    		f.execute(destination);
    		System.out.println(f.getExportParameterList().getStructure("RETURN").getString("TYPE"));
    		System.out.println(f.getExportParameterList().getStructure("RETURN").getString("MESSAGE"));
    		JCoTable table = f.getTableParameterList().getTable("SALES_ORDERS");
    		for (int i = 0; i < table.getNumRows(); i++)
    		{
    			table.setRow(i);
    			for (JCoFieldIterator iter = table.getFieldIterator(); iter.hasNextField();)
        		{
        			JCoField field = iter.nextField();
        			areas.add(field.getName() + " " + field .getString());;
        		}
    			areas.add("-----------------------------------------------");
    		}
    		
    	} catch(Exception e){}
    	return areas;
    }
    
    public ArrayList<String> GetBillingDocs()
    {	ArrayList<String> areas = new ArrayList<String>();
    	try{
    		JCoFunction f = destination.getRepository().getFunction("BAPI_BILLINGDOC_GETLIST");
    		JCoParameterList inputs = f.getImportParameterList();
    		JCoStructure struc = inputs.getStructure("REFDOCRANGE");
    		struc.setValue("SIGN", "I");
    		struc.setValue("REF_DOC_LOW", "0000008078");
    		struc.setValue("REF_DOC_HIGH", "0000008078");
    		struc.setValue("OPTION", "EQ");
    		f.execute(destination);
    		System.out.println(f.getExportParameterList().getStructure("RETURN").getString("TYPE"));
    		System.out.println(f.getExportParameterList().getStructure("RETURN").getString("MESSAGE"));
    		JCoTable table = f.getTableParameterList().getTable("BILLINGDOCUMENTDETAIL");
    		for (JCoFieldIterator i = table.getFieldIterator(); i.hasNextField();)
    		{
    			JCoField field = i.nextField();
    			areas.add(field.getName() + " " + field .getString());
    		}
    	} catch(Exception e){}
    	return areas;
    }
    
    
    public static void main(String[] args) throws Exception
    {
    	
    	Connector c = new Connector();
 
    	for (String s : c.GetSalesOrders())
    	{
    		System.out.println(s);
    	}
    	
//    	for (String s : c.GetBillingDocs())
//    	{
//    		System.out.println(s);
//    	}
//        c.destination.ping();
//        System.out.println("ABAP_AS destination is ok");
        
    }
    
    
}

