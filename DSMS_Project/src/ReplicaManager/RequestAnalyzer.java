package ReplicaManager;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContext;
import org.omg.CosNaming.NamingContextHelper;

import ReplicaServer.DSMS_CORBA.DSMS_Interface;

public class RequestAnalyzer extends Thread{
	
	String result = null;
	
	String managerId;
    String clinicServer;
    DSMS_Interface Stub;

    DatagramSocket socket = null;
    DatagramPacket request = null;
	
	public RequestAnalyzer(String requestMessage) {
		
		String functionCMD = requestMessage.split(",")[1];
		String managerID = requestMessage.split(",")[2];
		
		Stub = getServerReferrence(managerID);
		
		switch (functionCMD) {
		case "createDRecord":
			String firstName_1 = requestMessage.split(",")[3];
			String lastName_1 = requestMessage.split(",")[4];
			String address_1 = requestMessage.split(",")[5];
			String phone_1 = requestMessage.split(",")[6];
			String specialization_1 = requestMessage.split(",")[7];
			String location_1 = requestMessage.split(",")[8];
			
			result = Stub.createDRecord(managerID, firstName_1, lastName_1, address_1, phone_1, specialization_1, location_1);
			break;
		case "createNRecord":
			String firstName_2 = requestMessage.split(",")[3];
			String lastName_2 = requestMessage.split(",")[4];
			String designation_2 = requestMessage.split(",")[5];
			String status_2 = requestMessage.split(",")[6];
			String statusDate_2 = requestMessage.split(",")[7];
			
			result = Stub.createNRecord(managerID, firstName_2, lastName_2, designation_2, status_2, statusDate_2);
			break;
		case "getRecordCounts":
			
			result = Stub.getRecordCounts(managerID);
			break;
		case "editRecord":
			String recordID_4 = requestMessage.split(",")[3];
			String fieldName_4 = requestMessage.split(",")[4];
			String newValue_4 = requestMessage.split(",")[5];
			
			result = Stub.editRecord(managerID, recordID_4, fieldName_4, newValue_4);
			break;
		case "transferRecord":
			String recordID_5 = requestMessage.split(",")[3];
			String remoteClinicServerName_5 = requestMessage.split(",")[4];
			
			result = Stub.transferRecord(managerID, recordID_5, remoteClinicServerName_5);
			break;
		}
		this.start();
	}
	
	public RequestAnalyzer(DatagramSocket n_socket, DatagramPacket n_request) {
		this.socket = n_socket;
		this.request = n_request;
		
		String functionCMD = new String(request.getData()).trim().split(",")[1];
		String managerID = new String(request.getData()).trim().split(",")[2];
		
		Stub = getServerReferrence(managerID);
		
		switch (functionCMD) {
		case "createDRecord":
			String firstName_1 = new String(request.getData()).trim().split(",")[3];
			String lastName_1 = new String(request.getData()).trim().split(",")[4];
			String address_1 = new String(request.getData()).trim().split(",")[5];
			String phone_1 = new String(request.getData()).trim().split(",")[6];
			String specialization_1 = new String(request.getData()).trim().split(",")[7];
			String location_1 = new String(request.getData()).trim().split(",")[8];
			
			result = Stub.createDRecord(managerID, firstName_1, lastName_1, address_1, phone_1, specialization_1, location_1);
			break;
		case "createNRecord":
			String firstName_2 = new String(request.getData()).trim().split(",")[3];
			String lastName_2 = new String(request.getData()).trim().split(",")[4];
			String designation_2 = new String(request.getData()).trim().split(",")[5];
			String status_2 = new String(request.getData()).trim().split(",")[6];
			String statusDate_2 = new String(request.getData()).trim().split(",")[7];
			
			result = Stub.createNRecord(managerID, firstName_2, lastName_2, designation_2, status_2, statusDate_2);
			break;
		case "getRecordCounts":
			
			result = Stub.getRecordCounts(managerID);
			break;
		case "editRecord":
			String recordID_4 = new String(request.getData()).trim().split(",")[3];
			String fieldName_4 = new String(request.getData()).trim().split(",")[4];
			String newValue_4 = new String(request.getData()).trim().split(",")[5];
			
			result = Stub.editRecord(managerID, recordID_4, fieldName_4, newValue_4);
			break;
		case "transferRecord":
			String recordID_5 = new String(request.getData()).trim().split(",")[3];
			String remoteClinicServerName_5 = new String(request.getData()).trim().split(",")[4];
			
			result = Stub.transferRecord(managerID, recordID_5, remoteClinicServerName_5);
			break;
		}
		this.start();
	}
	
	@Override
	public void run() {
		try {
			if(result!= null){
				System.out.println(result);
				DatagramPacket reply = new DatagramPacket(result.getBytes(),result.getBytes().length, request.getAddress(), request.getPort());
				socket.send(reply);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

	private DSMS_Interface getServerReferrence(String managerId) {
		this.managerId = managerId;
        this.clinicServer = managerId.substring(0, 3);

        try {
            // ghetto hardcode the parameters
            ORB orb = ORB.init(new String[]{"-ORBInitialHost", "localhost", "-ORBInitialPort", "1050"}, null);

            org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
            NamingContext ncRef = NamingContextHelper.narrow(objRef);

            NameComponent nc = new NameComponent(clinicServer, "");
            NameComponent path[] = {nc};
            Stub = ReplicaServer.DSMS_CORBA.DSMS_InterfaceHelper.narrow(ncRef.resolve(path));   
        } catch (Exception ex) {
        	ex.printStackTrace();
        }
        return Stub;
	}

}
