package smiling.crackers.fraudalerts.FraudSurvilance;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import smiling.crackers.fraudalerts.AccessObject.MasterDataDAO;
import smiling.crackers.fraudalerts.AccessObject.TransactionDAO;
import smiling.crackers.fraudalerts.datamodel.MasterDataDTO;
import smiling.crackers.fraudalerts.datamodel.TransactionDTO;
import smiling.crackers.fraudalerts.util.FileUtil;
import smiling.crackers.fraudalerts.util.Utility;

public class FraudSurvilanceThread extends Thread{
	
	private HashMap propmap;
	private HashMap conditionmap;
	private FileReader fread;
	private File PassBookFilePath;
	private HashMap<String,Long> ProcessedList;
	private TransactionDAO transdao;
	private MasterDataDAO mastdao;
	private FileUtil filewrite;
	private String threadname;
	
	public FraudSurvilanceThread (String threadname)
	{
		ArrayList<String> configparams=new ArrayList<String>();
		configparams.add("CONDITIONPARAM");
		configparams.add("ACCPASSBOKPATH");
		configparams.add("MSFILEPATH");
		configparams.add("RISKACCOLOGPATH");
		configparams.add("RISKTXNLOGPATH");
		configparams.add("NORMALACCPATH");
		configparams.add("NORMALTXNPATH");
		propmap=Utility.GetProperties("config.properties", configparams);
		conditionmap=(HashMap)propmap.get("CONDITIONPARAM");
		PassBookFilePath=new File((String)propmap.get("ACCPASSBOKPATH"));
		ProcessedList=new HashMap<String,Long>();
		transdao=new TransactionDAO();
		mastdao=new MasterDataDAO();
		filewrite=new FileUtil();
		this.threadname=threadname;
		System.out.println(threadname+" is started....");
		
	}
	

	
	public void run()
	{
		while(true)
		{
			File[] filelist=PassBookFilePath.listFiles();
			for(File file : filelist)
			{
				ArrayList<TransactionDTO> translist=new ArrayList<TransactionDTO>();
				if(ProcessedList.get(file.getName())!=null && ProcessedList.get(file.getName())==file.lastModified())
				{
					System.out.println("Scanning for a new transaction...");
				}
				else
				{
					//fread=new FileReader(file);
					translist=transdao.readTransactionCSV(file.getAbsolutePath());
					ArrayList<String> ProcessedMonthYear=new ArrayList<String>();
					for(TransactionDTO transdto : translist)
					{
						String MonthYear=new SimpleDateFormat("MMM").format(transdto.getTxnDate())+new SimpleDateFormat("yyyy").format(transdto.getTxnDate());
						
						if(!ProcessedMonthYear.contains(MonthYear))
						{
							MasterDataDTO masterdata=mastdao.findMasterByAccNo(transdto.getAccNo(), propmap.get("MSFILEPATH").toString());
							HashMap monthyearreportmap=transdao.getMonthYearRecord(translist, MonthYear);
							ArrayList<TransactionDTO> monthyearlist=(ArrayList<TransactionDTO>)monthyearreportmap.get("TransList");
							int DrCount=Integer.parseInt(monthyearreportmap.get("DrCount").toString());
							int CrCount=Integer.parseInt(monthyearreportmap.get("CrCount").toString());
							int SumCreditAmount=Integer.parseInt(monthyearreportmap.get("SumCreditAmnt").toString());
							for(Object key : conditionmap.keySet())
							{
								String conditionstring=(String)conditionmap.get(key);
								String[] params=conditionstring.split(",");
								long datecondtion;
								long transactiondate;
								String riskacclog="";
								String normalacclog="";
								
								
								try {
									datecondtion = new SimpleDateFormat("dd/MM/yyyy").parse(params[0]).getTime();
									transactiondate=new SimpleDateFormat("dd/MM/yyyy").parse(new SimpleDateFormat("dd/MM/yyyy").format(transdto.getTxnDate())).getTime(); 
									if(params[1].equals("B") && transactiondate<=datecondtion)
									{
										
										if(DrCount>Integer.parseInt(masterdata.getCategory()==1?params[3]:masterdata.getCategory()==2?params[4].toString():params[5].toString()) && CrCount>Integer.parseInt(masterdata.getCategory()==1?params[5]:masterdata.getCategory()==2?params[6].toString():params[7].toString()))
											riskacclog=MonthYear+","+masterdata.getAccNo()+","+DrCount+","+CrCount+","+(DrCount-Integer.parseInt(masterdata.getCategory()==1?params[3]:masterdata.getCategory()==2?params[4].toString():params[5].toString()))+" No of excess Debit count and "+(CrCount-Integer.parseInt(masterdata.getCategory()==1?params[5]:masterdata.getCategory()==2?params[6].toString():params[7].toString()))+" No of excess credit count";
											
										
										else if(DrCount>Integer.parseInt(masterdata.getCategory()==1?params[3]:masterdata.getCategory()==2?params[4].toString():params[5].toString()))
											riskacclog=MonthYear+","+masterdata.getAccNo()+","+DrCount+","+CrCount+","+(DrCount-Integer.parseInt(masterdata.getCategory()==1?params[3]:masterdata.getCategory()==2?params[4].toString():params[5].toString()))+" No of excess Debit count";
										else if(CrCount>Integer.parseInt(masterdata.getCategory()==1?params[5]:masterdata.getCategory()==2?params[6].toString():params[7].toString()))
											riskacclog=MonthYear+","+masterdata.getAccNo()+","+DrCount+","+CrCount+","+(CrCount-Integer.parseInt(masterdata.getCategory()==1?params[5]:masterdata.getCategory()==2?params[6].toString():params[7].toString()))+" No of excess credit count";
										else
											normalacclog=MonthYear+","+masterdata.getAccNo();
										int limitAmount;
										if(SumCreditAmount>(limitAmount=Integer.parseInt(masterdata.getCategory()==1?params[8]:masterdata.getCategory()==2?params[9].toString():params[10].toString())))
										{
											ArrayList<TransactionDTO> txnlist=transdao.getCreditTxns(monthyearlist);
											for(TransactionDTO txn : txnlist)
											{
												txn.setRiskFlag("Y");
												filewrite.WriteLine(txn.getCSV()+",Huge amount deposit for the month "+MonthYear, propmap.get("RISKTXNLOGPATH").toString());
											}
										}
										else
										{
											for(TransactionDTO txn : monthyearlist)
											{
												filewrite.WriteLine(txn.getCSV(), propmap.get("NORMALTXNPATH").toString());
											}
										}
									
										
									}
									else if (params[1].equals("A") && transactiondate>datecondtion)
									{
										

										if(DrCount>Integer.parseInt(masterdata.getCategory()==1?params[3]:masterdata.getCategory()==2?params[4].toString():params[5].toString()) && CrCount>Integer.parseInt(masterdata.getCategory()==1?params[5]:masterdata.getCategory()==2?params[6].toString():params[7].toString()))
											riskacclog=MonthYear+","+masterdata.getAccNo()+","+DrCount+","+CrCount+","+(DrCount-Integer.parseInt(masterdata.getCategory()==1?params[3]:masterdata.getCategory()==2?params[4].toString():params[5].toString()))+" No of excess Debit count and "+(CrCount-Integer.parseInt(masterdata.getCategory()==1?params[5]:masterdata.getCategory()==2?params[6].toString():params[7].toString()))+" No of excess credit count";
											
										
										else if(DrCount>Integer.parseInt(masterdata.getCategory()==1?params[3]:masterdata.getCategory()==2?params[4].toString():params[5].toString()))
											riskacclog=MonthYear+","+masterdata.getAccNo()+","+DrCount+","+CrCount+","+(DrCount-Integer.parseInt(masterdata.getCategory()==1?params[3]:masterdata.getCategory()==2?params[4].toString():params[5].toString()))+" No of excess Debit count";
										else if(CrCount>Integer.parseInt(masterdata.getCategory()==1?params[5]:masterdata.getCategory()==2?params[6].toString():params[7].toString()))
											riskacclog=MonthYear+","+masterdata.getAccNo()+","+DrCount+","+CrCount+","+(CrCount-Integer.parseInt(masterdata.getCategory()==1?params[5]:masterdata.getCategory()==2?params[6].toString():params[7].toString()))+" No of excess credit count";
										else
											normalacclog=MonthYear+","+masterdata.getAccNo();
										int limitAmount;
										if(SumCreditAmount>(limitAmount=Integer.parseInt(masterdata.getCategory()==1?params[8]:masterdata.getCategory()==2?params[9].toString():params[10].toString())))
										{
											ArrayList<TransactionDTO> txnlist=transdao.getCreditTxns(monthyearlist);
											for(TransactionDTO txn : txnlist)
											{
												txn.setRiskFlag("Y");
												filewrite.WriteLine(txn.getCSV()+",Huge amount deposit for the month "+MonthYear, propmap.get("RISKTXNLOGPATH").toString());
											}
										}
										else
										{
											for(TransactionDTO txn : monthyearlist)
											{
												filewrite.WriteLine(txn.getCSV(), propmap.get("NORMALTXNPATH").toString());
											}
										}
									
									}
									
								
									if(!riskacclog.equals(""))
										filewrite.WriteLine(riskacclog, propmap.get("RISKACCOLOGPATH").toString());
									if(!normalacclog.equals(""))
										filewrite.WriteLine(normalacclog, propmap.get("NORMALACCPATH").toString());
									
									
								
									
								} catch (ParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							
							}
							ProcessedMonthYear.add(MonthYear);

						}
					}
					ProcessedList.put(file.getName(),file.lastModified());
				}
			}
		}
	}
	
	
 	

}
