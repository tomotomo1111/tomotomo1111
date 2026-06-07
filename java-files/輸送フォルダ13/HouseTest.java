class HouseTest extends House{
	public HouseTest(int num, int num2, int num3) {
		super(num, num2, num3);
	}
	
	public String Construction() {
		if(breadth <= 100 && floors <= 2) {
			return "RC‘¢";
		} else if(floors >= 3) {
			return "“Sœ‘¢";
		} else {
			return "–Ø‘¢²‘gH–@";
		}
	}
}
