package kr.co.sist.multichat.server.helper;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.Socket;
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
			
			nick = readStream.readUTF();
			broadcast(nick+"���� �����Ͽ����ϴ�.");
			jtaChatDisplay.append(nick+"���� �����Ͽ����ϴ�.\n");
			jspChatDisplay.getVerticalScrollBar().setValue(jspChatDisplay.getVerticalScrollBar().getMaximum());
			
		} catch (IOException e) {
			JOptionPane.showMessageDialog(sv, "��Ʈ�� ���� ����");
		}
	}
	
	@Override
	public void run() {
		if (readStream != null) {
			try {
				String revMsg = "";
				StringBuilder csvUser = new StringBuilder();
				ServerHelper tempSh = null;
				while (true) {
					revMsg = readStream.readUTF();
					
					if(revMsg.equals("!requestClient")) {
						for(int i=0; i<listClient.size(); i++) {
							tempSh = listClient.get(i);
							csvUser.append(tempSh.getNick()).append(",");
						}
						writeStream.writeUTF(csvUser.toString());
						writeStream.flush();
						System.out.println(csvUser);
						csvUser.delete(0, csvUser.length());
					} else {
						jtaChatDisplay.append(revMsg+"\n");
						broadcast(revMsg);
						jspChatDisplay.getVerticalScrollBar().setValue(jspChatDisplay.getVerticalScrollBar().getMaximum());
					}
				}
			} catch (IOException e) {
				JOptionPane.showMessageDialog(sv, nick+"�԰� ������ ���������ϴ�.");
				jtaChatDisplay.append(nick+"�԰� ������ ���������ϴ�.\n");
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
