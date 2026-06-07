public class Question1of5 {
	public static void main(String[] args) {
		int[] input = {6, 7, 8, 9, 10};
		System.out.println(returnMaxDistance(input));
	}
	
	private static int returnMaxDistance(int[] array) {
		
		int min = array[0];
		int max = array[0];
		for(int k=0; k<array.length; k++) {
			if(min > array[k]) min = array[k];
			if(max < array[k]) max = array[k];
		}
		return max - min;
	}
}