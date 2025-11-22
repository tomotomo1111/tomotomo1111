//
// 本プログラムは，
//
//  金森隼斗君（愛知県立大学情報科学部卒）
//
// が 2012 年度に作成したものを元に微修正したものです．著作権は
// 原作者である金森君にあるので，注意して下さい．なお，このプログラムを
// 実行することで，DKA 法による多項式求解 GUI プログラムが起動します．
//
// コンパイル: javac Button.java
// 実行: java Button
//
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class Button extends JFrame implements ActionListener
{
	static int MAX = 1000;
    int i = 0;
    double[] a = new double[MAX];
    JTextField ainput;
    JLabel label = new JLabel();
    JLabel label2 = new JLabel();
    static JLabel label3 = new JLabel("");
    static JLabel label4 = new JLabel("※21次以上の解は表示されないので注意, 係数は2つ以上入力して下さい.");
    static JLabel[] label5 = new JLabel[20];

    JButton button1 = new JButton("");
    JButton button2 = new JButton("");
    JButton button3 = new JButton("");
    JButton button4 = new JButton("");

    static JPanel p = new JPanel();
    static int kaisu;
    static double[] kre = new double[MAX];
    static double[] kim = new double[MAX];
    static int kkk = 1;

    public static void main(String[] args){
    	Button bt = new Button("DKA法収束過程描写プログラム");
    	bt.setVisible(true);
    }

    Button(String title){
    	setTitle(title);
    	setBounds(100,100,450,600);
    	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    	button1.setText("入力");
    	button1.addActionListener(this);
    	button2.setText("計算開始");
    	button2.addActionListener(this);
    	button3.setText("やめる");
    	button3.addActionListener(this);
    	button4.setText("例題の値で計算開始");
    	button4.addActionListener(this);

    	ainput = new JTextField(15);
    	label.setText("a["+i+"]=");
    	label2.setText("反復回数:");
    	p.add(label);
    	p.add(ainput);
    	p.add(button1);
    	p.add(button2);
    	p.add(button3);
    	p.add(button4);
    	p.add(label4);
    	p.add(label2);
    	p.add(label3);
    	for(int q = 0; q < 20; q++)
    	{
    		label5[q] = new JLabel("");
    		label5[q].setAlignmentX(CENTER_ALIGNMENT);
    		p.add(label5[q]);
    	}
    	Container contentPane = getContentPane();
    	contentPane.add(p, BorderLayout.CENTER);
    }

    public void actionPerformed(ActionEvent e){
    	if(e.getSource() == button1){
    		a[i] = Double.valueOf(ainput.getText()).doubleValue();
    		i++;
    		label.setText("a["+i+"]=");
    		ainput.setText("");
    	}
    	else if(e.getSource() == button3){
    		System.exit(0);
    	}
    	else{
            if(e.getSource() == button4){
            	a[0] = 1.0D;
            	a[1] = -10.0D;
            	a[2] = 43.0D;
            	a[3] = -104.0D;
            	a[4] = 150.0D;
            	a[5] = -100.0D;
            	i = 6;
            }

            DKA dkaobj = new DKA(i - 1, a);
            dkaobj.getSol();

            label3.setText(Integer.toString(kaisu));

            for(int c = 0; c < kkk; c++){
            	label5[c].setText("");
            	label5[c].setAlignmentX(CENTER_ALIGNMENT);
            }
            for(int r = 0; r < i-1; r++){
            	label5[r].setText("z[" + r + "].re : " + kre[r] + "  z[" + r + "].im : "+ kim[r]);
            	label5[r].setAlignmentX(CENTER_ALIGNMENT);
            	if(r == 19) break;
            }

            kkk = i;
            i = 0;
            label.setText("a["+i+"]=");
    	}
    }
}
