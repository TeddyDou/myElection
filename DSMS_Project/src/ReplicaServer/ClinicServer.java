package ReplicaServer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContext;
import org.omg.CosNaming.NamingContextHelper;


public class ClinicServer extends ReplicaServer.DSMS_CORBA._DSMS_InterfaceImplBase{
	
	final static String[] clinicServers = {"MTL", "LVL", "DDO"};
	
	String name, managerID;
    RecordContainer records;
    
    public ClinicServer(String clinicServer) {
    	super();
        this.name = clinicServer;
        this.records = RecordContainer.getRecordContainer(name);
        
        logFile(name, "Server "+ name + "is running");
	}
    
    /** Creates Logs **/
	public static void logFile(String fileName, String Operation)
			throws SecurityException {
		// System.out.println("loggin file");
		fileName = fileName + "_ServerLog.txt";
		File log = new File(fileName);
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();

		try {
			if (!log.exists()) {
			}
			log.setWritable(true);
			FileWriter fileWriter = new FileWriter(log, true);

			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
			bufferedWriter.write(Operation + " "
					+ dateFormat.format(date));
			bufferedWriter.newLine();
			bufferedWriter.close();
			// System.out.println("logged file");
		} catch (IOException e) {
			System.out.println("COULD NOT LOG!!");
		}
	}
	
	/**
     * Start all of the servers
     *
     * @param args
     */
    public static void main(String[] args) {
       
        // Start a thread of each station
        for (String clinicServer : clinicServers) {
            ClinicServer clinic = new ClinicServer(clinicServer);

            clinic.startServerThreads();
        }
    }
    
    /**
     * Given a serverName, determine which port its UDP server is on
     *
     * @param string clinicName
     * @return port
     */
    protected int portHash(String string) {
        int bucket = 0;
        for (char ch : string.toCharArray()) {
            // totally awkward mixing function
            bucket = (bucket * 94 + ch - 33) % (94 * 94 - 36);
        }
        // make sure the port number is not restricted by adding the first
        // non restricted port
        return bucket + 1024;
    }

	private void startServerThreads() {
		new Thread(new Runnable() {
            @Override
            public void run() {
                exportUDP();
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                exportRPC();
            }
        }).start();
		
	}
	
	/**
     * initial CORBA
     */
	public void exportRPC() {
		
		ORB orb = ORB.init(new String[]{"-ORBInitialHost", "localhost", "-ORBInitialPort", "1050"}, null);

        orb.connect(this);
        
        org.omg.CORBA.Object objRef;
        try {
            objRef = orb.resolve_initial_references("NameService");
        } catch (Exception ex) {
            throw new RuntimeException("NameService", ex);
        }
        NamingContext ncRef = NamingContextHelper.narrow(objRef);

        // bind the Object Reference in Naming
        NameComponent nc = new NameComponent(this.name, "");
        NameComponent path[] = {nc};
        try {
            ncRef.rebind(path, this);;
        } catch (Exception ex) {
            throw new RuntimeException("bind", ex);
        }
        System.out.println(this.name + "Clinic Server Exiting ...");
        orb.run();
		
	}
	
	protected void exportUDP() {
		int recvPort = portHash(name);
        DatagramSocket socket;
        try {
            socket = new DatagramSocket(recvPort);
        } catch (SocketException ex) {
        	System.out.println("UDP server could not be started, UDP will be unavailable");
            return;
        }
        System.out.println("UDP for " + name + " on " + portHash(name));
        // wait for packets
        byte[] buffer = new byte[1500];
        while (true) {
            DatagramPacket recvPacket = new DatagramPacket(buffer, buffer.length);
            try {
                socket.receive(recvPacket);

                InetAddress sendAddr = recvPacket.getAddress();
                int sendPort = recvPacket.getPort();
                String requestString = new String(buffer).substring(0, recvPacket.getLength());

                Map<String,String> request = MapSerializer.parse(requestString);
                Map<String,String> response = handleRequest(request);
                String responseString = MapSerializer.stringify(response);

                DatagramPacket sendPacket = new DatagramPacket(responseString.getBytes(), responseString.length(), sendAddr, sendPort);
                socket.send(sendPacket);

            } catch (IOException ex) {
                logFile(name,ex.toString() + " " + ex.getMessage());
            }
        }
	}

	/**
     * Take a UDP request map and return a response map
     *
     * @param request
     * @return
     */
	protected Map<String,String> handleRequest(Map<String, String> request) {
		String action = request.get("action");
		HashMap<String,String> response = new HashMap<String,String>();
		
		if (action == null) {
            action = null;
        }
		if ("recordCount".equals(action)) {
            int count = records.getRecordCount();
            response.put("recordCount", Integer.toString(count));
            return response;
        }
		if ("transfer".equals(action)) {
            long newid = records.getNextFreeId();
            request.put("id", Long.toString(newid));
            Record record = RecordFactory.createRecordFromMap(request);
            response.put("status", "success");
            records.addRecord(record);
            response.put("id", record.getId());
            return response;
        }
		
		response.put("error", "invalid Action");
		
        return response;
	
	}

