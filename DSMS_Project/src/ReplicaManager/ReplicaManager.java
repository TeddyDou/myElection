package ReplicaManager;

import java.util.HashMap;
import java.util.Map;

import BroadCaster.Broad_Cast_Listener;
import HeartBeat.Heart;
import HeartBeat.HeartBeatCallBack;
import HeartBeat.Heart_Beat_Listener;

public class ReplicaManager {
	
	public static Map<String, String> REQUEST_HASH_TABLE = new HashMap<String, String>();
	
	public static int FRONT_END_PORT = 4000;
	public static String REPLICA_MANAGER_NAME = "rep1";
	public static String REPLICA_MANAGER_IP = "localhost";
	public static int REPLICA_MANAGER_ID = 3;
	public static int LOCAL_PORT = 5000;
	public static int BROAD_CAST_PORT = 6000;
	public static int HEARTBEAT_PORT1 = 5015;
	public static int HEARTBEAT_PORT2 = 5025;
	public static int LEADER_ELECTION_PORT = 7001;
	private static int leader_port;

	public static void main(String[] args) throws Exception {
		LeaderElection el = new LeaderElection();
		leader_port = el.initialElection();
		System.out.println("Current leader port: " + leader_port);
		System.out.println("Front End Listener read and waiting");
		new Thread(new Replica_Manager_Listener(REPLICA_MANAGER_NAME)).start();
		System.out.println("Broad Cast Listener read and waiting");
		new Thread(new Broad_Cast_Listener(REPLICA_MANAGER_NAME)).start();
		System.out.println("Heart Beat Listener read and waiting");
		
		Heart h1 = new Heart("localhost", 5005);
		h1.beat();
		Heart h2 = new Heart("localhost", 5006);
		h2.beat();
		
		new Heart_Beat_Listener(HEARTBEAT_PORT1, new HeartBeatCallBack() {
			
			public void up() {
				System.out.println("REPLICA " + "rep2" + " IS UP");
			}
			
			public void down() {
				System.out.println("REPLICA " + "rep2" + " IS DOWN");
				try {
					System.out.println("New leader port is: " + el.ElectLeader());
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		new Heart_Beat_Listener(HEARTBEAT_PORT2, new HeartBeatCallBack() {
			
			public void up() {
				System.out.println("REPLICA " + "rep3" + " IS UP");
			}
			
			public void down() {
				System.out.println("REPLICA " + "rep3" + " IS DOWN");
				try {
					System.out.println("New leader port is: " + el.ElectLeader());
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}

}
