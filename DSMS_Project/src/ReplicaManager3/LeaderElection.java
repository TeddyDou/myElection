package ReplicaManager3;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map.Entry;
import Front_End.LeaderInfo;
import Front_End.Front_End_Impl;

public class LeaderElection {

	private boolean isLeaderFlag = false;
	private boolean isElectingFlag = false;
	private static final Object lock = new Object();
	
	public ReplicaListener rl = new ReplicaListener();
	
	int FE_port = 4000;
	String FE_IP = "127.0.0.1";
	ReplicaManagerInfo[] gm = new ReplicaManagerInfo[3];
	
	public LeaderElection(){
		gm[0] = new ReplicaManagerInfo("rep1", "127.0.0.1", 5000, 3, 7001);
		gm[1] = new ReplicaManagerInfo("rep2", "127.0.0.1", 5001, 2, 7002);
		gm[2] = new ReplicaManagerInfo("rep3", "127.0.0.1", 5002, 1, 7003);
		new Thread(rl).start();
	}
	
	public int initialElection() {
		DatagramSocket socket = null;
		try {
			String msg = "Is there a leader?";
			socket = new DatagramSocket();
			InetAddress FE_addr = InetAddress.getByName(FE_IP);
			byte[] sendData = msg.getBytes();
			byte[] receiveData = new byte[4096];
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, FE_addr, FE_port);
			socket.send(sendPacket);
			System.out.println(ReplicaManager3.REPLICA_MANAGER_NAME + " is Sending message to FE: " + msg);
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			socket.receive(receivePacket);
			String out = new String(receivePacket.getData()).trim();
			System.out.println(ReplicaManager3.REPLICA_MANAGER_NAME + " received message from FE: " + out);
			if (!Boolean.parseBoolean(out)){
					sendData = (Integer.toString(ReplicaManager3.LOCAL_PORT).getBytes());
					sendPacket = new DatagramPacket(sendData, sendData.length, FE_addr, FE_port);
					socket.send(sendPacket);
					System.out.println(ReplicaManager3.REPLICA_MANAGER_NAME + " is sending local port to FE: " + Integer.toString(ReplicaManager3.LOCAL_PORT));
					socket.receive(receivePacket);
					out = new String(receivePacket.getData()).trim();
					System.out.println(out);
					if (out.equalsIgnoreCase("succeed"))
						this.isLeaderFlag = true;
					else 
						System.out.println("Error in leader election!");
					return ReplicaManager3.LOCAL_PORT;
				}
				else {
					sendData = "leader?".getBytes();
					sendPacket = new DatagramPacket(sendData, sendData.length, FE_addr, FE_port);
					socket.send(sendPacket);
					System.out.println(ReplicaManager3.REPLICA_MANAGER_NAME + " is questing leader port from FE.");
					socket.receive(receivePacket);
					out = new String(receivePacket.getData()).trim();
					System.out.println(ReplicaManager3.REPLICA_MANAGER_NAME + " received leader port: " + out);					
					return Integer.parseInt(out);
				}

			}catch (Exception e) {
				return -3;
			}finally{
				if(socket != null) socket.close();
			}
	}
	
	public int ElectLeader(){
		int newLeaderPort = -1;
		try {

			this.setElectingFlag(true);
			System.out.println(ReplicaManager3.REPLICA_MANAGER_NAME + " is holding an election.");
			if (this.isLeaderFlag){
				System.out.println(ReplicaManager3.REPLICA_MANAGER_NAME + "is still leader.");
				newLeaderPort = ReplicaManager3.LOCAL_PORT;
				notifyNewLeader(FE_IP ,FE_port);
				for (int i = 0; i <this.gm.length; i++){
					if (gm[i].getElection_port() > ReplicaManager3.LEADER_ELECTION_PORT){
						notifyNewLeader(gm[i].getIP(), gm[i].getElection_port());
					}
				}
			}
			else{
				if(!sendMessage())
				{//elect self as co-ordinator
					newLeaderPort = ReplicaManager3.LOCAL_PORT;
                    //System.out.println("New Leader: [" + ReplicaManager3.REPLICA_MANAGER_NAME + "]");
					notifyNewLeader(FE_IP ,FE_port);
                    for (int i = 0; i <this.gm.length; i++){
                    	if (gm[i].getElection_port() > ReplicaManager3.LEADER_ELECTION_PORT){
                    		notifyNewLeader(gm[i].getIP(), gm[i].getElection_port());
    					}
    				}
                }
				else
					newLeaderPort = receiveLeader(ReplicaManager3.REPLICA_MANAGER_IP, ReplicaManager3.LEADER_ELECTION_PORT);				
			}
		
		} catch (Exception e) {
			System.out.println(e);
		}

		return newLeaderPort;
		
	}

	private void notifyNewLeader(String addr, int port) {
		String msg = Integer.toString(ReplicaManager3.LOCAL_PORT);
		DatagramSocket socket = null;
		try{
			socket = new DatagramSocket();
			InetAddress IP_addr = InetAddress.getByName(addr);
			byte[] sendData = msg.getBytes();
			byte[] receiveData = new byte[4096];
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IP_addr, port);
			socket.send(sendPacket);
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			socket.receive(receivePacket);
			String out = new String(receivePacket.getData()).trim();
			if (out.equalsIgnoreCase("succeed")){
				System.out.println("Leader port is updated in " + addr + ": " + port);
				this.isLeaderFlag = true;
			}
			else 
				System.out.println("Failed to notify leader port to: " + addr + ": " + port);
		}
		catch(Exception e){
			System.out.println(e);
		}finally{
			if(socket != null) socket.close();
		}
		
	}

	private int receiveLeader(String addr, int port) {
		int out = -1;
		DatagramSocket socket = null;
		String msg = "success";
		try{
			socket = new DatagramSocket(port, InetAddress.getByName(addr));
			byte[] receiveData = new byte[4096];
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			socket.receive(receivePacket);
			out = Integer.parseInt(new String(receivePacket.getData()).trim());
			byte[] sendData = msg.getBytes();
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, receivePacket.getAddress(), receivePacket.getPort());
			socket.send(sendPacket);
		}
		catch(Exception e){
			System.out.println("Fail to get the leader port.");
		}finally{
			if(socket != null) socket.close();
		}
		return out;
	}

	private boolean sendMessage() {
		boolean result = false;
		for(int i = ReplicaManager3.REPLICA_MANAGER_ID; i < gm.length; i++){
			try {
				Socket electionMessage = new Socket(InetAddress.getByName(gm[2-i].getIP()), gm[2-i].getElection_port());
				System.out.println(ReplicaManager3.REPLICA_MANAGER_NAME + " -> " + gm[2-i].getName() + ": respond successfully!");
				electionMessage.close();
				result = true;
			} catch (Exception e) {
				System.out.println(ReplicaManager3.REPLICA_MANAGER_NAME + " -> " + gm[2-i].getName() + ": no respond!");
			}						
		}
		return result;
	}
	


