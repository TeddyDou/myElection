package ReplicaServer.DSMS_CORBA;

/**
 * Interface definition: DSMS_Interface.
 * 
 * @author OpenORB Compiler
 */
public interface DSMS_InterfaceOperations
{
    /**
     * Operation createDRecord
     */
    public String createDRecord(String managerID, String firstName, String lastName, String address, String phone, String specialization, String location);

    /**
     * Operation createCRecord
     */
    public String createNRecord(String managerID, String firstName, String lastName, String designation, String status, String statusDate);

    /**
     * Operation getRecordCounts
     */
    public String getRecordCounts(String managerID);

    /**
     * Operation editRecord
     */
    public String editRecord(String managerID, String recordID, String fieldName, String newValue);

    /**
     * Operation transferRecord
     */
    public String transferRecord(String managerID, String recordID, String remoteClinicServerName);

}
