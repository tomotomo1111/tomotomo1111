public class SeisekiArray implements SeisekiSet {
	int[] engArray;
	int[] mathArray;
	int[] liteArray;
	String[] nameArray;
	public int num;
	
	public SeisekiArray() {
		engArray = new int[8];
		mathArray = new int[8];
		liteArray = new int[8];
		nameArray = new String[8];
		num = 0;
	}
	
	public void addScore(int eng, int math, int lite, String name) {
		if(num >= engArray.length) {
			int[] newEngArray = new int[engArray.length + 8];
			int[] newMathArray = new int[mathArray.length + 8];
			int[] newLiteArray = new int[liteArray.length + 8];
			String[] newNameArray = new String[nameArray.length + 8];
			for(int i = 0; i < engArray.length; i++) {
				newEngArray[i] = engArray[i];
				newMathArray[i] = mathArray[i];
				newLiteArray[i] = liteArray[i];
				newNameArray[i] = nameArray[i];
			}
			engArray = newEngArray;
			mathArray = newMathArray;
			liteArray = newLiteArray;
			nameArray = newNameArray;
		}
		engArray[num] = eng;
		mathArray[num] = math;
		liteArray[num] = lite;
		nameArray[num] = name;
		num++;
	}
	
	public int size() {
		return num;
	}
	
	public double requestAve(String sub) {
		int sum = 0;
		double ave = 0;
		
		if(sub.equals("English")) {
			for(int i=0; i<num; i++) {
				sum += engArray[i];
			}
			ave = (double)sum / num;
		}
		if(sub.equals("Math")) {
			for(int i=0; i<num; i++) {
				sum += mathArray[i];
			}
			ave = (double)sum / num;
		}
		if(sub.equals("Literature")) {
			for(int i=0; i<num; i++) {
				sum += liteArray[i];
			}
			ave = (double)sum / num;
		}
		return ave;
	}
	
	public void AllDataShow() {
		
		for(int i=0; i<num; i++) {
			System.out.println(nameArray[i] + "の成績  国語：" + liteArray[i] + " 数学：" + mathArray[i] + " 英語：" + engArray[i]);
		}
	}
}