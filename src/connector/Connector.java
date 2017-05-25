package connector;

import java.util.*;
import java.util.Map.Entry;

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
import com.sun.org.apache.xml.internal.security.keys.content.KeyValue;


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
    
    
    static String Pad(String string, int finalLength, char padding)
    {
    	char[] result = new char[finalLength];
    	char[] input = string.toCharArray();
    	int missing = finalLength - input.length;
    	for (int i = 0; i < finalLength; i++)
    	{
    		if (i < missing)
    		{
    			result[i] = padding;
    		} else {
    			result[i] = input[i - missing];
    		}
    	}
    	return new String(result);
    }
    
    
    
    // 0000001390
    public ArrayList<HashMap<String,String>> GetSalesOrders(String customerNumber)
    {
    	
    	try{
    		JCoFunction f = destination.getRepository().getFunction("BAPI_SALESORDER_GETLIST");
    		JCoParameterList inputs = f.getImportParameterList();
    		inputs.setValue("CUSTOMER_NUMBER", customerNumber); // 10 characters long - pad with leading zeros
    		inputs.setValue("SALES_ORGANIZATION", "1000"); // 4 characters long - pad with leading zeros
    		f.execute(destination);
    		System.out.println(f.getExportParameterList().getStructure("RETURN").getString("TYPE"));
    		System.out.println(f.getExportParameterList().getStructure("RETURN").getString("MESSAGE"));
    		JCoTable table = f.getTableParameterList().getTable("SALES_ORDERS");
    		return Table(table);
    		
    	} catch(Exception e){}
    	return null;
    }
    
    
    static ArrayList<HashMap<String, String>> Table(JCoTable table)
    {
    	ArrayList<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>();
    	for (int i = 0; i < table.getNumRows(); i++)
		{
    		result.add(Row(table,i));
		}
    	return result;
    }
    
    static HashMap<String, String> Row(JCoTable table, int row)
    {
    	HashMap<String, String> result = new HashMap<String, String>();
    	table.setRow(row);
    	for (JCoFieldIterator iter = table.getFieldIterator(); iter.hasNextField();)
		{
			JCoField field = iter.nextField();
			result.put(field.getName(), field .getString());
		}
    	return result;
    }
    
    public ArrayList<Invoice> Invoices(String customerNumber)
    {
    	customerNumber = Pad(customerNumber,10,'0');
    	ArrayList<HashMap<String,String>> salesOrders = GetSalesOrders(customerNumber);
    	ArrayList<Invoice> result = new ArrayList<Invoice>();
    	for (HashMap<String,String> salesOrder : salesOrders)
    	{
    		result.add(new Invoice(customerNumber, salesOrder));
    	}
    	return result;
    }
    
    class Invoice
    {
    	
    	public Boolean valid = false; // wenn keine Rechnungsinfos gefunden werden konnten: valid = false
    	
    	public String
    	invoiceNumber, 
    	customerNumber,
    	customerName, 
    	itemNumber, // Artikelnummer
    	itemName, // Artikelbeschreibung
    	requiredQuantity, // angeforderte Menge?
    	deliveredQuantity, // ausgelieferte Menge
    	invoiceDate, // Rechnungsdatum
    	requiredDate, // Faelligkeitsdatum?
    	netPrice, // netto
    	taxPrice, // brutto
    	currency; // Waehrung
    	
    	public HashMap<String,String> details;
    	
    	
    	public Invoice(String customerNumber, HashMap<String,String> details)
    	{
    		this.details = details;
    		this.customerNumber = customerNumber;
    		
    		if (details == null)
    		{
    			return;
    		} else {
    			valid = true;
    		}
    		
    		for (Entry<String, String> entry : details.entrySet())
    		{
    			String v = entry.getValue();
    			switch(entry.getKey())
    			{
	    			case "SD_DOC":
	    				invoiceNumber = v;
	    				break;
	    			case "ITM_NUMBER":
	    				itemNumber = v;
	    				break;
	    			case "SHORT_TEXT":
	    				itemName = v;
	    				break;
	    			case "DOC_DATE":
	    				invoiceDate = v;
	    				break;
	    			case "REQ_QTY":
	    				requiredQuantity = v;
	    				break;
	    			case "REQ_DATE":
	    				requiredDate = v;
	    				break;
	    			case "NAME":
	    				customerName = v;
	    				break;
	    			case "DLV_QTY":
	    				deliveredQuantity = v;
	    				break;
	    			case "NET_PRICE":
	    				netPrice = v;
	    				break;
	    			case "CURRENCY":
	    				currency = v;
	    				break;
    			}
    		}
    		
    		ArrayList<HashMap<String,String>> orderDetails = GetOrderDetails(invoiceNumber);
    		if (orderDetails != null)
    		{
    			for (HashMap<String,String> detail : orderDetails)
    			{
    				String val = detail.get("TAX_VALUE");
    				if (val != null)
    				{
    					taxPrice = val;
    					break;
    				}
    			}
    		}	
    	}
    	
    	public String toString(){
    		return 
    				"";
    	}
    }
    
    public ArrayList<HashMap<String,String>> GetOrderDetails(String invoiceNumber)
    {	
    	try{
    		invoiceNumber = Pad(invoiceNumber, 10, '0');
    		JCoFunction f = destination.getRepository().getFunction("BAPI_BILLINGDOC_GETLIST");
    		JCoParameterList inputs = f.getImportParameterList();
    		JCoStructure struc = inputs.getStructure("REFDOCRANGE");
    		struc.setValue("SIGN", "I");
    		struc.setValue("REF_DOC_LOW", invoiceNumber);
    		struc.setValue("REF_DOC_HIGH", invoiceNumber);
    		struc.setValue("OPTION", "EQ");
    		f.execute(destination);
    		System.out.println(f.getExportParameterList().getStructure("RETURN").getString("TYPE"));
    		System.out.println(f.getExportParameterList().getStructure("RETURN").getString("MESSAGE"));
    		JCoTable table = f.getTableParameterList().getTable("BILLINGDOCUMENTDETAIL");
    		return Table(table);
    	} catch(Exception e){}
    	return null;
    }
    
    static void PrintStuff(ArrayList<HashMap<String,String>> stuff)
    {
    	for (HashMap<String,String> map : stuff)
    	{
    		for (Entry<String,String> entry : map.entrySet())
    		{
    			System.out.println(entry.getKey() + " " + entry.getValue());
    		}
    		System.out.println("----------------------------------------------------");
    	}
    }
    
    
    public static void main(String[] args) throws Exception
    {
    	
    	
    	Connector c = new Connector();
    	PrintStuff(c.GetOrderDetails("0000008078"));
// 
//    	for (String s : c.GetSalesOrders())
//    	{
//    		System.out.println(s);
//    	}
    	
//    	for (String s : c.GetBillingDocs())
//    	{
//    		System.out.println(s);
//    	}
//        c.destination.ping();
//        System.out.println("ABAP_AS destination is ok");
        
    }
    
    
}

