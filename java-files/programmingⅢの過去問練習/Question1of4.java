public class Question1of4 {
	
    public static int gcd(int a, int b) {
        while (b != 0) {
            int remainder = a % b;
            a = b;
            b = remainder;
        }
        return a;
    }

    public static void main(String[] args) {
		
        int num1 = 36;
        int num2 = 89;
		
        if (num1 < num2) {
            int temp = num1;
            num1 = num2;
            num2 = temp;
        }
		
        int result = gcd(num1, num2);
        System.out.println("Å‘åŒö–ñ”‚ÍG" + result);
    }
}