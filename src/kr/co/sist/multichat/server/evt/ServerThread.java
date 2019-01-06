package kr.co.sist.multichat.server.evt;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultListModel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import kr.co.sist.multichat.server.helper.ServerHelper;
import kr.co.sist.multichat.server.view.ServerView;

public class ServerThread extends Thread {

	private ServerView sv;
	private ServerSocket server;
	private Socket client;
	private JTextArea jtaChatDisplay;
	private List<ServerHelper> listClient;
	private JScrollPane jspChatDisplay;
	private List<String> listUser;
	
	public ServerThread(ServerSocket server, JTextArea jtaChatDisplay, 
			List<ServerHelper> listClient,ServerView sv, JScrollPane jspChatDisplay,
			List<String> listUser) {
		this.server = server;
		this.jtaChatDisplay = jtaChatDisplay;
		this.listClient = listClient;
		this.sv = sv;
		this.jspChatDisplay = jspChatDisplay;
		this.listUser = listUser;
	}
	
	@Override
	public void run() {
		
		try {
			ServerHelper sh = null;
			
			while(true) {
				client = server.accept();
				sh = new ServerHelper(client, jtaChatDisplay, sv, listClient
						, jspChatDisplay, listUser);
				sh.start();
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
