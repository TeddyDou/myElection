package Client;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.omg.CORBA.ORB;

import FrontEnd_CORBA.Front_End;
import FrontEnd_CORBA.Front_EndHelper;

public class ManagerClient {
	
	static String MANAGER_ID = null;
	
	/**
	 * this is a constructor of the class
	 */
	public ManagerClient() {
		super();
	}
	
	/**
	 * This is a local function for check manager format use Regular expression.
	 * @param n_managerID
	 * @return
	 */
	public static Boolean checkManagerIDFormat(String n_managerID){
		String pattern = "^(MTL|LVL|DDO)(\\d{4})$";
		Pattern re = Pattern.compile(pattern,Pattern.CASE_INSENSITIVE);
		Matcher matcher = re.matcher(n_managerID);
		if(matcher.find()){
			return true;
		}else{
			System.err.println("Usage:[MTL,LVL,DDO]+[1000]\n");
			return false;
		}
	}
	
	/**
	 * This is a loop for require user to input the managerID, like Login.
	 */
	public static void checkManagerLogIn(){
		Boolean valid = false;
		while(!valid){
			Scanner keyboard = new Scanner(System.in);
			do{
				System.out.println("****Please input the manager ID****");
				MANAGER_ID = keyboard.next();
			}while(!checkManagerIDFormat(MANAGER_ID));
			valid = true;
		}
	}
	
	/**
	 * Define the Menu list.
	 * @param managerID
	 */
	public static void showMenu(String managerID) {
		System.out.println("****Welcome to DSMS****");
		System.out.println("****Manager: "+managerID +"****\n");
		System.out.println("Please select an option (1-5)");
		System.out.println("1. Create Doctor Record.");
		System.out.println("2. Create Nurse Record");
		System.out.println("3. Get Record Counts");
		System.out.println("4. Edit Record");
		System.out.println("5. Transfer Record");
		System.out.println("6. Exit DSMS");
	}
	
	public static void main(String[] args) throws Exception{
		
		ORB orb = ORB.init(args, null);
		BufferedReader br = new BufferedReader(new FileReader("ior.txt"));
		String FEIor = br.readLine();
		System.out.println("READ SERVER!");

		br.close();
		org.omg.CORBA.Object FEobject = orb.string_to_object(FEIor);

		final Front_End STUB = Front_EndHelper.narrow(FEobject);

		System.out.println("Start client...");
		
		checkManagerLogIn();
		
		try {
			
			int userChoice=0;
			Scanner keyboard = new Scanner(System.in);
			showMenu(MANAGER_ID);
			
			while(true)
			{
				Boolean valid = false;
				while(!valid)
				{
					try{
						userChoice=keyboard.nextInt();
						valid=true;
					}
					catch(Exception e)
					{
						System.out.println("Invalid Input, please enter an Integer");
						valid=false;
						keyboard.nextLine();
					}
				}
				
				switch(userChoice)
				{
				case 1:
					System.out.println("Please input the FirstName");
					String d_firstname = keyboard.next();
					System.out.println("Please input the LastName");
					String d_lastname = keyboard.next();
					System.out.println("Please input the Address");
					String d_address = keyboard.next();
					System.out.println("Please input the Phone");
					String d_phone = keyboard.next();
					System.out.println("Please input the Specialization");
					String d_specialization = keyboard.next();
					System.out.println("Please input the Location(mtl/lvl/ddo)");
					String d_location =keyboard.next();
					String d_result = STUB.createDRecord(MANAGER_ID, d_firstname, d_lastname, d_address, d_phone, d_specialization, d_location);
					
					if(!d_result.contains("is not right")){
						System.out.println("Manager Create Doctor Record Succeed!" + "\n" + d_result);
					}
					showMenu(MANAGER_ID);
					break;
				case 2:
					System.out.println("Manager Choose Create Nurse Record.");
					System.out.println("Please input the FirstName");
					String n_firstname = keyboard.next();
					System.out.println("Please input the LastName");
					String n_lastname = keyboard.next();
					System.out.println("Please input the Designation(junior/senior)");
					String n_designation = keyboard.next();
					System.out.println("Please input the Status(active/terminated)");
					String n_status = keyboard.next();
					System.out.println("Please input the Status Date(yyyy/mm/dd/)");
					String n_status_date = keyboard.next();
					String n_result = STUB.createNRecord(MANAGER_ID ,n_firstname, n_lastname, n_designation, n_status, n_status_date);
					
					if(!n_result.contains("is not right")){
						System.out.println("Manager Create Doctor Record Succeed!" + "\n" + n_result);
					}
					showMenu(MANAGER_ID);
					break;
				case 3:
					System.out.println("Manager Choose Get Record Counts.");
					String s_result = STUB.getRecordCounts(MANAGER_ID);
					
					System.out.println("Get Record Counts: " + "\n" + s_result);
					showMenu(MANAGER_ID);
					break;
				case 4:
					System.out.println("Manager Choose Edit Record.");
					System.out.println("Please input the RecordID");
					String e_recordID = keyboard.next();
					System.out.println("Please input the FieldName");
					String e_fieldname = keyboard.next();
					System.out.println("Please input the New Value");
					String e_newvalue = keyboard.next();
					String e_result = STUB.editRecord(MANAGER_ID, e_recordID, e_fieldname, e_newvalue);
					
					if(!e_result.contains("is not right")){
						System.out.println("Manager Create Doctor Record Succeed!" + "\n" + e_result);
					}
					showMenu(MANAGER_ID);
					break;
				case 5:
					System.out.println("Manager Choose Transfer Record.");
					System.out.println("Please input the RecordID");
					String t_recordID = keyboard.next();
					System.out.println("Please input the remote clinic server name.(mtl/lvl/ddo)");
					String t_remoteClinicServerName = keyboard.next();
					String t_result = STUB.transferRecord(MANAGER_ID, t_recordID, t_remoteClinicServerName);
					
					if(!t_result.contains("is not right")){
						System.out.println("Manager Transfer Record Succeed!" + "\n" + t_result);
					}
					showMenu(MANAGER_ID);
					break;
				case 6:
					System.out.println("Manager Exit the DSMS");
					System.out.println("Have a nice day!");
					keyboard.close();
					System.exit(0);
				default:
					System.out.println("Invalid Input, please try again.");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
