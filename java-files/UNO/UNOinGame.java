import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class UNOinGame {
    private static UNO uno;

    public static void main(String[] args) {
        uno = new UNO();
        uno.inGame();

        while (true) {
            waitForControl();
            if (isManHandZero()) {
                System.out.println("");
                System.out.println("Congratulation!!");
                System.exit(1);
            } else if(isComHandZero()) {
                System.out.println("");
                System.out.println("You lose");
                System.exit(1);
            } else {

            }
            try {
                Thread.sleep(100);
            } catch (Exception e) {

            }
        }
    }

    private static void waitForControl() {
        uno.showAllHand();
        uno.showTrash();
        System.out.print("write the card index" + "(1-" + uno.getHands("man") + ")" + "that you want to put ==(input)=>");
        Scanner scan = new Scanner(System.in);
        String input = scan.next();
        try {
            int index = Integer.parseInt(input);
            if (index == 0) {
                uno.draw("man");
                uno.computerMove();
            } else if (uno.canPutCard(index - 1, "man") && index - 1 < uno.getHands("man") && index - 1 >= 0) {
                uno.putCard(index - 1, "man");
                uno.computerMove();
            }
        } catch (Exception e) {
            System.out.println("you must write numeric only on this field!!");
            // UNOinGame.waitforControl();
        }
        System.out.println();
    }

    private static boolean isManHandZero() {
        if (uno.getHands("man") == 0) {
            return true;
        } else {
            return false;
        }
    }

    private static boolean isComHandZero() {
        if (uno.getHands("com") == 0) {
            return true;
        } else {
            return false;
        }
    }
}

class UNO {
    private CARD[] mount = new CARD[76];
    private ArrayList<CARD> hand_man = new ArrayList<>();
    private ArrayList<CARD> hand_com = new ArrayList<>();
    private ArrayList<CARD> trash = new ArrayList<>();
    private int currentTop = 0;
    private final int MAX_HAND = 7;

    public UNO() {
    }

    public void inGame() {
        cardSet();
        shuffle();
        firstHandAndStash();
    }

    private void cardSet() {
        mount[0] = new CARD("RED", 0);
        mount[1] = new CARD("BLUE", 0);
        mount[2] = new CARD("GREEN", 0);
        mount[3] = new CARD("YELLOW", 0);

        for (int i = 4; i < (mount.length - 4) / 2 + 4; i++) {
            for (int j = 1; j < 10; j++) {
                for (int c = 0; c < 4; c++) {
                    if (c == 0) {
                        mount[i] = new CARD("RED", j);
                        i++;
                        mount[i] = new CARD("RED", j);
                        i++;
                    }
                    if (c == 1) {
                        mount[i] = new CARD("BLUE", j);
                        i++;
                        mount[i] = new CARD("BLUE", j);
                        i++;
                    }
                    if (c == 2) {
                        mount[i] = new CARD("GREEN", j);
                        i++;
                        mount[i] = new CARD("GREEN", j);
                        i++;
                    }
                    if (c == 3) {
                        mount[i] = new CARD("YELLOW", j);
                        i++;
                        mount[i] = new CARD("YELLOW", j);
                        i++;
                    }
                }
            }
        }
    }

    private void shuffle() {
        int i = 100;
        CARD buffer = new CARD("RED", 0);
        Random rand1 = new Random();
        Random rand2 = new Random();
        int index1, index2;
        while (i-- > 0) {
            index1 = rand1.nextInt(mount.length);
            index2 = rand2.nextInt(mount.length);

            buffer = mount[index1];
            mount[index1] = mount[index2];
            mount[index2] = buffer;
        }
    }

    private void firstHandAndStash() {
        int i = 0;
        while (i++ < MAX_HAND) {
            draw("man");
            draw("com");
        }
        draw("trash");
    }

    public void draw(String player) {
        if (currentTop < mount.length) {
            if (player.equals("man")) {
                hand_man.add(mount[currentTop]);
            } else if (player.equals("com")) {
                hand_com.add(mount[currentTop]);
            } else if (player.equals("trash")) {
                trash.add(mount[currentTop]);
            }
            currentTop++;
        }
    }

    public int getHands(String name) {
        if(name.equals("man")){
             return hand_man.size();
        } else if(name.equals("com")){
            return hand_com.size();
        } else {
            return 0;
        }
    }

    public void showAllHand() {
        CARD card = new CARD("RED", 0);
        System.out.print("PLAYER HAND : ");
        for (int i = 0; i < hand_man.size(); i++) {
            card = hand_man.get(i);
            System.out.print(card.getColor() + "-" + card.getNum() + " ");
        }
        System.out.println("");
    }

    public void showTrash() {
        CARD card = new CARD("RED", 0);
        card = trash.get(trash.size() - 1);
        System.out.print("current trash is (" + card.getColor() + "-" + card.getNum()
                + ") and if you cant put any card, write 0");
        System.out.println("");
    }

    public boolean canPutCard(int index, String name) {
        CARD card_h = new CARD("RED", 0);
        CARD card_t = new CARD("RED", 0);
        card_h = name.equals("man") ? hand_man.get(index) : hand_com.get(index);
        card_t = trash.get(trash.size() - 1);
        if (card_h.getColor().equals(card_t.getColor()) || card_h.getNum() == card_t.getNum()) {
            return true;
        } else {
            return false;
        }
    }

    public void putCard(int index, String name) {
        CARD card_h = new CARD("RED", 0);
        CARD card_t = new CARD("RED", 0);
        card_h = name.equals("man") ? hand_man.get(index) : hand_com.get(index);
        trash.add(card_h);
        if (name.equals("man"))
            hand_man.remove(index);
        if (name.equals("com"))
            hand_com.remove(index);
    }

    public void computerMove() {
        boolean canput = false;
        int canputIndex = 0;
        for (int i = 0; i < hand_com.size(); i++) {
            if (canPutCard(i, "com")) {
                canput = true;
                canputIndex = i;
            }
        }

        if (canput) {
            showAllComHand();
            System.out.println(
                    "computer put " + hand_com.get(canputIndex).getColor() + "-" + hand_com.get(canputIndex).getNum());
            System.out.println("");
            putCard(canputIndex, "com");
        } else {
            draw("com");
            showAllComHand();
            System.out.println("computer couldnt put any card");
            System.out.println("");
            
        }

        try {
            Thread.sleep(1000);
        } catch (Exception e) {

        }
    }

    private void showAllComHand() {
        CARD card = new CARD("RED", 0);
        System.out.println("");
        System.out.print("COMPUTER HAND : ");
        for (int i = 0; i < hand_com.size(); i++) {
            card = hand_com.get(i);
            System.out.print(card.getColor() + "-" + card.getNum() + " ");
        }
        System.out.println("");
    }
}

class CARD {
    private String color;
    private int num;

    public CARD(String color, int num) {
        this.color = color;
        this.num = num;
    }

    public String getColor() {
        return color;
    }

    public int getNum() {
        return num;
    }
}