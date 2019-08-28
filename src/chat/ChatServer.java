package chat;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ChatServer {
	private static final int PORT = 5000;//포트는 전역으로 관리
	public static void main(String[] args) {
		ServerSocket serverSocket=null;
		List<PrintWriter> list=new ArrayList<PrintWriter>();
		try {
			//1.서버소켓 생성
			serverSocket = new ServerSocket();
			String hostAddress =InetAddress.getLocalHost().getHostAddress();
			
			//2.Binding : Socket에 SocketAddress(IPAddress+port)을 연결
			serverSocket.bind(new InetSocketAddress("127.0.0.1",PORT));
			System.out.println("[TCPServer] binding "+hostAddress+":"+PORT);
			//3.accept:
			while(true) {
				Socket socket=serverSocket.accept();
				new ChatServerThread(socket,list).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			//8. 서버 소켓 자원 정리
			try {
				if(serverSocket!=null && serverSocket.isClosed()==false)//서버 소켓이 이미 닫혀져 있을 수도 있기 떄문에 한번 더 물어주는 코드 필요
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
