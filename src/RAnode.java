import java.io.DataInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;


public class RAnode {
	
	static Socket clientSocket;
	static ServerSocket serverSocket;
	DataInputStream dis; 
	InetAddress address; 
	static int portNumber;
	
	public static void main(String[] args)
	{
		
		
		try {
	           clientSocket = new Socket("Machine name", portNumber);
	    }
	    catch (IOException e) {
	        System.out.println(e);
	    }
		
		try {
		       serverSocket = new ServerSocket(portNumber);
		        }
		        catch (IOException e) {
		           System.out.println(e);
		        }
		
		
	}

}
