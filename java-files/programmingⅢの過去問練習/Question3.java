import java.io.*;

public class Question3{
	private static double kokugoAve, sugakuAve, eigoAve, allAve;
	private static String filename = "Question3.txt";
	private static PrintWriter writer;
	
	public static void main(String[] args) throws IOException{
		String[] nameArray = {"A君", "Bさん", "C君", "D君", "Eさん", "F君", "Gさん", "H君", "Iさん", "Jさん"};
		int[] kokugoArray = {38, 56, 80, 70, 77, 54, 84, 79, 80, 65};
		int[] sugakuArray = {86, 89, 84, 73, 89, 98, 23, 45, 89, 62};
		int[] eigoArray = {89, 67, 43, 70, 54, 67, 80, 45, 90, 64};
		JuniorSeiseki[] dataArray = new JuniorSeiseki[nameArray.length];
		writer = new PrintWriter(new BufferedWriter(new FileWriter(filename)));
		
		for(int i=0; i<nameArray.length; i++) {
			dataArray[i] = new JuniorSeiseki(nameArray[i], kokugoArray[i], sugakuArray[i], eigoArray[i]);
		}
		
		calculateAllAve(dataArray);
		showAllStudentAve(dataArray);
		showAllSubjectAve();
		showAllStudentSeisekiRank(dataArray);
		writer.close();
	}
	
	private static void calculateAllAve(JuniorSeiseki[] dataArray) {
		int sumKokugo, sumSugaku, sumEigo;
		sumKokugo = sumSugaku = sumEigo = 0;
		
		for(int i=0; i<dataArray.length; i++ ){
			sumKokugo += dataArray[i].getKokugo();
			sumSugaku += dataArray[i].getSugaku();
			sumEigo += dataArray[i].getEigo();
		}
		
		kokugoAve = ((int)((sumKokugo) / dataArray.length * 10))/10.0;
		sugakuAve = ((int)((sumSugaku) / dataArray.length * 10))/10.0;
		eigoAve = ((int)((sumEigo) / dataArray.length * 10))/10.0;
		allAve = (kokugoAve + sugakuAve + eigoAve) / 3;
	}
	
	private static void showAllStudentAve(JuniorSeiseki[] dataArray){
		String sentense = null;
		for(int i=0; i<dataArray.length; i++ ){
			sentense = "氏名: " + dataArray[i].getName() + ", 三教科の平均点：" + dataArray[i].getAve();
			System.out.println(sentense);
			writer.println(sentense);
		}
	}
	
	private static void showAllSubjectAve(){
		String sentense = "国語平均点：" + kokugoAve + ", 数学平均点：" + sugakuAve + ", 英語平均点：" + eigoAve + ", 全体平均点：" + allAve;
		System.out.println(sentense);
		writer.println(sentense);
	}
	
	private static void showAllStudentSeisekiRank(JuniorSeiseki[] dataArray){
		String sentense = null;
		for(int i=0; i<dataArray.length; i++ ){
			sentense = "氏名: " + dataArray[i].getName() + ", 国語：" + dataArray[i].calRank(dataArray[i].getKokugo(), kokugoAve) + ", 数学：" + dataArray[i].calRank(dataArray[i].getSugaku(), sugakuAve) + ", 英語：" + dataArray[i].calRank(dataArray[i].getEigo(), eigoAve) + ", 平均：" + dataArray[i].calRank(dataArray[i].getAve(), allAve);
			System.out.println(sentense);
			writer.println(sentense);
		}
	}
}

class JuniorSeiseki {
	private String name;
	private int Kokugo, Sugaku, Eigo;
	
	public JuniorSeiseki(String name, int score1, int score2, int score3) {
		this.name = name;
		Kokugo = score1;
		Sugaku = score2;
		Eigo = score3;
	}
	
	public double getAve() {
		return ((int)((Kokugo + Sugaku + Eigo) / 0.3))/10.0;
	}
	
	public int getKokugo() {
		return Kokugo;
	}
	
	public int getSugaku() {
		return Sugaku;
	}
	
	public int getEigo() {
		return Eigo;
	}
	
	public String getName() {
		return name;
	}
	
	public String calRank(int score, double ave) {
		String answer = null;
		if(score >= ave + 10) {
			answer = "A";
		} else if(score < ave + 10 && score >= ave) {
			answer = "B";
		} else if(score < ave && score >= score - 10) {
			answer = "C";
		} else {
			answer = "D";
		}
		return answer;
	}
	
	public String calRank(double score, double ave) {
		String answer = null;
		if(score >= ave + 10) {
			answer = "A";
		} else if(score < ave + 10 && score >= ave) {
			answer = "B";
		} else if(score < ave && score >= score - 10) {
			answer = "C";
		} else {
			answer = "D";
		}
		return answer;
	}
}