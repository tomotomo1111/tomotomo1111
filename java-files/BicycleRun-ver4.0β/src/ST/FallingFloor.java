package ST;

public class FallingFloor extends Map{
	
	private static final int FALLTIMER_MAX = 100;//100が丁度良い
	
	public static int[][] col_F = new int[map.length][map[0].length];
	
	public static void checkExistanceOfFallingFloor() {
		
		for(int j = 0; j < map.length; j++) {
			
			for(int i = 0; i < map[0].length; i++) {
				
				if(map[j][i] == 9 && col_F[j][i] == 999) {
					
					col_F[j][i] = 1;
				}
			}
		}
	}
	
	public static void resetFallingTimer() {
		
		for(int j = 0; j < map.length; j++) {
			
			for(int i = 0; i < map[0].length; i++) {
				
				col_F[j][i] = 999;
			}
		}
	}
	
	public static void fallingTimer() {
		
		for(int j = 0; j < map.length; j++) {
			for(int i = 0; i < map[0].length; i++) {
				if(col_F[j][i] != 999) col_F[j][i]++;
				if(col_F[j][i] == FALLTIMER_MAX) {
					col_F[j][i] = 999;
					fallingFloor(j,i);
				}
			}
		}
	}
	
	public static void fallingFloor(int y, int x) {
		
		if(map[y][x] == 9) {
			
			map[y][x] = 0;
			
			if(map[y+1][x] == 0) {
				
				map[y+1][x] = 9;
				col_F[y+1][x] = 0;
			}
		}
	}
}