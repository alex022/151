import java.io.*;
import java.net.*;
import java.util.ArrayList;

import javax.swing.JOptionPane;


public class RAnode {
	//writers
	PrintWriter w1;
	PrintWriter w2;
	ArrayList<PrintWriter> outputStreams = new ArrayList<PrintWriter>();
	
	//readers
	BufferedReader in1;
	BufferedReader in2;
	
	int numWrites;
	RA_Algorithm me;
	int node;
	
	public RAnode(String arg[]) {	
		
		numWrites = 0;
		node = Integer.parseInt(arg[0]);
		
		//Initialize peer sockets
		try{
			ServerSocket firstNode;
			ServerSocket secondNode;
			ServerSocket thirdNode;
			Socket first;
			Socket second;
			Socket third;
			
			if(node == 1){
				BufferedWriter clearWrite = new BufferedWriter(new FileWriter("CriticalSectionOutput.txt"));
				clearWrite.write("\n");
				clearWrite.close();
				
				System.out.println("Node 1 being initialized");
				
				firstNode = new ServerSocket(5000); //serversocket for node 2
				secondNode = new ServerSocket(5001); //serversocket for node 3
				
				first = firstNode.accept();
				System.out.println("Connected to node 2");
				
				second = secondNode.accept();
				System.out.println("Connected to node 3");
				
				System.out.println("Node 1 has been initialized");
			}
			else if(node == 2){
				System.out.println("Node 2 being initialized");
				
				first = new Socket("cartman.cs.ucsb.edu", 5000); //connect to node 1
				System.out.println("Connected to node 1");
				
				secondNode = new ServerSocket(5001); //serversocket for node 3
				second = secondNode.accept();
				System.out.println("Connected to node 3");
				
				System.out.println("Node 2 has been initialized");
			}
			else{
				System.out.println("Node 3 being initialized");
				
				first = new Socket("cartman.cs.ucsb.edu",5001); //connect to node 1
				System.out.println("Connected to node 1");
				
				second = new Socket("bart.cs.ucsb.edu",5001); //connect to node 2
				System.out.println("Connected to node 2");
				
				System.out.println("Node 3 has been initialized");
			}
			System.out.println("Sockets have been successfully set");
			
			//Creation of the writers and readers
			w1 = new PrintWriter(first.getOutputStream(), true);
			w2 = new PrintWriter(second.getOutputStream(), true);

			in1 = new BufferedReader(new InputStreamReader(first.getInputStream()));
			in2 = new BufferedReader(new InputStreamReader(second.getInputStream()));

			outputStreams.add(w1);
			outputStreams.add(w2);

			
			me = new RA_Algorithm(node, 0, this);
			me.w[0] = w1;
			me.w[1] = w2;

			
			//Initialization of threads
			Thread tr1 = new Thread(new ChannelHandler(first));
			tr1.start();

			Thread tr2 = new Thread(new ChannelHandler(second));
			tr2.start();
			
		} catch(Exception e){}
		
		while(numWrites < 3){
			try{
				//System.out.println("Critical section requested");
				request();
				numWrites++;
				Thread.sleep(250);
			} catch(Exception e){}
		}
	}
	
	//Critical section call
	public static boolean criticalSection(int node, int numbWrites)
	{
		System.out.println("Node" + node + "has entered critical section");
		try
		{
			BufferedWriter criticalSection = new BufferedWriter(new FileWriter("CriticalSectionOutput.txt", true));

			criticalSection.write(node + "has started critical section access");
			criticalSection.newLine();
			Thread.sleep(100);
			//criticalSection.write(nodeName + " has now accessed it's critical section " + numberOfWrites + " times.");
			criticalSection.write(node + "has ended critical section access");
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
		System.out.println("OK!");
		criticalSection(node, numWrites);
		

		//Once we are done with CS, release CS
		me.releaseCS();
		System.out.println("Critical section released");
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
					System.out.println(node + "has received message: " + message);

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
	
	public static void main(String[] arg) 
	{
		new RAnode(arg);
	}
}
