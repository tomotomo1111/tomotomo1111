class IntArrayBase implements IntSetBase{
	public int[] arrayNum;
	public String[] arrayName;
	public int num;
	
	public IntArrayBase(){
		arrayNum = new int[8];
		arrayName = new String[8];
		num = 0;
	}
	
	public void add(int value, String name) {
		if(num >= arrayNum.length) {
			int[] newArrayNum = new int[num+1];
			String[] newArrayName = new String[num+1];
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
	
	public boolean findName(String name) {
		for(int i = 0; i < num; i++) {
			if(arrayName[i].equals(name)) {
				return true;
			}
		}
		return false;
	}
}