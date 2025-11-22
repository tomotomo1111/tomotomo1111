import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class toi1 {
    static double pi = Math.PI;
    static double epsa = Double.MIN_VALUE;
    static double epsr = Math.ulp(1.0D);

    public static void main(String[] args){
        int i, j;

        int n = 50;

		double c = 0.0D, d = 1.0D;
        double h = (d - c) / n;

		double[] x = new double[n + 1];
		double[] fval = new double[n + 1];
        double[] pval = new double[n + 1];
        double[] uexact = new double[n + 1];

        double[][] a = new double[n + 1][n + 1];
        double[] fh = new double[n + 1];
        double[] uh = new double[n + 1];

        double maxerr = 0.0D;
        double errval = 0.0D;

		for (i = 0 ; i <= n ; i++){
			x[i] = c + i * h;
			fval[i] = f(x[i]);
            pval[i] = p(x[i]);
            uexact[i] = u(x[i]);
		}

        for(i = 0; i <= n; i++){
            for(j = 0; j <= n; j++) a[i][j] = 0;
        }
        for (i = 1; i < n; i++){
            a[i][i - 1] = -1;
            a[i][i] = 2 + p(i) * Math.pow(h, 2);
            a[i][i + 1]= -1;
        }
        a[0][0] = a[n][n] = 1;

        fh[0] = 2;
        for(i = 1; i < n; i++) fh[i] = Math.pow(h,2) * fval[i];
        fh[n] = -2;

        Jacobi(a, fh, uh);

        try {
            PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter("toi1-n"+n+".dat")));
            for(i = 0; i <= n; i++){
                errval = Math.abs(uh[i] - uexact[i]);
                System.out.printf("x[%.3f] : uh= %f , uexact= %f , error= %f\n", x[i], uh[i], uexact[i], errval);
                pw.printf("%.3f %f %f%n",x[i], uh[i], uexact[i]);
                if(maxerr < errval) maxerr = errval;
            }
            System.out.printf("maxerr=%17.15e", maxerr);
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static double f(double x) {

        return((1 + Math.pow(pi, 2) + Math.sin(pi * x)) * (Math.sin(pi * x) + 2 * Math.cos(pi * x)));
    }

    static double p(double x) {

        return(1 + Math.sin(pi * x));
    }

    static double u(double x) {

        return(Math.sin(pi * x) + 2 * Math.cos(pi * x));
    }

    static void Jacobi(double[][] a, double[] b, double[] x){
        int i, j;
        
        try{
            int n = a.length;
    
            if(n == 0){
                throw new IllegalArgumentException("配列を使用して下さい．");
            } else if (a[0].length != n || b.length != n || x.length != n) {
                throw new IllegalArgumentException("配列サイズが一致しません．");
            } else {
                double err = 0.0D;
                double oldnorm, newnorm, sumnorm;
                double [] newx = new double[n];
                double [] errvec = new double[n];
        
                    oldnorm = norm(x);
                do {
                    for (i = 0 ; i < n ; i++) {
                        newx[i] = b[i];
                        for(j = 0 ; j < n ; j++){
                            if (j != i) newx[i] -= a[i][j] * x[j];
                        }
                        newx[i] /= a[i][i];
                    }
        
                    newnorm = norm(newx);
                    sumnorm = oldnorm + newnorm;
                    errvec = vecm(newx, x);
                    err = norm(errvec);
        
                    for(i = 0 ; i < n ; i++) x[i] = newx[i];
                    oldnorm = newnorm;
                } while (err >= epsa + epsr * sumnorm);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    static double norm(double[] x) {
        int n = x.length;
        double sum = 0;

        for (int i = 0; i < n; i++) sum += x[i] * x[i];
        return sum;
    }

    static double[] vecm(double[] x, double[] y) {
        int n = x.length;
        double[] rvec = new double[n];
        for (int i = 0 ; i < n ; i++) rvec[i] = x[i] - y[i];
        return rvec;
    }
}