package ST;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;

public class damageCalculator implements MouseWheelListener, FocusListener, KeyListener {
	
	JFrame frame = new JFrame();
	static JTextField text1, text2, text3, text4, text5, text6, text7, text8;
	JButton button1, button2;
	
	boolean loop = true;
	boolean canViewAnswer = false;
	static boolean consoleLog = false;
	
	static String[] extendWords;
	static String[] viewWords;
	static ArrayList<String> log = new ArrayList<>();
	static String focusCmp = "none";
	
	int scrollCount = 0;
	static int logCount = 0;
	
	damageCalculator() {
		
		frame.setSize(500,200);
		frame.setLayout(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.getContentPane().setBackground(Color.DARK_GRAY);
		
		button1 = new JButton("start calculating");
		button1.setBounds(130, 10, 180, 30);
		button1.setFont(new Font("Arial", Font.BOLD, 15));
		button1.setForeground(Color.BLACK);
		button1.setFocusable(false);
		button1.addActionListener(new ActionListener() {
			//@Override
			public void actionPerformed(ActionEvent e) {
				String[] inputString;
				
				if(text4.getText().isEmpty()) {
					inputString = new String[3];
					
					inputString[0] = text1.getText();
					inputString[1] = text2.getText();
					inputString[2] = text3.getText();
					
				} else {
					inputString = new String[4];
					
					inputString[0] = text1.getText();
					inputString[1] = text2.getText();
					inputString[2] = text3.getText();
					inputString[3] = text4.getText();
				}
				
				canViewAnswer = true;
				consoleLog = false;
				scrollCount = 0;
				
				if(checkIncludeHelp(inputString)) {
					descript();
				} else {
					calculate(inputString);
				}
			}
		});
		
		button2 = new JButton("viewLog");
		button2.setBounds(350, 10, 100, 30);
		button2.setFont(new Font("Arial", Font.BOLD, 12));
		button2.setForeground(Color.BLACK);
		button2.setFocusable(false);
		button2.addActionListener(new ActionListener() {
			//@Override
			public void actionPerformed(ActionEvent e) {
				
				consoleLog = !consoleLog;
				scrollAnswer(0);
				scrollCount = 0;
			}
		});
		
		text1 = new JTextField(20);
		text1.setBounds(10,10,50,20);
		text1.addFocusListener(this);
		text1.addKeyListener(this);
		text2 = new JTextField(20);
		text2.setBounds(10,50,50,20);
		text2.addFocusListener(this);
		text2.addKeyListener(this);
		text3 = new JTextField(20);
		text3.setBounds(10,90,50,20);
		text3.addFocusListener(this);
		text3.addKeyListener(this);
		text4 = new JTextField(20);
		text4.setBounds(10,130,50,20);
		text4.addFocusListener(this);
		text4.addKeyListener(this);
		text5 = new JTextField(50);
		text5.setBounds(80,70,400,20);
		text5.setFont(new Font("Arial", Font.ITALIC, 15));
		text5.setEditable(false);
		text5.addMouseWheelListener(this);
		text6 = new JTextField(50);
		text6.setBounds(80,90,400,20);
		text6.setFont(new Font("Arial", Font.ITALIC, 15));
		text6.setEditable(false);
		text6.addMouseWheelListener(this);
		text7 = new JTextField(50);
		text7.setBounds(80,110,400,20);
		text7.setFont(new Font("Arial", Font.ITALIC, 15));
		text7.setEditable(false);
		text7.addMouseWheelListener(this);
		text8 = new JTextField(50);
		text8.setBounds(80,130,400,20);
		text8.setFont(new Font("Arial", Font.ITALIC, 15));
		text8.setEditable(false);
		text8.addMouseWheelListener(this);
		
		frame.add(button1);
		frame.add(button2);
		frame.add(text1);
		frame.add(text2);
		frame.add(text3);
		frame.add(text4);
		frame.add(text5);
		frame.add(text6);
		frame.add(text7);
		frame.add(text8);
		frame.setVisible(true);
		
		while(loop) {
			
			String[] copyInput = new String[4];
			
			copyInput[0] = text1.getText();
			copyInput[1] = text2.getText();
			copyInput[2] = text3.getText();
			copyInput[3] = text4.getText();
			
			if(checkNumeric(copyInput,0,3) || (checkIncludeInitials(copyInput) && checkNumeric(copyInput,1,2) && copyInput[3].isEmpty()) || checkIncludeHelp(copyInput)) {
				button1.setEnabled(true);
			} else {
				button1.setEnabled(false);
			}
			
			button2.setEnabled(canViewAnswer);
			if(consoleLog) {
				button2.setText("viewResult");
			} else {
				button2.setText("viewLog");
			}
			System.out.println(focusCmp);
			try {
				Thread.sleep(30);
			} catch (InterruptedException e1) {
				// TODO 自動生成された catch ブロック
				e1.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
		
		damageCalculator frame = new damageCalculator();
	}
	
	public static void calculate(String[] args) {
		
		if(checkIncludeInitials(args)) {
			if(checkNumeric(args,1,3) && checkLength(args,3)) {
				simple(args);
			} else {
				error();
			}
		} else {
			if(checkNumeric(args,0,3) && checkLength(args,4)) {
				system(args);
			} else {
				error();	
			}
		}
	}
	
	public static boolean checkIncludeHelp(String[] input) {
		String[] str = {"help","HELP"};
		
		for(int i=0; i<input.length; i++) {
			for(int j=0; j<str.length; j++) {
				if(input[i].equals(str[j])) {
					return true;
				}
			}
		}
		return false;
	}
	
	public static boolean checkIncludeInitials(String[] input) {
		String[] str = {"l","g","c","i","d","n"};
		
		if(input.length ==0) return false;
		
		for(int i=0; i<str.length; i++) {
			if(input[0].equals(str[i])) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean checkNumeric(String[] input, int minIndex, int maxIndex) {
		
		boolean numeric = true;
		
		String[] word = new String[input.length];
		
		for(int i=0; i<input.length; i++) {
			word[i] = input[i];
		}
		
		for(int i=minIndex; i<Math.min(input.length,maxIndex+1); i++) {
			if(input[i] == null || input[i].isEmpty()) return false;
			for(int j=0; j<word[i].length(); j++) {
				if(Character.isDigit(word[i].charAt(j))) {
					continue;
				} else {
					numeric = false;
					break;
				}
			}
		}
		return numeric;
	}
	
	public static boolean checkLength(String[] input, int length) {
		if(input.length == length) {
			return true;
		} else {
			return false;
		}
	}
	
	public static void error() {
		String[] message1 = {"error!! You do not meet the required input","%%% defensePoint armorToughness damage protectionPoint %%%","or","%%% initial(l,g,c,i,d,n) damage protectionPoint %%%"};
		
		for(String str: message1) {
				System.out.println(str);
		}
		viewAnswer(message1);
	}
	
	public static void descript() {
		String[] message = {"#defensePoint","leather : 1 3 2 1 sum7","gold : 2 5 3 1 sum11","chain 2 5 4 1 sum12","iron 2 6 5 2 sum15","dia&netherite 3 8 6 3 sum20","","#armorToughness","dia = 2 2 2 2 sum8","netherite = 3 3 3 3 sum12"};
		
		for(String str: message) {
			System.out.println(str);
		}
		viewAnswer(message);
	}
	
	public static void simple(String[] input) {
		
		String[] data = new String[input.length + 1];
		
		for(int i=0; i<data.length;i++) {
			data[i] = "0";
		}
		
		data[2] = input[1];
		data[3] = input[2];
		
		switch(input[0]) {
			case "l":
			data[0] = "7";
			data[1] = "0";
			break;
			case "g":
			data[0] = "11";
			data[1] = "0";
			break;
			case "c":
			data[0] = "12";
			data[1] = "0";
			break;
			case "i":
			data[0] = "15";
			data[1] = "0";
			break;
			case "d":
			data[0] = "20";
			data[1] = "8";
			break;
			case "n":
			data[0] = "20";
			data[1] = "12";
			break;
			default:
		}
		
		system(data);
	}
	
	public static void system(String[] input) {
		double[] answer = damageCalcurate(input);
		String[] message2 = {"error!! Protection value is greater than 20 or less than 0","","",""};
		String word_1 = answer[0]+" ("+answer[1]+"%)"+" "+(int)answer[2]+"HIT";
		
		if(answer[0] == -2) {
			for(String str: message2) {
				System.out.println(str);
			}
			viewAnswer(message2);
		} else {
			System.out.println(word_1);
			viewAnswer(word_1);
			log.add(logCount,word_1);
			logCount++;
		}
	}
	
	public static double[] damageCalcurate(String[] element) {
		double[] damageCalcurateAnswer = new double[3];
		damageCalcurateAnswer[0] = 0;
		damageCalcurateAnswer[1] = 0;
		damageCalcurateAnswer[2] = 0;
		
		if(element.length != 4) {
			damageCalcurateAnswer[0] = -1.0;
			return damageCalcurateAnswer;
		}
		
		double defensePoint = Double.parseDouble(element[0]);
		double armorToughness = Double.parseDouble(element[1]);
		double damage = Double.parseDouble(element[2]);
		double protectionPoint = Double.parseDouble(element[3]);
		double trueDamage;
		
		if(protectionPoint > 20 || protectionPoint < 0) {
			damageCalcurateAnswer[0] = -2.0;
			return damageCalcurateAnswer;
		}
		
		trueDamage = (damage * (1 - (Math.min(20, Math.max((defensePoint / 5), (defensePoint - ((4 * damage) / (armorToughness + 8))))) / 25))) * (1 - ((protectionPoint * 4) / 100));
		damageCalcurateAnswer[0] = (Math.floor(trueDamage * 1000)) / 1000;
		damageCalcurateAnswer[1] = Math.floor(trueDamage / damage * 1000) / 10;
		damageCalcurateAnswer[2] = Math.floor(20 / damageCalcurateAnswer[0]) + 1;
		
		return damageCalcurateAnswer;
	}
	
	public static void viewAnswer(String[] words) {
		extendWords = new String[words.length];
		viewWords = new String[4];
		
		for(int i=0; i<4; i++) {
			viewWords[i] = "";
		}
		
		for(int i=0; i<words.length; i++) {
			extendWords[i] = words[i];
		}
		
		scrollAnswer(0);
	}
	
	public static void scrollAnswer(int scrollNum) {
		
		if(consoleLog) {
			for(int i=0; i<Math.min(log.size(), 4); i++) {
				viewWords[i] = log.get(i + scrollNum);
			}
			if(log.size() < 4) {
				for(int j=0; j<4-log.size();  j++) {
					viewWords[log.size() + j] = "";
				}
			}
		} else {
			for(int i=0; i<Math.min(extendWords.length, 4); i++) {
				viewWords[i] = extendWords[i + scrollNum];
			}
		}
		
		text5.setText(viewWords[0]);
		text6.setText(viewWords[1]);
		text7.setText(viewWords[2]);
		text8.setText(viewWords[3]);
	}
	
	public static void viewAnswer(String word) {
		extendWords = new String[4];
		viewWords = new String[4];
		
		for(int i=0; i<4; i++) {
			extendWords[i] = "";
			viewWords[i] = "";
		}
		extendWords[0] = word;
		
		scrollAnswer(0);
	}
	
	public static int getMarginOfIndex(String mode) {
		int answer = 0;
		if(mode.equals("array")) {
			answer = extendWords.length - viewWords.length;
		} else if(mode.equals("list")) {
			answer = log.size() - viewWords.length;
		}
		return answer;
	}
	
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		
		if(canViewAnswer) {
			if ( e.getWheelRotation() == -1 ) {
				if(0 < scrollCount) {
					scrollCount -= 1;
					scrollAnswer(scrollCount);
				}
			} else if (e.getWheelRotation() == 1) {
				if(consoleLog) {
					if(getMarginOfIndex("list") > scrollCount) {
						scrollCount += 1;
						scrollAnswer(scrollCount);
					}
				} else {
					if(getMarginOfIndex("array") > scrollCount) {
						scrollCount += 1;
						scrollAnswer(scrollCount);
					}
				}
			}
		}
	}
	
	@Override
	public void focusGained(FocusEvent e) {
		// TODO 自動生成されたメソッド・スタブ
		
		if(e.getSource() == text1) {
			focusCmp = "text1";
		}
		
		if(e.getSource() == text2) {
			focusCmp = "text2";
		}
		
		if(e.getSource() == text3) {
			focusCmp = "text3";
		}
		
		if(e.getSource() == text4) {
			focusCmp = "text4";
		}
	}
	
	@Override
	public void focusLost(FocusEvent e) {
		// TODO 自動生成されたメソッド・スタブ
		
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO 自動生成されたメソッド・スタブ
		
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		// TODO 自動生成されたメソッド・スタブ
		System.out.println("aa");
		if(e.getKeyCode() == KeyEvent.VK_ENTER) {
			
			switch(focusCmp) {
			
			case "text1":
				text1.transferFocus();
				break;
			case "text2":
				text2.transferFocus();
				break;
			case "text3":
				text3.transferFocus();
				break;
			case "text4":
				button1.doClick();
				break;
			default:
				
			}
		}
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		// TODO 自動生成されたメソッド・スタブ
		
	}
}