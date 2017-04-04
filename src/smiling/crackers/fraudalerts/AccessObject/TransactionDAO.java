package smiling.crackers.fraudalerts.AccessObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import smiling.crackers.fraudalerts.datamodel.MasterDataDTO;
import smiling.crackers.fraudalerts.datamodel.TransactionDTO;
import smiling.crackers.fraudalerts.util.Utility;

public class TransactionDAO {
	
	public ArrayList<TransactionDTO> readTransactionCSV(String filepath)
	{
		ArrayList<TransactionDTO> transactionlist=new ArrayList<TransactionDTO>();
		try {
			BufferedReader bufread=new BufferedReader(new FileReader(filepath));
			String line;
			while((line=bufread.readLine())!=null)
			{
				String[] values=line.split(",");
				if(values.length==4)
				{
					TransactionDTO transdata=new TransactionDTO();
					transdata.setAccNo(values[1]);
					transdata.setAmount(Integer.parseInt(values[2]));
					transdata.setTxnDate(new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy").parse(values[0]));
					transdata.setCrDrFlag(values[3]);
					transactionlist.add(transdata);
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
		catch(ParseException e)
		{
			e.printStackTrace();
		}
		finally
		{
			return transactionlist;
		}
	}
	
	
	public HashMap getMonthYearRecord(ArrayList<TransactionDTO> translist,String monthyear)
	{
		HashMap output=new HashMap();
		ArrayList<TransactionDTO> translist2=new ArrayList<TransactionDTO>();
		int crcount=0,drcount=0;
		int creditamount=0;
		for(TransactionDTO transdto : translist)
		{
			if(monthyear.equals(new SimpleDateFormat("MMM").format(transdto.getTxnDate())+new SimpleDateFormat("yyyy").format(transdto.getTxnDate())))
			{
				translist2.add(transdto);
				if(transdto.getCrDrFlag().equals("D"))
					drcount+=1;
				else
				{
					crcount+=1;
					creditamount+=transdto.getAmount();
				}
				
			}
			
			
		}
		output.put("TransList", translist2);
		output.put("DrCount", drcount);
		output.put("CrCount", crcount);
		output.put("SumCreditAmnt", creditamount);
		return output;
	}
	
	public ArrayList<TransactionDTO> getCreditTxns(ArrayList<TransactionDTO> input)
	{
		ArrayList<TransactionDTO> creditlist=new ArrayList<TransactionDTO>();
		for(TransactionDTO trans : input)
			if(trans.getCrDrFlag().equals("C"))
				creditlist.add(trans);
		return creditlist;
	}

}
