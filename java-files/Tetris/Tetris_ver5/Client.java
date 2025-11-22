import java.net.*;
import java.net.Socket;
import java.util.*;
import java.util.Scanner;
import java.io.*;
import java.io.PrintWriter;

public class Client {
    private static int w = Map.MINIMAP_W + Map.MINIMAP_OFFSET_PLUS * 2;
    private static int h = Map.MINIMAP_H + Map.MINIMAP_OFFSET_PLUS * 2;

    private Socket socket;
    private PrintWriter output;
    private String serverIP = "000.0.0.0";
    private ClientInputThread cit;
    private CurrentMino mino;
    private int[] next;
    private Map map;

    public Client(Map map) {
        this.map = map;
    }

    public void join() {
        try {
            socket = new Socket(serverIP, 3838);
            cit = new ClientInputThread(socket, this, map);
            cit.start();
            output = new PrintWriter(socket.getOutputStream());
            map.isOnline = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // screen next reserve
    public synchronized void sendMessage(int[][] screen) {
        try {
            mino = map.mino;
            next = map.next;
            output.println("1" + (char) (map.myBanme + '0') + (char) (mino.pos.x + '0') + (char) (mino.pos.y + '0')
                    + (char) (mino.mode + '0') + (char) (mino.label + '0') + (char) (next[0] + '0')
                    + (char) (next[1] + '0') + (char) (next[2] + '0') + (char) (next[3] + '0')
                    + (char) (map.reserveMino + '0') + (char) (map.gotDamage + '0') + "#"
                    + from2dToString(screen));
            output.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // DAMAGE TURNWAITINGCHECK
    public synchronized void sendMessage(String mode, int num) {
        if ("DAMAGE".equals(mode)) {
            try {
                output.println("2" + (char) (map.myBanme + '0') + "#" + (char) (num + '0'));
                output.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if ("TURNWAITINGCHECK".equals(mode)) {
            try {
                output.println("4" + (char) (map.myBanme + '0') + "#" + (char) (num + '0'));
                output.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // LOSE
    public synchronized void sendMessage(String str) {
        try {
            output.println("3#" + str);
            output.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setIP(String ip) {
        serverIP = ip;
    }

    public void quit() {
        try {
            if (cit != null)
                cit.stopClientThread();
            if (output != null)
                output.close();
            if (socket != null)
                socket.close();
            map.isOnline = false;
            map.myBanme = 1;
            map.setPlayers(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String from2dToString(int[][] screen) {
        int w = screen[0].length;
        int size = screen[0].length * screen.length;
        StringBuilder sb = new StringBuilder(size);
        for (int i = 0; i < size; i++)
            sb.append((char) ('0' + screen[i / w][i % w]));
        return sb.toString();
    }

    static class ClientInputThread extends Thread {
        private Socket socket;
        private boolean isWorking = true;
        private Client client;
        private Map map;

        public ClientInputThread(Socket socket, Client client, Map map) {
            this.socket = socket;
            this.client = client;
            this.map = map;
        }

        @SuppressWarnings("unchecked")
        public void run() {
            Scanner input;
            String[] word = new String[] {};
            int players = 1; // players
            int banme, mposX, mposY, mmode, mlabel, reserve, gotdamage, sentDamage; // word[0] : 1 2 3 4 5 10 11,
                                                                                    // word[1] : 0
            int[] next; // word[0] : 6 7 8 9
            String message; // word[1]
            int wcStatusIndex;
            try {
                input = new Scanner(socket.getInputStream());
                while (isWorking) {
                    word = input.nextLine().split("#");
                    if (word.length == 2) {
                        if (word[0].charAt(0) == '1') {
                            // charAtはunicodeにするから - '0' 必須
                            banme = (word[0].charAt(1) - '0');
                            mposX = (word[0].charAt(2) - '0');
                            mposY = (word[0].charAt(3) - '0');
                            mmode = (word[0].charAt(4) - '0');
                            mlabel = (word[0].charAt(5) - '0');
                            reserve = (word[0].charAt(10) - '0');
                            gotdamage = (word[0].charAt(11) - '0');
                            next = new int[] { (word[0].charAt(6) - '0'), (word[0].charAt(7) - '0'),
                                    (word[0].charAt(8) - '0'), (word[0].charAt(9) - '0') };
                            map.gotDamageArray[banme - 1] = gotdamage;
                            map.setMap(fromStringTo2d(word[1]), banme, false, mposX, mposY, mmode, mlabel);
                            map.viewNextAndKeep(banme, false, next, reserve);
                            map.viewFence2(banme, false);
                        }
                        if (word[0].charAt(0) == '2') {
                            players = Integer.valueOf(word[1]);
                            System.out.println("[CLIENT] 現在 " + players + " 人");
                            map.setPlayers(players);
                            map.viewFence1(players, false);
                        }
                        if (word[0].charAt(0) == '3') {
                            message = word[1];
                            // System.out.println("[CLIENT] " + message);
                            if ("START".equals(message)) {
                                map.changeScreen(EnumScreenMode.ScreenMode.INGAME);
                                map.initMap();
                                System.out.println("[CLIENT] ゲーム開始");

                            }
                            if ("YOUWIN".equals(message)) {
                                map.inGame = false;
                                System.out.println("[CLIENT] あなたはゲームに勝ちました");

                            }
                            if ("GAMESET".equals(message)) {
                                map.inGame = false;
                                System.out.println("[CLIENT] ゲーム終了");
                            }
                            if ("BACKTO-C-ST".equals(message)) {
                                if (map.myBanme != 1) {
                                    map.changeScreen(EnumScreenMode.ScreenMode.CSERVER);
                                    try {
                                        Thread.sleep(500);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    map.IPvault(map.tf_inputIP, "VAULT2TEXTFIELD");
                                } else {
                                    map.changeScreen(EnumScreenMode.ScreenMode.PSERVER);
                                    try {
                                        Thread.sleep(500);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    map.IPvault(map.tf_showIP, "VAULT2TEXTFIELD");
                                }
                                System.out.println("[CLIENT] あるプレイヤーがゲームを強制終了しました");
                            }
                        }
                        if (word[0].charAt(0) == '4') {
                            banme = (word[1].charAt(0) - '0');
                            System.out.println("[CLIENT] " + "あなたは " + banme + " 番目です");
                            map.myBanme = banme;
                        }
                        if (word[0].charAt(0) == '5') {
                            banme = (word[0].charAt(1) - '0');
                            sentDamage = (word[1].charAt(0) - '0');
                            if (map.myBanme != banme) {
                                map.gotDamage += sentDamage;
                                map.damageFrag = true;
                                System.out.println("[CLIENT] あなたは " + (word[1].charAt(0) - '0') + " ダメージを受けた");
                            }
                        }
                        if (word[0].charAt(0) == '6') {
                            wcStatusIndex = (word[0].charAt(1) - '0') - 1;
                            synchronized (map.waitCheckStatusArray) {
                                if (wcStatusIndex >= 0 && wcStatusIndex < players)
                                    map.waitCheckStatusArray[wcStatusIndex] = (word[1].charAt(0) - '0');
                            }
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("[CLIENT] サーバーから切断されました.");
                e.printStackTrace();
                /*　ポイント
                System.out.print("[CLIENT] word[0] ");
                for (int j = 0; j < word[0].length(); j++)
                    System.out.print((word[0].charAt(j) - '0') + " ");
                System.out.print(" ,word[1] ");
                System.out.print(word[1] + '\n');
                */
                client.quit();
            }
        }

        private static int[][] fromStringTo2d(String str) {

            if (w * h != str.length())
                throw new IllegalArgumentException();
            int[][] screen = new int[h][w];
            for (int i = 0; i < str.length(); i++)
                screen[i / w][i % w] = str.charAt(i) - '0';
            return screen;
        }

        public void stopClientThread() {
            isWorking = false;
        }
    }
}