	@Override
	public String createDRecord(String managerID, String firstName, String lastName, String address, String phone,
			String specialization, String location) {
		
		DoctorRecord record = new DoctorRecord(records.getNextFreeId(), firstName, lastName, address, phone, specialization, location);
		String id = record.getId();
		records.addRecord(record);
		logFile(name," Create Doctor Record: " + id + "\n" +record.toString());
		return id + "\n" +record.toString();
	}

	@Override
	public String createNRecord(String managerID, String firstName, String lastName, String designation, String status,
			String statusDate) {
		
		NurseRecord record = new NurseRecord(records.getNextFreeId(), firstName, lastName, designation, status, statusDate);
		String id = record.getId();
		records.addRecord(record);
		logFile(this.name," Create Nurse Record: " + id + "\n" +record.toString());
		return id + "\n" +record.toString();
	}

	@Override
	public String getRecordCounts(String managerID) {
		String counts = "";
		for (String clinicName : clinicServers){
			try {
                // only kind of request but send it anyways
                String request = "action:recordCount";

                DatagramSocket socket = new DatagramSocket();
                InetAddress addr = InetAddress.getByName("localhost");
                int port = this.portHash(clinicName);
                DatagramPacket packet = new DatagramPacket(request.getBytes(), request.length(), addr, port);
                socket.send(packet);
                byte[] buffer = new byte[1500];
                packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                // 
                String response = new String(buffer, 0, packet.getLength());

                System.out.println(response);

                if (response.startsWith("recordCount:")) {
                    String count = response.substring(response.indexOf(":") + 1);
                    counts += clinicName + " : " + count + ", ";
                } else {
                    counts += clinicName + " responded badly, ";
                }

                socket.close();
			}catch (SocketException ex) {
                throw new RuntimeException(ex);
            } catch (UnknownHostException ex) {
                throw new RuntimeException(ex);
            } catch (IOException ex) {
                // station unavailible
                throw new RuntimeException(ex);
            }
		}
		logFile(name, "Get the record count --" + "\n" + counts);
		return counts;
	}

	@Override
	public String editRecord(String managerID, String recordID, String fieldName, String newValue) {
		
		if(recordID.contains("DR")){
			Record drecord = records.getRecord(recordID);
			if (drecord != null){
				synchronized (drecord){
					if(fieldName.equalsIgnoreCase("address")){
						drecord.setAddress(newValue);
					}else if(fieldName.equalsIgnoreCase("phone")){
						drecord.setPhone(newValue);
					}else if(fieldName.equalsIgnoreCase("specialization")){
						drecord.setSpecialization(newValue);
					}else if(fieldName.equalsIgnoreCase("location")){
						drecord.setLocation(newValue);
					}
				}
			
			}
		}else if(recordID.contains("NR")){
			Record nrecord = records.getRecord(recordID);
			if (nrecord != null){
				synchronized (nrecord){
					if(fieldName.equalsIgnoreCase("designation")){
						nrecord.setDesignation(newValue);
					}else if(fieldName.equalsIgnoreCase("status")){
						nrecord.setStatus(newValue);;
					}else if(fieldName.equalsIgnoreCase("statusDate")){
						nrecord.setStatusDate(newValue);
					}
				}
			}
		}
		logFile(this.name, "Manger has edit the " + fieldName + " of " + recordID + " to new value: " + newValue);
		return "Manger has edit the " + fieldName + " of " + recordID + " to new value: " + newValue;
	}

	@Override
	public String transferRecord(String managerID, String recordID, String remoteClinicServerName) {
		if (!Arrays.asList(clinicServers).contains(remoteClinicServerName)) {
            logFile(this.name, remoteClinicServerName + " server is not in list");
            return "_FAIL_";
        }
		try{
			Record record = records.getRecord(recordID);
			if (record == null) {
                logFile(this.name, "  transferRecord <FAILED> -- record not found");
                return "_FAIL_";
            }
			Map<String, String> request = RecordFactory.createMapFromRecord(record);

            request.put("action", "transfer");

            DatagramSocket socket = new DatagramSocket();
            InetAddress addr = InetAddress.getByName("localhost");

            String requeststr = MapSerializer.stringify(request);

            int port = this.portHash(remoteClinicServerName);

            DatagramPacket packet = new DatagramPacket(requeststr.getBytes(), requeststr.length(), addr, port);
            socket.send(packet);

            byte[] buffer = new byte[1500];
            packet = new DatagramPacket(buffer, buffer.length);
            socket.receive(packet);
			
            Map<String, String> response = MapSerializer.parse(new String(buffer, 0, packet.getLength()));

            String status = response.get("status");
            
            if ("success".equals(status)) {
                records.removeRecord(record.getId(), record.getLastName());
                logFile(this.name," TransferRecord Success : the Record - " + recordID + "has been transferred to " + remoteClinicServerName);
            } else {
            	logFile(this.name," transferRecord <FAIL> " );
            }

            socket.close();

            if (response.containsKey("id")) {
                return response.get("id")+ " has been transferred to " + remoteClinicServerName; // return new id
            }
            return "_FAIL_";
		}catch (Exception ex) {
            logFile(this.name, " transferRecord <FAILED> connection failed");
            return "_FAIL_";
        }
	}

}
