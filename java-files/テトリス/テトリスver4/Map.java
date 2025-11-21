import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

public class Map extends JPanel implements KeyListener, ActionListener {

    public static final int MINIMAP_W = 12;
    public static final int MINIMAP_H = 21;
    public static final int MINIMAP_OFFSET_PLUS = 3;

    private static final int MAP_W = 320;
    private static final int MAP_H = 480;
    private static final int TILE_W = 16;
    private static final int TILE_H = 16;
    private static final int OFFSET_X = 8;
    private static final int OFFSET_Y = 8;
    private static final int MINO_W = 4;
    private static final int MINO_H = 4;
    private static final int NEXT_NW_X = 11;
    private static final int NEXT_NW_Y = 0;
    private static final int RESERVE_NW_X = 0;
    private static final int RESERVE_NW_Y = 20;
    private static final int NEXT_W = 6;
    private static final int NEXT_H = 21;
    private static final int RESERVE_W = 6;
    private static final int RESERVE_H = 6;

    private static final int COLTIME_MAX = 31; // 1 = 0.016s, 31 = 0.496
    private static final int COLCOUNT_MAX = 15; // 回転及び左右方向キー移動が15回に達すると次の接触により強制停止

    public  int myBanme = 1;
    private static int ten_digit = 0;
    private static int timerFromGameStart = 0;
    private static int players = 1;
    public int damage = 0;
    private static int[][] map = new int[/*MAP_H * players*/ MyFrame.sc_hn][/*MAP_W * players*/ MyFrame.sc_wn];
    private static int[][] minimap = new int[MINIMAP_H + MINIMAP_OFFSET_PLUS * 2][MINIMAP_W + MINIMAP_OFFSET_PLUS * 2];
    private static int[][][][] mino_array = { TETRIS_MINO.Z, TETRIS_MINO.J, TETRIS_MINO.S, TETRIS_MINO.O,
            TETRIS_MINO.T, TETRIS_MINO.I, TETRIS_MINO.L };
    public  CurrentMino mino;
    public  int reserveMino = 0;
    public  int[] next = new int[4];
    private static ArrayList<Integer> m_arr = new ArrayList<>();
    
    private boolean reserveFrag = true;
    private boolean sButtonFrag = true;
    private boolean cButtonFrag = true;
    private boolean pButtonFrag = true;
    private boolean iButtonFrag = true;
    public boolean inGame = true;
    public boolean isOnline = false;
    public boolean damageFrag = false;
    private Random rand = new Random();
    private Client client = new Client(this);
    private Server server;
    private EnumScreenMode.ScreenMode screen = EnumScreenMode.ScreenMode.TITLE;

    private static int[][] input = { { 0, 0 }, { 0, 1 }, { 1, 0 }, { -1, 0 }, { 0, 0 } };

    public Map() {

        initMap();
        setLayout(null); // 閉じてもコンポの位置ずれがなくなる
        setFocusable(true);
        addKeyListener(this);
    }

    public void initMap() {

        ten_digit = timerFromGameStart = reserveMino = damage = 0;
        reserveFrag = true;
        sButtonFrag = true;
        cButtonFrag = true;
        pButtonFrag = true;
        iButtonFrag = true;
        inGame = true;
        damageFrag = false;

        for (int s = m_arr.size() - 1; s >= 0; s--) {
            m_arr.remove(s);
        }

        viewFence1(myBanme, true);

        for (int i = 0; i < next.length; i++)
            next[i] = 0;
        
        viewFence2(myBanme, true);
        lotteryMinoArray();
        generate();
    }

    private static void setTile(int x, int y, int label) {

        int p_x = x * TILE_W;
        int p_y = y * TILE_H;
        fill(p_x, p_y, TILE_W, TILE_H, label);
    }

    private static void fill(int x_l, int y_u, int t_w, int t_h, int label) {

        for (int j = y_u; j < y_u + t_h; j++) {
            for (int i = x_l; i < x_l + t_w; i++) {
                map[j + OFFSET_Y][i + OFFSET_X] = label;
            }
        }
    }

