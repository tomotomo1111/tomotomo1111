package ST;

public class FlyingFloor extends Map{
	
	private static int[][] multiBuffer = new int[map.length][map[0].length];
	
	private static final int flyingTimer_MAX = 100;
	
	private static int flyingTimer = 0;
	private static int directionNum = 1;//1で→、-1で←
	
	public static void flyingTimer() {
		if(flyingTimer == flyingTimer_MAX) {
			flyingTimer = 0;
			conductMovingAction();
		} else {
			flyingTimer++;
		}
	}
	
	public static void resetFlyingTimer() {
		flyingTimer = 0;
		directionNum = 1;
	}
	
	private static void conductMovingAction() {
		copyMapToMultiBuffer();
		flyingFloor();
		copyMultiBufferToMap();
	}
	
	private static void copyMapToMultiBuffer() {
		
		for(int y = 0; y < map.length; y++) {
			
			for(int x = 0; x < map[0].length; x++) {
				
				multiBuffer[y][x] = map[y][x];
			}
		}
	}
	
	private static void flyingFloor() {
		
		for(int y = 0; y < map.length; y++) {
			
			for(int x = 0; x < map[0].length; x++) {
				
				if(map[y][x] == 4) {
					if(multiBuffer[y][x+directionNum] == 3) {
						directionNum *= -1;
					}
				}
			}
		}
		
		for(int y = 0; y < map.length; y++) {
			
			for(int x = 0; x < map[0].length; x++) {
				
				if(map[y][x] == 4) multiBuffer[y][x] = 0;
			}
		}
		
		
		for(int y = 0; y < map.length; y++) {
			
			for(int x = 0; x < map[0].length; x++) {
				
				if(map[y][x] == 4 && x != 0 && x != map[0].length - 1) {
					
					if(multiBuffer[y][x+directionNum] == 3) {
						directionNum *= -1;
					}
					
					if(map[y][x+directionNum] == 0 || map[y][x+directionNum] == 4) {
						
						multiBuffer[y][x+directionNum] = 4;
					}
				}
			}
		}
	}
	
	private static void copyMultiBufferToMap() {
		
		for(int y = 0; y < map.length; y++) {
			
			for(int x = 0; x < map[0].length; x++) {
				
				map[y][x] = multiBuffer[y][x];
			}
		}
	}
}
