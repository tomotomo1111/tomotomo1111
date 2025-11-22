//
// î•ñ—‚¿‚ÌƒTƒ“ƒvƒ‹ Sample_losstg.java
//   ƒRƒ“ƒpƒCƒ‹: javac Sample_losstg.java
//   Às: java Sample_losstg
//
public class Sample_losstg {

	public static void main(String[] args) {
		long n = 1L;
		int N = 16;
		float s, sn, err;

		// ^‚Ì’l
		s = (float)(Math.PI * Math.PI / 6);

		// •”•ª˜a(n = 2, 2^2, ... , 2^10) ‚É‚æ‚é‹ß—’l‚ÌŒvZ
		for (int i = 1 ; i <= N ; i++) {
			n *= 2L;
			sn = calcsum(n); // n = 2^i ‚É‘Î‚·‚é•”•ª˜a‚Ì‹ß—’l
			err = Math.abs(sn - s);	// â‘ÎŒë·
			System.out.printf("€” %5d: ‹ß—’l = %9.7e (â‘ÎŒë· = %9.7e)\n", n, sn, err);
		}
	}

	//
	//  –³ŒÀ‹‰”‹ß—ŒvZƒƒ\ƒbƒh calcsum
	//
	//  y“ü—Íz
	//    n = €” (long Œ^)
	//
	//  yo—Íz
	//    ‚È‚µ
	//
	//  y•Ô‹p’lz
	//    –³ŒÀ‹‰”‚Ì‹ß—’l (float Œ^)
	//
	public static float calcsum(long n)
	{
		float sn = 0.0F;

		// –³ŒÀ‹‰”‚Ì•”•ª˜a‚Ì‹ß—ŒvZ
		// î•ñ—‚¿‘Îô•ÏXÏby©•ª
		for (long k = n ; k > 0 ; k--) {
			sn += 1.0F / (k * k);
		}

		return(sn);
	}
}
