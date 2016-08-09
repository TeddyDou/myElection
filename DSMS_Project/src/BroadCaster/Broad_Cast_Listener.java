package BroadCaster;

import java.util.HashMap;

import ReplicaManager.RequestAnalyzer;

public class Broad_Cast_Listener implements Runnable , ServerInterface{
	
	String name;
	
	public Broad_Cast_Listener(String _name) {
		this.name = _name;
	}
	
	@Override
	public void run() {
		
		try{
			UDPServer rRep1 = new UDPServer(this);
			while(true){
				System.out.println("start Broad Cast listener");
				String requestMessage = rRep1.deliver();
				System.out.println(requestMessage);
				new RequestAnalyzer(requestMessage);
			}
		}
		catch(Exception e){
			e.printStackTrace();
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
