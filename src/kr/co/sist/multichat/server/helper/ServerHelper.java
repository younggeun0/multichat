package kr.co.sist.multichat.server.helper;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JTextArea;

import kr.co.sist.multichat.server.view.ServerView;

public class ServerHelper extends Thread {

	private String nick;
	private Socket client;
	private JTextArea jtaChatDisplay;
	private DataInputStream readStream;
	private DataOutputStream writeStream;
	private List<ServerHelper> listClient;
	private int cnt;
	private ServerView sv;
	
	public ServerHelper(Socket client, JTextArea jtaChatDisplay, int cnt, ServerView sv, List<ServerHelper> listClient) {
		this.client = client;
		this.jtaChatDisplay = jtaChatDisplay;
		this.cnt = cnt;
		this.sv = sv;
		this.listClient = listClient;
		
		try {
			readStream = new DataInputStream(client.getInputStream());
			writeStream = new DataOutputStream(client.getOutputStream());
			
			nick = readStream.readUTF();
			jtaChatDisplay.append(nick+"님이 접속하였습니다.\n");
			
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
				}
			} catch (IOException e) {
				JOptionPane.showMessageDialog(sv, nick+"님과 연결이 끊어졌습니다.");
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
}
