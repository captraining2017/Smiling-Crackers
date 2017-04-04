package smiling.crackers.fraudalerts.DataLoader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.math.BigDecimal;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Random;

import smiling.crackers.fraudalerts.AccessObject.MasterDataDAO;
import smiling.crackers.fraudalerts.datamodel.MasterDataDTO;
import smiling.crackers.fraudalerts.util.FileUtil;

public class VirtualDataLoaderThread extends Thread{
	
	private Date StartDate,EndDate;
	private HashMap<String, String> conditionparams;
	private ArrayList<MasterDataDTO> masterdatalist;
	private int volume;
	private static MasterDataDAO masterdao;
	private int txncount;
	private String filepath;
	private String threadname;
	private static FileUtil filewriter;
	private String passbookfilepath;
	
	public VirtualDataLoaderThread(Date StartDate,Date EndDate,ArrayList<MasterDataDTO> masterdatalist,int volume,String filepath,String threadname,FileUtil filewriter,String passbookfilepath)
	{
		this.StartDate=StartDate;
		this.EndDate=EndDate;
		this.conditionparams=conditionparams;
		this.masterdatalist=masterdatalist;
		this.volume=volume;
		masterdao=new MasterDataDAO();
		txncount=0;
		this.filepath=filepath;
		this.threadname=threadname;
		this.filewriter=filewriter;
		this.passbookfilepath=passbookfilepath;
	}
	public void run()
	{
		System.out.println(threadname+" thread started...");
		Calendar datepointer=new GregorianCalendar();
		Calendar datelimit=new GregorianCalendar();
		SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
		String startdate=sdf.format(StartDate);
		String enddate=sdf.format(EndDate);
		datepointer.set(Integer.parseInt(startdate.substring(6,startdate.length())), Integer.parseInt(startdate.substring(0,2)), Integer.parseInt(startdate.substring(3,5)));
		datelimit.set(Integer.parseInt(enddate.substring(6,enddate.length())), Integer.parseInt(enddate.substring(0,2)), Integer.parseInt(enddate.substring(3,5)));
		int millisecdivision=(int)((( datelimit.getTimeInMillis()-datepointer.getTimeInMillis()))/volume);
		//System.out.println((( datelimit.getTimeInMillis()-datepointer.getTimeInMillis())/(1000*60*60)));
		Random r=new Random();
		String[] flag={"D","C"};
		while(datepointer.before(datelimit) && txncount < volume)
		{
			MasterDataDTO mastlist=masterdatalist.get(r.nextInt(masterdatalist.size()-1));
			String content=datepointer.getTime()+","+mastlist.getAccNo()+","+r.nextInt(mastlist.getSal()*4)+","+flag[r.nextInt(2)];
			filewriter.WriteLine(content, filepath);
     		filewriter.WriteLine(content, passbookfilepath+"\\"+mastlist.getAccNo()+".CSV");
			System.out.println("New Transaction Launched!");
			datepointer.add(Calendar.MILLISECOND, millisecdivision);
			txncount++;
		}

		
	}

}
