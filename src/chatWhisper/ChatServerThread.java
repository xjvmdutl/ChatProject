package chatWhisper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;

public class ChatServerThread extends Thread {
	private Person person;
	private List<Person> list;
	private Socket socket;
	public ChatServerThread(Socket socket,List<Person> listWriters) {
		this.socket=socket;
		list =listWriters;
	}
	@Override 
	public void run() {
		try{
			person=new Person(socket
					,new PrintWriter(new OutputStreamWriter(socket.getOutputStream(),"UTF-8"),true)
					,new BufferedReader(new InputStreamReader(socket.getInputStream(),"UTF-8")));
			while(true) {
				String request = person.getBr().readLine();
				String[] tokens = request.split(":");
				if("join".equals(tokens[0])) {
					doJoin(tokens[1],person.getPw());
				}else if("message".equals(tokens[0])){
					doMessage(tokens[1]);
				}else if("quit".equals(tokens[0])) {
					doQuit(person);
					break;
				}else if("whisper".equals(tokens[0])){
					doWhisper(tokens[1],tokens[2],person.getPw());
				}else {
					ChatServer.log("error : 알수 없는 데이터 타입("+tokens[0]+")");
				}
			}
		}catch(SocketException e){
			log(" abnormal closed by client");
		}catch(IOException e) {
			e.printStackTrace();
		}finally {
			if(person.getSocket().isClosed()==false && person.getSocket() != null)
				try {
					person.getSocket().close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}
	private static void log(String log) {
		System.out.println("[Chat Server] "+log);
	}
	private void doJoin(String nickName,PrintWriter pw) {
		this.person.setName(nickName);
		String data = person.getName()+"님이 입장하셨습니다.";
		broadcast(data);
		addPerson(person);
		pw.println("join:ok");
		pw.flush();
	}
	private void addPerson(Person person) {
		synchronized(list) {
			list.add(person);
		}
	}
	private void broadcast(String data) {
		synchronized(list) {
			for(Person p : list) {
				p.getPw().println(data);
				p.getPw().flush(); 
			}
		}
	}
	private void doMessage(String message) {
		broadcast(person.getName()+":"+message);
	}
	private void doQuit(Person person) {
		removePerson(person);
		String data = person.getName()+"님이 퇴장하셨습니다.";
		broadcast(data);
	}
	private void removePerson(Person person){
		list.remove(person);
	}
	private void doWhisper(String nick,String data,PrintWriter pw) {
		Person per=null;
		for(Person p : list) {
			if(nick.equals(p.getName())) {
				per=p;
				break;
			}
		}
		if(per==null) {
			pw.println(nick+"님이 없습니다");
			return;
		}
		per.getPw().println("(귓속말)"+this.person.getName()+":"+data);
		per.getPw().flush();
	}
}