    public void mainLoop() {

        long[] time = new long[2];
        long erapsedTime = 0;
        int fellInterval = 60;
        final int frameRate = 60;
        int executeInterval = 1000 / frameRate;
        while (true) {

            time[0] = System.nanoTime();
            switch (screen) {
                case TITLE:

                    break;
                case CSERVER:
                    break;
                case PSERVER:
                    break;
                case INGAME:
                    solidificateMino();
                    if ((timerFromGameStart - mino.geneTime) % (fellInterval) == 0 && inGame) move(input[1], true, 1);
                    if (isOnline) client.sendMessage(minimap);
                    timerFromGameStart++;
                    if (timerFromGameStart % (frameRate * 60) == 0 && fellInterval > 1) fellInterval -= (fellInterval == 10) ? 9 : 10; 
                    break;
                case RESULT:
                    break;
                default : 
            }
            time[1] = System.nanoTime();

            try {
                erapsedTime = (time[1] - time[0]) / 1000000;
                Thread.sleep((erapsedTime > executeInterval) ? 0 : executeInterval - erapsedTime);
            } catch (Exception e) {
                System.out.println("Thread error");
                System.exit(-1);
            }
        }
    }

    private boolean move(int[] vec, boolean enableMove, int count) {
        boolean canMove;
        boolean colFrag = false;
        do {
            int[] newPos = { mino.pos.x + vec[0], mino.pos.y + vec[1] };
            canMove = true;
            for (int j = 0; j < MINO_H; j++) {
                for (int i = 0; i < MINO_W; i++) {
                    if (mino.arr[mino.mode][j][i] == 1 && minimap[MINIMAP_OFFSET_PLUS + newPos[1] + j][MINIMAP_OFFSET_PLUS + newPos[0] + i] != 0)
                        canMove = false;
                }
            }

            if (enableMove) {
                if (canMove) {
                    mino.pos.x = newPos[0];
                    mino.pos.y = newPos[1];
                } else {
                    if (vec[1] > 0)
                        colFrag = true;
                    if (vec[0] == 0 && vec[1] == 0)
                        inGame = false;
                        //client.sendMessage("LOSE");
                }
            }
        } while (--count > 0);
        return canMove;
    }

    private void solidificateMino() {
        boolean touchGround = !move(input[1], false, 1);
        if (touchGround && (mino.groundContactAvoidCount >= COLCOUNT_MAX || mino.groundContactTime >= COLTIME_MAX)) {
            for (int j = 0; j < MINO_H; j++) {
                for (int i = 0; i < MINO_W; i++) {
                    if (mino.arr[mino.mode][j][i] == 1)
                        minimap[MINIMAP_OFFSET_PLUS + mino.pos.y + j][MINIMAP_OFFSET_PLUS + mino.pos.x + i] = mino.label;
                }
            }
            checkAndRemoveLine();
            ten_digit = (ten_digit + 10) % 1000;
            if (damageFrag) damage(damage);
            generate();
            reserveFrag = true;
        }
        if (touchGround) mino.groundContactTime++;
    }

    public void generate() {
        if (next[0] == 0) {
            for (int i = 0; i < next.length; i++) {
                next[i] = m_arr.get(i);
                m_arr.remove(i);
            }
        }

        int mode = 0;
        mino = new CurrentMino(mino_array[next[0] - 2], mode, next[0] + ten_digit, timerFromGameStart, 5, 0);
        for (int i = 0; i < next.length - 1; i++)
            next[i] = next[i + 1];
        if (m_arr.size() <= 0)
            lotteryMinoArray();
        next[next.length - 1] = m_arr.get(0);
        m_arr.remove(0);

        mino.collision = move(input[0], true, 1);
    }

