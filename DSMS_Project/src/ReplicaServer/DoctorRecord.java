package ReplicaServer;

public class DoctorRecord extends Record {
	
	public DoctorRecord(long recordID, String firstName, String lastName, String address, String phone, String specialization, String location){
		super(recordID, firstName, lastName, address, phone, specialization, location);
	}

	@Override
	public String getRecordType() {
		// TODO Auto-generated method stub
		return "DR";
	}
	
	public String toString() {
		String dr = "First Name: "+ getFirstName() + "\n" 
					+ "Last Name: " + getLastName() + "\n"
					+ "Address: " + getAddress() + "\n"
					+ "Phone: " + getPhone() + "\n"
					+ "specialization: " + getSpecialization() + "\n"
					+ "location: " + getLocation() + "\n";
		return dr;
	}

}
