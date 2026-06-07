import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;

public class MouseMove {
	
	static PointerInfo pi = MouseInfo.getPointerInfo();
	static Point pt = pi.getLocation();;
	static PointerInfo pia = MouseInfo.getPointerInfo();
	static Point pta = pia.getLocation();
	static PointerInfo pif = MouseInfo.getPointerInfo();
	static Point ptf = pif.getLocation();
	
	static Robot robot;
	
	static boolean enableMove = true;
	
	static final int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
	static final int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
	static int count = 30000;
	static int angle;
	static int[] prePosY = new int[10];
	
	public static void main(String[] args) throws AWTException{
		robot = new Robot();
		
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		while(enableMove) {
			
			if(count % 9 == 0) {
				pressWarpIsland();
			}
			
			swing();
			
			count--;
			if(count <= 0) enableMove = false;
		}
	}
	
	public static void pressWarpIsland() {
		
		robot.keyPress(KeyEvent.VK_SLASH);
		robot.keyRelease(KeyEvent.VK_SLASH);
		robot.delay(100);
		robot.keyPress(KeyEvent.VK_W);
		robot.keyRelease(KeyEvent.VK_W);
		robot.delay(100);
		robot.keyPress(KeyEvent.VK_A);
		robot.keyRelease(KeyEvent.VK_A);
		robot.delay(100);
		robot.keyPress(KeyEvent.VK_R);
		robot.keyRelease(KeyEvent.VK_R);
		robot.delay(100);
		robot.keyPress(KeyEvent.VK_P);
		robot.keyRelease(KeyEvent.VK_P);
		robot.delay(100);
		robot.keyPress(KeyEvent.VK_SPACE);
		robot.keyRelease(KeyEvent.VK_SPACE);
		robot.delay(100);
		robot.keyPress(KeyEvent.VK_I);
		robot.keyRelease(KeyEvent.VK_I);
		robot.delay(100);
		robot.keyPress(KeyEvent.VK_S);
		robot.keyRelease(KeyEvent.VK_S);
		robot.delay(100);
		robot.keyPress(KeyEvent.VK_L);
		robot.keyRelease(KeyEvent.VK_L);
		robot.delay(100);
		robot.keyPress(KeyEvent.VK_A);
		robot.keyRelease(KeyEvent.VK_A);
		robot.delay(100);
		robot.keyPress(KeyEvent.VK_N);
		robot.keyRelease(KeyEvent.VK_N);
		robot.delay(100);
		robot.keyPress(KeyEvent.VK_D);
		robot.keyRelease(KeyEvent.VK_D);
		robot.delay(100);
		robot.keyPress(KeyEvent.VK_ENTER);
		robot.keyRelease(KeyEvent.VK_ENTER);
		
	}
	
	public static void swing() {
		angle = 0;
		
		setPosByAngle(angle);
		
		while(angle < 360) {
			
			getPos(angle);
			angle++;
			setPosByAngle(angle);
			robot.mouseMove(pt.x, pt.y);
			robot.delay(10);
			checkDecreaseMouseY();
		}
	}
	
	public static void setPosByAngle(int angle) {
		double theta = Math.toRadians(angle);
		
		pt.x = (ptf.x + (int) (Math.cos(theta) * 150) + screenWidth) % screenWidth;
		pt.y = (ptf.y + (int) (Math.sin(theta) * 150) + screenHeight) % screenHeight;
	}
	
	public static void getPos(int angle) {
		double theta = Math.toRadians(angle);
		
		pi = MouseInfo.getPointerInfo();
		pt = pi.getLocation();
		
		ptf.x = (pt.x - (int) (Math.cos(theta) * 150) + screenWidth) % screenWidth;
		ptf.y = (pt.y - (int) (Math.sin(theta) * 150) + screenHeight) % screenHeight;
	}
	
	public static void checkDecreaseMouseY() {
		
		int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
		
		pia = MouseInfo.getPointerInfo();
		pta = pia.getLocation();
		
		for(int i = 9; i > 0; i--) {
			prePosY[i] = prePosY[i-1]; 
		}
		
		prePosY[0] = pta.y;
		
		if(Math.abs(prePosY[9] - prePosY[0]) > screenHeight / 2) {
			angle = 360;
			enableMove = false;
		} else {
			
		}
	}
}
