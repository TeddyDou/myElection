package ReplicaManager;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map.Entry;

import BroadCaster.ServerInterface;
import BroadCaster.UDPServer;

public class Replica_Manager_Listener implements Runnable, ServerInterface{
	
	String name;

	public Replica_Manager_Listener(String _name) {
		this.name = _name;
	}
	
	
	
	@Override
    public void run() {
        DatagramSocket socket = null;
        
        try {
        	UDPServer broadcast = new UDPServer(this);
            socket = new DatagramSocket(ReplicaManager.LOCAL_PORT); // port: 5000
            while (true) {
                System.out.println("start FE listener");
                byte[] buffer = new byte[1000];
                DatagramPacket request = new DatagramPacket(buffer, buffer.length);
                socket.receive(request);

                String request_ID = new String(request.getData()).trim().split("\n")[0];
                System.out.println("add request: " + request_ID + " to hash table");
                ReplicaManager.REQUEST_HASH_TABLE.put(request_ID, new String(request.getData()).trim().toString());

                String acknowledgement = "OK";
                DatagramPacket reply = new DatagramPacket(acknowledgement.getBytes(), acknowledgement.getBytes().length, request.getAddress(), request.getPort());
                
                broadcast.RMulticast(new String(request.getData()).trim().toString());
               
                System.out.println("delete request: " + request_ID + " from hash table");
                ReplicaManager.REQUEST_HASH_TABLE.remove(request_ID);

                System.out.println("Send acknowledgement back to FE." + reply.getPort());
                socket.send(reply);
                System.out.println("Create new thread to handle the request from FE");
                new RequestAnalyzer(socket, request);
                

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (socket != null) socket.close();
        }
    }

	@Override
	public HashMap<String, String> getGroupMember() {
		// TODO Auto-generated method stub
		HashMap<String, String> gm = new HashMap<String, String>();
		gm.put("rep1", "localhost:5004");
		gm.put("rep2", "192.168.1.3:5004");
		gm.put("rep3", "192.168.1.4:5004");
		return gm;
	}
	@Override
	public String getMyServerName() {
		// TODO Auto-generated method stub
		return name;
	}

}
