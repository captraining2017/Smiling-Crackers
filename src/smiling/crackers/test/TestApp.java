package smiling.crackers.test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import smiling.crackers.fraudalerts.datamodel.TransactionDTO;

public class TestApp {
	
	private static TransactionDTO trans ;
	
	public static void main(String[] args) {
//		Random r = new Random();
//		
//		
//		for(int i=0;i<100;i++)
//		{
//			System.out.println("ACC"+r.nextInt(5000)+",CUS"+r.nextInt(5000)+",AAA,"+r.nextInt(2000000)+","+null);
//		}
		try {
			System.out.println(new SimpleDateFormat("dd/MM/yyyy").parse("31/11/2016"));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	


}
