package kr.co.sist.multichat.server.evt;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import kr.co.sist.multichat.server.helper.ServerHelper;
import kr.co.sist.multichat.server.view.ServerView;

public class ServerEvt extends WindowAdapter implements ActionListener {

	private ServerView sv;	
	private ServerSocket server1, server2, server3, server4;
	private ServerSocket serverObj1, serverObj2, serverObj3, serverObj4; // ObjStream을 위한 서버소켓
	private static List<ServerHelper> listSh1, listSh2, listSh3, listSh4;
	private ServerThread serverThread1, serverThread2, serverThread3, serverThread4;
	
	public ServerEvt(ServerView sv) {
		this.sv = sv;
		
		listSh1 = new ArrayList<ServerHelper>();
		listSh2 = new ArrayList<ServerHelper>();
		listSh3 = new ArrayList<ServerHelper>();
		listSh4 = new ArrayList<ServerHelper>();
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
		
		server1 = new ServerSocket(6001);	serverObj1 = new ServerSocket(7001);
		server2 = new ServerSocket(6002);	serverObj2 = new ServerSocket(7002);
		server3 = new ServerSocket(6003);	serverObj3 = new ServerSocket(7003);
		server4 = new ServerSocket(6004);	serverObj4 = new ServerSocket(7004);
		
		serverThread1 = new ServerThread(server1, serverObj1, sv.getJtaChatDisplay1(), listSh1, sv, sv.getJspChatDisplay1());
		serverThread2 = new ServerThread(server2, serverObj2, sv.getJtaChatDisplay2(), listSh2, sv, sv.getJspChatDisplay2());
		serverThread3 = new ServerThread(server3, serverObj3, sv.getJtaChatDisplay3(), listSh3, sv, sv.getJspChatDisplay3());
		serverThread4 = new ServerThread(server4, serverObj4, sv.getJtaChatDisplay4(), listSh4, sv, sv.getJspChatDisplay4());
		
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
