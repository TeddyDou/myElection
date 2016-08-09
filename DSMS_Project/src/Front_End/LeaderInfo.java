package Front_End;

public class LeaderInfo {
	
	private static boolean leaderExistFlag = false;
	private static boolean questingLeaderFlag = false;
	public static final Object lock = new Object();
	
	public static boolean isQuestingLeaderFlag() {
		return questingLeaderFlag;
	}

	public static void setQuestingLeaderFlag(boolean questingLeaderFlag) {
		LeaderInfo.questingLeaderFlag = questingLeaderFlag;
	}

	public static void setLeaderExistFlag(boolean leaderExistFlag) {
		LeaderInfo.leaderExistFlag = leaderExistFlag;
	}

	public static boolean isLeaderExist() {
        return leaderExistFlag;
    }
}
