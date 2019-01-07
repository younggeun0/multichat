package kr.co.sist.multichat.server.helper;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import kr.co.sist.multichat.server.view.ServerView;

public class ServerHelper extends Thread {

	private String nick;
	private InetAddress ia;
	private Socket client;
	private JTextArea jtaChatDisplay;
	private DataInputStream readStream;
	private DataOutputStream writeStream;
	private ObjectOutputStream writeObjectStream;
	private String IaAndNick;
	private List<ServerHelper> listClient;
	private ServerView sv;
	private JScrollPane jspChatDisplay;
	private ArrayList<String> arrListUser;
	
	public ServerHelper(Socket client, JTextArea jtaChatDisplay, ServerView sv, 
			List<ServerHelper> listClient, JScrollPane jspChatDisplay,
			ArrayList<String> arrListUser) {
		
		listClient.add(this);
		ia = client.getInetAddress();
		
		this.client = client;
		this.jtaChatDisplay = jtaChatDisplay;
		this.sv = sv;
		this.listClient = listClient;
		this.jspChatDisplay = jspChatDisplay;
		this.arrListUser = arrListUser;
		
		
		try {
			readStream = new DataInputStream(client.getInputStream());
			writeStream = new DataOutputStream(client.getOutputStream());
			
			
			nick = readStream.readUTF();

			broadcast(nick+"님("+ia.toString()+")이 접속하였습니다.");
			jtaChatDisplay.append(nick+"님("+ia.toString()+"이 접속하였습니다.\n");
			jspChatDisplay.getVerticalScrollBar().setValue(
					jspChatDisplay.getVerticalScrollBar().getMaximum());
			System.out.println("~님이 접속, broadcast 완료");
			
			// ObjectStream 만들고 broadcast하는 순간  
			writeObjectStream = new ObjectOutputStream(client.getOutputStream());

			IaAndNick = ia.toString()+"@"+nick;
			arrListUser.add(IaAndNick);
			broadcast(arrListUser);
			System.out.println("유저 리스트 추가 후 직렬화 전송 완료");
			
			
		} catch (IOException e) {
			JOptionPane.showMessageDialog(sv, "스트림 생성 실패");
		}
	}
	
	@Override
	public void run() {
		
		if (readStream != null) {
			try {
				String revMsg = "";
				while (true) {
					revMsg = readStream.readUTF();
					System.out.println(revMsg);
					broadcast(revMsg);
					System.out.println("받은 메시지 broadcast 완료");
					jtaChatDisplay.append(revMsg+"\n");
				}
			} catch (IOException e) {
				JOptionPane.showMessageDialog(sv, nick+"님("+ia.toString()+")과 연결이 끊어졌습니다.");
				jtaChatDisplay.append(nick+"님("+ia.toString()+")과 연결이 끊어졌습니다.\n");
				jspChatDisplay.getVerticalScrollBar().setValue(
						jspChatDisplay.getVerticalScrollBar().getMaximum());
				
				arrListUser.remove(IaAndNick);
				broadcast(arrListUser);
				
				e.printStackTrace();
			} finally {
				try {
					if (readStream != null) {
						readStream.close();
					}
					if (writeStream != null) {
						writeStream.close();
					}
					if (writeObjectStream != null) {
						writeObjectStream.close();
					}
					if (client != null) {
						client.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	public synchronized void broadcast(String msg) {

		if (writeStream != null) {
			try {
				ServerHelper tempSh = null;
				for (int i=0; i<listClient.size(); i++) {
					tempSh = listClient.get(i);
					tempSh.writeStream.writeUTF(msg);
					tempSh.writeStream.flush();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public synchronized void broadcast(ArrayList<String> arrListUser) {
		
		if (writeObjectStream != null) {
			try {
				ServerHelper tempSh = null;
				for (int i=0; i<listClient.size(); i++) {
					tempSh = listClient.get(i);
					tempSh.writeObjectStream.writeObject(arrListUser);
					tempSh.writeObjectStream.flush();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	public Socket getClient() {
		return client;
	}
	public void setClient(Socket client) {
		this.client = client;
	}
	public String getNick() {
		return nick;
	}
	public InetAddress getIa() {
		return ia;
	}
}
