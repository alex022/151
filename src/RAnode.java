import java.io.DataInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;


public class RAnode {
	
	static Socket socket;
	DataInputStream dis; 
	InetAddress address; 
	static int portNumber;
	
	public static void main(String[] args)
	{
		try {
	           socket = new Socket("Machine name", portNumber);
	    }
	    catch (IOException e) {
	        System.out.println(e);
	    }
		
		
	}

}
