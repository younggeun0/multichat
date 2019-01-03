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
			broadcast(nick+"���� �����Ͽ����ϴ�.\n");
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
				while (true) {
					revMsg = readStream.readUTF();
					jtaChatDisplay.append(revMsg+"\n");
					broadcast(revMsg);
					jspChatDisplay.getVerticalScrollBar().setValue(jspChatDisplay.getVerticalScrollBar().getMaximum());
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
	
	// msg�� list�� ���� broadcast�ؾ��ϳ�..? 
	// �׷��ٸ� �о���� msg�� list�� �����Ͽ� �о�鿩 �����ϵ��� �����ؾߵ�
	// ���� �����غ���
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
