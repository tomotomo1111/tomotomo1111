import java.lang.*;

public class MatrixSolve {

    private static int[][] A = {{0, 3}, {3, 2}};
    private static int[][] B = {{3, 2}, {2, 2}};
    private static int[][] A_N_Copy = new int[2][2];
    private static int[][] A_N = new int[2][2];

    private static boolean notFound_N = true;
    private static int n = 1;
    private static final int ORDER = 7;

    public static void main(String[] args) {

        for(int i=0; i<A.length; i++) {
            for(int j=0; j<A[0].length;j++) {
                A_N[i][j] = A[i][j];
            }
        }

        while(notFound_N) {
            n++;

            for(int i=0; i<A_N.length; i++) {
                for(int j=0; j<A_N[0].length;j++) {
                    A_N_Copy[i][j] = A_N[i][j];
                }
            }

            A_N[0][0] = (A_N_Copy[0][0] * A[0][0] + A_N_Copy[0][1] * A[1][0]) % ORDER;
            A_N[0][1] = (A_N_Copy[0][0] * A[0][1] + A_N_Copy[0][1] * A[1][1]) % ORDER;
            A_N[1][0] = (A_N_Copy[1][0] * A[0][0] + A_N_Copy[1][1] * A[1][0]) % ORDER;
            A_N[1][1] = (A_N_Copy[1][0] * A[0][1] + A_N_Copy[1][1] * A[1][1]) % ORDER;

            if(checkEqualMatrix(A_N, B)) {
                notFound_N = false;
            }

            System.out.println("%%%%%%% " + "A^" + n + " %%%%%%%");
            for(int i=0; i<A_N.length; i++) {
                for(int j=0; j<A_N[0].length;j++) {
                    System.out.print(A_N[i][j] + " ");
                }
                System.out.println("");
            }
            System.out.println("");
        }

        System.out.println(n);
    }

    private static boolean checkEqualMatrix(int[][] A_N, int[][] B) {

        if(A_N[0][0] == B[0][0] && A_N[0][1] == B[0][1] && A_N[1][0] == B[1][0] && A_N[1][1] == B[1][1]) {
            return true;
        }

        return false;
    }
}