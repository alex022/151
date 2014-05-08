import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
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
		//Initialize server socket
		try {
		       serverSocket = new ServerSocket(portNumber);
		        }
		        catch (IOException e) {
		           System.out.println(e);
		        }
		
		//Accept connections from clients
		try {
		      clientSocket = serverSocket.accept();
		       }
		   catch (IOException e) {
		      System.out.println(e);
		   }
			
		//Input
		DataInputStream serverInput;
	    try {
	       serverInput = new DataInputStream(clientSocket.getInputStream());
	    }
	    catch (IOException e) {
	       System.out.println(e);
	    }
	    
	    //Output
	    PrintStream serverOutput;
	    try {
	       serverOutput = new PrintStream(clientSocket.getOutputStream());
	    }
	    catch (IOException e) {
	       System.out.println(e);
	    }
	}
}
