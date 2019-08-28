package chatWhisper;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Person {
	private Socket socket;
	private String name;
	private PrintWriter pw;
	private BufferedReader br;
	public Person(Socket socket,PrintWriter pw,BufferedReader br) {
		this.socket=socket;
		this.pw=pw;
		this.br=br;
	}
	public BufferedReader getBr() {
		return br;
	}
	public void setBr(BufferedReader br) {
		this.br = br;
	}
	public Socket getSocket() {
		return socket;
	}
	public void setSocket(Socket socket) {
		this.socket = socket;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public PrintWriter getPw() {
		return pw;
	}
	public void setPw(PrintWriter pw) {
		this.pw = pw;
	}
	
}
