package kr.co.sist.multichat.server.evt;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import kr.co.sist.multichat.server.helper.ServerHelper;
import kr.co.sist.multichat.server.view.ServerView;

public class ServerThread extends Thread {

	private ServerView sv;
	private ServerSocket server;
	private ServerSocket serverObj;
	private Socket client;
	private Socket clientObj;
	private JTextArea jtaChatDisplay;
	private List<ServerHelper> listSh;
	private ArrayList<String> arrListUser;
	private JScrollPane jspChatDisplay;
	
	public ServerThread(ServerSocket server, ServerSocket serverObj, JTextArea jtaChatDisplay, 
			List<ServerHelper> listSh, ServerView sv, JScrollPane jspChatDisplay) {
		this.server = server;
		this.serverObj = serverObj;
		this.jtaChatDisplay = jtaChatDisplay;
		this.listSh = listSh;
		this.sv = sv;
		this.jspChatDisplay = jspChatDisplay;
		
		arrListUser = new ArrayList<String>();
	}
	
	@Override
	public void run() {
		
		try {
			ServerHelper sh = null;
			
			while(true) {
				client = server.accept();
				clientObj = serverObj.accept();
				
				sh = new ServerHelper(client, clientObj, jtaChatDisplay, sv, listSh
						, jspChatDisplay, arrListUser);
				sh.start();
				sh.broadcast();
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
