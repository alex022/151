import java.io.*;


public class RA_Algorithm extends base {

	public boolean bRequestingCS;
	public int outstandingReplies;
	public int highestSeqNum;
	public int seqNum;
	public int nodeNum;
	public RAnode driverModule;

	//Holds our writers to use
	public PrintWriter[] w;

	//Number of peers each node has
	public int channelCount = 2;

	public boolean[] replyDeferred;

	public RA_Algorithm(int nodeNum, int seqNum, RAnode driverModule){
		bRequestingCS = false;

		outstandingReplies = channelCount;

		highestSeqNum = 0;
		this.seqNum = seqNum;
		this.driverModule = driverModule;

		w = new PrintWriter[channelCount];

		this.nodeNum = nodeNum;

		replyDeferred = new boolean[channelCount];
	}

	/** Invocation (begun in driver module with request CS) */
	public boolean invocation(){

		bRequestingCS = true;
		seqNum = highestSeqNum + 1;

		outstandingReplies = channelCount;

		for(int i = 1; i <= channelCount + 1; i++){
			if(i != nodeNum){
				requestTo(seqNum, nodeNum, i);
			}
		}

		//Makes node wait until replies come from all peer nodes
		while(outstandingReplies > 0)
		{
			try{
				Thread.sleep(5);

			}
			catch(Exception e){

			}
		}

		return true;
	}

	public void releaseCS()
	{
		bRequestingCS = false;

		for(int i = 0; i < channelCount; i++){
			if(replyDeferred[i]){
				replyDeferred[i] = false;
				if(i < (nodeNum - 1))
					replyTo(i + 1);
				else
					replyTo(i + 2);
			}
		}
	}

	// Receiving requests
	public void receiveRequest(int j, int k){
		System.out.println(stamp + ":" + "Received request from node " + k);
		stamp++;

		System.out.println(stamp + ":" + "Sent reply message to " + k);
		stamp++;
		replyTo(k);
	}

	// Receiving replies
	public void receiveReply(){
		outstandingReplies = Math.max((outstandingReplies - 1), 0);
		System.out.println(stamp + ":" + "Outstanding replies: " + outstandingReplies);
		stamp++;
	}

	public void replyTo(int k)
	{
		System.out.println(stamp + ":" + "Sending REPLY to node " + k);
		stamp++;
		if(k > nodeNum)
		{
			w[k-2].println("REPLY," + k);
		}
		else
		{
			w[k-1].println("REPLY," + k);
		}
	}

	public void requestTo(int seqNum, int nodeNum, int i)
	{
		System.out.println(stamp + ":" + "Sending REQUEST to node " + (((i))));
		stamp++;
		if(i > nodeNum)
		{
			w[i-2].println("REQUEST," + seqNum + "," + nodeNum);
		}
		else
		{
			w[i-1].println("REQUEST," + seqNum + "," + nodeNum);
		}
	}

}