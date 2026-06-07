package ST;

import java.awt.Container;

import javax.swing.JFrame;

public class InGame extends JFrame {
	public InGame() {
		
		MainFrame panel = new MainFrame();
		Container contentPane = getContentPane();
		setResizable(true);
		contentPane.add(panel);
		pack();
		setTitle("チャリ走");
		setLocationRelativeTo(null);
	}
	
	public static void main(String[] args) {
		InGame frame = new InGame();
		frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}

/*
特に改変する必要はありませんby神野
*/