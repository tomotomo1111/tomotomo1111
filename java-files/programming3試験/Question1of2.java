class Node{
    int japaneseScore;
    int mathScore;
    int englishScore;
    String name;
    Node next;

    public Node(int japaneseScore, int mathScore, int englishScore, String name) {
        this.japaneseScore = japaneseScore;
        this.mathScore = mathScore;
        this.englishScore = englishScore;
        this.name = name;
        this.next = null;
    }
}

class List{
    Node head;
	static int size = 0;

    public List(){
        this.head = null;
    }

    public void add(int japaneseScore, int mathScore, int englishScore, String name) {
        Node newNode = new Node(japaneseScore, mathScore, englishScore, name);
        if (head == null) {
            head = newNode;
        } else {
            Node current = head;
            while (current.next != null) {
                current = current.next;
            }
            current.next = newNode;
        }
		size++;
    }
	
	public int getMax(String sub) {
		int max = 0;
		Node point = head;
		for(int i=0; i<size; i++) {
			if(sub.equals("‘Œê")) {
				if(max < point.japaneseScore) max = point.japaneseScore;
			} else if(sub.equals("”Šw")) {
				if(max < point.mathScore) max = point.mathScore;
			} else if(sub.equals("‰pŒê")) {
				if(max < point.englishScore) max = point.englishScore;
			}
			point = point.next;
		}
		return max;
	}
}

public class Question1of2{
    public static void main(String[] args) {
        List scoreList = new List();
        scoreList.add(80, 70, 90, "‚è‚ã‚¤‚Æ");
        scoreList.add(85, 65, 78, "‚½‚¢‚¼‚¤");
        Node current = scoreList.head;
        while (current != null) {
            System.out.println("Name: " + current.name);
            System.out.println("Japanese Score: " + current.japaneseScore);
            System.out.println("Math Score: " + current.mathScore);
            System.out.println("English Score: " + current.englishScore);
            System.out.println("-----------");
            current = current.next;
        }
		System.out.println("‘Œê‚ÌÅ‚“_:" + scoreList.getMax("‘Œê"));
		System.out.println("”Šw‚ÌÅ‚“_:" + scoreList.getMax("”Šw"));
		System.out.println("‰pŒê‚ÌÅ‚“_:" + scoreList.getMax("‰pŒê"));
    }
}
