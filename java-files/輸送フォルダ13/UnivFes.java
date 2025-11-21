public class UnivFes{
	
	public static void main(String[] args) {
		
		UniversityFestival group_1, group_2, group_3, group_4, group_5;
		
		group_1 = new UniversityFestival("A", 15, 29);
		group_2 = new UniversityFestival("B", 20, 23);
		group_3 = new UniversityFestival("B-Man", 5, 18);
		group_4 = new UniversityFestival("C", 11, 23);
		group_5 = new UniversityFestival("D", 2, 23);
		
		UniversityFestival[] group = {group_1, group_2, group_3, group_4, group_5};
		
		for(int i=0; i<group.length; i++) {
			System.out.println("“o˜^ŽÒ " + group[i].getName() + "‚ÌŽx•¥‹àŠz‚Í " + group[i].FesPayment() + " ‚Å‚·.");
		}
		
		System.out.println("");
		System.out.println(group_1.getName());
		for(String x: group_1.OrderReturn(1)) {
			System.out.println(x);
		}
		
		System.out.println("");
		System.out.println(group_4.getName());
		for(String x: group_4.OrderReturn(4)) {
			System.out.println(x);
		}
	}
}