package client;

public class TestApp {
	
	private static String fileName;
	private static int replicationDegree;
	
	public static void main(String[] args) {
		
		if(!parseArguments(args)) return;
		
		System.out.println(fileName);
	}
	
	public static boolean parseArguments(String[] args) {
		
		//fazer check do service access point
		
		if(args[1].equals("BACKUP")) {
			if(matchesFileName(args[2])) {
				fileName = args[2];
				replicationDegree = Integer.parseInt(args[3]);
				return true;
			}
			System.out.println("The filename specified is not valid.");
			return false;
		}
		
		return false;
	}
	
	public static boolean matchesFileName(String input) {
		return input.matches("[a-zA-Z0-9]+.[a-zA-Z0-9]+");
	}

}
