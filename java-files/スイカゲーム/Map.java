import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Random;

public class Map extends JPanel implements MouseListener {
    private int width, height, margin;
    private int[][] map;
    private final int DOT_W = 1;
    private final int DOT_H = 1;
    private final int PLAYER_SIZE_F = 20;
    private final int POWER_ADJUSTMENT = 100;
    private Player player; 
    private double[][] drag = new double[2][2];
    private long[] time = new long[2];

    public Map(int width, int height, int margin) {
        this.width = width;
        this.height = height;
        this.margin = margin;
        map = new int[height - 2 * margin][width - 2 * margin];
        setFocusable(true);
        addMouseListener(this);
        player = new Player(new Vector2((width - PLAYER_SIZE_F) / 2, (height - PLAYER_SIZE_F) / 2), 0, 0, 0, 0, PLAYER_SIZE_F, 0.99);
    }

    public void initMap() {

        for (int j = 0; j < map.length; j++) {
            for (int i = 0; i < map[0].length; i++) {
                map[j][i] = (j < margin || i < margin || j > map.length - margin || i > map[0].length - margin) ? 1 : 0;
            }
        }
    }

    public void loop() {

        while (true) {
            //syori
            time[0] = System.nanoTime();
            pMove();
            time[1] = System.nanoTime();
            //syori
            try {
                long dist = (time[1] - time[0]) / 1000000;
                Thread.sleep((dist > 16) ? 0 : 16 - dist);
            } catch (Exception e) {
                System.exit(-1);
            }
        }
    }

    private void pMove() {

        Vector2 pos = player.getPos();
        Vector2 vec = player.getVec();

        Vector2 new_lu_pos = new Vector2(pos.x + vec.x, pos.y + vec.y);
        Vector2 new_rd_pos = new Vector2(pos.x + vec.x + player.getSize(), pos.y + vec.y + player.getSize());
        
        int index_lux = (int) new_lu_pos.x - margin;
        int index_luy = (int) new_lu_pos.y - margin;
        int index_rdx = (int) Math.ceil(new_rd_pos.x) - margin;
        int index_rdy = (int) Math.ceil(new_rd_pos.y) - margin;
        boolean didCol = false;
        Vector2 col_pos = pos;

        if (map[index_luy][index_lux] != 0) {
            didCol = true;
            col_pos = new_lu_pos;
        }
        if (map[index_rdy][index_rdx] != 0) {
            didCol = true;
            col_pos = new_rd_pos;
        }


        double p_angle = player.getAngle();
        if (!didCol){
            player.move();
        } else {
            if (col_pos.x < margin * 2) {
                player.setAngle((p_angle >= Math.PI) ? p_angle + calDst(1.5)
                        : p_angle + calDst(0.5));
            }
            if (col_pos.x > width - margin * 2) {
                player.setAngle(p_angle >= Math.PI ? p_angle + calDst(1.5)
                        : p_angle + calDst(0.5));
            }
            if (col_pos.y < margin * 2) {
                player.setAngle(isAngleLeft() ? p_angle + calDst(1.0)
                        : p_angle + calDst(0) + Math.PI * 2);
            }
            if (col_pos.y > height -10 - margin * 2) {
                player.setAngle(isAngleLeft() ? p_angle + calDst(1.0)
                        : p_angle + calDst(2) - Math.PI * 2);
            }
        }
    }

    private double calDst(double scale) {
        return (Math.PI * scale - player.getAngle()) * 2;
    }

    private boolean isAngleLeft() {
        double p_angle = player.getAngle();
        if (p_angle >= Math.PI * 0.5 && p_angle <= Math.PI * 1.5) return true;
        return false;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        paintMap(g);
        player.paintPlayer(g);
        repaint();
    }

    private void paintMap(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, width, height);
        for (int j = 0; j < map.length; j++) {
            for (int i = 0; i < map[0].length; i++) {
                g.setColor((map[j][i] == 1) ? Color.GRAY  : Color.WHITE);
                g.fillRect(margin + i, margin + j, DOT_W, DOT_H);
            }
        }
    }


    public void mouseClicked(MouseEvent e) {
        
    }

    public void mousePressed(MouseEvent e) {
        drag[0][0] = e.getX();
        drag[0][1] = e.getY();
    }

    public void mouseReleased(MouseEvent e) {
        drag[1][0] = e.getX();
        drag[1][1] = e.getY();
        // System.out.println("fx : " + drag[0][0] + ",fy : " + drag[0][1]  + ",lx : " + drag[1][0] + ",ly : " + drag[1][1] ); mark

        double newPower = 0;
        double newAngle = 0;
        if (drag[0][0] == drag[1][0] && drag[0][1] == drag[1][1]) {
        } else {
            newPower = Math.sqrt(Math.pow(drag[1][1] - drag[0][1], 2) + Math.pow(drag[1][0] - drag[0][0], 2)) / POWER_ADJUSTMENT;
            newAngle = Math.atan((drag[1][1] - drag[0][1]) / (drag[1][0] - drag[0][0]));
            newAngle += (drag[1][0] - drag[0][0] > 0) ? 0 : Math.PI;
        }
        player.setAngle(newAngle);
        player.setVelocity(newPower);
    }

    public void mouseEntered(MouseEvent e) {

    }

    public void mouseExited(MouseEvent e) {

    }
}