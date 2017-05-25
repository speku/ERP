package connector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import com.sap.conn.jco.JCoField;
import com.sap.conn.jco.JCoFieldIterator;
import com.sap.conn.jco.JCoTable;

public class Utility {

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
    
    static String EmptyOr(String input)
    {
    	return input.equals("") ? "" : input + "\n";
    }
}