    private void lotteryMinoArray() {
        if (m_arr.size() == 0) {
            for (int i = 0; i < mino_array.length; i++) {
                m_arr.add(i, i + 2);
            }

            int ten_digit = 20;
            int temp;
            int i_1, i_2;
            do {
                i_1 = rand.nextInt(mino_array.length);
                i_2 = rand.nextInt(mino_array.length);
                temp = m_arr.get(i_1);
                m_arr.set(i_1, m_arr.get(i_2));
                m_arr.set(i_2, temp);
            } while (ten_digit-- > 0);
        }
    }

    int streak = 0;
    private void checkAndRemoveLine() {
        boolean removeLine = true;
        int total_remove_row = 0;
        for (int j = 0; j < MINIMAP_H - 1; j++) {
            for (int i = 1; i < MINIMAP_W - 1; i++) {
                if (minimap[MINIMAP_OFFSET_PLUS + j][MINIMAP_OFFSET_PLUS + i] == 0) 
                    removeLine = false;
            }
            if (removeLine) {
                for (int i = 1; i < MINIMAP_W - 1; i++)
                    minimap[MINIMAP_OFFSET_PLUS + j][MINIMAP_OFFSET_PLUS + i] = 0;
                for (int j2 = j; j2 > 0; j2--) {
                    for (int i2 = 1; i2 < MINIMAP_W - 1; i2++) {
                        minimap[MINIMAP_OFFSET_PLUS + j2][MINIMAP_OFFSET_PLUS + i2] = minimap[MINIMAP_OFFSET_PLUS + j2 - 1][MINIMAP_OFFSET_PLUS + i2];
                    }
                }
                total_remove_row++;
            }
            removeLine = true;
        }
        if (isOnline && total_remove_row > 0){
            if (damage > 0) {
                if (damage >= total_remove_row) {
                    damage -= total_remove_row;
                } else {
                damage = 0;
                total_remove_row -= damage; 
                }
            } 
            if (damage == 0 && total_remove_row > 0) client.sendMessage(total_remove_row);
        }
    }

    public void damage(int damage) {
        for (int j = 0; j < damage; j++) {
            for (int j2 = 0; j2 < MINIMAP_H - 1; j2++) {
                for (int i2 = 0; i2 < MINIMAP_W - 1; i2++) {
                    minimap[MINIMAP_OFFSET_PLUS + j2][MINIMAP_OFFSET_PLUS + i2] = minimap[MINIMAP_OFFSET_PLUS + j2 + 1][MINIMAP_OFFSET_PLUS + i2];
                }
            }
            int hole_i = rand.nextInt(10) + 1;
            for (int i = 1; i < MINIMAP_W - 1; i++) {
                minimap[MINIMAP_OFFSET_PLUS + MINIMAP_H - 2][MINIMAP_OFFSET_PLUS + i] = (i == hole_i) ? 0 : 9;
            }
        }
        this.damage = 0;
        damageFrag = false;
    }

    private void rotate(int mode) {
        boolean col = false;
        int newMode = (mino.mode + mode + mino_array[0].length) % mino_array[0].length;
        int dor_excp_lmino = ((newMode % 2) == 0 && mode < 0) ? -1 : 1; // clock -> 1, cclock -> -1
        int dor_lmino = (mode < 0) ? 1 : 0; // clock -> 0, cclock -> 1

        int[] newPos = new int[2];
        for (int k = 0; k < TETRIS_MINO.SRS_CW.length; k++) {
            col = false;
            if (mino.mode != 7) {
                newPos[0] = mino.pos.x + TETRIS_MINO.SRS_CW[k][newMode][0] * dor_excp_lmino;
                newPos[1] = mino.pos.y + TETRIS_MINO.SRS_CW[k][newMode][1];
            } else {
                newPos[0] = mino.pos.x + TETRIS_MINO.SRS_I[k][newMode * 2 + dor_lmino][0];
                newPos[1] = mino.pos.y + TETRIS_MINO.SRS_I[k][newMode * 2 + dor_lmino][1];
            }
            
            for (int j = 0; j < MINO_H; j++) {
                for (int i = 0; i < MINO_W; i++) {
                    if (mino.arr[newMode][j][i] == 1 && minimap[MINIMAP_OFFSET_PLUS + newPos[1] + j][MINIMAP_OFFSET_PLUS + newPos[0] + i] != 0) {
                        if (mino.arr[newMode][j][i] == 1 && (minimap[MINIMAP_OFFSET_PLUS + newPos[1] + j][MINIMAP_OFFSET_PLUS + newPos[0] + i] == 1 || (minimap[MINIMAP_OFFSET_PLUS + newPos[1] + j][MINIMAP_OFFSET_PLUS + newPos[0] + i] % 10) > 1)) 
                            col = true;
                    }
                }
            }
            if (!col) {
                mino.pos.x = newPos[0];
                mino.pos.y = newPos[1];
                mino.mode = newMode;
                minoSolidicationTimeReset();
                break;
            }
        }
    }

