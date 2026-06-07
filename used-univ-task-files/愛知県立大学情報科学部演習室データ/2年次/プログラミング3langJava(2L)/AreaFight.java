import javax.swing.*;
import java.awt.*;

public class AreaFight {

    public static void main(String[] args) {

        Map map = new Map();

        MyFrame myFrame = new MyFrame();
        myFrame.setDefaultCloseOperation(myFrame.EXIT_ON_CLOSE);
        myFrame.setLocationRelativeTo(null);
        myFrame.pack();
        myFrame.setSize(640,400);
        myFrame.setVisible(true);
        

    }
}

class MyFrame extends JFrame {

    public void myFrame() {

        JFrame frame = new JFrame();
    }
}

class Map {

    private int[][] area = new int[1000][1000];

    public void Map() {
        
        for(int i = 0; i<area.length; i++) {
            
            if(i > 500) {

                for(int j = 0; j<area[0].length; j++) {

                    if(j > 500) {
                        area[i][j] = 0; //green
                    } else {
                        area[i][j] = 1; //yellow
                    }
                }
            } else {
                for(int j = 0; j<area[0].length; j++) {

                    if(j > 500) {
                        area[i][j] = 2; //blue
                    } else {
                        area[i][j] = 3; //red
                    }
                }
            }

            for(int a: area[i]) {
                System.out.print(a + " ");
            }

            System.out.println("");
        }
    }

public int getColor(int row, int col) {

        return area[row][col];
    }
}

class Display {

    public void Display() {

    }}
