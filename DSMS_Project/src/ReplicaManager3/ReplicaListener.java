package ReplicaManager3;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class ReplicaListener extends Thread{

	@Override
	public void run() {
		// TODO Auto-generated method stub
		StartListening();
		
	}
	
	public ReplicaListener (){
		
	}


	private void StartListening(){
	
		try {
			//ServerSocket s = new ServerSocket(ReplicaManager3.LEADER_ELECTION_PORT, 4, InetAddress.getByName(ReplicaManager3.REPLICA_MANAGER_IP));
			for(int i = 0; i <100; i++){
				Socket incoming = null;
				ServerSocket s = new ServerSocket(ReplicaManager3.LEADER_ELECTION_PORT);
				
					incoming = s.accept();
					System.out.println(ReplicaManager3.REPLICA_MANAGER_NAME + " is alive!");
					Scanner scan = new Scanner(incoming.getInputStream());
					if (scan.hasNextLine()) {
	                    System.out.println(scan.nextLine());
	                }
					
				s.close();
				scan.close();
				}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