    private void minoSolidicationTimeReset() {
        if (!move(input[1], false, 1)) {
            mino.groundContactTime = 0;
            mino.groundContactAvoidCount++;
        }
    }

    @Override
    public void paintComponent(Graphics g) {

        switch (screen) {
            case TITLE:
                title(g);
                break;
            case CSERVER:
                cserver(g);
                break;
            case PSERVER:
                pserver(g);
                break;
            case INGAME:
                paintMap(g);
                setMap(minimap, myBanme, true, mino.pos.x, mino.pos.y, mino.mode, mino.label);
                viewNextAndKeep(myBanme, true, next, reserveMino);
                viewFence2(myBanme, true);
                break;
            case RESULT:
                break;
            default :
        }
        repaint();
    }

    final int Y = 300;
    final int Y_M = 30;
    final int WIDTH = 230;
    final int HEIGHT = 20;
    int X = (MyFrame.sc_wn - WIDTH) / 2;
    JButton sButton_START;
    JButton sButton_CS;
    JButton sButton_SS;
    private void title(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, MyFrame.sc_wn, MyFrame.sc_hn);
        
        if(sButtonFrag) {
            sButtonFrag = false;
            rmrp(g);
            sButton_START = new JButton("START");
            sButton_CS = new JButton("CONNECT SERVER");
            sButton_SS = new JButton("SET UP SERVER");
            
            this.add(sButton_START);
            sButton_START.setBounds(X, Y + 0 * Y_M, WIDTH, HEIGHT);
            sButton_START.setForeground(Color.BLACK);
            sButton_START.addActionListener(new ActionListener() {
                // @Override
                public void actionPerformed(ActionEvent e) {
                    initMap();
                    changeScreen(EnumScreenMode.ScreenMode.INGAME);
                    sButtonFrag = true;
                    rmrp(g);
                }
            });
            this.add(sButton_CS);
            sButton_CS.setBounds(X, Y + 1 * Y_M, WIDTH, HEIGHT);
            sButton_CS.setForeground(Color.BLACK);
            sButton_CS.addActionListener(new ActionListener() {
                // @Override
                public void actionPerformed(ActionEvent e) {
                    changeScreen(EnumScreenMode.ScreenMode.CSERVER);
                    sButtonFrag = true;
                    rmrp(g);
                }
            });
            
            this.add(sButton_SS);
            sButton_SS.setBounds(X, Y + 2 * Y_M, WIDTH, HEIGHT);
            sButton_SS.setForeground(Color.BLACK);
            sButton_SS.addActionListener(new ActionListener() {
                // @Override
                public void actionPerformed(ActionEvent e) {
                    changeScreen(EnumScreenMode.ScreenMode.PSERVER);
                    sButtonFrag = true;
                    rmrp(g);
                }
            });
        }
    }

    JTextField tf_inputIP;
    JButton cButton_CONNECT;
    JButton cbutton_DISCONNECT;
    JButton cButton_BACKTITLE;
    private void cserver(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, MyFrame.sc_wn, MyFrame.sc_hn);
        

        if(cButtonFrag) {
            cButtonFrag = false;
            rmrp(g);
            cButton_CONNECT = new JButton("CONNECT");
            cbutton_DISCONNECT = new JButton("DISCONNECT");
            cButton_BACKTITLE = new JButton("BACK TO TITLE");

            tf_inputIP = new JTextField();
            this.add(tf_inputIP);
            tf_inputIP.setBounds(X, Y - 1 * Y_M, WIDTH, HEIGHT);
            tf_inputIP.setForeground(Color.BLACK);
            tf_inputIP.setEditable(true);
            
            this.add(cButton_CONNECT);
            cButton_CONNECT.setBounds(X, Y + 0 * Y_M, WIDTH, HEIGHT);
            cButton_CONNECT.setForeground(Color.BLACK);
            cButton_CONNECT.addActionListener(new ActionListener() {
                // @Override
                public void actionPerformed(ActionEvent e) {
                    client.join();
                }
            });

            this.add(cbutton_DISCONNECT);
            cbutton_DISCONNECT.setBounds(X, Y + 1 * Y_M, WIDTH, HEIGHT);
            cbutton_DISCONNECT.setForeground(Color.BLACK);
            cbutton_DISCONNECT.addActionListener(new ActionListener() {
                // @Override
                public void actionPerformed(ActionEvent e) {
                    client.quit();
                }
            });

            this.add(cButton_BACKTITLE);
            cButton_BACKTITLE.setBounds(X, Y + 2 * Y_M, WIDTH, HEIGHT);
            cButton_BACKTITLE.setForeground(Color.BLACK);
            cButton_BACKTITLE.addActionListener(new ActionListener() {
                // @Override
                public void actionPerformed(ActionEvent e) {
                    client.quit();
                    changeScreen(EnumScreenMode.ScreenMode.TITLE);
                    cButtonFrag = true;
                    rmrp(g);
                }
            });
        }
        cButton_CONNECT.setEnabled((isOnline) ? false : true);
        client.setIP(tf_inputIP.getText());
    }

    JTextField tf_showIP;
    JButton pButton_START;
    JButton pButton_SETUP;
    JButton pButton_DISCONNECT;
    JButton pButton_BACKTITLE;
    private void pserver(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, MyFrame.sc_wn, MyFrame.sc_hn);
        if(pButtonFrag) {
            rmrp(g);
            pButtonFrag = false;
            pButton_START = new JButton("START");
            pButton_SETUP = new JButton("SET UP SERVER");
            pButton_DISCONNECT = new JButton("DISCONNECT");
            pButton_BACKTITLE = new JButton("BACK TO TITLE");
            
            tf_showIP = new JTextField();
            this.add(tf_showIP);
            tf_showIP.setBounds(X, Y - 2 * Y_M, WIDTH, HEIGHT);
            tf_showIP.setForeground(Color.BLACK);
            tf_showIP.setEditable(false);
            
            this.add(pButton_START);
            pButton_START.setBounds(X, Y - 1 * Y_M, WIDTH, HEIGHT);
            pButton_START.setForeground(Color.BLACK);
            pButton_START.addActionListener(new ActionListener() {
                // @Override
                public void actionPerformed(ActionEvent e) {
                    server.sendAll("START");
                    pButtonFrag = true;
                    rmrp(g);
                }
            });

            this.add(pButton_SETUP);
            pButton_SETUP.setBounds(X, Y + 0 * Y_M, WIDTH, HEIGHT);
            pButton_SETUP.setForeground(Color.BLACK);
            pButton_SETUP.addActionListener(new ActionListener() {
                // @Override
                public void actionPerformed(ActionEvent e) {
                    server = new Server();
                    server.start();
                    try{
                        Thread.sleep(300);
                    }catch(Exception es) {
                        es.printStackTrace();
                    };
                    client.join();
                    tf_showIP.setText(server.getIP());
                }
            });
            
            this.add(pButton_DISCONNECT);
            pButton_DISCONNECT.setBounds(X, Y + 1 * Y_M, WIDTH, HEIGHT);
            pButton_DISCONNECT.setForeground(Color.BLACK);
            pButton_DISCONNECT.addActionListener(new ActionListener() {
                // @Override
                public void actionPerformed(ActionEvent e) {
                    client.quit();
                    if (server != null) {
                        server.stopServer();
                        server=null;
                    }
                    System.gc();
                }
            });

            this.add(pButton_BACKTITLE);
            pButton_BACKTITLE.setBounds(X, Y + 2 * Y_M, WIDTH, HEIGHT);
            pButton_BACKTITLE.setForeground(Color.BLACK);
            pButton_BACKTITLE.addActionListener(new ActionListener() {
                // @Override
                public void actionPerformed(ActionEvent e) {
                    client.quit();
                    if (server != null) {
                        server.stopServer();
                        server=null;
                    }
                    changeScreen(EnumScreenMode.ScreenMode.TITLE);
                    pButtonFrag = true;
                    rmrp(g);
                }
            });
        }
        pButton_SETUP.setEnabled((isOnline) ? false : true);
    }

    private void paintMap(Graphics g) {
        // 0 air black
        // 1 wall gray
        // 2 z red
        // 3 j blue
        // 4 s green
        // 5 o yellow
        // 6 t purple
        // 7 i cyan
        // 8 l orange
        if (iButtonFrag) rmrp(g);
        Color PURPLE = new Color(162, 96, 191);
        Color ORANGE = new Color(245, 130, 32);
        Color[] color = { Color.BLACK, Color.GRAY, Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW, PURPLE, Color.CYAN,
                ORANGE, Color.WHITE};
        int temp = 0;
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, MyFrame.sc_wn, MyFrame.sc_hn);

        for (int j = 0; j < MyFrame.sc_hn; j++) {
            for (int i = 0; i < MyFrame.sc_wn; i++) {
                temp = (map[j][i] % 10);
                if (i != 0 && i != MyFrame.sc_wn - 1 && j != 0 && j != MyFrame.sc_hn - 1) {
                    if (map[j][i] != map[j][i - 1] || map[j][i] != map[j][i + 1] || map[j][i] != map[j - 1][i]
                            || map[j][i] != map[j + 1][i]) {
                        g.setColor(color[temp]);
                    } else {
                        g.setColor(Color.BLACK);
                    }
                } else {
                    g.setColor(color[temp]);
                }

                g.fillRect(OFFSET_X + i, OFFSET_Y + j, 1, 1);
            }
        }
    }
    
    public synchronized void setMap(int[][] mmap, int banme, boolean itsme, int mposX, int mposY, int mmode, int mlabel) {
        int[] adjustLeftMargin = {MAP_W * (banme - 1), 20 * (banme - 1)};
        //System.out.println("banme : " +banme+", mposX : "+mposX+", mposY : "+mposY+", mmode : "+mmode+", mlabel : "+mlabel);
        int minonum = (mlabel % 10) - 2;
        if (itsme || banme != myBanme) {
            for (int j = 0; j < MINIMAP_H * TILE_H; j++) {
                for (int i = 0; i < MINIMAP_W * TILE_W; i++) {
                    map[j + OFFSET_Y][i + OFFSET_X + adjustLeftMargin[0]] = mmap[MINIMAP_OFFSET_PLUS + j / TILE_H][MINIMAP_OFFSET_PLUS + i / TILE_W];
                }
            }
            for (int j = 0; j < MINO_H; j++) {
                for (int i = 0; i < MINO_W; i++) {
                    if (mino_array[minonum][mmode][j][i] == 1 && mmap[MINIMAP_OFFSET_PLUS + mposY + j][MINIMAP_OFFSET_PLUS + mposX + i] == 0)
                        setTile(mposX + i + adjustLeftMargin[1], mposY + j, mlabel);
                }
            }
            if (itsme) {
                int ghost_height = 0;
                for (int j = 1; j < MINIMAP_H - mposY; j++) {
                    input[4][0] = 0;
                    input[4][1] = j;
                    if (move(input[4], false, 1)) {
                        ghost_height++;
                    } else {
                        System.out.println(j + " " + ghost_height);
                        break;
                    }
                }
                if (ghost_height != 0)  {
                    for (int j = 0; j < MINO_H; j++) {
                        for (int i = 0; i < MINO_W; i++) {
                            if (mino_array[minonum][mmode][j][i] == 1 && mmap[MINIMAP_OFFSET_PLUS + mposY + ghost_height + j][MINIMAP_OFFSET_PLUS + mposX + i] == 0)
                                setTile(mposX + i + adjustLeftMargin[1], mposY + ghost_height + j, 9);
                        }
                    }
                }
            }
        }
    }

    public synchronized void viewFence1(int banme, boolean itsme) {
        int[] adjustLeftMargin = {MAP_W * (banme - 1), 20 * (banme - 1)};
        for (int j = 0; j < MINIMAP_H; j++) {
            for (int i = 0; i < MINIMAP_W; i++) {
                if (itsme) minimap[MINIMAP_OFFSET_PLUS + j][MINIMAP_OFFSET_PLUS + i] = 0;
                setTile(adjustLeftMargin[1] + i, j, 0);
            }
        }
        for (int j = 0; j < MINIMAP_H; j++) {
            if (itsme) minimap[MINIMAP_OFFSET_PLUS + j][MINIMAP_OFFSET_PLUS + 0] = 1;
            if (itsme) minimap[MINIMAP_OFFSET_PLUS + j][MINIMAP_OFFSET_PLUS + MINIMAP_W - 1] = 1;
            setTile(adjustLeftMargin[1] + 0, j, 1);
            setTile(adjustLeftMargin[1] + MINIMAP_W - 1, j, 1);
        }
        for (int i = 0; i < MINIMAP_W; i++) {
            if (itsme) minimap[MINIMAP_OFFSET_PLUS + MINIMAP_H - 1][MINIMAP_OFFSET_PLUS + i] = 1;
            setTile(adjustLeftMargin[1] + i, MINIMAP_H - 1, 1);
        }
    }
    
    
    public synchronized void viewNextAndKeep(int banme, boolean itsme, int[] next, int reserveMino) {
        int[] adjustLeftMargin = {MAP_W * (banme - 1), 20 * (banme - 1)};
        int label = 0;
        if (itsme || banme != myBanme) {
            for (int c = 0; c < next.length; c++) {
                for (int j = 0; j < MINO_H; j++) {
                    for (int i = 0; i < MINO_W; i++) {
                        label = (mino_array[next[c] - 2][0][j][i] == 1) ? next[c] : 0;
                        setTile(adjustLeftMargin[1] + NEXT_NW_X + 1 + i, NEXT_NW_Y + 1 + j + c * (MINO_H + 1), label);
                    }
                }
            }
            if (reserveMino != 0) {
                for (int j = 0; j < MINO_H; j++) {
                    for (int i = 0; i < MINO_W; i++) {
                        label = (mino_array[reserveMino - 2][0][j][i] == 1) ? (reserveMino % 10) : 0;
                        setTile(adjustLeftMargin[1] + RESERVE_NW_X + 1 + i, RESERVE_NW_Y + 1 + j, label);
                    }
                }
            } else {
                for (int j = 0; j < MINO_H; j++) {
                    for (int i = 0; i < MINO_W; i++) setTile(adjustLeftMargin[1] + RESERVE_NW_X + 1 + i, RESERVE_NW_Y + 1 + j, 0);
                }
            }
        }
    }

    public synchronized void viewFence2(int banme, boolean itsme) {
        int[] adjustLeftMargin = {MAP_W * (banme - 1), 20 * (banme - 1)};
        if (itsme || banme != myBanme) {
            for (int j = 0; j < NEXT_H; j++) {
                for (int i = 0; i < NEXT_W; i++) {
                    if (i == 0 || i == (NEXT_W - 1) || (j % (NEXT_W - 1)) == 0)
                        setTile(adjustLeftMargin[1] + NEXT_NW_X + i, NEXT_NW_Y + j, 1);
                }
            }
            for (int j = 0; j < RESERVE_H; j++) {
                for (int i = 0; i < RESERVE_W; i++) {
                    if (i == 0 || i == (RESERVE_W - 1) || j == 0 || j == (RESERVE_H - 1))
                        setTile(adjustLeftMargin[1] + RESERVE_NW_X + i, RESERVE_NW_Y + j, 1);
                }
            }
        }
    }
    
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT && inGame) {
            minoSolidicationTimeReset();
            mino.collision = move(input[3], true, 1);
        }
        if (key == KeyEvent.VK_RIGHT && inGame) {
            minoSolidicationTimeReset();
            mino.collision = move(input[2], true, 1);
        }
        if (key == KeyEvent.VK_UP && inGame) {
            input[4][0] = 0;
            input[4][1] = 0;
            for (int j = 0; j < MINIMAP_H - 1 - mino.pos.y; j++) {
                input[4][1] = j;
                mino.collision = !move(input[4], false, 1);
                if (mino.collision) {
                    input[4][1] = j - 1;
                    mino.groundContactTime = COLTIME_MAX - 2; // 2 frame(0.032s) before next mino generation
                    mino.collision = move(input[4], true, 1);
                    mino.collision = move(input[1], true, 1);
                    solidificateMino();
                    break;
                }
            }
        }
        if (key == KeyEvent.VK_DOWN && inGame) {
            mino.collision = move(input[1], true, 3);
        }
        if (key == KeyEvent.VK_SPACE) {
            if (isOnline) {
            client.sendMessage("BACKTO-C-ST");
            } else {
                changeScreen(EnumScreenMode.ScreenMode.TITLE);
            }
            
        }
        if (key == KeyEvent.VK_A && inGame) {
            rotate(-1);
        }
        if (key == KeyEvent.VK_D && inGame) {
            rotate(1);
        }

        if (key == KeyEvent.VK_W && inGame && reserveFrag) {
            int temp = mino.label % 10;
            if (reserveMino != 0) {
                mino = new CurrentMino(mino_array[reserveMino - 2], 0, reserveMino + ten_digit, timerFromGameStart, 5, 0);
            } else {
                generate();
            }
            reserveMino = temp;
            reserveFrag = false;
        }
    }

    public void keyReleased(KeyEvent e) {
    }

    public void keyTyped(KeyEvent e) {
    }

    public void actionPerformed(ActionEvent e) {
    }

    private void rmrp(Graphics g) {
        removeAll();
        repaint();
        System.gc();
    }

    public void changeScreen(EnumScreenMode.ScreenMode mode) {
        screen = mode;
    }

    public void setPlayers(int newPlayers) {
        MyFrame.setFrameSize(newPlayers, 1);
        
        try {
            int temp = newPlayers;
            int[][] temparr = new int[MyFrame.sc_hn][MyFrame.sc_wn];
            System.out.println("wn : " + MyFrame.sc_wn + ",hn : " + MyFrame.sc_hn);
            if (players < newPlayers) temp = players;
            players = newPlayers;
            
            for (int j = 0; j < MAP_H; j++) {
                for (int i = 0; i < MAP_W * temp; i++) {
                    temparr[j][i] = map[j][i];
                } 
            }
            map = temparr;
        } catch (Exception e) {
            
            e.printStackTrace();
            System.exit(-1);
        }
    }
}