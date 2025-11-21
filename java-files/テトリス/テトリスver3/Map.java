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

    private static final int MAP_W = 320;
    private static final int MAP_H = 480;
    private static final int TILE_W = 16;
    private static final int TILE_H = 16;
    private static final int MINIMAP_W = 12;
    private static final int MINIMAP_H = 21;
    private static final int OFFSET_X = 8;
    private static final int OFFSET_Y = 8;
    private static final int M_MAP_OFFSET_PLUS = 3;
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

    private static int ten_digit = 0;
    private static int timer = 0;
    private static int players = 1;
    private static int banme = 1;
    private static int[][] map = new int[MAP_H * players][MAP_W * players];
    private static int[][] minimap = new int[MINIMAP_H + M_MAP_OFFSET_PLUS][MINIMAP_W + M_MAP_OFFSET_PLUS];
    private static int[][][][] mino_array = { TETRIS_MINO.Z, TETRIS_MINO.J, TETRIS_MINO.S, TETRIS_MINO.O,
            TETRIS_MINO.T, TETRIS_MINO.I, TETRIS_MINO.L };
    private static CurrentMino mino;
    private static int reserveMino = 0;
    private static int[] next = new int[4];
    private static ArrayList<Integer> m_arr = new ArrayList<>();
    
    private static boolean reserveFrag = true;
    private static boolean sButtonFrag = true;
    private static boolean cButtonFrag = true;
    private static boolean pButtonFrag = true;
    private static boolean iButtonFrag = true;
    private static boolean inGame = true;
    private static boolean isOnline = false;
    private static Random rand = new Random();
    private Client client = new Client();
    private Server server;
    private static EnumScreenMode.ScreenMode screen = EnumScreenMode.ScreenMode.TITLE;

    private static int[][] input = { { 0, 0 }, { 0, 1 }, { 1, 0 }, { -1, 0 }, { 0, 0 } };

    public Map() {

        initMap();
        setFocusable(true);
        addKeyListener(this);
    }

    public void initMap() {

        ten_digit = timer = reserveMino = 0;
        reserveFrag = true;
        sButtonFrag = true;
        cButtonFrag = true;
        pButtonFrag = true;
        iButtonFrag = true;

        for (int j = 0; j < MINIMAP_H; j++) {
            for (int i = 0; i < MINIMAP_W; i++) {
                minimap[j][i] = 0;
                setTile(i, j, 0);
            }
        }
        for (int j = 0; j < MINIMAP_H; j++) {
            minimap[j][0] = 1;
            minimap[j][MINIMAP_W - 1] = 1;
            setTile(0, j, 1);
            setTile(MINIMAP_W - 1, j, 1);
        }
        for (int i = 0; i < MINIMAP_W; i++) {
            minimap[MINIMAP_H - 1][i] = 1;
            setTile(i, MINIMAP_H - 1, 1);
        }
        for (int s = m_arr.size() - 1; s >= 0; s--) {
            m_arr.remove(s);
        }

        setFense(banme, true);

        for (int i = 0; i < next.length; i++)
            next[i] = 0;
        
        viewFence();
        lottery();
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

        while (true) {

            switch (screen) {
                case TITLE:

                    break;
                case CSERVER:
                    break;
                case PSERVER:
                    break;
                case INGAME:
                    if (timer == mino.geneTime && inGame) move(input[1], true, 1);
                    if (isOnline) client.sendMessage(minimap);
                    timer = (timer + 1) % 8;
                    break;
                case RESULT:
                    break;
                default :  
            }

            try {
                Thread.sleep(50);
            } catch (Exception e) {
                System.out.println("Thread error");
                System.exit(-1);
            }
        }
    }

    public void generate() {
        if (next[0] == 0) {
            for (int i = 0; i < next.length; i++) {
                next[i] = m_arr.get(i);
                m_arr.remove(i);
            }
        }

        System.out.print("size " + m_arr.size() + " :");
        for (int i = 0; i < m_arr.size(); i++) {
            System.out.print(" " + m_arr.get(i) + " ");
        }
        System.out.print("\n");

        int mode = 0;
        mino = new CurrentMino(mino_array[next[0] - 2], mode, next[0] + ten_digit, timer);
        for (int i = 0; i < next.length - 1; i++)
            next[i] = next[i + 1];
        if (m_arr.size() <= 0)
            lottery();
        next[next.length - 1] = m_arr.get(0);
        m_arr.remove(0);

        mino.collision = move(input[0], true, 1);
    }

    private void lottery() {
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

    private boolean move(int[] vec, boolean enable, int count) {
        boolean canMove;
        boolean colFrag = false;
        do {
            int[] newPos = { mino.pos[0] + vec[0], mino.pos[1] + vec[1] };
            canMove = true;
            for (int j = 0; j < MINO_H; j++) {
                for (int i = 0; i < MINO_W; i++) {
                    if (mino.arr[mino.mode][j][i] == 1 && minimap[newPos[1] + j][newPos[0] + i] != 0)
                        canMove = false;
                }
            }

            if (enable) {
                if (canMove) {
                    mino.pos[0] = newPos[0];
                    mino.pos[1] = newPos[1];
                } else {
                    if (vec[1] > 0)
                        colFrag = true;
                    if (mino.onFloor > 2) {
                        for (int j = 0; j < MINO_H; j++) {
                            for (int i = 0; i < MINO_W; i++) {
                                if (mino.arr[mino.mode][j][i] == 1)
                                    minimap[mino.pos[1] + j][mino.pos[0] + i] = mino.label;
                            }
                        }
                        checkAndRemoveLine();
                        ten_digit = (ten_digit + 10) % 1000;
                        generate();
                        reserveFrag = true;
                    }
                    if (vec[0] == 0 && vec[1] == 0)
                        inGame = false;
                }
            }
        } while (--count > 0);
        if (colFrag)
            mino.onFloor++;
        return canMove;
    }

    private void checkAndRemoveLine() {
        boolean removeLine = true;

        for (int j = 0; j < MINIMAP_H - 1; j++) {
            for (int i = 1; i < MINIMAP_W - 1; i++) {
                if (minimap[j][i] == 0)
                    removeLine = false;
            }
            if (removeLine) {
                for (int i = 1; i < MINIMAP_W - 1; i++)
                    minimap[j][i] = 0;
                for (int j2 = j; j2 > 0; j2--) {
                    for (int i2 = 0; i2 < MINIMAP_W - 1; i2++) {
                        minimap[j2][i2] = minimap[j2 - 1][i2];
                    }
                }
            }
            removeLine = true;
        }
    }

    private void rotate(int mode) {
        boolean colWall = false;
        boolean colMino = false;
        boolean canMove = true;
        int[] colPos = new int[2];
        int newMode = (mino.mode + mode + mino_array[0].length) % mino_array[0].length;
        int[] newPos = new int[2];
        
        for (int k = 0; k < TETRIS_MINO.SRS_CW.length; k++) {
            colWall = false;
            colMino = false;
            int c = (newMode > mino.mode) ? 1 : -1 ;;
            newPos[0] = mino.pos[0] + TETRIS_MINO.SRS_CW[k][newMode][0] * c;
            newPos[1] = mino.pos[1] + TETRIS_MINO.SRS_CW[k][newMode][1];
            for (int j = 0; j < MINO_H; j++) {
                for (int i = 0; i < MINO_W; i++) {
                    if (mino.arr[newMode][j][i] == 1 && minimap[newPos[1] + j][newPos[0] + i] != 0) {
                        if (mino.arr[newMode][j][i] == 1 && minimap[newPos[1] + j][newPos[0] + i] == 1) 
                            colWall = true;
                        if (mino.arr[newMode][j][i] == 1 && (minimap[newPos[1] + j][newPos[0] + i] % 10) > 1)
                            colMino = true;
                        if (k == 0) {
                            colPos[1] = j;
                            colPos[0] = i;
                        }
                    }
                }
            }
            if (!colWall && !colMino) {
                canMove = true;
                colWall = false;
                colMino = false;
                mino.pos[0] = newPos[0];
                mino.pos[1] = newPos[1];
                break;
            }
        }
        if (colWall && !colMino) {
            if (colPos[0] < 1) {
                mino.pos[0]++;
            } else {
                mino.pos[0]--;
                if (mino.getLabelColorNum() == 7)
                    mino.pos[0]--;
            }
            if (colPos[1] < 1) {
                mino.pos[1]++;
            } else {
                mino.pos[1]--;
                if (mino.getLabelColorNum() == 7)
                    mino.pos[1]--;
            }
        }
        if (!colWall && !colMino) {
            mino.mode = newMode;
        }

        System.out.println("COL : colWall " + colWall);
        System.out.println("COL : colMino " + colMino);
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
                setMap(minimap, banme, true);
                viewNextAndKeep();
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
    int X = (MyFrame.SCREEN_W - WIDTH) / 2;
    private void title(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, MyFrame.SCREEN_W, MyFrame.SCREEN_H);
        if(sButtonFrag) {
            sButtonFrag = false;
            rmrp(g);
            JButton button_START;
            JButton button_CS;
            JButton button_PS;
            
            button_START = new JButton("START");
            this.add(button_START);
            button_START.setBounds(X, Y + 0 * Y_M, WIDTH, HEIGHT);
            button_START.setForeground(Color.BLACK);
            button_START.addActionListener(new ActionListener() {
                // @Override
                public void actionPerformed(ActionEvent e) {
                    changeScreen(EnumScreenMode.ScreenMode.INGAME);
                    sButtonFrag = true;
                    rmrp(g);
                }
            });
            button_CS = new JButton("CONNECT SERVER");
            this.add(button_CS);
            button_CS.setBounds(X, Y + 1 * Y_M, WIDTH, HEIGHT);
            button_CS.setForeground(Color.BLACK);
            button_CS.addActionListener(new ActionListener() {
                // @Override
                public void actionPerformed(ActionEvent e) {
                    changeScreen(EnumScreenMode.ScreenMode.CSERVER);
                    sButtonFrag = true;
                    rmrp(g);
                }
            });
            button_PS = new JButton("PUBLISH SERVER");
            this.add(button_PS);
            button_PS.setBounds(X, Y + 2 * Y_M, WIDTH, HEIGHT);
            button_PS.setForeground(Color.BLACK);
            button_PS.addActionListener(new ActionListener() {
                // @Override
                public void actionPerformed(ActionEvent e) {
                    isOnline = true;
                    server = new Server();
                    server.start();
                    try{
                        Thread.sleep(300);
                    }catch(Exception es) {
                        es.printStackTrace();
                    };
                    client.join();
                    changeScreen(EnumScreenMode.ScreenMode.PSERVER);
                    sButtonFrag = true;
                    rmrp(g);
                }
            });
        }
    }

    JTextField tf_inputIP;
    private void cserver(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, MyFrame.SCREEN_W, MyFrame.SCREEN_H);
        if(cButtonFrag) {
            cButtonFrag = false;
            rmrp(g);
            JButton button_CONNECT;
            JButton button_DISCONNECT;
            JButton button_BACKTITLE;

            tf_inputIP = new JTextField();
            this.add(tf_inputIP);
            tf_inputIP.setBounds(X, Y - 1 * Y_M, WIDTH, HEIGHT);
            tf_inputIP.setForeground(Color.BLACK);
            tf_inputIP.setEditable(true);
            
            button_CONNECT = new JButton("CONNECT");
            this.add(button_CONNECT);
            button_CONNECT.setBounds(X, Y + 0 * Y_M, WIDTH, HEIGHT);
            button_CONNECT.setForeground(Color.BLACK);
            button_CONNECT.addActionListener(new ActionListener() {
                // @Override
                public void actionPerformed(ActionEvent e) {
                    isOnline = true;
                    client.join();
                }
            });

            button_DISCONNECT = new JButton("DISCONNECT");
            this.add(button_DISCONNECT);
            button_DISCONNECT.setBounds(X, Y + 1 * Y_M, WIDTH, HEIGHT);
            button_DISCONNECT.setForeground(Color.BLACK);
            button_DISCONNECT.addActionListener(new ActionListener() {
                // @Override
                public void actionPerformed(ActionEvent e) {
                    isOnline = false;
                    client.quit();
                }
            });

            button_BACKTITLE = new JButton("BACK TO TITLE");
            this.add(button_BACKTITLE);
            button_BACKTITLE.setBounds(X, Y + 2 * Y_M, WIDTH, HEIGHT);
            button_BACKTITLE.setForeground(Color.BLACK);
            button_BACKTITLE.addActionListener(new ActionListener() {
                // @Override
                public void actionPerformed(ActionEvent e) {
                    isOnline = false;
                    client.quit();
                    changeScreen(EnumScreenMode.ScreenMode.TITLE);
                    cButtonFrag = true;
                    rmrp(g);
                }
            });
        }
        
        client.setIP(tf_inputIP.getText());
    }

    private void pserver(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, MyFrame.SCREEN_W, MyFrame.SCREEN_H);
        if(pButtonFrag) {
            rmrp(g);
            pButtonFrag = false;
            JButton button_START;
            JButton button_DISCONNECT;
            JButton button_BACKTITLE;
            JTextField tf_showIP;
            
            button_START = new JButton("START");
            this.add(button_START);
            button_START.setBounds(X, Y + 0 * Y_M, WIDTH, HEIGHT);
            button_START.setForeground(Color.BLACK);
            button_START.addActionListener(new ActionListener() {
                // @Override
                public void actionPerformed(ActionEvent e) {
                    server.sendAll("START");
                    pButtonFrag = true;
                    rmrp(g);
                }
            });
            
            button_DISCONNECT = new JButton("DISCONNECT");
            this.add(button_DISCONNECT);
            button_DISCONNECT.setBounds(X, Y + 1 * Y_M, WIDTH, HEIGHT);
            button_DISCONNECT.setForeground(Color.BLACK);
            button_DISCONNECT.addActionListener(new ActionListener() {
                // @Override
                public void actionPerformed(ActionEvent e) {
                    isOnline = false;
                    client.quit();
                    if (server != null) {
                        server.stopServer();
                        server=null;
                    }
                    System.gc();
                }
            });

            button_BACKTITLE = new JButton("BACK TO TITLE");
            this.add(button_BACKTITLE);
            button_BACKTITLE.setBounds(X, Y + 2 * Y_M, WIDTH, HEIGHT);
            button_BACKTITLE.setForeground(Color.BLACK);
            button_BACKTITLE.addActionListener(new ActionListener() {
                // @Override
                public void actionPerformed(ActionEvent e) {
                    isOnline = false;
                    client.quit();
                    if (server != null) {
                        server.stopServer();
                        server=null;
                    }
                    changeScreen(EnumScreenMode.ScreenMode.TITLE);
                    cButtonFrag = true;
                    rmrp(g);
                }
            });

            tf_showIP = new JTextField();
            this.add(tf_showIP);
            tf_showIP.setBounds(X, Y - 1 * Y_M, WIDTH, HEIGHT);
            tf_showIP.setForeground(Color.BLACK);
            tf_showIP.setEditable(false);
            tf_showIP.setText(server.getIP());
        }
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
                ORANGE };
        int temp = 0;
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, MyFrame.SCREEN_W * players, MyFrame.SCREEN_H);

        for (int j = 0; j < MAP_H; j++) {
            for (int i = 0; i < MAP_W; i++) {
                temp = (map[j][i] % 10);
                if (i != 0 && i != MAP_W - 1 && j != 0 && j != MAP_H - 1) {
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

    public static void setMap(int[][] mmap, int banme, boolean itsme) {

        for (int j = 0; j < MINIMAP_H * TILE_H; j++) {
            for (int i = 0; i < MINIMAP_W * TILE_W; i++) {
                map[j + OFFSET_Y][i + OFFSET_X + MINIMAP_W * (banme - 1)] = mmap[j / TILE_H][i / TILE_W];
            }
        }

        if (itsme) {
            for (int j = 0; j < MINO_H; j++) {
                for (int i = 0; i < MINO_W; i++) {
                    if (mino.arr[mino.mode][j][i] == 1 && mmap[mino.pos[1] + j][mino.pos[0] + i] == 0)
                        setTile(mino.pos[0] + i, mino.pos[1] + j, mino.label);
                }
            }
        }
    }

    public static void setFense(int banme, boolean itsme) {

        for (int j = 0; j < MINIMAP_H; j++) {
            for (int i = 0; i < MINIMAP_W; i++) {
                if (itsme) minimap[j][i] = 0;
                setTile(i, j, 0);
            }
        }
        for (int j = 0; j < MINIMAP_H; j++) {
            if (itsme)  {
                minimap[j][0] = 1;
              minimap[j][MINIMAP_W - 1] = 1;
            }
            setTile(0, j, 1);
            setTile(MINIMAP_W - 1, j, 1);
        }
        for (int i = 0; i < MINIMAP_W; i++) {
             if (itsme) minimap[MINIMAP_H - 1][i] = 1;
            setTile(i, MINIMAP_H - 1, 1);
        }
        if (itsme) {
            for (int s = m_arr.size() - 1; s >= 0; s--) {
                m_arr.remove(s);
            }
        }
    }
    
    
    private void viewNextAndKeep() {
        int label = 0;
        for (int c = 0; c < next.length; c++) {
            for (int j = 0; j < MINO_H; j++) {
                for (int i = 0; i < MINO_W; i++) {
                    label = (mino_array[next[c] - 2][0][j][i] == 1) ? next[c] : 0;
                    setTile(NEXT_NW_X + 1 + i, NEXT_NW_Y + 1 + j + c * (MINO_H + 1), label);
                }
            }
        }
        if (reserveMino != 0) {
            for (int j = 0; j < MINO_H; j++) {
                for (int i = 0; i < MINO_W; i++) {
                    label = (mino_array[reserveMino - 2][0][j][i] == 1) ? (reserveMino % 10) : 0;
                    setTile(RESERVE_NW_X + 1 + i, RESERVE_NW_Y + 1 + j, label);
                }
            }
        } else {
            for (int j = 0; j < MINO_H; j++) {
                for (int i = 0; i < MINO_W; i++) setTile(RESERVE_NW_X + 1 + i, RESERVE_NW_Y + 1 + j, 0);
            }
        }
    }

    private void viewFence() {
        for (int j = 0; j < NEXT_H; j++) {
            for (int i = 0; i < NEXT_W; i++) {
                if (i == 0 || i == (NEXT_W - 1) || (j % (NEXT_W - 1)) == 0)
                    setTile(NEXT_NW_X + i, NEXT_NW_Y + j, 1);
            }
        }
        for (int j = 0; j < RESERVE_H; j++) {
            for (int i = 0; i < RESERVE_W; i++) {
                if (i == 0 || i == (RESERVE_W - 1) || j == 0 || j == (RESERVE_H - 1))
                    setTile(RESERVE_NW_X + i, RESERVE_NW_Y + j, 1);
            }
        }
    }
    
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT && inGame)
            mino.collision = move(input[3], true, 1);
        if (key == KeyEvent.VK_RIGHT && inGame)
            mino.collision = move(input[2], true, 1);
        if (key == KeyEvent.VK_UP && inGame) {
            input[4][0] = 0;
            input[4][1] = 0;
            for (int j = 0; j < MINIMAP_H - 1 - mino.pos[1]; j++) {
                input[4][1] = j;
                mino.collision = !move(input[4], false, 1);
                if (mino.collision) {
                    input[4][1] = j - 1;
                    mino.onFloor = 10;
                    mino.collision = move(input[4], true, 1);
                    mino.collision = move(input[1], true, 1);
                    break;
                }
            }
        }
        if (key == KeyEvent.VK_DOWN && inGame) {
            mino.collision = move(input[1], true, 3);
        }
        if (key == KeyEvent.VK_SPACE) {
            if (isOnline) client.quit();
            initMap();
            inGame = true;
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
                mino = new CurrentMino(mino_array[reserveMino - 2], 0, reserveMino + ten_digit, timer);
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

    public static void changeScreen(EnumScreenMode.ScreenMode mode) {
        screen = mode;
    }

    public static void setPlayers(int p) {
        players = p;
    }

    public static void setBanme(int b) {
        banme = b;
    }
}