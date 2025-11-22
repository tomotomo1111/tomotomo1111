//
// 本プログラムは，
//
//  金森隼斗君（愛知県立大学情報科学部卒）
//
// が 2012 年度に作成したものを元に修正したものです．
// 著作権は原作者である金森君にあるので，注意して下さい．
// なお，Button クラスを実行することで，
// DKA 法による多項式求解 GUI プログラムが起動します．
//
import javax.swing.*;
import java.awt.*;

//
// 複素数クラス
//
class ComplexNumber{
	private double rp;	// 実部
	private double ip;	// 虚部

	// コンストラクタ (初期化)
	public ComplexNumber(){
		this.rp = 0.0D;
		this.ip = 0.0D;
	}

	// ゲッターメソッド
	public double[] getValue(){
		double [] rvec = new double[2];
		rvec[0] = this.rp;
		rvec[1] = this.ip;
		return(rvec);
	}

	// セッターメソッド
	public void setValue(double rval, double ival){
		this.rp = rval;
		this.ip = ival;
	}

	// セッターメソッド
	public void setValue(ComplexNumber cn){
		double [] ctemp = cn.getValue();

		this.rp = ctemp[0];
		this.ip = ctemp[1];
	}

	//
	// 和計算メソッド
	// 【入力】
	//   cn = 加える複素数 (ComplexNumber オブジェクト)
	//
	public void add(ComplexNumber cn){
		double[] cval = cn.getValue();
		this.rp += cval[0];
		this.ip += cval[1];
	}

	//
	// 差計算メソッド
	// 【入力】
	//   cn = 減じる複素数 (ComplexNumber オブジェクト)
	//
	public void minus(ComplexNumber cn){
		double[] cval = cn.getValue();
		this.rp -= cval[0];
		this.ip -= cval[1];
	}

	//
	// 積計算メソッド
	// 【入力】
	//   cn = 乗じる複素数 (ComplexNumber オブジェクト)
	//
	public void multi(ComplexNumber cn){
		double[] cval = cn.getValue();
		double rtemp = this.rp;
		double itemp = this.ip;

		this.rp = rtemp * cval[0] - itemp * cval[1];
		this.ip = rtemp * cval[1] + itemp * cval[0];
	}

	//
	// 商計算メソッド
	// 【入力】
	//   cn = 割る複素数 (ComplexNumber オブジェクト)
	//
	public void div(ComplexNumber cn){
		double[] cval = cn.getValue();
		double rtemp = this.rp;
		double itemp = this.ip;
		double ctemp = cval[0] * cval[0] + cval[1] * cval[1];

		this.rp = (rtemp * cval[0] + itemp * cval[1]) / ctemp;
		this.ip = (itemp * cval[0] - rtemp * cval[1]) / ctemp;
	}

	//
	// サイズ計算メソッド
	//
	public double size(){
		return(Math.sqrt(this.rp * this.rp + this.ip * this.ip));
	}
}

//
// DKA クラス (Canvas クラスからの派生クラス)
//
class DKA extends Canvas{
	// 最大反復回数
    private int ITRMAX = 1000;
	// 絶対許容誤差 (IEEE754倍精度の最小有限値)
	private double epsa = Double.MIN_VALUE;
	// 相対許容誤差 (IEEE754倍精度計算機イプシロン X 8.0D)
	private double epsr = 8.0D * Math.ulp(1.0D);

	// 多項式の次数
	int ndim;
    // 多項式の実係数
    double [] coefa;
    // 近似解格納配列
    ComplexNumber [] zsol;

	//
	// コンストラクタ
	// 【入力】
    //   k = 多項式の次数
    //   c = 多項式の係数 (配列)
	//
	//
    DKA(int k, double[] c){
    	// 次数の格納
    	ndim = k;

    	// 係数配列の生成
    	coefa = new double[k + 1];

    	// 係数の格納 (ただし k 次の係数が 1 となるように規格化)
    	coefa[0] = 1.0D;
    	for(int i = 1; i <= k; i++){
    		coefa[i] = c[i] / c[0];
    	}

    	// 近似解配列の生成
    	zsol = new ComplexNumber[k];
    	for (int i = 0 ; i < k ; i++){
    		zsol[i] = new ComplexNumber();
    	}
    }

    //
    // 多項式計算メソッド
    //  (ホーナーの算法を使用)
    //
    private ComplexNumber fpoly(ComplexNumber cn)
    {
    	ComplexNumber fval = new ComplexNumber();
    	ComplexNumber ctemp = new ComplexNumber();

    	fval.setValue(coefa[0], 0.0D);
    	for (int i = 1 ; i <= ndim ; i++){
    		ctemp.setValue(coefa[i], 0.0D);
    		fval.multi(cn);
    		fval.add(ctemp);
    	}

    	return(fval);
    }

    //
    // アバースの初期値計算メソッド
    //
    private void calcInit(double r0)
    {
    	double theta;
    	double rtemp;
    	double itemp;

    	for (int i = 0 ; i < ndim ; i++){
    		theta = ((2.0D * i + 0.5D) * Math.PI) / ndim;
    		rtemp = -coefa[1] / ndim + r0 * Math.cos(theta);
    		itemp = r0 * Math.sin(theta);
    		zsol[i].setValue(rtemp, itemp);
    	}
    }

