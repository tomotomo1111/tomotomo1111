package ST;

public class Death {
	public static boolean isHitStop;
	
	public static int shakeScreenDegree_X = 0;
	public static int shakeScreenDegree_Y = 0;
	private static int HitStopTimer = 0;
	private static final int HitStopTimer_MAX = 30;
	private static int degree = 0;
	
	public static void deathEffect() {
		hitStop();
		shakeScreen();
	}
	
	public static void hitStop() {
		if(HitStopTimer == HitStopTimer_MAX) {
			HitStopTimer = 0;
			isHitStop = false;
			shakeScreenDegree_X = shakeScreenDegree_Y = 0;
		} else {
			HitStopTimer++;
			isHitStop = true;
		}
	}
	
	public static void shakeScreen() {
		
		makeDegree();
		shakeScreenDegree_X += degree;
		makeDegree();
		shakeScreenDegree_Y += degree;
		
	}
	
	private static void makeDegree() {
		degree = (int) Math.random() * 1 + 1;
		if(HitStopTimer >= HitStopTimer_MAX/2) {
			degree = -degree;
		}
	}
	
	public static void slowDrawing() {
		
	}
}
