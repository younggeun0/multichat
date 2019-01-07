package kr.co.sist.multichat.server.evt;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultListModel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import kr.co.sist.multichat.server.helper.ServerHelper;
import kr.co.sist.multichat.server.helper.ServerObjectHelper;
import kr.co.sist.multichat.server.view.ServerView;

public class ServerThread extends Thread {

	private ServerView sv;
	private ServerSocket server;
	private Socket client;
	private JTextArea jtaChatDisplay;
	private List<ServerHelper> listSh;
	private List<ServerObjectHelper> listSoh;
	private ArrayList<String> arrListUser;
	private JScrollPane jspChatDisplay;
	
	public ServerThread(ServerSocket server, JTextArea jtaChatDisplay, 
			List<ServerHelper> listSh, ServerView sv, JScrollPane jspChatDisplay,
			List<ServerObjectHelper> listSoh) {
		this.server = server;
		this.jtaChatDisplay = jtaChatDisplay;
		this.listSh = listSh;
		this.sv = sv;
		this.jspChatDisplay = jspChatDisplay;
		this.listSoh = listSoh;
		
		arrListUser = new ArrayList<String>();
	}
	
	@Override
	public void run() {
		
		try {
			ServerHelper sh = null;
			ServerObjectHelper soh = null;
			
			while(true) {
				client = server.accept();
				sh = new ServerHelper(client, jtaChatDisplay, sv, listSh
						, jspChatDisplay, arrListUser);
				sh.start();
				System.out.println("���� �޽��� ���� ������ ����");
				
				// ServerObjectHelper�� thread ����
				soh = new ServerObjectHelper(client, arrListUser, listSoh);
				soh.start();
				System.out.println("���� ��������Ʈ ���� ������ ����");
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (client != null) {
				try {
					client.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
