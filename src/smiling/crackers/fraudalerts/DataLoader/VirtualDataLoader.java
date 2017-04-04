package smiling.crackers.fraudalerts.DataLoader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Properties;

import smiling.crackers.fraudalerts.AccessObject.MasterDataDAO;
import smiling.crackers.fraudalerts.datamodel.MasterDataDTO;
import smiling.crackers.fraudalerts.util.FileUtil;
import smiling.crackers.fraudalerts.util.Utility;

public class VirtualDataLoader {
	
	private static String DSFilePath;
	private static ArrayList<String> propkey;
	
	public static void main(String args[])
	{
		MasterDataDAO masdao=new MasterDataDAO();
		propkey=new ArrayList<String>();
		propkey.add("DSFILEPATH");
		propkey.add("MSFILEPATH");
		propkey.add("CAT1DSPERCENT1");
		propkey.add("CAT1DSPERCENT2");
		propkey.add("CAT1DSPERCENT3");
		propkey.add("TOTALVOLUME");
		propkey.add("STARTDATE");
		propkey.add("ENDDATE");
		propkey.add("ACCPASSBOKPATH");



		HashMap<String,Object> propmap=Utility.GetProperties("config.properties", propkey);
		ArrayList<MasterDataDTO> mastlist=masdao.readMasterCSV(propmap.get("MSFILEPATH").toString());
		VirtualDataLoaderThread thread1 = null;
		VirtualDataLoaderThread thread2 = null;
		VirtualDataLoaderThread thread3 = null;
		FileUtil filewrite = new FileUtil();
	

		try {
			thread1 = new VirtualDataLoaderThread(new SimpleDateFormat("dd/MM/yyyy").parse(propmap.get("STARTDATE").toString()), new SimpleDateFormat("dd/MM/yyyy").parse(propmap.get("ENDDATE").toString()), mastlist, (Integer.parseInt(propmap.get("TOTALVOLUME").toString())*Integer.parseInt(propmap.get("CAT1DSPERCENT1").toString()))/100,propmap.get("DSFILEPATH").toString(),"MIN",filewrite,propmap.get("ACCPASSBOKPATH").toString());
			thread2 = new VirtualDataLoaderThread(new SimpleDateFormat("dd/MM/yyyy").parse(propmap.get("STARTDATE").toString()), new SimpleDateFormat("dd/MM/yyyy").parse(propmap.get("ENDDATE").toString()), mastlist, (Integer.parseInt(propmap.get("TOTALVOLUME").toString())*Integer.parseInt(propmap.get("CAT1DSPERCENT2").toString()))/100,propmap.get("DSFILEPATH").toString(),"MID",filewrite,propmap.get("ACCPASSBOKPATH").toString());
			thread3 = new VirtualDataLoaderThread(new SimpleDateFormat("dd/MM/yyyy").parse(propmap.get("STARTDATE").toString()), new SimpleDateFormat("dd/MM/yyyy").parse(propmap.get("ENDDATE").toString()), mastlist, (Integer.parseInt(propmap.get("TOTALVOLUME").toString())*Integer.parseInt(propmap.get("CAT1DSPERCENT3").toString()))/100,propmap.get("DSFILEPATH").toString(),"MAX",filewrite,propmap.get("ACCPASSBOKPATH").toString());


		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		thread1.start();
		thread2.start();
		thread3.start();

		
		
		
		
	}
	


}
