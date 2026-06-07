public class Question1of4 {
    public static void main(String[] args) {
    }
    public static int q4(int a, int b) {
        if(b > a) {
            return q4(b, a);
        }else {
            if(a%b == 0) {
                return b;
            }else {
                return q4(b, a%b);
            }
        }
    }
}