import java.io.*;

public class WriteExample3{
	public static void main(String[] args) {
		String fileName = "JavaFile2.txt";
		PrintWriter writer = null;
		
		try{
			writer = new PrintWriter(new BufferedWriter(new FileWriter(fileName)));
			writer.println("Name:Java JavaJava");
			writer.println("Age:184");
		}catch(IOException e) {
			System.out.println("Error!");
			System.out.println(e.getMessage());
		}finally{
			if(writer !=null) {
				writer.close();
			}
		}
	}
}