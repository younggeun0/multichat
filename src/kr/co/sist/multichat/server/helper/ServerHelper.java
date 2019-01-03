package kr.co.sist.multichat.server.helper;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.Socket;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import kr.co.sist.multichat.server.view.ServerView;

public class ServerHelper extends Thread implements Serializable {

	private String nick;
	private InetAddress ia;
	private Socket client;
	private JTextArea jtaChatDisplay;
	private DataInputStream readStream;
	private DataOutputStream writeStream;
	private ObjectOutputStream writeObjectStream;
	private List<ServerHelper> listClient;
	private int cnt;
	private ServerView sv;
	private JScrollPane jspChatDisplay;
	
	public ServerHelper(Socket client, JTextArea jtaChatDisplay, int cnt, ServerView sv, 
			List<ServerHelper> listClient, JScrollPane jspChatDisplay) {
		this.client = client;
		this.jtaChatDisplay = jtaChatDisplay;
		this.cnt = cnt;
		this.sv = sv;
		this.listClient = listClient;
		this.jspChatDisplay = jspChatDisplay;
		
		ia = client.getInetAddress();
		
		try {
			readStream = new DataInputStream(client.getInputStream());
			writeStream = new DataOutputStream(client.getOutputStream());
			writeObjectStream = new ObjectOutputStream(client.getOutputStream());
			
			nick = readStream.readUTF();
			broadcast(nick+"님이 접속하였습니다.\n");
			jtaChatDisplay.append(nick+"님이 접속하였습니다.\n");
			jspChatDisplay.getVerticalScrollBar().setValue(jspChatDisplay.getVerticalScrollBar().getMaximum());
			
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
					jtaChatDisplay.append(revMsg+"\n");
					broadcast(revMsg);
					jspChatDisplay.getVerticalScrollBar().setValue(jspChatDisplay.getVerticalScrollBar().getMaximum());
				}
			} catch (IOException e) {
				JOptionPane.showMessageDialog(sv, nick+"님과 연결이 끊어졌습니다.");
				jtaChatDisplay.append(nick+"님과 연결이 끊어졌습니다.\n");
				jspChatDisplay.getVerticalScrollBar().setValue(jspChatDisplay.getVerticalScrollBar().getMaximum());
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
	
	// msg랑 list를 같이 broadcast해야하나..? 
	// 그렇다면 읽어들인 msg와 list를 구분하여 읽어들여 저장하도록 구현해야됨
	// 내일 구현해볼것
	public synchronized void broadcast(String msg) {

		if (writeStream != null) {
			try {
				ServerHelper tempSh = null;
				for (int i=0; i<listClient.size(); i++) {
					tempSh = listClient.get(i);
					tempSh.writeStream.writeUTF(msg);
					tempSh.writeStream.flush();
					tempSh.writeObjectStream.writeObject(listClient);
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
