package connector;

import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoParameterList;
import com.sap.conn.jco.JCoStructure;

public class Organization {

	private JCoDestination destination;
	
	public String
	organization,
	division,
	group,
	office;
	
	public Organization(JCoDestination destination)
	{
		this.destination = destination;
		GetOrganizationDetails();
	}
	
	 public void GetOrganizationDetails()
     {	
    	try{
    		JCoFunction f = destination.getRepository().getFunction("BAPI_SALESORG_GET_DETAIL");
    		f.getImportParameterList().setValue("SALESORGANIZATION", "1000");
    		f.execute(destination);
    		
    		JCoParameterList exportParameters = f.getExportParameterList();
    		JCoStructure info = exportParameters.getStructure("SDORGDATA");
    		
    		System.out.println(exportParameters.getStructure("RETURN").getString("TYPE"));
    		System.out.println(exportParameters.getStructure("RETURN").getString("MESSAGE"));
    		
    		
    		organization = info.getString("SALESORG_TEXT");
    		division = info.getString("DIVISION_TEXT");
    		group = info.getString("SALES_GRP_TEXT");
    		office = info.getString("SALES_OFF_TEXT");
    	} catch(Exception e){}
     }
	
	public String toString()
	{
		return
    			Utility.EmptyOr(organization) + 
    			Utility.EmptyOr(division) + 
    			Utility.EmptyOr(group) +
    			Utility.EmptyOr(office);		
	}
}
