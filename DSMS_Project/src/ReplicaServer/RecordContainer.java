package ReplicaServer;

import java.util.HashMap;

public class RecordContainer {
	
	protected long nextId = 10000;
	
	protected final Object idlock = new Object();
    final HashMap<String, Record>[] records = new HashMap[26];
    final Object recordsLock = new Object();

	public static RecordContainer getRecordContainer(String name) {
		RecordContainer container = new RecordContainer();
        container.createRecordContainer();
        return container;
	}

	private void createRecordContainer() {
		for (int i = 0; i < 26; i++) {
            records[i] = new HashMap<String, Record>();
        }
		
	}

	public int getRecordCount() {
		int count = 0;
        synchronized (records) {
            for (HashMap map : records) {
                count += map.size();
            }
        }
        return count;
	}

	public long getNextFreeId() {
		synchronized (idlock) {
            return nextId++;
		}
	}

	public void addRecord(Record record) {
		String lastName = record.getLastName();

        // get the lower five bits representing which letter we are after
        // minus one since letters start at 1

        //int entry = (Character.getNumericValue(lastName.charAt(0)) & 0x1f) - 1;

        int entry = lastName.toLowerCase().codePointAt(0) - "a".codePointAt(0);

        if (entry < 0 || entry >= 26) {
            throw new RuntimeException(lastName + " is a bad last name");
        }

        HashMap<String, Record> entries = records[entry];

        synchronized (entries) {
            entries.put(record.getId(), record);
        }
		
	}

	public void removeRecord(String id, String lastName) {
		int entry = lastName.toLowerCase().codePointAt(0) - "a".codePointAt(0);

        if (entry < 0 || entry >= 26) {
            throw new RuntimeException(lastName + " is a bad last name");
        }

        HashMap<String, Record> entries = records[entry];

        synchronized (entries) {
            entries.remove(id);
        }
	}

	public Record getRecord(String recordID) {
		for (HashMap<String, Record> entries : records) {
            synchronized (entries) {
                if (entries.containsKey(recordID)) {
                    return entries.get(recordID);
                }
            }
        }
        return null;
	}

}
