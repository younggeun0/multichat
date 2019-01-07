package kr.co.sist.multichat.client.evt;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;

import kr.co.sist.multichat.client.view.ClientSelectUserView;

public class ClientSelectUserEvt extends WindowAdapter {

	private Socket client;
	private ClientSelectUserView csv;
	private ObjectInputStream readObjectStream;
	private ClientSelectUserEvt.ThreadReadObject rdu;
	private DefaultListModel<String> dlmUser;
	
	public ClientSelectUserEvt(ClientSelectUserView csv, Socket client, DefaultListModel<String> dlmUser) {
		this.csv = csv;
		this.client = client;
		this.dlmUser = dlmUser;
		
		try {
			readObjectStream = new ObjectInputStream(client.getInputStream());
			
			rdu = this.new ThreadReadObject();
			rdu.start();
			
		} catch (IOException e) {
			JOptionPane.showMessageDialog(csv, "서버 접속 실패");
			e.printStackTrace();
		}
	}
	
	@Override
	public void windowClosed(WindowEvent e) {
		csv.dispose();
	}
	
	public class ThreadReadObject extends Thread {
		@Override
		public void run() {
			
			if (readObjectStream != null) {
				ArrayList<String> arrListUser = null;
				
				while(true) {
					try {
						System.out.println("1111");
						arrListUser = (ArrayList<String>) readObjectStream.readObject();
						System.out.println("2222");
						System.out.println("클라이언트측 : " +arrListUser.toString());
						
						for(int i=0; i<arrListUser.size(); i++) {
							dlmUser.addElement(arrListUser.get(i));
						}
						
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
						break;
					}
				}
			}
		}
	}
}
