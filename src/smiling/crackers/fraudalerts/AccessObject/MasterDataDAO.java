package smiling.crackers.fraudalerts.AccessObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import smiling.crackers.fraudalerts.datamodel.MasterDataDTO;
import smiling.crackers.fraudalerts.util.Utility;


public class MasterDataDAO {
	
	public ArrayList<MasterDataDTO> readMasterCSV(String filepath)
	{
		ArrayList<MasterDataDTO> masterdtolist=new ArrayList<MasterDataDTO>();
		try {
			BufferedReader bufread=new BufferedReader(new FileReader(filepath));
			String line;
			while((line=bufread.readLine())!=null)
			{
				String[] values=line.split(",");
				if(values.length==5)
				{
					MasterDataDTO mastdata=new MasterDataDTO();

					mastdata.setAccNo(values[0]);
					mastdata.setCustId(values[1]);
					mastdata.setCustName(values[2]);
					mastdata.setSal((values[3]!=null?Integer.parseInt(values[3]):0));
					mastdata.setRiskFreq((values[4]==null?Integer.parseInt(values[4]):0));

					ArrayList configparams=new ArrayList();
					configparams.add("SALCATEGORY1");
					configparams.add("SALCATEGORY2");
					configparams.add("SALCATEGORY3");
			

					
					HashMap parameters=Utility.GetProperties("config.properties", configparams);
					if(mastdata.getSal()<Integer.parseInt(parameters.get("SALCATEGORY1").toString()))
						mastdata.setCategory(1);
										
					else if (mastdata.getSal()<Integer.parseInt(parameters.get("SALCATEGORY2").toString()))
						mastdata.setCategory(2);
					else
						mastdata.setCategory(3);
					masterdtolist.add(mastdata);

				}
				else
					System.out.println("The line \""+line+"\"skipped since it doesnt have enough parameters.");
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			return masterdtolist;
		}
	}
	
	public ArrayList<MasterDataDTO> filterbycategory(ArrayList<MasterDataDTO> masterdtolist,int category)
	{
		ArrayList<MasterDataDTO> filteredlist=new ArrayList<MasterDataDTO>();
		for(MasterDataDTO temp : masterdtolist)
		{
			if(temp.getCategory()==category)
				filteredlist.add(temp);
		}
		System.out.println(filteredlist);
		return filteredlist;
	}
	
	public MasterDataDTO findMasterByAccNo(String AccNo,String filePath)
	{
		ArrayList<MasterDataDTO> MasterList=readMasterCSV(filePath);
		for(MasterDataDTO master : MasterList)
		{
			if(master.getAccNo().equals(AccNo))
				return master;
		}
		System.out.println("No Master Data found for AccNo "+AccNo);
		return null;
	}

}
