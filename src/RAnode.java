import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;


public class RAnode {
	
	static Socket clientSocket;
	static ServerSocket serverSocket;
	static Socket serviceSocket; 
	DataInputStream dis; 
	InetAddress address; 
	static int portNumber;
	
	public static void main(String[] args)
	{
		
		//Initialize client socket
		try {
	           clientSocket = new Socket("Machine name", portNumber);
	    }
	    catch (IOException e) {
	        System.out.println(e);
	    }
		
		//Input to client
	    DataInputStream clientInput;
	    try {
	       clientInput = new DataInputStream(clientSocket.getInputStream());
	    }
	    catch (IOException e) {
	       System.out.println(e);
	    }
	    
	    //Output from client 
	    PrintStream clientOutput;
	    try {
	       clientOutput = new PrintStream(clientSocket.getOutputStream());
	    }
	    catch (IOException e) {
	       System.out.println(e);
	    }
		
		//Initialize server socket
		try {
		       serverSocket = new ServerSocket(portNumber);
		        }
		        catch (IOException e) {
		           System.out.println(e);
		        }
		
		//Accept connections from clients
		try {
		      serviceSocket = serverSocket.accept();
		       }
		   catch (IOException e) {
		      System.out.println(e);
		   }
			
		//Input to server
		DataInputStream serverInput;
	    try {
	       serverInput = new DataInputStream(serviceSocket.getInputStream());
	    }
	    catch (IOException e) {
	       System.out.println(e);
	    }
	    
	  //Output from server
	    PrintStream serverOutput;
	    try {
	       serverOutput = new PrintStream(serviceSocket.getOutputStream());
	    }
	    catch (IOException e) {
	       System.out.println(e);
	    }
	}
}
