import java.util.ArrayList;

public class Genshikon {
	static ArrayList<Integer> polynomial_1 = new ArrayList<>();
	static ArrayList<Integer> polynomial_2 = new ArrayList<>();
	
	static final int ORDER = 5;
	
	public static void main(String[] args) {
		for(int i=0; i<ORDER; i++) {
			System.out.println("%%%%%%%%%% a = " + i + " %%%%%%%%%%");
			System.out.println(calculate(i));
			System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%");
			System.out.println("");
		}
	}
	
	private static int calculate(int a) {
		boolean end_frag = false;
		
		int[] input = {0, 1, a};
		int[] copy_1 = new int[input.length];
		int[] copy_2 = new int[input.length];//answer
		
		for(int i=0; i<input.length; i++) {
			copy_1[i] = input[i];
			copy_2[i] = input[i];
		}
		
		int times = 1;
		while(!end_frag && times++ < 25) {
			copy_2[1] = (input[2] * copy_1[1] + input[1] * copy_1[2]) % ORDER;
			copy_2[2] = (input[2] * copy_1[2] + (input[1] * copy_1[1]) * 3) % ORDER;
			
			for(int j=0; j<input.length;j++) {
				copy_1[j] = copy_2[j];
			}
			System.out.println(copy_2[0] + "x^2 + " + copy_2[1] + "x + " + copy_2[2] + " ");
			
			if(copy_2[0] == 0 && copy_2[1] == 0 && copy_2[2] == 1) {
				end_frag = true;
			}
		}
		return times;
	}
}