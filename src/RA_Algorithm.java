import java.io.*;


public class RA_Algorithm extends base {

	public boolean requesting;
	public int numReplies;
	public int topSeq;
	public int seq;
	public int node;
	public RAnode driver;

	//Holds our writers to use
	public PrintWriter[] w;

	//Hard coded to 3 right now, for 3 other nodes in network
	public int channelCount = 2;

	public boolean[] replyDeferred;

	public RA_Algorithm(int node, int seq, RAnode driver){
		requesting = false;

		numReplies = channelCount;

		topSeq = 0;
		this.seq = seq;
		this.driver = driver;

		w = new PrintWriter[channelCount];

		// Node number is also used for priority (low node # == higher priority in RicartAgrawala scheme)
		// Node numbers are [1,channelCount]; since we're starting at 1 check for errors trying to access node '0'.
		this.node = node;

		replyDeferred = new boolean[channelCount];
	}

	/** Invocation (begun in driver module with request CS) */
	public boolean invocation(){

		requesting = true;
		seq = topSeq + 1;

		numReplies = channelCount;

		for(int i = 1; i <= channelCount + 1; i++){
			if(i != node){
				requestTo(seq, node, i);
			}
		}

		while(numReplies > 0)
		{
			try{
				Thread.sleep(5);

			}
			catch(Exception e){

			}
			/*wait until we have replies from all other processes */
		}

		//We return when ready to enter CS
		return true;


	}

	// The other half of invocation
	public void releaseCS()
	{
		requesting = false;

		for(int i = 0; i < channelCount; i++){
			if(replyDeferred[i]){
				replyDeferred[i] = false;
				if(i < (node - 1))
					replyTo(i + 1);
				else
					replyTo(i + 2);
			}
		}
	}

	/** Receiving Request 
	 * 
	 *	@param	j	The incoming message's sequence number
	 *	@param	k	The incoming message's node number 
	 * 
	 */
	public void receiveRequest(int j, int k){
		System.out.println(stamp + ":" + "Received request from node " + k);
		stamp++;
		boolean bDefer = false;

		topSeq = Math.max(topSeq, j);
		bDefer = requesting && ((j > seq) || (j == seq && k > node));
		if(bDefer){
			//System.out.println(stamp + ":" + "Deferred sending message to " + k);
			stamp++;
			if(k > node)
				replyDeferred[k - 2] = true;
			else
				replyDeferred[k - 1] = true;
		}
		else{ 
			System.out.println(stamp + ":" + "Sent reply message to " + k);
			stamp++;
			replyTo(k);
		}

	}

	/** Receiving Replies */
	public void receiveReply(){
		numReplies = Math.max((numReplies - 1), 0);
		System.out.println(stamp + ":" + "Outstanding replies: " + numReplies);
		stamp++;
	}

	public void replyTo(int k)
	{
		System.out.println(stamp + ":" + "Sending REPLY to node " + k);
		stamp++;
		if(k > node)
		{
			w[k-2].println("REPLY," + k);
		}
		else
		{
			w[k-1].println("REPLY," + k);
		}
	}

	public void requestTo(int seq, int node, int i)
	{
		System.out.println(stamp + ":" + "Sending REQUEST to node " + (((i))));
		stamp++;
		if(i > node)
		{
			w[i-2].println("REQUEST," + seq + "," + node);
		}
		else
		{
			w[i-1].println("REQUEST," + seq + "," + node);
		}
	}

}