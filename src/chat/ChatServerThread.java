package chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;

public class ChatServerThread extends Thread {
	private String nickname;
	private Socket socket;
	private List<PrintWriter> list;
	public ChatServerThread(Socket socket,List<PrintWriter> listWriters) {
		this.socket=socket;
		list =listWriters;
	}
	@Override 
	public void run() {
		try{
			BufferedReader br =new BufferedReader(new InputStreamReader(socket.getInputStream(),"UTF-8"));
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(),"UTF-8"),true);
			while(true) {
				String request = br.readLine();
				if(request == null) {
					log("클라이언트로부터 연결끊김");
					doQuit(pw);
					break;
				}
				String[] tokens = request.split(":");
				if("join".equals(tokens[0])) {
					doJoin(tokens[1],pw);
					System.out.println(this.nickname+"이 참여하였습니다.");
				}else if("message".equals(tokens[0])) {
					doMessage(tokens[1]);
					System.out.println(this.nickname+":"+tokens[1]);
				}else if("quit".equals(tokens[0])) {
					doQuit(pw);
					System.out.println(this.nickname+"이 퇴장하셨습니다.");
				}else {
					ChatServer.log("error : 알수 없는 요청("+tokens[0]+")");
				}
			}
		}catch(SocketException e){//소켓이 비정상적으로 종료가 되었을때
			log(" abnormal closed by client");
		}catch(IOException e) {
			e.printStackTrace();
		}finally {
			if(socket.isClosed()==false && socket != null)
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}
	private static void log(String log) {
		System.out.println("[Chat Server] "+log);
	}
	private void doJoin(String nickName,PrintWriter pw) {
		this.nickname=nickName;
		//전송 프로토콜
		String data = nickname+"님이 참여하였숩나다";
		broadcast(data);
		addWriter(pw);
		pw.println("join:ok");
		pw.flush();
	}
	private void addWriter(PrintWriter pw) {
		synchronized(list) {
			list.add(pw);
		}
	}
	private void broadcast(String data) {
		synchronized(list) {
			for(PrintWriter pw : list) {
				pw.println(data);
				pw.flush(); 
			}
		}
	}
	private void doMessage(String message) {
		broadcast(this.nickname+":"+message);
	}
	private void doQuit(PrintWriter pw) {
		pw.println("Bye");
		removeWriter(pw);
		String data = nickname+"님이 퇴장 하였습니다";
		broadcast(data);
	}
	private void removeWriter(PrintWriter pw){
		list.remove(pw);
	}
	
}
