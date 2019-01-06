package kr.co.sist.multichat.server.helper;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.Socket;
import java.util.List;

import javax.swing.DefaultListModel;
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
	private ObjectInputStream readObjectStream;
	private ObjectOutputStream writeObjectStream;
	private String IaAndNick;
	private List<String> listUser; 
	private List<ServerHelper> listClient;
	private ServerView sv;
	private JScrollPane jspChatDisplay;
	
	public ServerHelper(Socket client, JTextArea jtaChatDisplay, ServerView sv, 
			List<ServerHelper> listClient, JScrollPane jspChatDisplay,
			List<String> listUser) {
		
		listClient.add(this);
		this.listUser = listUser;
		ia = client.getInetAddress();
		
		this.client = client;
		this.jtaChatDisplay = jtaChatDisplay;
		this.sv = sv;
		this.listClient = listClient;
		this.jspChatDisplay = jspChatDisplay;
		
		
		try {
			readStream = new DataInputStream(client.getInputStream());
			writeStream = new DataOutputStream(client.getOutputStream());
			
//			readObjectStream = new ObjectInputStream(client.getInputStream());
//			writeObjectStream = new ObjectOutputStream(client.getOutputStream());
			
			nick = readStream.readUTF();

			broadcast(nick+"님("+ia.toString()+")이 접속하였습니다.");
			jtaChatDisplay.append(nick+"님("+ia.toString()+"이 접속하였습니다.\n");
			jspChatDisplay.getVerticalScrollBar().setValue(
					jspChatDisplay.getVerticalScrollBar().getMaximum());
			
			IaAndNick = ia.toString()+"@"+nick;
			listUser.add(IaAndNick);
//			broadcast(listUser);
//			System.out.println("listUser브로드////////////////"+listUser.toString());
			
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
					broadcast(revMsg);
					jtaChatDisplay.append(revMsg+"\n");
				}
			} catch (IOException e) {
				JOptionPane.showMessageDialog(sv, nick+"님("+ia.toString()+")과 연결이 끊어졌습니다.");
				jtaChatDisplay.append(nick+"님("+ia.toString()+")과 연결이 끊어졌습니다.\n");
				jspChatDisplay.getVerticalScrollBar().setValue(
						jspChatDisplay.getVerticalScrollBar().getMaximum());
				
				listUser.remove(IaAndNick);
				System.out.println("listUser빼기////////////////"+listUser.toString());
				broadcast(listUser);
				
				e.printStackTrace();
			} finally {
				try {
					if (readStream != null) {
						readStream.close();
					}
					if (writeStream != null) {
						writeStream.close();
					}
//					if (readObjectStream != null) {
//						readObjectStream.close();
//					}
//					if (writeObjectStream != null) {
//						writeObjectStream.close();
//					}
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
	
	public synchronized void broadcast(List<String> listUser) {
		if (writeObjectStream != null) {
			try {
				ServerHelper tempSh = null;
				for (int i=0; i<listClient.size(); i++) {
					tempSh = listClient.get(i);
					tempSh.writeObjectStream.writeObject(listUser);
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
