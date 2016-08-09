package ReplicaServer;

public abstract class Record {
	
	protected long id;
	protected String firstName, lastName, address, phone, specialization, location, designation, status, statusDate;
	
	protected abstract String getRecordType();
	
	public Record(long id, String firstName, String lastName, String address, String phone, String specialization, String location) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
		this.phone = phone;
		this.specialization = specialization;
		this.location = location;
    }
	
	public Record(long id, String firstName, String lastName, String designation, String status, String statusDate){
		this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.designation = designation;
		this.status = status;
		this.statusDate = statusDate;
	}
	
	/**
    *
    * @return
    */
   public String getFirstName() {
       return firstName;
   }

   /**
    *
    * @param firstName
    */
   public void setFirstName(String firstName) {
       this.firstName = firstName;
   }

   /**
    *
    * @return
    */
   public String getLastName() {
       return lastName;
   }

   /**
    *
    * @param lastName
    */
   public void setLastName(String lastName) {
       this.lastName = lastName;
   }

	
	/**
	 * this is a method of get address
	 * @return
	 */
	public String getAddress() {
		return address;
	}
	
	/**
	 * this is a method of set address
	 * @param address
	 */
	public void setAddress(String address) {
		this.address = address;
	}
	
	/**
	 * this is a method of get phone number
	 * @return
	 */
	public String getPhone() {
		return phone;
	}
	
	/**
	 * this is a method of set phone number
	 * @param phone
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	/**
	 * this is a method of get specialization
	 * @return
	 */
	public String getSpecialization() {
		return specialization;
	}
	
	/**
	 * this is a method of set specialization
	 * @param specialization
	 */
	public void setSpecialization(String specialization) {
		this.specialization = specialization;
	}
	
	/**
	 * this is a method of get the location
	 * @return
	 */
	public String getLocation() {
		return location;
	}
	
	/**
	 * this is a method of set the location
	 * @param location
	 */
	public void setLocation(String location) {
		this.location = location;
	}
	
	/**
	 * this is a method of get designation
	 * @return
	 */
	public String getDesignation() {
		return designation;
	}

	/**
	 * this is a method of set designation
	 * @param designation
	 */
	public void setDesignation(String designation) {
		this.designation = designation;
	}
	/**
	 * this is method of get status
	 * @return
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * this is a method of set status
	 * @param status
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	
	/**
	 * this is a method of get status date
	 * @return
	 */
	public String getStatusDate() {
		return statusDate;
	}
	
	/**
	 * this is a method of set status date 
	 * @param statusDate
	 */
	public void setStatusDate(String statusDate) {
		this.statusDate = statusDate;
	}

	public String getId() {
		// TODO Auto-generated method stub
		return getRecordType() + id;
	}

}
