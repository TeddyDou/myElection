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
	public static int REPLICA_MANAGER_ID = 3;
	public static int LOCAL_PORT = 5002;
	public static int BROAD_CAST_PORT = 5102;
	public static int HEARTBEAT_PORT = 5007;
	public static int LEADER_ELECTION_PORT = 5103;

	public static void main(String[] args) throws Exception {
		LeaderElection el = new LeaderElection();
		System.out.println(el.initialElection());
		System.out.println("Front End Listener read and waiting");
		new Thread(new Replica_Manager_Listener(REPLICA_MANAGER_NAME)).start();
		System.out.println("Broad Cast Listener read and waiting");
		new Thread(new Broad_Cast_Listener(REPLICA_MANAGER_NAME)).start();
		System.out.println("Heart Beat Listener read and waiting");
		
		Heart h1 = new Heart("localhost", 5005);
		h1.beat();
		Heart h2 = new Heart("localhost", 5006);
		h2.beat();
		new Heart_Beat_Listener(HEARTBEAT_PORT, new HeartBeatCallBack() {
			
			public void up() {
				System.out.println("REPLICA " + REPLICA_MANAGER_NAME + " IS UP");
			}
			
			public void down() {
				System.out.println("REPLICA " + REPLICA_MANAGER_NAME + " IS DOWN");
				try {
					System.out.println(el.ElectLeader());
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}
