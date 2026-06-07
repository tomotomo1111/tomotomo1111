import java.io.*;

public class ReadExample2{
	
	public static String readWord(BufferedReader reader) throws IOException {
		int c;
		StringBuffer sbuf = new StringBuffer();
		
		do{
			c = reader.read();
			if(c < 0) return null;
		} while (!Character.isLetterOrDigit((char)c));
		
		do{
			sbuf.append((char)c);
			c = reader.read();
		} while (c >= 0 && Character.isLetterOrDigit((char)c));
		return sbuf.toString();
	}
	
	public static void main(String[] args) throws IOException {
		String filename = "ReadExample.txt";
		BufferedReader reader = new BufferedReader(new FileReader(filename));
		
		String word;
		while((word = readWord(reader)) != null) {
			System.out.println(word);
		}
		reader.close();
	}
}