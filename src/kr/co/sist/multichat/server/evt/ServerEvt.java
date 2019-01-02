package kr.co.sist.multichat.server.evt;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import kr.co.sist.multichat.server.helper.ServerHelper;
import kr.co.sist.multichat.server.view.ServerView;

public class ServerEvt extends WindowAdapter implements ActionListener {

	private ServerView sv;
	private ServerSocket server1, server2, server3, server4;
	private List<ServerHelper> listServer1, listServer2, listServer3, listServer4;
	private ServerThread serverThread1, serverThread2, serverThread3, serverThread4;
	
	public ServerEvt(ServerView sv) {
		this.sv = sv;
		listServer1 = new ArrayList<ServerHelper>();
		listServer2 = new ArrayList<ServerHelper>();
		listServer3 = new ArrayList<ServerHelper>();
		listServer4 = new ArrayList<ServerHelper>();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == sv.getJbStart()) {
			try {
				if (server1 == null & server2 == null && server3 == null && server4 == null) {
					startServers();
				} else {
					JOptionPane.showMessageDialog(sv, "이미 가동중입니다.");
				}
			} catch (IOException ie) {
				JOptionPane.showMessageDialog(sv, "서버 접속 실패");
				ie.printStackTrace();
			}
		}
		if (e.getSource() == sv.getJbClose()) {
			sv.dispose();
		}
	}
	
	public void startServers() throws IOException {
		
		server1 = new ServerSocket(6001);
		server2 = new ServerSocket(6002);
		server3 = new ServerSocket(6003);
		server4 = new ServerSocket(6004);
		
		serverThread1 = new ServerThread(server1, sv.getJtaChatDisplay1(), listServer1, sv, sv.getJspChatDisplay1());
		serverThread2 = new ServerThread(server2, sv.getJtaChatDisplay2(), listServer2, sv, sv.getJspChatDisplay2());
		serverThread3 = new ServerThread(server3, sv.getJtaChatDisplay3(), listServer3, sv, sv.getJspChatDisplay3());
		serverThread4 = new ServerThread(server4, sv.getJtaChatDisplay4(), listServer4, sv, sv.getJspChatDisplay4());
		
		serverThread1.start();
		serverThread2.start();
		serverThread3.start();
		serverThread4.start();
		
		sv.getJtaChatDisplay1().setText("1조 채팅서버 가동 시작...\n");
		sv.getJtaChatDisplay2().setText("2조 채팅서버 가동 시작...\n");
		sv.getJtaChatDisplay3().setText("3조 채팅서버 가동 시작...\n");
		sv.getJtaChatDisplay4().setText("4조 채팅서버 가동 시작...\n");
	}
	
	@Override
	public void windowClosing(WindowEvent e) {
		sv.dispose();
	}
	@Override
	public void windowClosed(WindowEvent e) {
		try {
			if (server1 != null) {
				server1.close();
			}
			if (server2 != null) {
				server2.close();
			}
			if (server3 != null) {
				server3.close();
			}
			if (server4 != null) {
				server4.close();
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			System.exit(0);
		}
	}
}
