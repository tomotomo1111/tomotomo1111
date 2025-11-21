public class TotalMatrix {

	private static int[][] A = new int[2][2];
	private static final int m = 4;
	private static int count = 0;
	private static int count_det0include = 0;

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
						if(det != 0) count++;
						count_det0include++;

						System.out.println("%%%%% " + count + " (" + count_det0include + ") " + "det = " + det + " %%%%%");
						System.out.println(A[0][0] + " " + A[0][1]);
						System.out.println(A[1][0] + " " + A[1][1]);
						System.out.println("");
					}
				}
			}
		}

		System.out.println(count);
	}
}