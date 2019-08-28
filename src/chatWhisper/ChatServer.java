package chatWhisper;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ChatServer {
	private static final int PORT = 5000;
	public static void main(String[] args) {
		ServerSocket serverSocket=null;
		List<Person> list=new ArrayList<Person>();
		try {
			serverSocket = new ServerSocket();
			String hostAddress =InetAddress.getLocalHost().getHostAddress();
			serverSocket.bind(new InetSocketAddress("127.0.0.1",PORT));
			System.out.println("[TCPServer] binding "+hostAddress+":"+PORT);
			while(true) {
				Socket socket=serverSocket.accept();
				new ChatServerThread(socket,list).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				if(serverSocket!=null && serverSocket.isClosed()==false)
				serverSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public static void log(String log) {
		System.out.println("[ChatServer] "+log);
	}
}
