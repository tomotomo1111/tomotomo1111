import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Map extends JPanel implements KeyListener, ActionListener {

    public static final int MINIMAP_W = 12;
    public static final int MINIMAP_H = 21;
    public static final int MINIMAP_OFFSET_PLUS = 3;
    private static double SIZE_MULTIPILER_X = 1.0;
    private static double SIZE_MULTIPILER_Y = 1.0;

    private static final int MAP_W = (int) (MyFrame.SCREEN_W * SIZE_MULTIPILER_X);
    private static final int MAP_H = (int) (MyFrame.SCREEN_H * SIZE_MULTIPILER_Y);
    private static final int TILE_W = (int) (16 * SIZE_MULTIPILER_X);
    private static final int TILE_H = (int) (16 * SIZE_MULTIPILER_Y);
    private static final int OFFSET_X = (int) (8 * SIZE_MULTIPILER_X);
    private static final int OFFSET_Y = (int) (8 * SIZE_MULTIPILER_Y);
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

    private static final int REDLINE_BORDER_FROM_TOP = 2;

    int myBanme = 1;
    private static int tenDigit = 0;
    private static int gameTime = 0;
    private static int fellInterval = 60;
    private int players = 1;
    int gotDamage = 0;
    private static int[][] map = new int[MAP_H][MAP_W];
    private static int[][] minimap = new int[MINIMAP_H + MINIMAP_OFFSET_PLUS * 2][MINIMAP_W + MINIMAP_OFFSET_PLUS * 2];
    private static int[][][][] minoArray = { TETRIS_MINO.Z, TETRIS_MINO.J, TETRIS_MINO.S, TETRIS_MINO.O,
            TETRIS_MINO.T, TETRIS_MINO.I, TETRIS_MINO.L };
    CurrentMino mino;
    int reserveMino = 0;
    int[] next = new int[4];
    private static ArrayList<Integer> nextArray = new ArrayList<>();

    private boolean reserveFrag = true;
    private boolean sButtonFrag = true;
    private boolean cButtonFrag = true;
    private boolean pButtonFrag = true;
    private boolean iButtonFrag = true;
    boolean damageFrag = false;
    private boolean tSpinFrag = false;

    boolean inGame = true;
    boolean isOnline = false;

    private Random rand = new Random();
    private Client client = new Client(this);
    private Server server;
    private EnumScreenMode.ScreenMode screen = EnumScreenMode.ScreenMode.TITLE;

    private static int[][] input = { { 0, 0 }, { 0, 1 }, { 1, 0 }, { -1, 0 }, { 0, 0 } };

    int[] gotDamageArray = new int[players];
    int[] waitCheckStatusArray = new int[players];

    //JsonReader jsonReader = new JsonReader();

    Crypt crypt = new Crypt();

    public Map() {
    	
    	/*
         try {
        	 jsonReader.read("./arrangement.json");
         } catch (IOException e) {
             e.printStackTrace();
         }
         */

        initMap();
        setLayout(null); // 閉じてもコンポの位置ずれがなくなる
        setFocusable(true);
        addKeyListener(this);
    }

    public void initMap() {

        tenDigit = gameTime = reserveMino = gotDamage = 0;
        fellInterval = 60;
        reserveFrag = sButtonFrag = cButtonFrag = pButtonFrag = iButtonFrag = tSpinFrag = true;
        damageFrag = false;

        inGame = true;

        for (int s = nextArray.size() - 1; s >= 0; s--) {
            nextArray.remove(s);
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

        if (x_l < 0) x_l = 0;
        if (x_l + t_w > map[0].length) t_w = map[0].length - x_l;
        if (y_u < 0) y_u = 0;
        if (y_u + t_h > map.length) t_h = map.length - y_u;

        for (int j = y_u; j < y_u + t_h; j++) {
            for (int i = x_l; i < x_l + t_w; i++) {
                map[j + OFFSET_Y][i + OFFSET_X] = label;
            }
        }
    }

    public void mainLoop() {

        long[] time = new long[2];
        long erapsedTime = 0;
        final int frameRate = 60;
        int executeInterval = 1000 / frameRate;
        while (true) {

            time[0] = System.nanoTime();
            SIZE_MULTIPILER_X = MyFrame.getFrameWidthPro();
            SIZE_MULTIPILER_Y = MyFrame.getFrameHeightPro();
            fixMapArray();
            
            System.out.println(SIZE_MULTIPILER_X + " " + SIZE_MULTIPILER_Y);
            switch (screen) {
                case TITLE:

                    break;
                case CSERVER:
                    break;
                case PSERVER:
                    break;
                case INGAME:
                    solidificateMino();
                    if (checkOverRedLine() && inGame) beLose();
                    if ((gameTime - mino.geneTime) % (fellInterval) == 0 && inGame) move(input[1], true, 1);
                    if (isOnline) client.sendMessage(minimap);
                    gameTime++;
                    if (gameTime % (frameRate * 60) == 0 && fellInterval > 1) fellInterval -= (fellInterval == 10) ? 9 : 10;
                    break;
                case RESULT:
                    break;
                default:
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
                    if (mino.arr[mino.mode][j][i] == 1
                            && minimap[MINIMAP_OFFSET_PLUS + newPos[1] + j][MINIMAP_OFFSET_PLUS + newPos[0] + i] != 0)
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
                        beLose();
                }
            }
        } while (--count > 0);
        return canMove;
    }

    private void beLose() {
        inGame = false;
        if (isOnline)
            client.sendMessage("LOSE");
    }

    private void solidificateMino() {
        boolean touchGround = !move(input[1], false, 1);
        if (touchGround && (mino.groundContactAvoidCount >= COLCOUNT_MAX || mino.groundContactTime >= COLTIME_MAX)
                && inGame) {
            for (int j = 0; j < MINO_H; j++) {
                for (int i = 0; i < MINO_W; i++) {
                    if (mino.arr[mino.mode][j][i] == 1)
                        minimap[MINIMAP_OFFSET_PLUS + mino.pos.y + j][MINIMAP_OFFSET_PLUS + mino.pos.x
                                + i] = mino.label;
                }
            }
            checkAndRemoveLine();
            tenDigit = (tenDigit + 10) % 1000;
            if (damageFrag)
                gotDamage(gotDamage);
            generate();
            reserveFrag = true;
        }
        if (touchGround)
            mino.groundContactTime++;
    }

    public void generate() {
        if (next[0] == 0) {
            for (int i = 0; i < next.length; i++) {
                next[i] = nextArray.get(i);
                nextArray.remove(i);
            }
        }

        int mode = 0;
        mino = new CurrentMino(minoArray[next[0] - 2], mode, next[0] + tenDigit, gameTime, 5, REDLINE_BORDER_FROM_TOP - 3);
        for (int i = 0; i < next.length - 1; i++)
            next[i] = next[i + 1];
        if (nextArray.size() <= 0)
            lotteryMinoArray();
        next[next.length - 1] = nextArray.get(0);
        nextArray.remove(0);

        mino.collision = move(input[0], true, 1);
    }

    private void lotteryMinoArray() {
        if (nextArray.size() == 0) {
            for (int i = 0; i < minoArray.length; i++) {
                nextArray.add(i, i + 2);
            }

            int tenDigit = 20;
            int temp;
            int i_1, i_2;
            do {
                i_1 = rand.nextInt(minoArray.length);
                i_2 = rand.nextInt(minoArray.length);
                temp = nextArray.get(i_1);
                nextArray.set(i_1, nextArray.get(i_2));
                nextArray.set(i_2, temp);
            } while (tenDigit-- > 0);
        }
    }

    private boolean checkOverRedLine() {
        boolean isOver = false;
        for (int j = 4; j <= 13; j++) {
            if (minimap[REDLINE_BORDER_FROM_TOP + 2][j] != 0) isOver = true;
        }
        return isOver;
    }

    int chain = 0;
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
                        minimap[MINIMAP_OFFSET_PLUS + j2][MINIMAP_OFFSET_PLUS
                                + i2] = minimap[MINIMAP_OFFSET_PLUS + j2 - 1][MINIMAP_OFFSET_PLUS + i2];
                    }
                }
                total_remove_row++;
            }
            removeLine = true;
        }
        if (isOnline && total_remove_row > 0) {
            if (gotDamage > 0) {
                if (gotDamage >= total_remove_row) {
                    gotDamage -= total_remove_row;
                    total_remove_row = 0;
                } else {
                    total_remove_row -= gotDamage;
                    gotDamage = 0;
                }
            }
            if (gotDamage == 0 && total_remove_row > 0)
                client.sendMessage("DAMAGE", total_remove_row);
        }
    }

    public void gotDamage(int gotDamage) {
        for (int j = 0; j < gotDamage; j++) {
            for (int j2 = 0; j2 < MINIMAP_H - 1; j2++) {
                for (int i2 = 0; i2 < MINIMAP_W - 1; i2++) {
                    minimap[MINIMAP_OFFSET_PLUS + j2][MINIMAP_OFFSET_PLUS
                            + i2] = minimap[MINIMAP_OFFSET_PLUS + j2 + 1][MINIMAP_OFFSET_PLUS + i2];
                }
            }
            int hole_i = rand.nextInt(10) + 1;
            for (int i = 1; i < MINIMAP_W - 1; i++) {
                minimap[MINIMAP_OFFSET_PLUS + MINIMAP_H - 2][MINIMAP_OFFSET_PLUS + i] = (i == hole_i) ? 0 : 9;
            }
        }
        this.gotDamage = 0;
        damageFrag = false;
    }

    private void rotate(int mode) {
        boolean col = false;
        int newMode = (mino.mode + mode + minoArray[0].length) % minoArray[0].length;
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
                    if (mino.arr[newMode][j][i] == 1
                            && minimap[MINIMAP_OFFSET_PLUS + newPos[1] + j][MINIMAP_OFFSET_PLUS + newPos[0] + i] != 0) {
                        if (mino.arr[newMode][j][i] == 1 && (minimap[MINIMAP_OFFSET_PLUS + newPos[1]
                                + j][MINIMAP_OFFSET_PLUS + newPos[0] + i] == 1
                                || (minimap[MINIMAP_OFFSET_PLUS + newPos[1] + j][MINIMAP_OFFSET_PLUS + newPos[0] + i]
                                        % 10) > 1))
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
                viewDamage(g);
                viewFence2(myBanme, true);
                break;
            case RESULT:
                break;
            default:
        }
        repaint();
    }

    private static final double BUTTON_POSITION_RELATIVE_X_PRO_FOR_FRAME = 0.5;
    private static final double BUTTON_SIZE_X_PRO_FOR_FRAME = 0.75;
    private static final double BUTTON_POSITION_RELATIVE_Y_PRO_FOR_FRAME = 0.66;
    private static final double BUTTON_SIZE_Y_PRO_FOR_FRAME = 0.0416;
    private static final double BUTTON_MARGIN_Y_PRO_FOR_FRAME = 0.0625;
    
    final int Y = pro2PosUpY(BUTTON_POSITION_RELATIVE_Y_PRO_FOR_FRAME, BUTTON_SIZE_Y_PRO_FOR_FRAME, "DEFAULT");
    final int Y_M = pro2PosY(BUTTON_MARGIN_Y_PRO_FOR_FRAME, "DEFAULT");
    final int WIDTH = pro2PosX(BUTTON_SIZE_X_PRO_FOR_FRAME, "DEFAULT");
    final int HEIGHT = pro2PosY(BUTTON_SIZE_Y_PRO_FOR_FRAME, "DEFAULT");
    int X = pro2PosLeftX(BUTTON_POSITION_RELATIVE_X_PRO_FOR_FRAME, BUTTON_SIZE_X_PRO_FOR_FRAME, "DEFAULT");;
    JButton sButton_START;
    JButton sButton_CS;
    JButton sButton_SS;

    private void title(Graphics g) {

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, MyFrame.sc_wn, MyFrame.sc_hn);
        viewTETLIS(g);
        if (sButtonFrag) {
            sButtonFrag = false;
            rmrp(g);
            sButton_START = new JButton("START");
            sButton_CS = new JButton("CONNECT SERVER");
            sButton_SS = new JButton("SET UP SERVER");

            this.add(sButton_START);
            sButton_START.setForeground(Color.WHITE);
            sButton_START.setBackground(Color.BLACK);
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
            sButton_CS.setForeground(Color.WHITE);
            sButton_CS.setBackground(Color.BLACK);
            sButton_CS.addActionListener(new ActionListener() {
                // @Override
                public void actionPerformed(ActionEvent e) {
                    changeScreen(EnumScreenMode.ScreenMode.CSERVER);
                    sButtonFrag = true;
                    rmrp(g);
                }
            });

            this.add(sButton_SS);
            sButton_SS.setForeground(Color.WHITE);
            sButton_SS.setBackground(Color.BLACK);
            sButton_SS.addActionListener(new ActionListener() {
                // @Override
                public void actionPerformed(ActionEvent e) {
                    changeScreen(EnumScreenMode.ScreenMode.PSERVER);
                    sButtonFrag = true;
                    rmrp(g);
                }
            });
        }
        sButton_START.setBounds(X, Y + 0 * Y_M, WIDTH, HEIGHT);
        sButton_CS.setBounds(X, Y + 1 * Y_M, WIDTH, HEIGHT);
        sButton_SS.setBounds(X, Y + 2 * Y_M, WIDTH, HEIGHT);
    }

    JTextField tf_inputIP;
    JButton cButton_CONNECT;
    JButton cbutton_DISCONNECT;
    JButton cButton_BACKTITLE;
    JButton cButton_MYWAITCHECK;

    private void cserver(Graphics g) {

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, MyFrame.sc_wn, MyFrame.sc_hn);

        if (cButtonFrag) {
            rmrp(g);
            cButtonFrag = false;
            cButton_CONNECT = new JButton("CONNECT");
            cbutton_DISCONNECT = new JButton("DISCONNECT");
            cButton_BACKTITLE = new JButton("BACK TO TITLE");
            cButton_MYWAITCHECK = new JButton("");

            tf_inputIP = new JTextField();
            this.add(tf_inputIP);
            tf_inputIP.setBounds(X, Y - 1 * Y_M, WIDTH, HEIGHT);
            tf_inputIP.setForeground(Color.WHITE);
            tf_inputIP.setBackground(Color.BLACK);
            tf_inputIP.setEditable(true);

            this.add(cButton_CONNECT);
            cButton_CONNECT.setBounds(X, Y + 0 * Y_M, WIDTH, HEIGHT);
            cButton_CONNECT.setForeground(Color.WHITE);
            cButton_CONNECT.setBackground(Color.BLACK);

            cButton_CONNECT.addActionListener(new ActionListener() {
                // @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        client.setIP(crypt.decryptIP(tf_inputIP.getText()));
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    client.join();
                    IPvault(tf_inputIP, "TEXTFIELD2VAULT");
                }
            });

            this.add(cbutton_DISCONNECT);
            cbutton_DISCONNECT.setBounds(X, Y + 1 * Y_M, WIDTH, HEIGHT);
            cbutton_DISCONNECT.setForeground(Color.WHITE);
            cbutton_DISCONNECT.setBackground(Color.BLACK);
            cbutton_DISCONNECT.addActionListener(new ActionListener() {
                // @Override
                public void actionPerformed(ActionEvent e) {
                    client.quit();
                }
            });

            this.add(cButton_BACKTITLE);
            cButton_BACKTITLE.setBounds(X, Y + 2 * Y_M, WIDTH, HEIGHT);
            cButton_BACKTITLE.setForeground(Color.WHITE);
            cButton_BACKTITLE.setBackground(Color.BLACK);
            cButton_BACKTITLE.addActionListener(new ActionListener() {
                // @Override
                public void actionPerformed(ActionEvent e) {
                    client.quit();
                    changeScreen(EnumScreenMode.ScreenMode.TITLE);
                    cButtonFrag = true;
                    rmrp(g);
                }
            });

            this.add(cButton_MYWAITCHECK);
            cButton_MYWAITCHECK.setBorderPainted(false);
            cButton_MYWAITCHECK.setContentAreaFilled(false);
            cButton_MYWAITCHECK.setOpaque(false);
            cButton_MYWAITCHECK.addMouseListener(new MouseAdapter() {
                // @Override
                public void mouseClicked(MouseEvent e) {
                    if (isOnline) {
                        if (waitCheckStatusArray[myBanme - 1] > 0) {
                            waitCheckStatusArray[myBanme - 1] = -1;
                            client.sendMessage("TURNWAITINGCHECK", -1);
                        } else {
                            waitCheckStatusArray[myBanme - 1] = 1;
                            client.sendMessage("TURNWAITINGCHECK", 1);
                        }
                    }
                }
            });
        }

        int adjustLeftMargin = (myBanme - 1) * MAP_W;
        tf_inputIP.setBounds(adjustLeftMargin + X, Y - 1 * Y_M, WIDTH, HEIGHT);
        cButton_CONNECT.setBounds(adjustLeftMargin + X, Y + 0 * Y_M, WIDTH, HEIGHT);
        cbutton_DISCONNECT.setBounds(adjustLeftMargin + X, Y + 1 * Y_M, WIDTH, HEIGHT);
        cButton_BACKTITLE.setBounds(adjustLeftMargin + X, Y + 2 * Y_M, WIDTH, HEIGHT);

        cButton_CONNECT.setEnabled(!isOnline && !"".equals(tf_inputIP.getText()));
        cbutton_DISCONNECT.setEnabled(isOnline);

        viewTETLIS(g);
        viewWaitOrCheck(g, cButton_MYWAITCHECK);
    }

    JTextField tf_showIP;
    JButton pButton_START;
    JButton pButton_SETUP;
    JButton pButton_DISCONNECT;
    JButton pButton_BACKTITLE;
    JButton pButton_MYWAITCHECK;

    private void pserver(Graphics g) {

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, MyFrame.sc_wn, MyFrame.sc_hn);

        if (pButtonFrag) {
            rmrp(g);
            pButtonFrag = false;
            pButton_START = new JButton("START");
            pButton_SETUP = new JButton("SET UP SERVER");
            pButton_DISCONNECT = new JButton("DISCONNECT");
            pButton_BACKTITLE = new JButton("BACK TO TITLE");
            pButton_MYWAITCHECK = new JButton("");

            tf_showIP = new JTextField();
            this.add(tf_showIP);
            tf_showIP.setForeground(Color.WHITE);
            tf_showIP.setBackground(Color.BLACK);
            tf_showIP.setEditable(false);

            this.add(pButton_START);
            pButton_START.setForeground(Color.WHITE);
            pButton_START.setBackground(Color.BLACK);
            pButton_START.addActionListener(new ActionListener() {
                // @Override
                public void actionPerformed(ActionEvent e) {
                    server.sendAll("START");
                    pButtonFrag = true;
                    rmrp(g);
                }
            });

            this.add(pButton_SETUP);
            pButton_SETUP.setForeground(Color.WHITE);
            pButton_SETUP.setBackground(Color.BLACK);
            pButton_SETUP.addActionListener(new ActionListener() {
                // @Override
                public void actionPerformed(ActionEvent e) {
                    server = new Server();
                    server.start();
                    try {
                        Thread.sleep(300);
                    } catch (Exception es) {
                        es.printStackTrace();
                    }
                    ;
                    client.join();
                    try {
                        tf_showIP.setText(crypt.encryptIP(server.getIP()));
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    IPvault(tf_showIP, "TEXTFIELD2VAULT");
                }
            });

            this.add(pButton_DISCONNECT);
            pButton_DISCONNECT.setForeground(Color.WHITE);
            pButton_DISCONNECT.setBackground(Color.BLACK);
            pButton_DISCONNECT.addActionListener(new ActionListener() {
                // @Override
                public void actionPerformed(ActionEvent e) {
                    client.quit();
                    if (server != null) {
                        server.stopServer();
                        server = null;
                    }
                    System.gc();
                }
            });

            this.add(pButton_BACKTITLE);
            pButton_BACKTITLE.setForeground(Color.WHITE);
            pButton_BACKTITLE.setBackground(Color.BLACK);
            pButton_BACKTITLE.addActionListener(new ActionListener() {
                // @Override
                public void actionPerformed(ActionEvent e) {
                    client.quit();
                    if (server != null) {
                        server.stopServer();
                        server = null;
                    }
                    changeScreen(EnumScreenMode.ScreenMode.TITLE);
                    pButtonFrag = true;
                    rmrp(g);
                }
            });

            this.add(pButton_MYWAITCHECK);
            pButton_MYWAITCHECK.setBorderPainted(false);
            pButton_MYWAITCHECK.setContentAreaFilled(false);
            pButton_MYWAITCHECK.setOpaque(false);
            pButton_MYWAITCHECK.addMouseListener(new MouseAdapter() {
                // @Override
                public void mouseClicked(MouseEvent e) {
                    if (isOnline) {
                        if (waitCheckStatusArray[myBanme - 1] > 0) {
                            waitCheckStatusArray[myBanme - 1] = -1;
                            client.sendMessage("TURNWAITINGCHECK", -1);
                        } else {
                            waitCheckStatusArray[myBanme - 1] = 1;
                            client.sendMessage("TURNWAITINGCHECK", 1);
                        }
                    }
                }
            });
        }
        tf_showIP.setBounds(X, Y - 2 * Y_M, WIDTH, HEIGHT);
        pButton_START.setBounds(X, Y - 1 * Y_M, WIDTH, HEIGHT);
        pButton_SETUP.setBounds(X, Y + 0 * Y_M, WIDTH, HEIGHT);
        pButton_DISCONNECT.setBounds(X, Y + 1 * Y_M, WIDTH, HEIGHT);
        pButton_BACKTITLE.setBounds(X, Y + 2 * Y_M, WIDTH, HEIGHT);

        pButton_START.setEnabled(players > 1 && checkIsAllChecked());
        pButton_SETUP.setEnabled(!isOnline);
        pButton_DISCONNECT.setEnabled(isOnline);

        viewTETLIS(g);
        viewWaitOrCheck(g, pButton_MYWAITCHECK);

    }

    private boolean checkIsAllChecked() {
        boolean isAllChecked = true;
        for (int j = 0; j < waitCheckStatusArray.length; j++) {
            if (waitCheckStatusArray[j] < 0)
                isAllChecked = false;
        }
        return isAllChecked;
    }

    private void viewTETLIS(Graphics g) {
        int adjustLeftMargin = (myBanme - 1) * MAP_W;
        ImageIcon imageicon;
        int[] size;

        imageicon = new ImageIcon("./images/t1.png");
        size = getImageiconSizeFromPro(imageicon, SIZE_MULTIPILER_X, SIZE_MULTIPILER_Y);
        g.drawImage(imageicon.getImage(), adjustLeftMargin + pro2PosXImage(0.25, "DEFAULT", imageicon),
                pro2PosYImage(0.2, "DEFAULT", imageicon), size[0], size[1], null);
        imageicon = new ImageIcon("./images/e.png");
        size = getImageiconSizeFromPro(imageicon, SIZE_MULTIPILER_X, SIZE_MULTIPILER_Y);
        g.drawImage(imageicon.getImage(), adjustLeftMargin + pro2PosXImage(0.35, "DEFAULT", imageicon),
                pro2PosYImage(0.2, "DEFAULT", imageicon), size[0], size[1], null);
        imageicon = new ImageIcon("./images/t2.png");
        size = getImageiconSizeFromPro(imageicon, SIZE_MULTIPILER_X, SIZE_MULTIPILER_Y);
        g.drawImage(imageicon.getImage(), adjustLeftMargin + pro2PosXImage(0.45, "DEFAULT", imageicon),
                pro2PosYImage(0.2, "DEFAULT", imageicon), size[0], size[1], null);
        imageicon = new ImageIcon("./images/l.png");
        size = getImageiconSizeFromPro(imageicon, SIZE_MULTIPILER_X, SIZE_MULTIPILER_Y);
        g.drawImage(imageicon.getImage(), adjustLeftMargin + pro2PosXImage(0.55, "DEFAULT", imageicon),
                pro2PosYImage(0.2, "DEFAULT", imageicon), size[0], size[1], null);
        imageicon = new ImageIcon("./images/i.png");
        size = getImageiconSizeFromPro(imageicon, SIZE_MULTIPILER_X, SIZE_MULTIPILER_Y);
        g.drawImage(imageicon.getImage(), adjustLeftMargin + pro2PosXImage(0.65, "DEFAULT", imageicon),
                pro2PosYImage(0.2, "DEFAULT", imageicon), size[0], size[1], null);
        imageicon = new ImageIcon("./images/s.png");
        size = getImageiconSizeFromPro(imageicon, SIZE_MULTIPILER_X, SIZE_MULTIPILER_Y);
        g.drawImage(imageicon.getImage(), adjustLeftMargin + pro2PosXImage(0.75, "DEFAULT", imageicon),
                pro2PosYImage(0.2, "DEFAULT", imageicon), size[0], size[1], null);
    }

    private synchronized void viewWaitOrCheck(Graphics g, JButton jButton_MYWAITCHECK) {
        int adjustLeftMargin;
        ImageIcon imageicon;
        int[] imageiconSize;
        synchronized(waitCheckStatusArray) {
            if (players == gotDamageArray.length && players == waitCheckStatusArray.length) {
                for (int j = 1; j <= players; j++) {
                    imageicon = (waitCheckStatusArray[j - 1] > 0) ? (new ImageIcon("./images/check.gif"))
                        : (new ImageIcon("./images/waiting.gif"));
                    imageiconSize = getImageiconSizeFromPro(imageicon, SIZE_MULTIPILER_X, SIZE_MULTIPILER_Y);
                    // imageicon = new ImageIcon(imageicon.getImage().getScaledInstance(imageiconSize[0], imageiconSize[1], java.awt.Image.SCALE_SMOOTH));
                    
                    adjustLeftMargin = (j - 1) * MAP_W;
                    if (j == myBanme) {
                        jButton_MYWAITCHECK.setIcon(imageicon);
                        jButton_MYWAITCHECK.setBounds(adjustLeftMargin + pro2PosXImage(0.5, "DEFAULT", imageicon),
                                pro2PosYImage(0.45, "DEFAULT", imageicon), imageiconSize[0],
                                imageiconSize[1]);
                    } else {
                        g.drawImage(imageicon.getImage(), adjustLeftMargin + pro2PosXImage(0.5, "DEFAULT", imageicon),
                                pro2PosYImage(0.45, "DEFAULT", imageicon), imageiconSize[0], imageiconSize[1], null);
                        g.setColor(Color.RED);
                        g.drawRoundRect(adjustLeftMargin + pro2PosX(0.1, "DEFAULT"), pro2PosY(0.1, "DEFAULT"),
                                pro2PosX(0.8, "DEFAULT"), pro2PosY(0.8, "DEFAULT"), pro2PosX(0.1, "DEFAULT"),
                                pro2PosY(0.1, "DEFAULT"));
                    }
                }
            }
        }
    }

    private int[] getImageiconSizeFromPro(ImageIcon imageicon, double pro_x, double pro_y) {
        return new int[] {(int) (imageicon.getIconWidth() * pro_x), (int) (imageicon.getIconHeight() * pro_y)};
    }

    private int pro2PosXImage(double proX, String mode, ImageIcon imageicon) {
        return pro2PosX(proX, mode) - (int) (imageicon.getIconWidth() * SIZE_MULTIPILER_X / 2);
    }

    private int pro2PosYImage(double proY, String mode, ImageIcon imageicon) {
        return pro2PosY(proY, mode) - (int) (imageicon.getIconHeight() * SIZE_MULTIPILER_Y / 2);
    }

    private int pro2PosX(double proX, String mode) {
        boolean isDefault = "DEFAULT".equals(mode);
        return (int) (((isDefault) ? MyFrame.SC_WN_DEFAULT - MyFrame.FRAME_OFFSET_X : MAP_W - MyFrame.FRAME_OFFSET_X) * proX);
    }

    private int pro2PosY(double proY, String mode) {
        boolean isDefault = "DEFAULT".equals(mode);
        return (int) (((isDefault) ? MyFrame.SC_HN_DEFAULT - MyFrame.FRAME_OFFSET_Y : MAP_H - MyFrame.FRAME_OFFSET_Y) * proY);
    }

    private int pro2PosLeftX(double c_proX, double w_proX, String mode) {
        boolean isDefault = "DEFAULT".equals(mode);
        return pro2PosX(c_proX, mode) - pro2PosX(w_proX, mode) / 2;
    }

    private int pro2PosUpY(double c_proY, double h_proY, String mode) {
        boolean isDefault = "DEFAULT".equals(mode);
        return pro2PosY(c_proY, mode) - pro2PosY(h_proY, mode) / 2;
    }


    private String ip;

    synchronized void IPvault(JTextField tf, String mode) {
        if ("TEXTFIELD2VAULT".equals(mode))
            this.ip = tf.getText();
        if ("VAULT2TEXTFIELD".equals(mode))
            tf.setText(this.ip);
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
        // 9 block&ghost white

        if (iButtonFrag)
            rmrp(g);
        Color PURPLE = new Color(162, 96, 191);
        Color ORANGE = new Color(245, 130, 32);
        Color[] color = { Color.BLACK, Color.GRAY, Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW, PURPLE, Color.CYAN,
                ORANGE, Color.WHITE };
        int temp = 0;
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, MyFrame.sc_wn, MyFrame.sc_hn);

        for (int j = 0; j < map.length; j++) {
            for (int i = 0; i < map[0].length; i++) {
                temp = (map[j][i] % 10);
                if (i != 0 && i != map[0].length - 1 && j != 0 && j != map.length - 1) {
                    if (map[j][i] != map[j][i - 1] || map[j][i] != map[j][i + 1] || map[j][i] != map[j - 1][i]
                            || map[j][i] != map[j + 1][i] || map[j][i] == 9) {
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

        g.setColor(Color.RED);
        int x_1 = TILE_W * 2, y_1 = TILE_H * (REDLINE_BORDER_FROM_TOP + 1);
        for (int j = 1; j <= players; j++) {
            g.drawLine(MAP_W * (j - 1) + x_1, y_1, MAP_W * (j - 1) + x_1 + TILE_W * 10, y_1);
        }   
    }

    public synchronized void setMap(int[][] mmap, int banme, boolean itsme, int mposX, int mposY, int mmode,
            int mlabel) {
        int[] adjustLeftMargin = { MAP_W * (banme - 1), 20 * (banme - 1) };
        // System.out.println("banme : " +banme+", mposX : "+mposX+", mposY : "+mposY+",
        // mmode : "+mmode+", mlabel : "+mlabel);
        int minonum = (mlabel % 10) - 2;

        if (itsme || banme != myBanme) {
            // board
            for (int j = 0; j < MINIMAP_H * TILE_H; j++) {
                for (int i = 0; i < MINIMAP_W * TILE_W; i++) {
                    map[j + OFFSET_Y][i + OFFSET_X
                            + adjustLeftMargin[0]] = mmap[MINIMAP_OFFSET_PLUS + j / TILE_H][MINIMAP_OFFSET_PLUS
                                    + i / TILE_W];
                }
            }
            // maneuvering mino
            for (int j = 0; j < MINO_H; j++) {
                for (int i = 0; i < MINO_W; i++) {
                    if (minoArray[minonum][mmode][j][i] == 1
                            && mmap[MINIMAP_OFFSET_PLUS + mposY + j][MINIMAP_OFFSET_PLUS + mposX + i] == 0)
                        setTile(mposX + i + adjustLeftMargin[1], mposY + j, mlabel);
                }
            }
            // ghost
            if (itsme) {
                int ghost_height = 0;
                for (int j = 1; j < MINIMAP_H - mposY; j++) {
                    input[4][0] = 0;
                    input[4][1] = j;
                    if (move(input[4], false, 1)) {
                        ghost_height++;
                    } else {
                        break;
                    }
                }
                if (ghost_height != 0) {
                    for (int j = 0; j < MINO_H; j++) {
                        for (int i = 0; i < MINO_W; i++) {
                            if (minoArray[minonum][mmode][j][i] == 1
                                    && mmap[MINIMAP_OFFSET_PLUS + mposY + ghost_height + j][MINIMAP_OFFSET_PLUS + mposX
                                            + i] == 0
                                    && ghost_height > 3)
                                setTile(mposX + i + adjustLeftMargin[1], mposY + ghost_height + j, 19);
                        }
                    }
                }
            }
        }
    }

    public synchronized void viewFence1(int banme, boolean itsme) {
        int[] adjustLeftMargin = { MAP_W * (banme - 1), 20 * (banme - 1) };
        // board reset
        for (int j = 0; j < MINIMAP_H; j++) {
            for (int i = 0; i < MINIMAP_W; i++) {
                if (itsme)
                    minimap[MINIMAP_OFFSET_PLUS + j][MINIMAP_OFFSET_PLUS + i] = 0;
                setTile(adjustLeftMargin[1] + i, j, 0);
            }
        }
        // vertical fence
        for (int j = 0; j < MINIMAP_H + MINIMAP_OFFSET_PLUS; j++) {
            if (itsme)
                minimap[j][MINIMAP_OFFSET_PLUS + 0] = 1;
            if (itsme)
                minimap[j][MINIMAP_OFFSET_PLUS + MINIMAP_W - 1] = 1;
            setTile(adjustLeftMargin[1] + 0, j - MINIMAP_OFFSET_PLUS, 1);
            setTile(adjustLeftMargin[1] + MINIMAP_W - 1, j - MINIMAP_OFFSET_PLUS, 1);
        }
        // horizontal fence
        for (int i = 0; i < MINIMAP_W; i++) {
            if (itsme)
                minimap[MINIMAP_OFFSET_PLUS + MINIMAP_H - 1][MINIMAP_OFFSET_PLUS + i] = 1;
            setTile(adjustLeftMargin[1] + i, MINIMAP_H - 1, 1);
        }
    }

    public synchronized void viewNextAndKeep(int banme, boolean itsme, int[] next, int reserveMino) {
        int[] adjustLeftMargin = { MAP_W * (banme - 1), 20 * (banme - 1) };
        int label = 0;
        if (itsme || banme != myBanme) {
            for (int c = 0; c < next.length; c++) {
                for (int j = 0; j < MINO_H; j++) {
                    for (int i = 0; i < MINO_W; i++) {
                        label = (minoArray[next[c] - 2][0][j][i] == 1) ? next[c] : 0;
                        setTile(adjustLeftMargin[1] + NEXT_NW_X + 1 + i, NEXT_NW_Y + 1 + j + c * (MINO_H + 1), label);
                    }
                }
            }
            if (reserveMino != 0) {
                for (int j = 0; j < MINO_H; j++) {
                    for (int i = 0; i < MINO_W; i++) {
                        label = (minoArray[reserveMino - 2][0][j][i] == 1) ? (reserveMino % 10) : 0;
                        setTile(adjustLeftMargin[1] + RESERVE_NW_X + 1 + i, RESERVE_NW_Y + 1 + j, label);
                    }
                }
            } else {
                for (int j = 0; j < MINO_H; j++) {
                    for (int i = 0; i < MINO_W; i++)
                        setTile(adjustLeftMargin[1] + RESERVE_NW_X + 1 + i, RESERVE_NW_Y + 1 + j, 0);
                }
            }
        }
    }

    public void viewDamage(Graphics g) {
        int[] adjustLeftMargin;
        int gotDamage;
        Font damageFont = new Font("Arial", Font.BOLD, 
                (int) (64 * ((SIZE_MULTIPILER_X < SIZE_MULTIPILER_Y) ? SIZE_MULTIPILER_X : SIZE_MULTIPILER_Y)));
        for (int j = 1; j <= players; j++) {
            adjustLeftMargin = new int[] {MAP_W * (j - 1), 20 * (j - 1)};
            gotDamage = gotDamageArray[j - 1];
            if (gotDamage == 0) g.setColor(Color.BLUE);
            if (gotDamage > 0 && gotDamage < 5) g.setColor(Color.YELLOW);
            if (gotDamage >= 5) g.setColor(Color.RED);
            g.setFont(damageFont);
            g.drawString(Integer.toString(gotDamage), adjustLeftMargin[0] + TILE_W * 8, TILE_H * 26);
        }
    }

    public synchronized void viewFence2(int banme, boolean itsme) {
        int[] adjustLeftMargin = { MAP_W * (banme - 1), 20 * (banme - 1) };
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
                mino = new CurrentMino(minoArray[reserveMino - 2], 0, reserveMino + tenDigit, gameTime, 5, 0);
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

    public synchronized void changeScreen(EnumScreenMode.ScreenMode mode) {
        screen = mode;
    }

    public synchronized void setPlayers(int newPlayers) {
        int i, j;
        MyFrame.setFrameSize(newPlayers, 1, SIZE_MULTIPILER_X, SIZE_MULTIPILER_Y);

        try {
            int temp = newPlayers;

            int[][] new_map_array = new int[MyFrame.sc_hn - MyFrame.FRAME_OFFSET_Y][MyFrame.sc_wn - MyFrame.FRAME_OFFSET_X];
            int[] new_got_damage_array = new int[temp];
            int[] new_view_waitcheck_status_array = new int[temp];

            for (j = 0; j < temp; j++) {
                new_got_damage_array[j] = 0;
                new_view_waitcheck_status_array[j] = -1;
            }
            synchronized(waitCheckStatusArray) {
                if (players < newPlayers) temp = players;
                players = newPlayers;

                for (j = 0; j < MAP_H; j++) {
                    for (i = 0; i < MAP_W * temp; i++) {
                        new_map_array[j][i] = map[j][i];
                    }
                }

                map = new_map_array;
                gotDamageArray = new_got_damage_array;
                waitCheckStatusArray = new_view_waitcheck_status_array;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public synchronized void fixMapArray() {
        int sc_wn_pre = MyFrame.sc_wn;
        int sc_hn_pre = MyFrame.sc_hn;
        MyFrame.fixFrameSizeField();
        if (sc_wn_pre != MyFrame.sc_wn || sc_hn_pre != MyFrame.sc_hn) {
            MyFrame.fixFrameSizeField();
            int[][] new_map_array = new int[MyFrame.sc_hn - MyFrame.FRAME_OFFSET_Y][MyFrame.sc_wn - MyFrame.FRAME_OFFSET_X];
            boolean isNewMapBiggerW = (sc_wn_pre < MyFrame.sc_wn);
            boolean isNewMapBiggerH = (sc_hn_pre < MyFrame.sc_hn);
            for (int i = 0; i < ((isNewMapBiggerH) ? map.length : new_map_array.length); i++) {
                for (int j = 0; j < ((isNewMapBiggerW) ? map[0].length : new_map_array[0].length); j++) {
                    new_map_array[i][j] = 0;
                }
            }
            map = new_map_array;
        }
    }
}