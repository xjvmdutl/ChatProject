package chat;

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
				if("Bye".equals(message)) {
					break;
				}
				System.out.println(message);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (this.socket != null && !this.socket.isClosed()) {
				try {
					this.socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
