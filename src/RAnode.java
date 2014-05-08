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
	
	int numWrites;
	RA_Algorithm me;
	
	public RAnode() {	
		
		numWrites = 0;
		
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
			
			me = new RA_Algorithm(nodeNum, 0, this);
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
		
		while(numWrites < 3){
			try{
				System.out.println("Critical section requested");
				request();
				numWrites++;
				Thread.sleep(250);
			} catch(Exception e){}
		}
	}
	
	//Critical section call
	public static boolean criticalSection(int numbWrites)
	{
		System.out.println("Node has entered critical section");
		try
		{
			BufferedWriter criticalSection = new BufferedWriter(new FileWriter("CriticalSectionOutput.txt", true));

			criticalSection.write("Node has started critical section access");
			criticalSection.newLine();
			Thread.sleep(100);
			//criticalSection.write(nodeName + " has now accessed it's critical section " + numberOfWrites + " times.");
			criticalSection.write("Node has ended critical section access");
			criticalSection.newLine();
			criticalSection.newLine();
			criticalSection.flush(); //flush stream
			criticalSection.close(); //close write
		} 
		catch(Exception e){ System.out.println("Wrong");}
		return true;
	}
	
	public void request()
	{

		me.invocation();

		//After invocation returns, we can safely call CS
		criticalSection(numWrites);

		//Once we are done with CS, release CS
		me.releaseCS();
	}
	
	class ChannelHandler implements Runnable
	{
		BufferedReader reader;
		PrintWriter writer;
		Socket sock;

		public ChannelHandler(Socket s)
		{
			try
			{
				sock = s;
				InputStreamReader iReader = new InputStreamReader(sock.getInputStream());
				reader = new BufferedReader(iReader);

			}catch(Exception ex)
			{
				ex.printStackTrace();
			}
		}

		/** Continuously runs and reads all incoming messages, passing messages to ME */

		public void run()
		{
			String message;

			try
			{
				//As long as this reader is open, will take action the moment a message arrives.
				while(( message = reader.readLine() ) != null)
				{
					System.out.println("Node has received message: " + message);

					//Tokenize our message to determine RicartAgrawala step

					String tokens[] = message.split(",");
					String messageType = tokens[0];

					if(messageType.equals("REQUEST"))
					{
						/*We are receiving request(j,k) where j is a seq# and k a node#.
						  This call will decide to defer or ack with a reply. */
						me.receiveRequest(Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]));
					}
					else if(messageType.equals("REPLY"))
					{
						/* Received a reply. We'll decrement our outstanding replies */
						me.receiveReply();
					}
				}

			}catch(Exception ex)
			{
				ex.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) 
	{
		new RAnode();
	}
}
