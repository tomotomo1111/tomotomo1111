public class Question1of1 {
	public static void main(String[] args) {
		double[][] input = {{5.3,3.4},{2.2,4.3},{6.2,2.5}};
		System.out.println(returnMin(input));
	}
	
	private static double returnMin(double[][] array) {
		int length = array.length * array[0].length;
		double[] newArray = new double[length];
		
		int l = 0;
		for(int i=0; i<array.length; i++) {
			
			for(int j=0; j<array[0].length; j++) {
				newArray[l] = array[i][j];
				l++;
			}
		}
		
		double min = newArray[0];
		for(int k=0; k<newArray.length; k++) {
			if(min > newArray[k]) min = newArray[k];
		}
		return min;
	}
}