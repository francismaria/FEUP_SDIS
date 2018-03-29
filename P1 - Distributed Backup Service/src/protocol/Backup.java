package protocol;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Backup extends Thread{
	
	private File file = null;
	
	public Backup(File file) {
		this.file = file;
	}
	
	public void run() {
	
		System.out.println("STARTING BACKUP THREAD " + file.getAbsolutePath());
		
		Scanner input = null;
		
		try {
			input = new Scanner(file);
		} catch (FileNotFoundException e) {
			System.out.println("It occurred an error with the file. File was not found.");
		}
		
		while(input.hasNextLine()) {
			System.out.println(input.nextLine());
		}
	}

}