    //
    // DKA 法による近似解計算メソッド
    //
    void getSol()
    {
    	ComplexNumber [] oldz = new ComplexNumber[ndim];
    	ComplexNumber [] errz = new ComplexNumber[ndim];
    	ComplexNumber ztemp = new ComplexNumber();
    	ComplexNumber zseki = new ComplexNumber();

    	// 収束判定使用配列
    	double [] oldnorm = {0.0D, 0.0D};
    	double [] newnorm = {0.0D, 0.0D};
    	double [] sumnorm = {0.0D, 0.0D};
    	double [] errval = {0.0D, 0.0D};

    	// 描画用配列
    	double [][] zsolre = new double[ITRMAX + 1][ndim];
    	double [][] zsolim = new double[ITRMAX + 1][ndim];

    	// 近似値取得用配列
    	double [] zval = new double[2];

    	// アバースの初期値計算
    	double r0 = 5.0D;
    	this.calcInit(r0);

    	// 初期値ベクトルの実部・虚部最大値ノルム計算
    	for (int i = 0 ; i < ndim ; i++){
    		zval = zsol[i].getValue();
    		for (int j = 0 ; j < 2 ; j++){
    			if (oldnorm[j] < Math.abs(zval[j])) oldnorm[j] = Math.abs(zval[j]);
    		}
    	}

    	// カウンタ変数
    	int counter = 0;

    	for (int i = 0 ; i < ndim ; i++){
    		oldz[i] = new ComplexNumber();
    		errz[i] = new ComplexNumber();
    		oldz[i].setValue(zsol[i]);
    		zval = zsol[i].getValue();
    		zsolre[counter][i] = zval[0];
    		zsolim[counter][i] = zval[1];
    	}

    	// DK 法による反復計算
    	do{
    		// 各近似値の更新
    		newnorm[0] = 0.0D; newnorm[1] = 0.0D;
    		for (int i = 0 ; i < ndim ; i++){
    			// 多項式値の計算
    			errz[i].setValue(fpoly(zsol[i]));

    			// 更新のための複素数値 (分母) 計算
    			zseki.setValue(1.0D, 0.0D);
    			for (int j = 0 ; j < ndim ; j++){
    				if (j == i) continue;
    				ztemp.setValue(oldz[i]);
    				ztemp.minus(oldz[j]);
    				zseki.multi(ztemp);
    			}
    			errz[i].div(zseki);

    			// 近似値の更新
    			zsol[i].minus(errz[i]);

    			zval = zsol[i].getValue();
    			zsolre[counter + 1][i] = zval[0];
        		zsolim[counter + 1][i] = zval[1];

        		for (int l = 0 ; l < 2 ; l++){
        			if (newnorm[l] < Math.abs(zval[l])) newnorm[l] = Math.abs(zval[l]);
        		}
    		}

    		// 収束判定および次ステップのための計算
    		for (int j = 0 ; j < 2 ; j++){
    			sumnorm[j] = oldnorm[j] + newnorm[j];
    			oldnorm[j] = newnorm[j];
    		}
    		errval[0] = 0.0D; errval[1] = 0.0D;
			for (int i = 0 ; i < ndim ; i++){
				zval = errz[i].getValue();
				for (int j = 0 ; j < 2 ; j++){
					if (errval[j] < Math.abs(zval[j])) errval[j] = Math.abs(zval[j]);
				}

				oldz[i].setValue(zsol[i]);
			}

    		// カウンタの更新
    		counter++;
    		if (counter >= ITRMAX) break;
    	} while ((errval[0] >= epsa + epsr * sumnorm[0]) || (errval[1] >= epsa + epsr * sumnorm[1]));

    	if(counter >= ITRMAX){
    	    JOptionPane.showMessageDialog(null, "最大反復回数に到達. 収束しませんでした.");
    	    System.exit(1);
    	}
    	else{
    		// 標準出力への計算結果表示
    		System.out.printf("反復回数 = %3d%n", counter);
    		System.out.printf("近似解:%n");
    		for (int i = 0 ; i < ndim ; i++){
    			zval = zsol[i].getValue();
    			System.out.printf("z[%2d] = %18.16e (実部)  %18.16e (虚部)%n", i, zval[0], zval[1]);
    		}
    	}

    	// 座標軸設定のための計算
    	double rmax = 0.0D, imax = 0.0D;
    	for(int i = 0; i < ndim; i++){
    		zval = zsol[i].getValue();
    		if(Math.abs(zval[0]) > rmax) rmax = Math.abs(zval[0]);
    		if(Math.abs(zval[1]) > imax) imax = Math.abs(zval[1]);
    	}
    	if(rmax < imax) rmax = imax;

    	// 近似解のプロット
    	JFrame frame = new JFrame();
    	frame.setTitle("DKA法による近似プロット");
    	frame.setBounds(0, 0, 568, 568);
    	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	frame.setResizable(false);
    	frame.setLayout(null);

    	JPanel cp = new JPanel();
    	cp.setLayout(null);
    	frame.add(cp);
    	cp.setBounds(0, 0, 561, 561);

    	Can canvas = new Can(rmax,zsolre, zsolim, counter, ndim);
    	cp.add(canvas);
    	canvas.setBounds(0, 0, 561, 561);
    	frame.setVisible(true);
    	Button.kaisu = counter;
    	for(int bb = 0; bb < ndim; bb++){
    		Button.kre[bb] = zsolre[counter][bb];
    		Button.kim[bb] = zsolim[counter][bb];
    	}
    }
}
