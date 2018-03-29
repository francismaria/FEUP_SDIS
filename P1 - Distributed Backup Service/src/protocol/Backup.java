package protocol;

import java.io.File;

public class Backup extends Thread{
	
	private File file;
	
	public Backup(File file) {
		this.file = file;
	}
	
	public void run() {
		
		System.out.println("STARTING BACKUP THREAD " + file.getAbsolutePath());
	}

}
