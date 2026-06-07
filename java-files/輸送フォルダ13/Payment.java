abstract class Payment{
	private String groupName;
	private int groupNum;
	final String special = "ÉQÉXÉg";
	
	public Payment(String n, int m) {
		groupName = n;
		groupNum = m;
	}
	
	public int AmountOfMoney(int m, int a, String x) {
		if(x.equals(special)){
			return (m*groupNum)-(a*groupNum);
		} else if(groupName.equals(x)) {
			return (m-a)*groupNum;
		} else {
			return m*groupNum;
		}
	}
	
	abstract int FesPayment();
	
	public String getName() {
		return groupName;
	}
	
	public int getNum() {
		return groupNum;
	}
}