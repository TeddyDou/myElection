package ReplicaManager;

import java.util.HashMap;
import java.util.Map.Entry;

public class leader {
	public String initialize() {
		int i = GroupMemberPriority().size();
		//clock(5);
		if (i > 0){
			System.out.println("success to start the replicas!");
			return electMaxID(GroupMemberPriority());
		}
		else {
			System.out.println("Fail to start the replicas!");
			return "HEllo";
		}
	}

	public HashMap<String, Integer> GroupMemberPriority() {
		// TODO Auto-generated method stub
		HashMap<String, Integer> gms = new HashMap<String, Integer>();
		gms.put("rep1", 3);
		gms.put("rep2", 2);
		gms.put("rep3", 1);
		
		return gms;
	}
	
	public String electMaxID(HashMap<String, Integer> gms) {
	int max = 0;
	for(Entry<String, Integer> entry : gms.entrySet()){
		if (entry.getValue() > max)
			max = entry.getValue();
	}
	for(Entry<String, Integer> entry : gms.entrySet()){
		if (entry.getValue() == max)
			return entry.getKey();
	}
	return null;
}
}
