package smiling.crackers.fraudalerts.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;

public class FileUtil {
	
	private FileWriter writer;
	public void WriteLine(String content,String filepath)
	{
		
		try {
			File file=new File(filepath);
			synchronized (this) {
				writer=new FileWriter(file,true);
				while(!file.canWrite())
				{
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				writer.write(content+"\n");
			}
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
				try {
					if(writer!=null)
						writer.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
		}
		
	}

}
