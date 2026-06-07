package ST;

public class CalculateAllScore extends Bicycle{ 
	static int breakCounter, doubleJumpCounter, throughMapCounter = 0;
	static double scoreX;
	
	public static void addTotalBrake() {
		if(velocityX != 0) breakCounter++;
	}
	
	public static void addTotalDoubleJump() {
		if(velocityY != 0)doubleJumpCounter++;
	}
	
	public static void addTotalThroughMap() {
		throughMapCounter++;
	}
	
	public static void addTotalX() {
		scoreX += velocityX;
	}
	
	public static int getTotalBreak() {
		return breakCounter;
	}
	
	public static int getTotalDoubleJump() {
		return doubleJumpCounter;
	}
	
	public static int getTotalThroughMap() {
		return throughMapCounter;
	}
	
	public static int getTotalScore() {
		return (int) scoreX;
	}
	
	public static int getAllScore() {
		return (int) getTotalScore() - 10 * (getTotalBreak() + getTotalDoubleJump()) + 100 * getTotalThroughMap();
	}
	
	public static void resetAllCounter() {
		breakCounter = doubleJumpCounter = throughMapCounter = 0;
		scoreX = 0;
	}
}
