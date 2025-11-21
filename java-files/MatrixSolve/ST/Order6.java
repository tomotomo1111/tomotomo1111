import java.lang.*;
import java.util.ArrayList;

public class Order6 {

	private static int[][] A = new int[2][2];
	private static int[][] B = { { 1, 0 }, { 0, 1 } };
	private static int[][] A_N_Copy = new int[2][2];
	private static int[][] A_N = new int[2][2];

	private static ArrayList<int[][]> answer = new ArrayList<int[][]>();

	private static boolean below6;
	private static int n;
	private static int count;

	private static final int m = 4;
	private static final int MAX = 6;
	private static final int ORDER = 4;

	public static void main(String[] args) {

		for (int A_00 = 0; A_00 < m; A_00++) {

			for (int A_01 = 0; A_01 < m; A_01++) {

				for (int A_10 = 0; A_10 < m; A_10++) {

					for (int A_11 = 0; A_11 < m; A_11++) {

						for (int i = 0; i < A.length; i++) {

							for (int j = 0; j < A[0].length; j++) {

								if (i == 0 && j == 0)
									A[i][j] = A_00;
								if (i == 0 && j == 1)
									A[i][j] = A_01;
								if (i == 1 && j == 0)
									A[i][j] = A_10;
								if (i == 1 && j == 1)
									A[i][j] = A_11;
							}
						}

						int det = A[0][0] * A[1][1] - A[0][1] * A[1][0];
						if (det != 0) {

							for (int i = 0; i < A.length; i++) {
								for (int j = 0; j < A[0].length; j++) {
									A_N[i][j] = A[i][j];
								}
							}
							below6 = true;
							n = 0;
							count++;
							while (below6) {
								n++;

								for (int i = 0; i < A_N.length; i++) {
									for (int j = 0; j < A_N[0].length; j++) {
										A_N_Copy[i][j] = A_N[i][j];
									}
								}

								A_N[0][0] = (A_N_Copy[0][0] * A[0][0] + A_N_Copy[0][1] * A[1][0]) % ORDER;
								A_N[0][1] = (A_N_Copy[0][0] * A[0][1] + A_N_Copy[0][1] * A[1][1]) % ORDER;
								A_N[1][0] = (A_N_Copy[1][0] * A[0][0] + A_N_Copy[1][1] * A[1][0]) % ORDER;
								A_N[1][1] = (A_N_Copy[1][0] * A[0][1] + A_N_Copy[1][1] * A[1][1]) % ORDER;

								if (n == MAX) {

									System.out.println("%%%%%%% (" + A[0][0] + " " + A[0][1] + ") ^" + MAX + " % " + count);
									System.out.println("%%%%%%% (" + A[1][0] + " " + A[1][1] + ")    %%%%%");
									for (int i = 0; i < A.length; i++) {
										for (int j = 0; j < A[0].length; j++) {
											System.out.print(A_N[i][j] + " ");
										}
										System.out.println("");
									}

									System.out.println("");

									if (checkEqualMatrix(A_N, B)) {
										int[][] in = new int[2][2];

										for (int i = 0; i < A.length; i++) {
											for (int j = 0; j < A[0].length; j++) {
												in[i][j] = A[i][j];
											}
										}

										answer.add(in);
									}

									below6 = false;
								}
							}
						}
					}
				}
			}
		}
		for(int i=0; i<3; i++) {
			System.out.println("%%%%%%%%%%%%%%%%%%%");
		}
		System.out.println("");

		int[][] out = new int[2][2];
		for (int o = 0; o < answer.size(); o++) {
			out = answer.get(o);
			for (int i = 0; i < out.length; i++) {
				for (int j = 0; j < out[0].length; j++) {
					System.out.print(out[i][j] + " ");
				}
				System.out.println("");
			}
		}
	}

	private static boolean checkEqualMatrix(int[][] A_N_in, int[][] B_in) {

		if (A_N_in[0][0] == B_in[0][0] && A_N_in[0][1] == B_in[0][1] && A_N_in[1][0] == B_in[1][0]
				&& A_N_in[1][1] == B_in[1][1]) {
			return true;
		}

		return false;
	}
}