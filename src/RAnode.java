import java.io.*;
import java.net.*;
import java.util.ArrayList;

import javax.swing.JOptionPane;


public class RAnode {
	//writers
	PrintWriter w1;
	PrintWriter w2;
	PrintWriter w3;
	ArrayList<PrintWriter> outputStreams = new ArrayList<PrintWriter>();
	
	//readers
	BufferedReader in1;
	BufferedReader in2;
	BufferedReader in3;
	
	RicartAgrawala me;
	
	public RAnode() {	
		
		//Initialize peer sockets
		try{
			ServerSocket firstNode;
			ServerSocket secondNode;
			ServerSocket thirdNode;
			Socket first;
			Socket second;
			Socket third;
			
			//Creates two client sockets and one server socket for the node
			first = new Socket("",5000);
			second = new Socket("",5000);
			thirdNode = new ServerSocket(5001);
			
			third = thirdNode.accept();
			
			System.out.println("Sockets have been successfully set");
			
			//Creation of the writers and readers
			w1 = new PrintWriter(first.getOutputStream(), true);
			w2 = new PrintWriter(second.getOutputStream(), true);
			w3 = new PrintWriter(third.getOutputStream(), true);
			in1 = new BufferedReader(new InputStreamReader(first.getInputStream()));
			in2 = new BufferedReader(new InputStreamReader(second.getInputStream()));
			in3 = new BufferedReader(new InputStreamReader(third.getInputStream()));
			
			outputStreams.add(w1);
			outputStreams.add(w2);
			outputStreams.add(w3);
			
			me = new RicartAgrawala(nodeNum, 0, this);
			me.w[0] = w1;
			me.w[1] = w2;
			me.w[2] = w3;
			
			//Initialization of threads
			Thread tr1 = new Thread(new ChannelHandler(first));
			tr1.start();

			Thread tr2 = new Thread(new ChannelHandler(second));
			tr2.start();

			Thread tr3 = new Thread(new ChannelHandler(third));
			tr3.start();
		} catch(Exception e){}
		
		
	}
	public static void main(String[] args) 
	{
		new RAnode();
	}
}
