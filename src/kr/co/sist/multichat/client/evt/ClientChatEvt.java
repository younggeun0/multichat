package kr.co.sist.multichat.client.evt;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import kr.co.sist.multichat.client.view.ClientChatView;

public class ClientChatEvt extends Thread implements ActionListener {
	
	private ClientChatView ccv;
	private int portNum;
	private Socket client;
	private DataInputStream readStream;
	private DataOutputStream writeStream;

	public ClientChatEvt(ClientChatView ccv, int portNum) {
		this.ccv = ccv;
		this.portNum = portNum;
		System.out.println(portNum);
	}
	
	@Override
	public void run() {
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == ccv.getJbConnect()) {
			try {
				client = new Socket("localhost", portNum);
				
				readStream = new DataInputStream(client.getInputStream());
				writeStream = new DataOutputStream(client.getOutputStream());
				
				
			} catch (UnknownHostException uhe) {
				uhe.printStackTrace();
			} catch (IOException ie) {
				ie.printStackTrace();
			}
		}
		if (e.getSource() == ccv.getJbCapture()) {
			
		}
		if (e.getSource() == ccv.getJbClose()) {
			
		}
		if (e.getSource() == ccv.getJbUser()) {
			
		}
		if (e.getSource() == ccv.getJtfTalk()) {
			
		}
	}
}
