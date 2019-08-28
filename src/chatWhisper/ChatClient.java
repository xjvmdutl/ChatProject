package chatWhisper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

public class ChatClient {
	private static String SERVER_IP="127.0.0.1";
	private static int SERVER_PORT=5000;
	public static void main(String[] args) {
		Scanner sc = null;
		Socket socket = null;
		try {
			sc = new Scanner(System.in);
			socket = new Socket();
			socket.connect(new InetSocketAddress(SERVER_IP,SERVER_PORT));
			BufferedReader br =new BufferedReader(new InputStreamReader(socket.getInputStream(),"UTF-8"));
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(),"UTF-8"),true);
			System.out.print("닉네임>>");
			String nickname = sc.nextLine();
			pw.println("join:"+nickname);
			pw.flush();
			new ChatClientThread(socket,br).start();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			while(true) {
				System.out.print(">>");
				String input = sc.nextLine();
				if("quit".equals(input)) {
					pw.println("quit:");
					break;
				}
				if("".equals(input))
					input=" ";
				if("whisper".equals(input)) {
					System.out.print("귓속말 대상:data=");
					input=sc.nextLine();
					if(!("".equals(input)))
						pw.println("whisper:"+input);	
					continue;
				}
				pw.println("message:"+input);
			}
		}catch(IOException e) {
			log("error : "+e);
		}finally {
			if(sc!=null)
				sc.close();
		}
	}
	private static void log(String log) {
		System.out.println("[Client] "+log);
	}

}
