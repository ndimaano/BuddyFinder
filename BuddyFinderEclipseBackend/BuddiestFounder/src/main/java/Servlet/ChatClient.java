package Servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ChatClient {

	public static void main(String [] args) {
		Socket s = null;
		PrintWriter pw = null;
		BufferedReader br = null;
		Scanner scan = null;
		try {
			s = new Socket("localhost", 8080);
			System.out.println("Connected to localhost:8080");
			pw = new PrintWriter(s.getOutputStream());
			br = new BufferedReader(new InputStreamReader(s.getInputStream()));
			scan = new Scanner(System.in);
			while(true) {
				String line = scan.nextLine();
				sendMessage(line, pw);
				String lineReceived = br.readLine();
				System.out.println("From Server: " + lineReceived);
			}
		} catch (IOException ioe) {
			System.out.println("ioe: " + ioe.getMessage());
		} finally {
			try {
				if (br != null) {
					br.close();
				}
			} catch (IOException ioe) { 
				System.out.println("ioe: " + ioe.getMessage());
			}
			if (pw != null) {
				pw.close();
			}
			try {
				if (s != null) {
					s.close();
				}
			} catch (IOException ioe) { 
				System.out.println("ioe: " + ioe.getMessage());
			}
			if (scan != null) {
				scan.close();
			}
		}
	}
	public static void sendMessage(String message, PrintWriter pw) {
		pw.println(message);
		pw.flush();
	}
}
