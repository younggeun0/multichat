package kr.co.sist.multichat.server.evt;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
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
	private JScrollPane jspChatDisplay;
	private List<ServerHelper> listSh;
	
	public ServerThread(ServerSocket server, ServerSocket serverObj, JTextArea jtaChatDisplay, 
		ServerView sv, JScrollPane jspChatDisplay) {
		this.sv = sv;
		this.server = server;
		this.serverObj = serverObj;
		this.jtaChatDisplay = jtaChatDisplay;
		this.jspChatDisplay = jspChatDisplay;
		
		listSh = new ArrayList<ServerHelper>();
	}
	
	@Override
	public void run() {
		
		try {
			ServerHelper sh = null;
			
			while(true) {
				client = server.accept();
				clientObj = serverObj.accept();
				
				sh = new ServerHelper(client, clientObj, jtaChatDisplay, sv, listSh, jspChatDisplay);
				sh.start();
			}
			
		} catch (SocketException se) {
			System.out.println("Å¬¶óÀÌ¾ðÆ®¿Í ¿¬°á ²÷±è");
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
