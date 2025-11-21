
public class cal {
    
    public static void main(String args[]) {
        double l;
        double c1, c2, n1, n2;
        c1 = 3646394.74;
        c2 = 3588225.94;
        n1 = n2 = 5;
        l = ((c1 + c2) * (c1 + c2)) / ((c1 * c1) / (n1 - 1)) + ((c2 * c2) / (n2 - 1));
        System.out.println(l);
    }
}