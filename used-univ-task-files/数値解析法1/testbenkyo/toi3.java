import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

public class toi3 {
	static int ITERMAX = 1000;
	static double epsa = Double.MIN_VALUE;
	static double epsr = 8.0D * Math.ulp(1.0D);
    static double epsilon = Math.ulp(1.0D);

	static double exactval = 0.6417143708728827D;
	static double[] exactx = {0.5D, 0.25D};

	static double solve(double x0) {
		int k = 0;
		double xnew = 0.5D, x = x0, dfx;
		double sumnorm, oldnorm, newnorm;
		sumnorm = oldnorm = newnorm = 0;
		double olderrval, errval, error;
		olderrval = errval = error = 0;

		olderrval = Math.abs(x - exactval);
		oldnorm = Math.abs(x);

		try(PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(new File("toi2-1000.dat"))))){
			pw.printf("%4d  %17.15e  %17.15e  %17.15e\n", k, x, olderrval, Math.log(olderrval));

			do{
				dfx = dif(x);
				if (Math.abs(dfx) <= epsa){
				       System.out.println("微分係数値が非常に小さくなりました．終了します．");
				       break;
				}

				xnew = x - (func(x) / dfx);
				x = xnew;
				newnorm = Math.abs(x);
				sumnorm = oldnorm + newnorm;
				oldnorm = newnorm;
				
				k++;
				errval = Math.abs(xnew - exactval);
				error = errval;
				pw.printf("%4d  %17.15e  %17.15e  %17.15e  %17.15e\n", k, xnew, errval, Math.log(errval), errval / olderrval);
				olderrval = errval;				
			} while (error >= epsa + epsr * sumnorm && k < ITERMAX);
			pw.close();
		} catch(IOException e) {
			System.err.println(e);
	    }

		if (k == ITERMAX){
			System.out.println("Not converged ...");
        } else {
		    System.out.printf("Converged: num. of iterations = %d\n",k);
		}
		
		return(xnew);
	}

	static double func(double x) {

		return(Math.pow(Math.cos(x), 2) - x);
	}

	static double dif(double x) {

		return(-2 * Math.sin(x) - 1);
	}

	public static void main(String[] args) {
	    double x0 = 0.5D;

		double sol1d = solve(x0);
		System.out.printf("Approxmate soltion = %17.15e\n", sol1d);
        System.out.printf("error = %17.15e\n", Math.abs(sol1d - exactval));
	}
}
