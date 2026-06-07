public class Enshu8Mondai4{
	public static void main(String[] args) {
		IntSetBase set1 = new IntArrayBase();
		IntSetBase set2 = new IntListBase();
		
		int[] numA = {20201,20202,20203};
		String[] nameA = {"伊藤1","伊藤2","伊藤3"};
		
		for(int i=0; i<numA.length; i++) {
			set1.add(numA[i], nameA[i]);
			set2.add(numA[i], nameA[i]);
		}
		System.out.println("A組に伊藤7さんはいますか?");
		if(set1.findName("伊藤7")) {
			System.out.println("A組にいます.");
		} else {
			System.out.println("A組にいません.");
		}
		
		System.out.println("A組に伊藤3さんはいますか?");
		if(set2.findName("伊藤3")) {
			System.out.println("A組にいます.");
		} else {
			System.out.println("A組にいません.");
		}
	}
}