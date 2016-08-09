package HeartBeat;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPTransporter {
	
	// this function sends a message to a port and waits for reply
	public static String transport(String host, int port, String msg) {
		try {
			DatagramSocket clientSocket = new DatagramSocket();
			InetAddress IPAddress = InetAddress.getByName(host);
			byte[] sendData = msg.getBytes();
			byte[] receiveData = new byte[4096];
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
			clientSocket.send(sendPacket);
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			clientSocket.receive(receivePacket);
			String out = new String(receivePacket.getData());
			clientSocket.close();
			return out;
		}catch (Exception e) {
			return null;
		}
		
	}
	
	public static void send(String host, int port, String msg) {
		try {
			DatagramSocket clientSocket = new DatagramSocket();
			InetAddress IPAddress = InetAddress.getByName(host);
			byte[] sendData = msg.getBytes();
			byte[] receiveData = new byte[4096];
			
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
			clientSocket.send(sendPacket);
			clientSocket.close();
		}catch (Exception e) {
		}
		
	}
	
	/// this create a server that listens
	// on the specified port and runs the function
	// whenever any message come.
	public static void server(int port, final PacketServer packetServer) throws Exception{
		final DatagramSocket server = new DatagramSocket(port);
		Thread listener = new Thread(new Runnable() {
			
			public void run() {
				while (true) {
					byte[] recData = new byte[1024];
					DatagramPacket receivePacket = new DatagramPacket(recData, recData.length);
					try {
						server.receive(receivePacket);
					} catch (IOException e) {
						e.printStackTrace();
						return;
					}
					
					String reply = packetServer.serve(new String(receivePacket.getData()));
					if (reply != null) {
						byte[] send = reply.getBytes();
						int prt = receivePacket.getPort();
						InetAddress IPAddress = receivePacket.getAddress();
						DatagramPacket sendPacket = new DatagramPacket(send, send.length, IPAddress, prt);
						try {
							server.send(sendPacket);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					
					
				}	
			}
		});
		listener.start();
	}
	
}
