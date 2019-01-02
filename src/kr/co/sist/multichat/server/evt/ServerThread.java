package kr.co.sist.multichat.server.evt;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

import javax.swing.JTextArea;

import kr.co.sist.multichat.server.helper.ServerHelper;
import kr.co.sist.multichat.server.view.ServerView;

public class ServerThread extends Thread {

	private ServerView sv;
	private ServerSocket server;
	private Socket client;
	private JTextArea jtaChatDisplay;
	private List<ServerHelper> listClient;
	
	public ServerThread(ServerSocket server, JTextArea jtaChatDisplay, List<ServerHelper> listServer,ServerView sv) {
		this.server = server;
		this.jtaChatDisplay = jtaChatDisplay;
		this.listClient = listServer;
		this.sv = sv;
	}
	
	@Override
	public void run() {
		
		try {
			ServerHelper sh = null;
			
			for(int cnt=1;;cnt++) {
				client = server.accept();
				
				sh = new ServerHelper(client, jtaChatDisplay, cnt, sv, listClient);
				listClient.add(sh);
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
