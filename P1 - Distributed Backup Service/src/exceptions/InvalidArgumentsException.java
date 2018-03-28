package exceptions;

public class InvalidArgumentsException extends Exception{
	
	public InvalidArgumentsException() {}
	
	public InvalidArgumentsException(String service){
		
		if(service.equals("peer")) {
			System.out.println("Wrong usage of program.\njava Peer <protocol-version> "
					+ "<server-id> <service-print> <MC 'name'> <MDB 'name'> <MDR 'name'>\n");
		}
		else {
			System.out.println("Wrong usage of program.\njava Client <etc...>\n");
		}
	}
}
