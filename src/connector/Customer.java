package connector;

import java.util.ArrayList;
import java.util.HashMap;

import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoParameterList;
import com.sap.conn.jco.JCoStructure;
import com.sap.conn.jco.JCoTable;

public class Customer {
	
	public String number;
	public ArrayList<Invoice> invoices;
	
	private Connector connector;
	public Organization organization;
	
	public String
	firstName,
	lastName,
	city,
	district,
	postBox,
	postalCode,
	countryCode,
	cityCode,
	street,
	telephoneNumber,
	postBoxPostalCode;
	
	public Customer(String number, Connector connector)
	{
		this.number = number;
		this.connector = connector;
		this.organization = connector.organization;
		GetCustomerDetails();
		invoices = Invoices(number);
		
		// check retrieved data
		for (Invoice invoice : invoices)
		{
			System.out.println(invoice.toString());
			System.out.println("------------------------------");
		}
	}
	
	private ArrayList<Invoice> Invoices(String customerNumber)
    {
    	customerNumber = Utility.Pad(customerNumber,10,'0');
    	ArrayList<HashMap<String,String>> salesOrders = GetSalesOrders(customerNumber);
    	ArrayList<Invoice> result = new ArrayList<Invoice>();
    	for (HashMap<String,String> salesOrder : salesOrders)
    	{
    		result.add(new Invoice(this, salesOrder));
    	}
    	return result;
    }

	 public ArrayList<HashMap<String,String>> GetOrderDetails(String invoiceNumber)
     {	
    	try{
    		invoiceNumber = Utility.Pad(invoiceNumber, 10, '0');
    		JCoFunction f = connector.destination.getRepository().getFunction("BAPI_BILLINGDOC_GETLIST");
    		JCoParameterList inputs = f.getImportParameterList();
    		JCoStructure struc = inputs.getStructure("REFDOCRANGE");
    		struc.setValue("SIGN", "I");
    		struc.setValue("REF_DOC_LOW", invoiceNumber);
    		struc.setValue("REF_DOC_HIGH", invoiceNumber);
    		struc.setValue("OPTION", "EQ");
    		f.execute(connector.destination);
    		System.out.println(f.getExportParameterList().getStructure("RETURN").getString("TYPE"));
    		System.out.println(f.getExportParameterList().getStructure("RETURN").getString("MESSAGE"));
    		JCoTable table = f.getTableParameterList().getTable("BILLINGDOCUMENTDETAIL");
    		return Utility.Table(table);
    	} catch(Exception e){}
    	return null;
     }
	 
	 public void GetCustomerDetails()
     {	
    	try{
    		JCoFunction f = connector.destination.getRepository().getFunction("BAPI_CUSTOMER_GETDETAIL2");
    		f.getImportParameterList().setValue("CUSTOMERNO", number);
    		f.execute(connector.destination);
    		
    		JCoParameterList exportParameters = f.getExportParameterList();
    		JCoStructure info = exportParameters.getStructure("CUSTOMERADDRESS");
    		
    		System.out.println(exportParameters.getStructure("RETURN").getString("TYPE"));
    		System.out.println(exportParameters.getStructure("RETURN").getString("MESSAGE"));
    		
    		
    		firstName = info.getString("NAME");
    		lastName = info.getString("NAME_2");
    		city = info.getString("CITY");
    		district = info.getString("DISTRICT");
    		postBox = info.getString("PO_BOX");
    		postBoxPostalCode = info.getString("POBX_PCD");
    		postalCode = info.getString("POSTL_CODE");
    		countryCode = info.getString("COUNTYCODE"); 	
    		cityCode = info.getString("CITY_CODE");
    		street = info.getString("STREET");
    		telephoneNumber = info.getString("TELEPHONE");
    	} catch(Exception e){}
     }
	 
	    private ArrayList<HashMap<String,String>> GetSalesOrders(String customerNumber)
	    {
	    	
	    	try{
	    		JCoFunction f = connector.destination.getRepository().getFunction("BAPI_SALESORDER_GETLIST");
	    		JCoParameterList inputs = f.getImportParameterList();
	    		inputs.setValue("CUSTOMER_NUMBER", customerNumber); // 10 characters long - pad with leading zeros
	    		inputs.setValue("SALES_ORGANIZATION", "1000"); // 4 characters long - pad with leading zeros
	    		f.execute(connector.destination);
	    		System.out.println(f.getExportParameterList().getStructure("RETURN").getString("TYPE"));
	    		System.out.println(f.getExportParameterList().getStructure("RETURN").getString("MESSAGE"));
	    		JCoTable table = f.getTableParameterList().getTable("SALES_ORDERS");
	    		return Utility.Table(table);
	    		
	    	} catch(Exception e){}
	    	return null;
	    }
	    
	    public String toString()
	    {
	    	return
	    			Utility.EmptyOr(firstName) + 
	    			Utility.EmptyOr(lastName) + 
	    			Utility.EmptyOr(street) +
	    			Utility.EmptyOr(city);		
	    }
	
}
