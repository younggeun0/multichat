package kr.co.sist.multichat.server.helper;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
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
	private ObjectOutputStream writeObjectStream;
	private String IaAndNick;
	private DefaultListModel<String> dlmUser;
	private List<ServerHelper> listClient;
	private ServerView sv;
	private JScrollPane jspChatDisplay;
	private ServerHelper.DlmVO dv;
	
	public ServerHelper(Socket client, JTextArea jtaChatDisplay, ServerView sv, 
			List<ServerHelper> listClient, JScrollPane jspChatDisplay,
			DefaultListModel<String> dlmUser) {
		
		listClient.add(this);
		this.dlmUser = dlmUser;
		ia = client.getInetAddress();
		
		this.client = client;
		this.jtaChatDisplay = jtaChatDisplay;
		this.sv = sv;
		this.listClient = listClient;
		this.jspChatDisplay = jspChatDisplay;
		
		
		try {
			readStream = new DataInputStream(client.getInputStream());
			writeStream = new DataOutputStream(client.getOutputStream());
			
			writeObjectStream = new ObjectOutputStream(client.getOutputStream());
			
			nick = readStream.readUTF();

			broadcast(nick+"님("+ia.toString()+")이 접속하였습니다.");
			jtaChatDisplay.append(nick+"님("+ia.toString()+"이 접속하였습니다.\n");
			jspChatDisplay.getVerticalScrollBar().setValue(
					jspChatDisplay.getVerticalScrollBar().getMaximum());
			
			IaAndNick = ia.toString()+"@"+nick;
			dlmUser.addElement(IaAndNick);
			dv = this.new DlmVO(dlmUser);
			broadcast(dv);
			System.out.println("dlmUser브로드////////////////"+dlmUser.toString());
			
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
				
				dlmUser.removeElement(IaAndNick);
				System.out.println("dlmUser빼기////////////////"+dlmUser.toString());
				broadcast(dv);
				
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
	
	public synchronized void broadcast(DlmVO dv) {
		if (writeObjectStream != null) {
			try {
				ServerHelper tempSh = null;
				for (int i=0; i<listClient.size(); i++) {
					tempSh = listClient.get(i);
					tempSh.writeObjectStream.writeObject(dv);
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
	
	public class DlmVO implements Serializable {

		private static final long serialVersionUID = 6978768523394300105L;
		private DefaultListModel<String> dlmUser;
		
		public DlmVO(DefaultListModel<String> dlmUser) {
			this.dlmUser = dlmUser;
		}

		public DefaultListModel<String> getDlmUser() {
			return dlmUser;
		}
		public void setDlmUser(DefaultListModel<String> dlmUser) {
			this.dlmUser = dlmUser;
		}
	}
}
