package ReplicaManager3;

import java.util.HashMap;
import java.util.Map;

import BroadCaster.Broad_Cast_Listener;
import HeartBeat.Heart;
import HeartBeat.HeartBeatCallBack;
import HeartBeat.Heart_Beat_Listener;

public class ReplicaManager3 {
	
	public static Map<String, String> REQUEST_HASH_TABLE = new HashMap<String, String>();
	
	public static int FRONT_END_PORT = 4000;
	public static String REPLICA_MANAGER_NAME = "rep3";
	public static String REPLICA_MANAGER_IP = "localhost";
	public static int REPLICA_MANAGER_ID = 1;
	public static int LOCAL_PORT = 5002;
	public static int BROAD_CAST_PORT = 6002;
	public static int HEARTBEAT_PORT1 = 5006;
	public static int HEARTBEAT_PORT2 = 5016;
	public static int LEADER_ELECTION_PORT = 7003;
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
		
		Heart h1 = new Heart("localhost", 5025);
		h1.beat();
		Heart h2 = new Heart("localhost", 5026);
		h2.beat();
		new Heart_Beat_Listener(HEARTBEAT_PORT1, new HeartBeatCallBack() {
			
			public void up() {
				System.out.println("REPLICA " + "rep1" + " IS UP");
			}
			
			public void down() {
				System.out.println("REPLICA " + "rep1" + " IS DOWN");
				try {
					System.out.println("New leader port is: " + el.ElectLeader());
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		new Heart_Beat_Listener(HEARTBEAT_PORT2, new HeartBeatCallBack() {
			
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

	}

}
