//
// オーバーフロー・アンダーフローサンプル Sample_ouflow.java
//   コンパイル: javac Sample_ouflow.java
//   実行: java Sample_ouflow
//
public class Sample_ouflow {

	public static void main(String[] args) {
		float x, y;
		x = 2.0F * Float.MAX_VALUE;
		y = Float.MIN_VALUE / 2.0F;
		System.out.println("2 * xmax = " + x);
		System.out.println("xmin / 2 = " + y);
	}
}
