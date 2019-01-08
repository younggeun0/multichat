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
	private Socket clientObj;
	private JTextArea jtaChatDisplay;
	private DataInputStream readStream;
	private DataOutputStream writeStream;
	private ObjectOutputStream writeObjectStream;
	private String IaAndNick;
	private List<ServerHelper> listSh;
	private ServerView sv;
	private JScrollPane jspChatDisplay;
	private ArrayList<String> arrListUser;
	
	public ServerHelper(Socket client, Socket clientObj, JTextArea jtaChatDisplay, ServerView sv, 
			List<ServerHelper> listSh, JScrollPane jspChatDisplay,
			ArrayList<String> arrListUser) {
		
		listSh.add(this);
		ia = client.getInetAddress();
		
		this.client = client;
		this.clientObj = clientObj;
		this.jtaChatDisplay = jtaChatDisplay;
		this.sv = sv;
		this.listSh = listSh;
		this.jspChatDisplay = jspChatDisplay;
		this.arrListUser = arrListUser;
		
		
		try {
			readStream = new DataInputStream(client.getInputStream());
			writeStream = new DataOutputStream(client.getOutputStream());
			
			writeObjectStream = new ObjectOutputStream(clientObj.getOutputStream());
			
			nick = readStream.readUTF();
			
			IaAndNick = ia.toString()+"@"+nick;
			arrListUser.add(IaAndNick);
			broadcast(arrListUser);

			broadcast(nick+"님("+ia.toString()+")이 접속하였습니다.");
			jtaChatDisplay.append(nick+"님("+ia.toString()+"이 접속하였습니다.\n");
			jspChatDisplay.getVerticalScrollBar().setValue(
					jspChatDisplay.getVerticalScrollBar().getMaximum());
			
			
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
				for (int i=0; i<listSh.size(); i++) {
					tempSh = listSh.get(i);
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
				for (int i=0; i<listSh.size(); i++) {
					tempSh = listSh.get(i);
					System.out.println("server broadcast : "+arrListUser);
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
