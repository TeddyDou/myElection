package ReplicaManager2;

public class ReplicaManagerInfo {

	private String name;
	private String IP;
	private int local_port;
	private int ID;
	private int election_port;
	
	public ReplicaManagerInfo(String name, String iP, int localPort, int iD, int electionPort) {
		super();
		this.name = name;
		IP = iP;
		this.local_port = localPort;
		ID = iD;
		this.election_port = electionPort;
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

	public int getLocal_port() {
		return local_port;
	}

	public void setLocal_port(int local_port) {
		this.local_port = local_port;
	}

	public int getElection_port() {
		return election_port;
	}

	public void setElection_port(int election_port) {
		this.election_port = election_port;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}
	

}