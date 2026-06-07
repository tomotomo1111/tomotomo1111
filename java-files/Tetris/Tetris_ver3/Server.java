import java.io.*;
import java.io.PrintWriter;
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
    }

    public void run() {
        System.out.println("サーバー起動...");
        try {
            InetAddress localhost = InetAddress.getLocalHost();
            String ipAddress = localhost.getHostAddress();
            System.out.println("IPアドレス: " + ipAddress);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        while (isWorkingServer) {
            try{
                ServerSocket server = new ServerSocket(PORT, 5);
                ServerSocketThread sst = new ServerSocketThread(server.accept(), ++players);
                socketThreads.add(sst);
                sendAll(players);
                sst.start();
                server.close();
                System.out.println("接続されました!");
            } catch (Exception e){

            }   
        }
    }

    public String getIP() {
        String ip = null;
        try {
            InetAddress localhost = InetAddress.getLocalHost();
            ip= localhost.getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return ip;
    }

    public static void sendAll(int[][] message, int banme) {
        for (int i = 0; i < socketThreads.size(); i++) socketThreads.get(i).sendMessage(message, banme);
    }

    public static void sendAll(int message) {
        for (int i = 0; i < socketThreads.size(); i++) socketThreads.get(i).sendMessage(message);
    }

    public static void sendAll(String message) {
        for (int i = 0; i < socketThreads.size(); i++) socketThreads.get(i).sendMessage(message);
    }

    public void stopServer() {
        isWorkingServer = false;
        pm.closePort();
    }

    static class ServerSocketThread extends Thread {
        private int w = 12 + 3;
        private int h = 21 + 3;
        private Socket socket;
        private PrintWriter output;
        private Scanner input;
        private int banme;

        public ServerSocketThread(Socket socket, int banme){
            this.socket = socket;
            this.banme = banme;
        }
    
        public void run(){
            
            try {
                while(isWorkingServer){
                    System.out.println("スレッド " + Thread.currentThread().getId() + " は稼働しています.");
                    output = new PrintWriter(socket.getOutputStream());
                    sendMessage("banme", banme);
                    
                    input = new Scanner(socket.getInputStream());
                    String[] word = input.nextLine().split("#");
                    if (word.length == 2) {
                        if (word[0].charAt(0) == '1') sendAll(word[1]);
                        if (word[0].charAt(0) == '2') System.out.println(word[1]);
                        if (word[0].charAt(0) == '3') System.out.println(word[1]);
                    }
                }
            } catch (Exception e) {
                socketThreads.remove(this);
                players--;
                System.out.println("スレッド " + Thread.currentThread().getId() + " は切断されました.");
            }
        }
    
        private void screen2d(String str) {
            if (w * h != str.length()) throw new IllegalArgumentException();
            int[][] screen = fromStringTo2d(str);
            
            for (int j = 0; j < h; j++) {
                for (int i = 0; i < w; i++) {
                    System.out.print((screen[j][i] % 10)+ " ");
                }
                System.out.println();
            }
            
        }
    
        private String from2dToString(int[][] screen) {
            int w = screen[0].length;
            int size = screen[0].length * screen.length;
            StringBuilder sb = new StringBuilder(size);
            for(int i=0; i<size; i++) sb.append((char)('0'+screen[i/w][i%w]));
            return sb.toString();
        }
    
        private int[][] fromStringTo2d(String str) {
            if(w * h != str.length()) throw new IllegalArgumentException();
            int[][] screen = new int[h][w];
            for(int i=0; i<str.length(); i++) screen[i/w][i%w] = str.charAt(i)-'0';
            return screen;
        }

        public void sendMessage(int[][] screen, int banme) {
            try{
                output.println("1" + banme + "#"+ from2dToString(screen));
                output.flush();
            } catch (Exception e){
                System.out.println(e);
            }
        }
    
        public void sendMessage(int num) {
            try{
                output.println("2#"+ num);
                output.flush();
            } catch (Exception e){
                System.out.println(e);
            }
        }
    
        public void sendMessage(String str) {
            try{
                output.println("3#"+ str);
                output.flush();
            } catch (Exception e){
                System.out.println(e);
            }
        }

        public void sendMessage(String str, int num) {
            try{
                if ("banme".equals(str)) {
                    output.println("4#"+ num);
                    output.flush();
                }
            } catch (Exception e){
                System.out.println(e);
            }
        }
    }
}