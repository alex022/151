import java.io.*;
import java.net.*;
import java.util.Date;

import javax.swing.JOptionPane;


public class RAnode {
	//writers
	PrintWriter w1;
	PrintWriter w2;
	PrintWriter w3;
	
	//readers
	BufferedReader in1;
	BufferedReader in2;
	BufferedReader in3;
	
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
			
			
		} catch(Exception e){
			
		}
	}
	public static void main(String[] args) 
	{
		new RAnode();
	}
}
