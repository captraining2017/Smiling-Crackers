package smiling.crackers.fraudalerts.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import smiling.crackers.fraudalerts.datamodel.*;


public class Utility {
	
	public static HashMap<String,Object> GetProperties(String filepath,ArrayList<String> keys)
	{
		HashMap<String,Object> propmap=new HashMap<String,Object>();
		Properties props=new Properties();
		try {
			props.load(new FileInputStream(new File(filepath)));
			for(String key : keys)
			{

				if(!props.getProperty(key).contains(":") || props.getProperty(key).contains(":\\"))
					propmap.put(key, props.getProperty(key));
				else
				{
					HashMap<String,String> data=new HashMap<String,String>();

					String[] values=props.getProperty(key).split(";");
					for(String value : values)
						data.put(value.split(":")[0], value.split(":")[1]);
					propmap.put(key, data);
				}
				
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return propmap;
		
	}
	
	
	
	


}
