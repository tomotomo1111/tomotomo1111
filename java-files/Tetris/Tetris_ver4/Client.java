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
    public void sendMessage(int[][] screen) {
        try {
            mino = map.mino;
            next = map.next;
            output.println("1" + (char) (map.myBanme + '0') + (char) (mino.pos.x + '0') + (char) (mino.pos.y + '0')
                    + (char) (mino.mode + '0') + (char) (mino.label + '0') + (char) (next[0] + '0')
                    + (char) (next[1] + '0') + (char) (next[2] + '0') + (char) (next[3] + '0')
                    + (char) (map.reserveMino + '0') + "#"
                    + from2dToString(screen));
            output.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // damage
    public void sendMessage(int num) {
        try {
            output.println("2" + (char) (map.myBanme + '0') +"#" + (char) (num + '0'));
            output.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // Lose
    public void sendMessage(String str) {
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

        public void run() {
            Scanner input;
            int temp; // players
            try {
                input = new Scanner(socket.getInputStream());
                while (isWorking) {

                    String[] word = input.nextLine().split("#");
                    if (word.length == 2) {
                        if (word[0].charAt(0) == '1') {
                            // charAtはunicodeにするから - '0' 必須
                            map.setMap(fromStringTo2d(word[1]), (word[0].charAt(1) - '0'), false,
                                    (word[0].charAt(2) - '0'),
                                    (word[0].charAt(3) - '0'), (word[0].charAt(4) - '0'), (word[0].charAt(5) - '0'));
                            int[] next = { (word[0].charAt(6) - '0'), (word[0].charAt(7) - '0'),
                                    (word[0].charAt(8) - '0'), (word[0].charAt(9) - '0') };
                                    map.viewNextAndKeep((word[0].charAt(1) - '0'), false, next, (word[0].charAt(10) - '0'));
                            map.viewFence2((word[0].charAt(1) - '0'), false);
                        }
                        if (word[0].charAt(0) == '2') {
                            System.out.println("[CLIENT] 現在 " + word[1] + " 人");
                            temp = Integer.valueOf(word[1]);
                            map.setPlayers(temp);
                            map.viewFence1(temp, false);
                        }
                        if (word[0].charAt(0) == '3') {
                            System.out.println("[CLIENT] " + word[1]);
                            if ("START".equals(word[1])) {
                                map.initMap();
                                map.changeScreen(EnumScreenMode.ScreenMode.INGAME);
                            }
                            if ("YOUWIN".equals(word[1])) map.inGame = false;
                            if ("GAMESET".equals(word[1])) {
                            }
                            if ("BACKTO-C-ST".equals(word[1])) {
                                if (map.myBanme != 1) {
                                    map.changeScreen(EnumScreenMode.ScreenMode.CSERVER);
                                } else {
                                    map.changeScreen(EnumScreenMode.ScreenMode.PSERVER);
                                }
                            }
                        }
                        if (word[0].charAt(0) == '4') {
                            System.out.println("[CLIENT] " + "私は " + (word[1].charAt(0) - '0') + " 番目です");
                            map.myBanme = (word[1].charAt(0) - '0');
                        }
                        if (word[0].charAt(0) == '5') {
                            if (map.myBanme != (word[0].charAt(1) - '0')) {
                                map.damage += (word[1].charAt(0) - '0');
                                map.damageFrag = true;
                                System.out.println("[CLIENT] " + (word[1].charAt(0) - '0') + " ダメージ");
                            }
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("[CLIENT] サーバーから切断されました.");
                e.printStackTrace();
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