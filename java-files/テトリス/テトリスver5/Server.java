import java.io.*;
import java.net.*;
import java.util.*;

public class Server extends Thread {

    private static final int PORT = 3838;
    private static ArrayList<ServerSocketThread> socketThreads = new ArrayList<ServerSocketThread>();
    private static boolean isWorkingServer = true;
    private static int players = 0;
    private static PortManage pm = new PortManage(PORT);

    public Server() {
        pm.openPort();
        players = 0;
    }

    public void run() {
        System.out.println("[SERVER] サーバー起動...");
        try {
            InetAddress localhost = InetAddress.getLocalHost();
            String ipAddress = localhost.getHostAddress();
            System.out.println("[SERVER] IPアドレス: " + ipAddress);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        while (isWorkingServer) {
            try {
                ServerSocket server = new ServerSocket(PORT, 5);
                ServerSocketThread sst = new ServerSocketThread(server.accept(), ++players, this);
                socketThreads.add(sst);
                sst.start();
                server.close();
                System.out.println("[SERVER] 接続されました! 現在 " + players + " 人");
            } catch (Exception e) {
                
            }
        }
    }

    public String getIP() {
        String ip = null;
        try {
            InetAddress localhost = InetAddress.getLocalHost();
            ip = localhost.getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return ip;
    }
    // screen next reserve
    public synchronized void sendAll(int[][] message, int aabanme, int mposX, int mposY, int mmode, int mlabel,
            int next1, int next2, int next3, int next4, int mreserve, int gotDamage) {
        for (int i = 0; i < socketThreads.size(); i++) {
            socketThreads.get(i).sendMessage(message, aabanme, mposX, mposY, mmode, mlabel, next1, next2, next3, next4,
                    mreserve, gotDamage);
        }
    }

    public synchronized void sendAll(String keyword, int message) {
        for (int i = 0; i < socketThreads.size(); i++) {
            socketThreads.get(i).sendMessage(keyword, message);
        }
    }
    // DAMAGE TURNWAITINGCHECK
    public synchronized void sendAll(String mode, int banme, int message) {
        for (int i = 0; i < socketThreads.size(); i++) {
            socketThreads.get(i).sendMessage(mode, banme, message);
        }
    }
    // YOUWIN START(server.sendMessage("START"))
    public synchronized void sendAll(String message) {
        for (int i = 0; i < socketThreads.size(); i++) {
            socketThreads.get(i).sendMessage(message);
        }
    }

    public synchronized int checkWin() {
        int losers = 0;
        int winner_i = 0;
        for (int i = 0; i < socketThreads.size(); i++) {
            if (socketThreads.get(i).isLosed()) {
                losers++;
            } else {
                winner_i = i;
            }
        }
        if (losers != players - 1) winner_i = -1; 
        return winner_i;
    }

    public void stopServer(){
        isWorkingServer = false;
        for (int i = 0; i < socketThreads.size(); i++) {
            try {
                socketThreads.get(i).socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            Thread.sleep(500);
        } catch (Exception e) {
            e.printStackTrace();
        }
        pm.closePort();
    }

    static class ServerSocketThread extends Thread {
        private int w = Map.MINIMAP_W + Map.MINIMAP_OFFSET_PLUS * 2;
        private int h = Map.MINIMAP_H + Map.MINIMAP_OFFSET_PLUS * 2;
        private Socket socket;
        private PrintWriter output;
        private Scanner input;
        private InputStream dconnect_observer;
        private int banme;
        private Server server;
        private boolean isLosed = false;

        public ServerSocketThread(Socket socket, int banme, Server server) {
            this.socket = socket;
            this.banme = banme;
            this.server = server;
        }

        public void run() {
            try {
                output = new PrintWriter(socket.getOutputStream());
                input = new Scanner(socket.getInputStream());
                sendMessage("banme", banme);
                server.sendAll("players", players);
                while (isWorkingServer) {
                    dconnect_observer = socket.getInputStream();
                    // System.out.println("[SERVER] スレッド " + Thread.currentThread().getId() + " は稼働しています.");
                    String[] word = input.nextLine().split("#");
                    if (word.length == 2) {
                        if (word[0].charAt(0) == '1') {
                            server.sendAll(fromStringTo2d(word[1]), (word[0].charAt(1) - '0'), (word[0].charAt(2) - '0'),
                                    (word[0].charAt(3) - '0'), (word[0].charAt(4) - '0'), (word[0].charAt(5) - '0'),
                                    (word[0].charAt(6) - '0'), (word[0].charAt(7) - '0'), (word[0].charAt(8) - '0'),
                                    (word[0].charAt(9) - '0'),
                                    (word[0].charAt(10) - '0'), (word[0].charAt(11) - '0'));
                        }
                        if (word[0].charAt(0) == '2') {
                            server.sendAll("DAMAGE", word[0].charAt(1) - '0', word[1].charAt(0) - '0');
                        }
                        if (word[0].charAt(0) == '3') {
                            if ("LOSE".equals(word[1])) {
                                isLosed = true;
                                int win_i = server.checkWin();
                                if (win_i != -1) {
                                    System.out.println("[SERVER] 勝者は " + (win_i + 1) + " 番目のプレイヤーです");
                                    socketThreads.get(win_i).sendMessage("YOUWIN");
                                    server.sendAll("GAMESET");
                                }
                            }
                            if ("BACKTO-C-ST".equals(word[1])) {
                                server.sendAll(word[1]);
                                for (int j = 0; j < socketThreads.size(); j++) socketThreads.get(j).isLosed = false;
                            }
                            // System.out.println("[SERVER] " + word[1]); ポイント
                        }
                        if (word[0].charAt(0) == '4') {
                            server.sendAll("TURNWAITINGCHECK", word[0].charAt(1) - '0', word[1].charAt(0) - '0');
                        }
                    }
                }
            } catch (Exception e) {
                socketThreads.remove(this);
                server.sendAll("players", --players);
                System.out.println("[SERVER] スレッド " + Thread.currentThread().getId() + " は切断されました.");
            }
        }

        private synchronized String from2dToString(int[][] screen) {
            int w = screen[0].length;
            int size = screen[0].length * screen.length;
            StringBuilder sb = new StringBuilder(size);
            for (int i = 0; i < size; i++)
                sb.append((char) ('0' + screen[i / w][i % w]));
            return sb.toString();
        }

        private synchronized int[][] fromStringTo2d(String str) {
            if (w * h != str.length())
                throw new IllegalArgumentException();
            int[][] screen = new int[h][w];
            for (int i = 0; i < str.length(); i++)
                screen[i / w][i % w] = str.charAt(i) - '0';
            return screen;
        }
        // screen next reserve
        public synchronized void sendMessage(int[][] screen, int abanme, int mposX, int mposY, int mmode, int mlabel,
                int next1, int next2, int next3, int next4, int mreserve, int gotDamage) {
            try {
                output.println("1" + (char) (abanme + '0') + (char) (mposX + '0') + (char) (mposY + '0')
                        + (char) (mmode + '0') + (char) (mlabel + '0') + (char) (next1 + '0') + (char) (next2 + '0')
                        + (char) (next3 + '0') + (char) (next4 + '0') + (char) (mreserve + '0') + (char) (gotDamage + '0') + "#"
                        + from2dToString(screen));
                output.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // YOUWIN
        public synchronized void sendMessage(String str) {
            try {
                output.println("3#" + str);
                output.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public synchronized void sendMessage(String str, int num) {
            try {
                if ("players".equals(str)) {
                    output.println("2#" + (char) (num + '0'));
                    output.flush();
                }
                if ("banme".equals(str)) {
                    output.println("4#" + (char) (num + '0'));
                    output.flush();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // gotDamage
        public synchronized void sendMessage(String mode, int banme, int num) {
            if ("DAMAGE".equals(mode)) {
                try {
                    output.println("5" + (char) (banme + '0') + "#" + (char) (num + '0'));
                    output.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if ("TURNWAITINGCHECK".equals(mode)) {
                try {
                    output.println("6" + (char) (banme + '0') + "#" + (char) (num + '0'));
                    output.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        public synchronized boolean isLosed() {
            return isLosed;
        }
    }
}