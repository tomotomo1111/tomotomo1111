import javax.swing.*;

public class MyFrame extends JFrame {

    public MyFrame() {
        super();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);
        setSize(AreaFight.WIDTH, AreaFight.HEIGHT);
        setLocationRelativeTo(null);
    }
}