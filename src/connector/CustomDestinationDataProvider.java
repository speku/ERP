package connector;

import java.util.Properties;

import com.sap.conn.jco.AbapException;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.ext.DestinationDataEventListener;
import com.sap.conn.jco.ext.DestinationDataProvider;


public class CustomDestinationDataProvider
{
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
    
    public static void main(String[] args) throws Exception
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
        
        JCoDestination ABAP_AS = JCoDestinationManager.getDestination("ABAP_AS");
        
        JCoFunction function = ABAP_AS.getRepository().getFunction("STFC_CONNECTION");
        if(function == null)
            throw new RuntimeException("BAPI_COMPANYCODE_GETLIST not found in SAP.");

        function.getImportParameterList().setValue("REQUTEXT", "Hello SAP");
        
        try
        {
            function.execute(ABAP_AS);
        }
        catch(AbapException e)
        {
            System.out.println(e.toString());
            return;
        }
        
        System.out.println("STFC_CONNECTION finished:");
        System.out.println(" Echo: " + function.getExportParameterList().getString("ECHOTEXT"));
        System.out.println(" Response: " + function.getExportParameterList().getString("RESPTEXT"));
        System.out.println();
        
        
        ABAP_AS.ping();

        System.out.println("ABAP_AS destination is ok");
        
    }
    
    
}

