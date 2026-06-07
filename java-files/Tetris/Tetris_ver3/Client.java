import java.net.*;
import java.net.Socket;
import java.util.*;
import java.util.Scanner;
import java.io.*;
import java.io.PrintWriter;
        

public class Client {
    private static int w = 12 + 3;
    private static int h = 21 + 3;

    private static Socket socket;
    private static PrintWriter output;
    private String serverIP = "000.0.0.0";
    //private static boolean isConnected = false;
    private static ClientInputThread cit;

    public void join() {
        try {
            socket = new Socket(serverIP, 3838);
            //isConnected = true;
            cit = new ClientInputThread(socket);
            cit.start();
            output = new PrintWriter(socket.getOutputStream());
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void sendMessage(int[][] screen) {
        try{
            output.println("1#"+ from2dToString(screen));
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

    public void setIP(String ip) {
        serverIP = ip;
    }

    public void quit() {
        try{
            //isConnected = false;
            cit.stopClientThread();
            if (output != null) output.close();
            if (socket != null) socket.close();
        } catch (Exception e){
            System.out.println(e);
        }
    }

    private static String from2dToString(int[][] screen) {
            int w = screen[0].length;
            int size = screen[0].length * screen.length;
            StringBuilder sb = new StringBuilder(size);
            for (int i=0; i<size; i++) sb.append((char)('0'+screen[i/w][i%w]));
            return sb.toString();
        }

    static class ClientInputThread extends Thread {
        Socket socket;
        boolean isWorking = true;
        public ClientInputThread(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            Scanner input;
            int temp; //players
            try {

                while(isWorking){
                    input = new Scanner(socket.getInputStream());
                    String[] word = input.nextLine().split("#");
                    if (word.length == 2) {
                        if (word[0].charAt(0) == '1') {
                            Map.setMap(fromStringTo2d(word[1]), Integer.valueOf(word[0].charAt(1)), false);
                        }
                        if (word[0].charAt(0) == '2') {
                            temp = Integer.valueOf(word[1]);
                            Map.setPlayers(temp);
                            MyFrame.setFrameSize(temp);
                            Map.setFense(temp, false);
                        }
                        if (word[0].charAt(0) == '3') {
                            System.out.println(word[1]);
                            if("START".equals(word[1])) {
                                Map.changeScreen(EnumScreenMode.ScreenMode.INGAME);
                            }
                        }
                        if (word[0].charAt(0) == '4') {
                            Map.setBanme(Integer.valueOf(word[1]));
                        }
                    }
                }
            } catch (Exception e) {
                //isConnected = false;
                System.out.println("サーバーから切断されました.");
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

        private static int[][] fromStringTo2d(String str) {
            
            if (w * h != str.length()) throw new IllegalArgumentException();
            int[][] screen = new int[h][w];
            for (int i=0; i<str.length(); i++) screen[i/w][i%w] = str.charAt(i)-'0';
            return screen;
        }

        public void stopClientThread() {
            isWorking = false;
        }
    }
}