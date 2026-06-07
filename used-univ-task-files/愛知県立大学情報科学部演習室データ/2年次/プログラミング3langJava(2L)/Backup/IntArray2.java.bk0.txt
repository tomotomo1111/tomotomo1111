class IntArray2 implements IntSet2 {
	public int[] arrayNum;
	public String[] arrayName;
	public int num;
	public IntArray2() {
		arrayNum = new int[8];
		arrayName = new String[8];
		num = 0;
	}
	
	public void add(int value, String name) {
		if(num >= arrayNum.length) {
			int[] newArrayNum = new int[arrayNum.length + 8];
			String[] newArrayName = new String[arrayName.length + 8];
			for(int i = 0; i < arrayNum.length; i++) {
				newArrayNum[i] = arrayNum[i];
				newArrayName[i] = arrayName[i];
			}
			arrayNum = newArrayNum;
			arrayName = newArrayName;
		}
		arrayNum[num] = value;
		arrayName[num] = name;
		num++;
	}
	
	public String getName(int index) {
		return arrayName[index];
	}
	
	public boolean findName(String name) {
		for(int i = 0; i < arrayName.length; i++) {
			if(arrayName[i].equals(name)) {
				return true;
			}
		}
		return false;
	}
	
	public int size() {
		return num;
	}
}