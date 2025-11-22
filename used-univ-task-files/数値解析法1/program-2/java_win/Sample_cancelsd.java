//
// Œ…—‚¿ƒTƒ“ƒvƒ‹ƒvƒƒOƒ‰ƒ€ Sample_cancelsd.java
//   ƒRƒ“ƒpƒCƒ‹: javac Sample_cancelsd.java
//   Às: java Sample_cancelsd
//
import java.util.Scanner;

public class Sample_cancelsd{

	// ŒvZ‹@ƒCƒvƒVƒƒ“
	static double eps = Math.ulp(1.0D);

	public static void main(String[] args) {
		int rval;
		double [] coef = new double[3];
		double [] sol = new double[2];

		// ŒW”‚Ì“ü—Í (Scanner ƒNƒ‰ƒX‚ğg—p‚µ‚ÄƒL[ƒ{[ƒh‚æ‚è“ü—Í)
		Scanner keyBoardScanner = new Scanner(System.in);
		System.out.println("2Ÿ•û’ö®‚ÌŒW”‚ğ“ü—Í‚µ‚Ä‰º‚³‚¢:");
		System.out.print("a = ");
		coef[0] = keyBoardScanner.nextDouble();
		System.out.print("b = ");
		coef[1] = keyBoardScanner.nextDouble();
		System.out.print("c = ");
		coef[2] = keyBoardScanner.nextDouble();

		// 2Ÿ•û’ö®‹‰ğ
		rval = getsol(coef, sol);

		if (rval == 0)
		{
			System.out.printf("d‰ğ: ‰ğ = %18.16e\n", sol[0]);
		}
		else if (rval == 1)
		{
			System.out.println("ˆÙ‚È‚é2‚Â‚Ì‹•”‰ğiŒvZ‚µ‚È‚¢j");
		}
		else
		{
			System.out.printf("ˆÙ‚È‚é2‚Â‚ÌÀ”‰ğ:\n‰ğ1 = %18.16e\n‰ğ2 = %18.16e\n", sol[0], sol[1]);
		}
	}

	//
	//  “ñŸ•û’ö®‚Ì‹‰ğƒƒ\ƒbƒh getsol
	//
	//  y“ü—Íz
	//    coef[] = ŒW”‚ªŠi”[‚³‚ê‚½ doule Œ^”z—ñ
	//
	//  yo—Íz
	//    sol[] = ‹ß—‰ğ‚ªŠi”[‚³‚ê‚½ double Œ^”z—ñ
	//            ’) d‰ğ‚ğ‚Âê‡‚Í“¯‚¶’l‚ªŠi”[‚³‚ê‚éD
	//            ’) ‹•”‰ğ‚Ìê‡‚ÍC0 ‚ªŠi”[‚³‚ê‚éD
	//
	//  y•Ô‹p’lz
	//    ‰ğ‚Ì”»•ÊŒ‹‰ÊF0 -> d‰ğ, 1 -> ˆÙ‚È‚é2‚Â‚Ì‹•”‰ğ,  2 -> ˆÙ‚È‚é2‚Â‚ÌÀ”‰ğ
	//
	public static int getsol(double [] coef, double [] sol)
	{
		int hantei = 0;

		double a, b, c, d;

		a = coef[0]; b = coef[1]; c = coef[2];

		// ”»•Ê®‚ÌŒvZ
		d = b * b - 4.0D * a * c;

		if (Math.abs(d) <= eps)
		{
		    // d‰ğ‚ğ‚Âê‡
		    sol[0] = - b / (2.0D * a);
		    sol[1] = sol[0];
		}
		else if (d < -eps)
		{
		    // ‹¤–ğ•¡‘f”‰ğ‰ğ‚ğ‚Âê‡F‰ğ‚ÍŒvZ‚µ‚È‚¢
		    hantei = 1;
		    sol[0] = 0.0D;
		    sol[1] = 0.0D;
		}
		else
		{
		    // 2 ‚Â‚ÌˆÙ‚È‚éÀ”‰ğ‚ğ‚à‚Âê‡
			// Œ…—‚¿‘ÎôÏby©•ª
			if(b >= 0) {
				sol[0] = (-2.0D * c) / (b + Math.sqrt(d));
		    	sol[1] = (-b - Math.sqrt(d)) / (2.0D * a);
			} else {
				sol[0] = (-b + Math.sqrt(d)) / (2.0D * a);
		    	sol[1] = (-2.0D * c) / (b - Math.sqrt(d));
			}
		    hantei = 2;
		}

		return(hantei);
	}
}