//	public ReplicaManagerInfo[] GroupMember() {
//		
//		ReplicaManagerInfo[] gm = new ReplicaManagerInfo[3];
//		gm[0] = new ReplicaManagerInfo("rep1", "127.0.0.1", 5000, 1);
//		gm[1] = new ReplicaManagerInfo("rep2", "127.0.0.1", 5001, 2);
//		gm[2] = new ReplicaManagerInfo("rep3", "127.0.0.1", 5002, 3);
//		return gm;
//		
//	}
	
//	public String initialize() {
//		
//		int i = GroupMemberPriority().size();
//		//clock(5);
//		if (i > 0){
//			System.out.println("success to start the replicas!");
//			return electMaxID(GroupMemberPriority());
//		}
//		else {
//			System.out.println("Fail to start the replicas!");
//			return "HEllo";
//		}
//	}
//	public HashMap<String, Integer> GroupMemberPriority() {
//		// TODO Auto-generated method stub
//		HashMap<String, Integer> gms = new HashMap<String, Integer>();
//		gms.put("rep1", 3);
//		gms.put("rep2", 2);
//		gms.put("rep3", 1);
//		
//		return gms;
//	}
	
//	public String electMaxID(HashMap<String, Integer> gms) {
//		int max = 0;
//		for(Entry<String, Integer> entry : gms.entrySet()){
//			if (entry.getValue() > max)
//				max = entry.getValue();
//		}
//		for(Entry<String, Integer> entry : gms.entrySet()){
//			if (entry.getValue() == max)
//				return entry.getKey();
//		}
//		return null;
//	}

	public boolean isLeaderFlag() {
		return isLeaderFlag;
	}

	public void setLeaderFlag(boolean isLeaderFlag) {
		this.isLeaderFlag = isLeaderFlag;
	}

	public boolean isElectingFlag() {
		return isElectingFlag;
	}

	public void setElectingFlag(boolean isElectingFlag) {
		this.isElectingFlag = isElectingFlag;
	}
	
	
}
