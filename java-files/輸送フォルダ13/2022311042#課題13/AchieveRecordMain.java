import java.io.*;
public class AchieveRecordMain{
	
	public static void main(String[] args) throws IOException{
		AchievementRecord record = new AchievementRecord("Account.txt");
		record.WriteFile("Account.txt");
		record.CreateClass();
	}
}