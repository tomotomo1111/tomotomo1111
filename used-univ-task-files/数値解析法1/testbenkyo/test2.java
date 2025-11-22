public class test2 {
    static double sigma = 1.0D, m = 0.0D;
    static int n = 8;
    static double [] x = {
        -0.9602898564975365,
        -0.796666477413627,
        -0.525532409916329,
        -0.1834346424956498,
         0.1834346424956498,
         0.5255324099163291,
         0.7966664774136265,
         0.9602898564975361
    };
    static double [] c = {
        0.1012285362903728,
        0.2223810344533736,
        0.3137066458778873,
        0.362683783378362,
        0.362683783378362,
        0.3137066458778879,
        0.2223810344533753,
        0.1012285362903782
    };

	public static void main(String[] args){
		double a = -1.0D;
        double b = 1.0D;
		double exactval = 0.68268949213708D;
        double glval;
		
		try {
			double [] x = new double[n];
			double [] fval = new double[n];

			init(x);

			for (int i = 0 ; i < n ; i++) fval[i] = function(((b - a) * x[i] + a + b)/2);

			glval = calculate(a, b, fval);

			System.out.printf("Approximate value = %17.15e\n", glval);
			System.out.printf("Absolute error    = %17.15e\n", Math.abs(glval - exactval));
			System.out.printf("Relative error    = %17.15e\n", Math.abs(glval - exactval) / Math.abs(exactval));
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

    static void init(double[] point) {

		if (point.length != n) {
			throw new IllegalArgumentException("配列サイズに問題があります．");
		} else {
			try{
			    for (int i = 0 ; i < n ; i++) point[i] = x[i];
			} catch(ArrayIndexOutOfBoundsException e) {
				throw e;
			}
		}
	}

	static double function(double x) {

		return((1 / Math.sqrt(2 * Math.PI) * sigma) * Math.exp(-Math.pow(x - m, 2) / (2 * Math.pow(sigma, 2))));
	}

	static double calculate(double a, double b, double[] function) {
		double rval = 0.0D;

		if (a >= b) {
			throw new IllegalArgumentException("区間端点の値に問題があります．");
		} else if (function.length != n) {
			throw new IllegalArgumentException("関数値配列のサイズに問題があります．");
		} else {
			try{
				double temp = 0;
				for (int i = 0; i < n; i++) temp += c[i] * function[i];
                rval = (b - a) / 2 * temp;
			} catch(ArrayIndexOutOfBoundsException e) {
                throw e;
			}
		}
		return(rval);
	}
}

