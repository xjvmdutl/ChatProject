package chatWhisper;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;

public class ChatClientThread extends Thread {
	private Socket socket = null;
	private BufferedReader br;
	public ChatClientThread(Socket socket,BufferedReader br) {
		socket = this.socket;
		this.br= br;
	}
	@Override 
	public void run() {
		String message;
		try {
			while(true) {
				message = br.readLine();
				if("join:ok".equals(message)) {
					continue;
				}
				if(message==null)
					break;
				System.out.println(message);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				if(socket!=null&&socket.isClosed()==false)
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}
}