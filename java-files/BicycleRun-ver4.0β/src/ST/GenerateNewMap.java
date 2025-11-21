package ST;

import java.util.ArrayList;

public class GenerateNewMap extends FallingFloor{
	
	private static final int[][][] CELLALL = {cell_1,cell_2,cell_3,cell_4,cell_5,cell_6,cell_7,cell_8,cell_9,cell_10,cell_11,cell_12,cell_13,cell_14,cell_15,cell_16,cell_17,cell_18,cell_19,cell_20,cell_21,cell_22,cell_23,cell_24,cell_25,cell_26,cell_27,cell_28,cell_29,cell_30,cell_31,cell_32,cell_33,cell_34,cell_35,cell_36,cell_37,cell_38,cell_39,cell_40};
	private static int[][] spareMap = new int[map.length][map[0].length];
	private static int[][] cell;
	private static int cellTotal = CELLALL.length;
	private static int candidateCellNumber = 0;
	private static boolean selectNumFromMapMaking = true;
	private static ArrayList<Integer> mapTypeOrder = new ArrayList<>();
	public static String mapTypeName;
	
	public static void initMapType() {
			mapTypeOrder.clear();
	}
	
	public static void GenerateMap() {
		
		int[] F = electMapType();
		resetFallingTimer();
		
		generateNewMapFromCell(8,4,F,selectNumFromMapMaking);
		generateNewMapFromCell(21,4,F,selectNumFromMapMaking);
		generateNewMapFromCell(34,4,F,selectNumFromMapMaking);
		generateNewMapFromCell(47,4,F,selectNumFromMapMaking);
		
		makeSpareMap();
	}
	
	private static String returnArray(ArrayList<Integer> a, int b) {
		String str0 = String.valueOf(a.get(0));
		if(b == 0) return str0 + " first";
		String strn = a.get(b) + " " + returnArray(a, b-1);
		return strn;
	}
	
	private static int[] electMapType() {
		
		int[] F = {17,37,38,39,40};
		selectNumFromMapMaking = true;
		int RandomMapType = 0;
		
		if(mapTypeOrder.size() == 0) {
			do {
			RandomMapType = (int)(Math.random() * 9);
			if(mapTypeOrder.indexOf(RandomMapType) == -1)mapTypeOrder.add(mapTypeOrder.size(),RandomMapType);
			} while(mapTypeOrder.size() != 9);
			
			System.out.println(returnArray(mapTypeOrder,mapTypeOrder.size()-1));
		}
		
		int ElectedMapType = mapTypeOrder.get(0);
		mapTypeOrder.remove(0);
		
		switch(ElectedMapType ) {
		case 0://厚い雲
			int[] F0 = {14,15,21,22,23};
			F = F0;
			mapTypeName = "積乱雲";
			break;
		case 1://薄い雲
			int[] F1 = {10,11,12,16,24};
			F = F1;
			mapTypeName = "雲海";
			break;
		case 2://細い床、湖
			int[] F2 = {2,8,25,26,27};
			F = F2;
			mapTypeName = "杭の湖";
			break;
		case 3://太い床、湖
			int[] F3 = {1,20,28,29,30};
			F = F3;
			mapTypeName = "床の湖";
			break;
		case 4://街
			int[] F4 = {4,9,13,18,19};
			F = F4;
			mapTypeName = "街";
			break;
		case 5://森
			int[] F5 = {3,5,31,32,33};
			F = F5;
			mapTypeName = "森";
			break;
		case 6://板、湖
			int[] F6 = {6,7,34,35,36};
			F = F6;
			mapTypeName = "板の湖";
			break;
		case 7://リフト
			int[] F7 = {17,37,38,39,40};
			F = F7;
			mapTypeName = "飛行板の空";
			break;
		default:
			selectNumFromMapMaking = false;
			mapTypeName = "ランダム";
			return F;
		}
		return F;
	}
	
	//reviveMap()で参照するコピーマップを、マップ生成時に作成しておく。
	public static void makeSpareMap() {
		
		for(int y = 0; y < map.length; y++) {
			for(int x = 0; x < map[0].length; x++) {
				spareMap[y][x] = map[y][x];
			}
		}
	}
	
	/*
	注意 int leftRangeX upRangeY は行列 map の左から数えた番号であり、0は含まない。
	しかし、出力側 map[i-1][j-1]は左の位置を0として数える正式な二次元配列としてる。
	*/
	
	//selectNumは指定した番号のセルを生成する。0ならランダムに生成する
	public static void generateNewMapFromCell(int leftRangeX, int upRangeY, int[] selectNums, boolean selectNumFromMapMaking) {
		
		int cellNumber = lotteryMap(selectNums, selectNumFromMapMaking);
		
		cell = CELLALL[cellNumber - 1];
		
		for(int i = upRangeY; i < upRangeY + cell.length; i++) {
			for(int j = leftRangeX; j < leftRangeX + cell[0].length; j++) {
				map[i-1][j-1] = cell[i - upRangeY][j - leftRangeX];
			}
		}
	}
	
	private static int lotteryMap(int[] selectNums, boolean selectNumFromMapMaking) {
		
		candidateCellNumber = (int)(Math.random() * cellTotal) + 1;
		
		if(selectNumFromMapMaking ^ checkCellNumberExistanceInForbidNum(candidateCellNumber, selectNums)) {
			lotteryMap(selectNums, selectNumFromMapMaking);
		}
		return candidateCellNumber;
	}
	
	private static boolean checkCellNumberExistanceInForbidNum(int cellNumber, int[] selectNums) {
		
		int i = 0;
		if(selectNums.length == 0) return false;
		while(cellNumber != selectNums[i]) {
			i++;
			if(i == selectNums.length) return false;
		}
		return true;
	}
	
	//リスタート時マップをコピーから再生成するために実装されている。マップを変化させるタイプのギミックもこれで再生成する。
	public static void regenerateFromMapCopy() {
		
		resetFallingTimer();
		
		for(int y = 0; y < map.length; y++) {
			for(int x = 0; x < map[0].length; x++) {
				map[y][x] = spareMap[y][x];
			}
		}
	}
}
