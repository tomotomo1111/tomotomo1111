import java.io.*;
import java.util.Random;

class AchievementRecord{
	private String[] member = new String[15];
	private int[] kokugo = new int[15];
	private int[] sugaku = new int[15];
	private int[] eigo = new int[15];
	
	public AchievementRecord(String file) throws IOException{
		ReadAccountFile(file);
		CreateScoreBox();
	}
	
	public void ReadAccountFile(String file) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String line;
		int i=0;
		while((line = reader.readLine()) != null) {
			member[i++] = line;
		}
		reader.close();
	}
	
	public void CreateScoreBox() {
		for(int i=0; i<kokugo.length; i++) {
			kokugo[i] = MakeRandomInt(30);
			sugaku[i] = MakeRandomInt(30);
			eigo[i] = MakeRandomInt(30);
		}
	}
	
	public int MakeRandomInt(int offset) {
		Random rand = new Random();
		return rand.nextInt(70) + offset;
	}
	
	public void WriteFile(String file) throws IOException{
		String fileName = file;
		WriteAllResultInText();
		PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(fileName)));
		writer.println("それぞれの成績は下記になります．");
		for(int i=0; i<member.length; i++) {
			writer.println(member[i]);
		}
		writer.close();
	}
	
	public void WriteAllResultInText() {
		for(int i=0; i<member.length; i++) {
			double ave = (double)(kokugo[i] + sugaku[i] + eigo[i]) / 3;
			ave = (int)(100 * ave) / 100;
			member[i] = member[i] + "国語:" + kokugo[i] + " 数学:" + sugaku[i] + " 英語:" + eigo[i] + " 平均点:" + ave;
		}
	}
	
	public void CreateClass() throws IOException{
		int[] array = RandomIntOrder(15);
		String fileName1 = "classA.txt";
		PrintWriter writer1 = new PrintWriter(new BufferedWriter(new FileWriter(fileName1)));
		String fileName2 = "classB.txt";
		PrintWriter writer2 = new PrintWriter(new BufferedWriter(new FileWriter(fileName2)));
		String fileName3 = "classC.txt";
		PrintWriter writer3 = new PrintWriter(new BufferedWriter(new FileWriter(fileName3)));
		
		for(int i=0; i<array.length; i++) {
			if(i<5 && i>=0) {
				writer1.println(member[array[i]]);
			} else if(i<10 && i>=5) {
				writer2.println(member[array[i]]);
			} else {
				writer3.println(member[array[i]]);
			}
		}
		writer1.close();
		writer2.close();
		writer3.close();
	}
	
	public int[] RandomIntOrder(int max) {
		Random rand_1 = new Random();
		Random rand_2 = new Random();
		int index_1, index_2;
		int trace = 100;
		int[] arr = new int[max];
		for(int i=0; i<max; i++) {
				arr[i] = i;
		}
		
		do{
			do{
				index_1 = rand_1.nextInt(max);
				index_2 = rand_1.nextInt(max);
			} while(index_1 == index_2);
			
			int buffer = arr[index_1];
			arr[index_1] = arr[index_2];
			arr[index_2] = buffer;
			
		}while(trace-- > 0);
		
		return arr;
	}
}