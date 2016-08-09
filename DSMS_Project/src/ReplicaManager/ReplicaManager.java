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
	public static int LOCAL_PORT = 5000;
	public static int BROAD_CAST_PORT = 5004;
	public static int HEARTBEAT_PORT = 5005;
	public static int LEADER_ELECTION_PORT = 5006;

	public static void main(String[] args) throws Exception {
		leader el = new leader();
		System.out.println(el.initialize());
		System.out.println();
		System.out.println("Front End Listener read and waiting");
		new Thread(new Replica_Manager_Listener(REPLICA_MANAGER_NAME)).start();
		System.out.println("Broad Cast Listener read and waiting");
		new Thread(new Broad_Cast_Listener(REPLICA_MANAGER_NAME)).start();
		System.out.println("Heart Beat Listener read and waiting");
		
		Heart h1 = new Heart("localhost", 5005);
		h1.beat();
		Heart h2 = new Heart("localhost", 5005);
		h2.beat();
		new Heart_Beat_Listener(HEARTBEAT_PORT, new HeartBeatCallBack() {
			
			public void up() {
				System.out.println("REPLICA " + REPLICA_MANAGER_NAME + " IS UP");
			}
			
			public void down() {
				System.out.println("REPLICA " + REPLICA_MANAGER_NAME + " IS DOWN");
				try {
					//new leaderElection().initialize();
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}
