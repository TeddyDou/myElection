package Front_End;

import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;

import org.omg.CORBA.ORB;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;

import FrontEnd_CORBA.Front_EndPOA;

enum Sequencer {
	REQUESTID;

	private static int ID = 0;

	public synchronized int getID() {
		ID++;
		return ID;
	}
}

enum RMSequencer {
	RMREQUESTID;
	private static int ID = 0;

	public synchronized int getID() {
		ID++;
		return ID;
	}
}

public class Front_End_Impl extends Front_EndPOA{
	
	private String ServerName;
	private int FEport;
	private ORB orb = null;
	
	// Leader Host IP and Port
	static String LEADER_HOST_IP = "localhost" ; // "localhost" 
	static int LEADER_HOST_PORT = 5000; //  5000
	
	static Map<String, String> REQUEST_HASH_TABLE = new HashMap<String, String>();

	
	public Front_End_Impl() {
		this.ServerName = "FE";
		this.FEport = 4000;
	}
	
	// This is the CORBA set up methods
	public void ORBsetup(String[] Args) throws Exception {
			orb = ORB.init(Args, null);

			POA rootPOA = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));

			byte[] FEId = rootPOA.activate_object(this);
			org.omg.CORBA.Object FEref = rootPOA.id_to_reference(FEId);

			String FEIor = orb.object_to_string(FEref);

			PrintWriter file = new PrintWriter("ior.txt");
			file.println(FEIor);

			file.close();
			rootPOA.the_POAManager().activate();
			System.out.println("ORB runs!");
			orb.run();

			}

	@Override
	public String createDRecord(String managerID, String firstName, String lastName, String address, String phone,
			String specialization, String location) {
		
		int requestID = Sequencer.REQUESTID.getID();
		String StationCheck = managerID.substring(0, 3);
		String result = "";
		System.out.println(StationCheck);
		
		String functionCMD = "createDRecord";
		String recordInfo = firstName + "," + lastName + "," + address 
						+ "," + phone + "," + specialization + "," + location;
		String requestMessage = requestID + ","+ functionCMD + "," + managerID + "," + recordInfo;
		
		result = sendRequestToLeader(requestMessage);
		
		return result;
	}

	@Override
	public String createNRecord(String managerID, String firstName, String lastName, String designation, String status,
			String statusDate) {

		int requestID = Sequencer.REQUESTID.getID();
		String StationCheck = managerID.substring(0, 3);
		String result = "";
		System.out.println(StationCheck);
		
		String functionCMD = "createNRecord";
		String recordInfo = firstName + "," + lastName + "," + designation 
						+ "," + status + "," + statusDate ;
		String requestMessage = requestID + ","+ functionCMD + "," + managerID + "," + recordInfo;
		
		result = sendRequestToLeader(requestMessage);
		
		return result;
	}

	@Override
	public String getRecordCounts(String managerID) {

		int requestID = Sequencer.REQUESTID.getID();
		String StationCheck = managerID.substring(0, 3);
		String result = "";
		System.out.println(StationCheck);
		
		String functionCMD = "getRecordCounts";
		String recordInfo = null;
		String requestMessage = requestID + ","+ functionCMD + "," + managerID + "," + recordInfo;
		
		result = sendRequestToLeader(requestMessage);
		
		return result;
	}

	@Override
	public String editRecord(String managerID, String recordID, String fieldName, String newValue) {

		int requestID = Sequencer.REQUESTID.getID();
		String StationCheck = managerID.substring(0, 3);
		String result = "";
		System.out.println(StationCheck);
		
		String functionCMD = "editRecord";
		String recordInfo = recordID + "," + fieldName + "," + newValue;
		String requestMessage = requestID + ","+ functionCMD + "," + managerID + "," + recordInfo;
		
		result = sendRequestToLeader(requestMessage);
		
		return result;
	}

	@Override
	public String transferRecord(String managerID, String recordID, String remoteClinicServerName) {

		int requestID = Sequencer.REQUESTID.getID();
		String StationCheck = managerID.substring(0, 3);
		String result = "";
		System.out.println(StationCheck);
		
		String functionCMD = "transferRecord";
		String recordInfo = recordID + "," + remoteClinicServerName;
		String requestMessage = requestID + ","+ functionCMD + "," + managerID + "," + recordInfo;
		
		result = sendRequestToLeader(requestMessage);
		
		return result;
	}
	
	public static String sendRequestToLeader(String requestMessage){
		
		System.out.println("Add request: "+ requestMessage.split(",")[0]);
		REQUEST_HASH_TABLE.put(requestMessage.split(",")[0], requestMessage);
		
		DatagramSocket socket = null;
	    try {
	    	socket = new DatagramSocket();
	    	byte[] message = (new String(requestMessage)).getBytes();
	    	InetAddress leaderHost = InetAddress.getByName(LEADER_HOST_IP);
	    	DatagramPacket request = new DatagramPacket(message, message.length, leaderHost, LEADER_HOST_PORT);
	    	
	    	socket.send(request);
	    	socket.setSoTimeout(3000);
	    	byte[] buffer = new byte[1000];
	    	DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
	    	while(true){
	    		try {
	    			socket.receive(reply);
	    			String result = new String(reply.getData()).trim();
	    			if(result.equals("OK")){
	    	    		// if acknowledgement is OK then remove the request from hash map;
	    	    		System.out.println("delete request: "+ requestMessage.split(",")[0] +" from hashtable");
	    	    		REQUEST_HASH_TABLE.remove(requestMessage.split(",")[0]);
	    	    	}else{
	    	    		return result;
	    	    	}
	    		} catch (SocketTimeoutException e) {
	    			InetAddress host_resend = InetAddress.getByName(LEADER_HOST_IP);
	    			DatagramPacket request_resend = new DatagramPacket(message, message.length, host_resend, LEADER_HOST_PORT);
	    			socket.send(request_resend);
	    		}
	    	}
	    }
	    catch(Exception e){
	    	e.printStackTrace();
	    }
		finally{
			if(socket != null){
				socket.close();
				}
		}
		return null;	
	}
	
	public static void main(String[] args) throws Exception {
		Front_End_Impl server = new Front_End_Impl();

		server.ORBsetup(args); // ORB setup function	
		server.Front_End_Listener(); //setup UDP listener for communicating with leader host

	}

	private void Front_End_Listener() {
		Thread getLeader = new Thread(new Runnable() {
			@Override
			public void run() {
				DatagramSocket socket = null;
				try{
					socket = new DatagramSocket(FEport); // 4000
					while(true){
						System.out.println("start listener for communicating with leader host");
						byte[] buffer = new byte[1000]; 
						DatagramPacket request = new DatagramPacket(buffer, buffer.length);
						socket.receive(request);
						String content = new String(request.getData()).trim();
						if(content.equals("leader?")){
							String resend_leader_port = Integer.toString(LEADER_HOST_PORT);
							DatagramPacket reply_leader = new DatagramPacket(resend_leader_port.getBytes(),resend_leader_port.getBytes().length, request.getAddress(), request.getPort());
							socket.send(reply_leader);
						}else{
							int leader_port = Integer.parseInt(new String(request.getData()).trim());
							LEADER_HOST_PORT = leader_port;
							String acknowledgement = "OK";
							DatagramPacket update_leader = new DatagramPacket(acknowledgement.getBytes(),acknowledgement.getBytes().length, request.getAddress(), request.getPort());
							System.out.println("leader host's port is " + leader_port);
							socket.send(update_leader);
						}
					}	
				}
				catch(Exception e){
					e.printStackTrace();
				}
				finally{
					if(socket != null) socket.close();
				}
			}
		});
		System.out.println("start listening... ...");
		getLeader.start();
	}


}
