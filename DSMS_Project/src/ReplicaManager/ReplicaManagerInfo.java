package ReplicaManager;

public class ReplicaManagerInfo {

	private String name;
	private String IP;
	private int port;
	private int ID;
	
	public ReplicaManagerInfo(String name, String iP, int port, int iD) {
		super();
		this.name = name;
		IP = iP;
		this.port = port;
		ID = iD;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIP() {
		return IP;
	}

	public void setIP(String iP) {
		IP = iP;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}
	

}
