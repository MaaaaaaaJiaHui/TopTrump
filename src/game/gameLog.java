package game;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class gameLog {
	static FileOutputStream fos = null;
	static File logfile;

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void init() throws IOException {
		// TODO Auto-generated method stub

		logfile = new File("toptrumps.log");

		fos = new FileOutputStream(logfile);

        /* test if exists or delete it. */ 
		logfile.delete();
        if (!logfile.exists()) {
        	logfile.createNewFile();
        }
	}
	public static void write(String str) throws IOException{
		fos.write(str.getBytes());
		fos.flush();
	}
	public static void close() throws IOException{
		fos.close();
	}
}
