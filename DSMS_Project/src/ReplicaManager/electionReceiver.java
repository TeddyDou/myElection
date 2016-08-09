package ReplicaManager;

import java.util.HashMap;

import BroadCaster.UDPServer;

public class electionReceiver implements Runnable{

	UDPServer receiver = null;
	HashMap<String, Integer> replicaStatus = null;
	
	public electionReceiver(UDPServer _server, HashMap<String, Integer> replicaID){
		receiver = _server;
		replicaStatus = replicaID;
	}
	@Override
	public void run() {
		while(true)
		{
			try {
				String recData;
				recData = receiver.deliver();
			
				String replicaManagerName = recData.trim().split(",")[0];
				Integer replicaManagerID = Integer.parseInt(recData.trim().split(",")[1]);
				replicaStatus.put(replicaManagerName, replicaManagerID);
				}		// TODO Auto-generated method stub
			catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
}
