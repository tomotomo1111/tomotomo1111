//
// 本プログラムは，
//
//  金森隼斗君（愛知県立大学情報科学部卒）
//
// が 2012 年度に作成したものを元に微修正したものです．著作権は
// 原作者である金森君にあるので，注意して下さい．なお，Button クラスを
// 実行することで，DKA 法による多項式求解 GUI プログラムが起動します．
//
import java.awt.*;
import java.awt.Graphics;

class Can extends Canvas{

	static int MAX = 1000;
    static int rmax;
    static int i;
    static double[][] ansre = new double[MAX][MAX];
    static double[][] ansim = new double[MAX][MAX];
    static int counter;
    static int kainokosu;

    Can(double u, double[][] a, double[][] b, int count, int p)
    {
    	rmax = (int)u;
        kainokosu = p;
        i = count;
        for(int g = 0; g < count; g++)
        {
        	for(int h = 0; h < p; h++)
        	{
        		ansre[g][h] = a[g][h];
        		ansim[g][h] = b[g][h];
        	}
        }
    }

    public void paint(Graphics g){
    	int ii, k;
    	counter = i;

    	// キャンバスサイズ
    	int csize = 600;
    	// 点のサイズ
    	int psize = 12;

    	// 原点の座標
    	int [] iorigin = {csize / 2 - csize / 60, csize / 2 - csize / 15};

    	// 座標絶対値最大の設定
    	int irmax = (int)rmax + 1;
    	while ((csize / 2) % irmax == 0)
    	{
    		irmax++;
    	}
    	int iwidth = (csize / 2) / irmax;

    	// グリット線の描画
		g.drawLine(0, iorigin[1], csize, iorigin[1]);	// x 軸
		g.drawLine(iorigin[0], 0, iorigin[0], csize);	// y 軸
    	for(ii = 1 ; ii <= irmax ; ii++)
    	{
    		g.drawLine(0, iorigin[1] + ii * iwidth, csize, iorigin[1] + ii * iwidth);
    		g.drawLine(0, iorigin[1] - ii * iwidth, csize, iorigin[1] - ii * iwidth);
    		g.drawLine(iorigin[0] + ii* iwidth, 0, iorigin[0] + ii * iwidth, csize);
    		g.drawLine(iorigin[0] - ii* iwidth, 0, iorigin[0] - ii * iwidth, csize);
    	}

    	// 原点の描画
    	g.fillOval(iorigin[0] - psize / 2, iorigin[1] - psize / 2, psize, psize);
    	g.drawString("0", iorigin[0] - 15, iorigin[1] + 15);

    	int haba = 1;

    	// 各軸の座標値描画
        for(ii=1; ii< irmax; ii++)
    	{
    		if(ii*haba > 9)
    		{
            	g.drawString(String.valueOf(ii*haba), iorigin[0] - 15 + ii * iwidth, iorigin[1] + 15);
            	g.drawString(String.valueOf(-ii*haba), iorigin[0] - 20 - ii * iwidth, iorigin[1] + 15);
    			g.drawString(String.valueOf(ii*haba), iorigin[0] - 15, iorigin[1] + 15 - ii * iwidth);
    			g.drawString(String.valueOf(-ii*haba), iorigin[0] - 20, iorigin[1] + 15 + ii * iwidth);
    		}
    		else
    		{
            	g.drawString(String.valueOf(ii*haba), iorigin[0] - 10 + ii * iwidth, iorigin[1] + 15);
            	g.drawString(String.valueOf(-ii*haba), iorigin[0] - 15 - ii * iwidth, iorigin[1] + 15);
    			g.drawString(String.valueOf(ii*haba), iorigin[0] - 10, iorigin[1] + 15 - ii * iwidth);
            	g.drawString(String.valueOf(-ii*haba), iorigin[0] - 15, iorigin[1] + 15 + ii * iwidth);
    		}
    	}

    	int f = 0;

    	// 点の色設定
    	while(f < counter)
    	{
    		for(double u=0; u < 100000000; u+=1);

    		// 近似履歴の描画
    		for(k = 0 ; k < kainokosu ; k++)
    		{
    			// 点の色の設定
        		switch(k)
        		{
        			case 0:
        				g.setColor(Color.WHITE);
        				break;
        			case 1:
        				g.setColor(Color.ORANGE);
        				break;
        			case 2:
        				g.setColor(Color.BLUE);
        				break;
        			case 3:
        				g.setColor(Color.YELLOW);
        				break;
        			case 4:
        				g.setColor(Color.LIGHT_GRAY);
        				break;
        			case 5:
        				g.setColor(Color.PINK);
        				break;
        			case 6:
        				g.setColor(Color.GREEN);
        				break;
        			case 7:
        				g.setColor(Color.GRAY);
        				break;
        			case 8:
        				g.setColor(Color.MAGENTA);
        				break;
        			case 9:
        				g.setColor(Color.DARK_GRAY);
        				break;
        			case 10:
        				g.setColor(Color.CYAN);
        				break;
        			case 11:
        				g.setColor(Color.BLACK);
        				break;
        			default:
        				g.setColor(Color.WHITE);
        				break;
        		}

    			g.fillOval((int)(iorigin[0] - psize / 2 + ansre[f][k] / haba * iwidth) ,(int)(iorigin[1] - psize / 2 + ansim[f][k] * haba * iwidth), psize, psize);
    		}
    		f++;
    	}

    	// 近似解の描画
		g.setColor(Color.RED);
		for(k = 0 ; k < kainokosu ; k++)
		{
			g.fillOval((int)(iorigin[0] - psize / 2 + ansre[counter - 1][k] / haba * iwidth) ,(int)(iorigin[1] - psize / 2 + ansim[counter - 1][k] * haba * iwidth), psize, psize);
		}
    }
}
