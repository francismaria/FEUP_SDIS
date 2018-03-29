package exceptions;

public class FileNotFound extends Exception{
	
	private static final long serialVersionUID = 1L;

	public FileNotFound(String path) {
		System.out.println("The file specified by the path'" + path + "' was not found.\n"
				+ "Program won't start.");
	}
}
