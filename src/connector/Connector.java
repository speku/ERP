package connector;

import java.util.*;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.ext.DestinationDataEventListener;
import com.sap.conn.jco.ext.DestinationDataProvider;


public class Connector
{
	
	public JCoDestination destination;
	public Organization organization;
	
    static class MyDestinationDataProvider implements DestinationDataProvider
    {
        private DestinationDataEventListener eL;

        private Properties ABAP_AS_properties; 
        
        public Properties getDestinationProperties(String destinationName)
        {
            if(destinationName.equals("ABAP_AS") && ABAP_AS_properties!=null)
                return ABAP_AS_properties;
            
            return null;
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
         connectProperties.setProperty(DestinationDataProvider.JCO_USER,   "username"); // your user
         connectProperties.setProperty(DestinationDataProvider.JCO_PASSWD, "password"); // your password
         connectProperties.setProperty(DestinationDataProvider.JCO_LANG,   "en");

         MyDestinationDataProvider myProvider = new MyDestinationDataProvider();
         
         com.sap.conn.jco.ext.Environment.registerDestinationDataProvider(myProvider);
         myProvider.changePropertiesForABAP_AS(connectProperties);
         
         destination = JCoDestinationManager.getDestination("ABAP_AS");
         organization = new Organization(destination);
         
    }
    
    public Customer Customer(String customerNumber)
    {
    	return new Customer(customerNumber, this);
    }
    
    
    public static void main(String[] args) throws Exception
    {
    	// 1. instantiate Connector to setup connection details
    	Connector connector = new Connector(); 
    	// 2. instantiate Customer - invoices are retrieved automatically
        // Customer customer = connector.Customer("0000001390");
    	// 3. a list of the customer's invoices can be accessed via customer.invoices
    	// 4. invoice details can be accessed like so:
    	// Invoice invoice = customer.invoices.get(index)
    	// String item = invoice.itemName // etc.
    	connector.Customer("1390").Check(); // works
    	
    	// a few more
//    	connector.Customer("1300").Check();
//    	connector.Customer("1390").Check();
    	connector.Customer("1350").Check(); // works
    	connector.Customer("1000").Check(); // works
//    	connector.Customer("1300").Check();
//    	connector.Customer("2300").Check();
    	
    	
    	
    }
    
    
}

