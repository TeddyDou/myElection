package ReplicaServer;

import java.util.HashMap;
import java.util.Map;


public class RecordFactory {

	public static Record createRecordFromMap(Map<String, String> map) {

		String recordID = map.get("id");
        String firstName = map.get("fn");
        String lastName = map.get("ln");
        String type = map.get("ty");
        
        int idnum = (int) Long.parseLong(recordID.replace(type, ""), 10);
        
        if ("DR".equals(type)) {
        	 String address = map.get("ad");
             String phone = map.get("ph");
             String specialization = map.get("sp");
             String location = map.get("lo");
             return new DoctorRecord(idnum, firstName, lastName, address, phone, specialization, location);

        }
        if ("MR".equals(type)) {
            
            String designation = map.get("de");
            String status = map.get("st");
            String statusDate = map.get("sd");
            return new NurseRecord(idnum, firstName, lastName, designation, status, statusDate);
        }
      
		return null;
	}

	public static Map<String, String> createMapFromRecord(Record record) {
		Map<String, String> map = new HashMap<String, String>();
        map.put("id", record.getId());
        map.put("fn", record.getFirstName());
        map.put("ln", record.getLastName());
        map.put("ty", record.getRecordType());

        try {
            DoctorRecord dr = (DoctorRecord) record;
            map.put("ad", dr.getAddress());
            map.put("ph", dr.getPhone());
            map.put("sp", dr.getSpecialization());
            map.put("lo", dr.getLocation());
            		
        } catch (ClassCastException ex) {
        }

        try {
            NurseRecord nr = (NurseRecord) record;
            map.put("de", nr.getDesignation());
            map.put("st", nr.getStatus());
            map.put("sd", nr.getStatusDate());
        } catch (ClassCastException ex) {
        }

        return map;
	}

}
