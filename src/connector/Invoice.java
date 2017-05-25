package connector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public class Invoice {

	public Customer customer;
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
	
	
	public Invoice(Customer customer, HashMap<String,String> details)
	{
		this.details = details;
		this.customer = customer;
		this.customerNumber = customer.number;
		
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
		
		ArrayList<HashMap<String,String>> orderDetails = customer.GetOrderDetails(invoiceNumber);
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
				"Rechnungsnummer:\t" + invoiceNumber + "\n" + 
    			"Kundennummer:\t\t" + customerNumber + "\n" + 
    			"Kunde:\t\t\t" + customerName +"\n" + 
    			"Artikelnummer:\t\t" + itemNumber + "\n" + 
    			"Artikelbeschreibung:\t" + itemName + "\n" + 
    			"Angeforderte Menge:\t" + requiredQuantity + "\n" + 
    			"Gelieferte Menge:\t" + deliveredQuantity + "\n" + 
    			"Rechnungsdatum:\t\t" + invoiceDate + "\n" + 
    			"Faelligkeitsdatum:\t" + requiredDate + "\n" + 
    			"Stueckpreis Netto:\t" + netPrice + "\n" + 
    			"Stueckpreis Brutto:\t" + taxPrice + "\n" + 
    			"Waehrung:\t\t" + currency + "\n";
	}
	
}
