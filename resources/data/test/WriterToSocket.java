package test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class WriterToSocket {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		try {
			Socket socket = new Socket(args[0], Integer.valueOf(args[1]));
			
			System.out.println("Start to write in socket.");
			String line = "";
			
			PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
			while(!line.equals("exit")){
				line = br.readLine();
				printWriter.println(line);
				printWriter.flush();
			}
			
			printWriter.close();
			socket.close();
			System.out.println("End to write in socket.");
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
