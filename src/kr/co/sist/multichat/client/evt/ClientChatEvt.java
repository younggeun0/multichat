package kr.co.sist.multichat.client.evt;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;
import javax.swing.JTextField;

import kr.co.sist.multichat.client.view.ClientChatView;
import kr.co.sist.multichat.client.view.ClientSelectUserView;

public class ClientChatEvt extends WindowAdapter implements ActionListener, Runnable {
	
	private String nick;
	private ClientChatView ccv;
	private int portNum;
	private Socket client;
	private DataInputStream readStream;
	private DataOutputStream writeStream;
	private Thread readThread;

	public ClientChatEvt(ClientChatView ccv, int portNum) {
		this.ccv = ccv;
		this.portNum = portNum;
	}
	
	@Override
	public void run() {
		if (readStream != null) {
			try {
				String revMsg = "";
				while (true) {
					revMsg = readStream.readUTF();
					ccv.getJtaChatDisplay().append(revMsg+"\n");
					ccv.getJspChatDisplay().getVerticalScrollBar().setValue(ccv.getJspChatDisplay().getVerticalScrollBar().getMaximum());
				}
			} catch (IOException e) {
				JOptionPane.showMessageDialog(ccv, "서버와의 접속이 끊겼습니다.");
				ccv.getJtaChatDisplay().append("서버와의 접속이 끊겼습니다.\n");
				ccv.getJspChatDisplay().getVerticalScrollBar().setValue(ccv.getJspChatDisplay().getVerticalScrollBar().getMaximum());
				e.printStackTrace();
			}
		}
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == ccv.getJbConnect()) {
			try {
				nick = ccv.getJtfNick().getText().trim();
				if(nick.isEmpty()) {
					JOptionPane.showMessageDialog(ccv, "대화명을 입력해주세요.");
					ccv.getJtfNick().requestFocus();
					return;
				}
				client = new Socket("localhost", portNum);
				ccv.getJtaChatDisplay().setText("서버와 연결되었습니다.\n");
				readStream = new DataInputStream(client.getInputStream());
				writeStream = new DataOutputStream(client.getOutputStream());
				
				writeStream.writeUTF(nick);
				writeStream.flush();
				
				readThread = new Thread(this);
				readThread.start();
				
			} catch (UnknownHostException uhe) {
				uhe.printStackTrace();
			} catch (IOException ie) {
				ie.printStackTrace();
			}
		}
		if (e.getSource() == ccv.getJbCapture()) {
			try {
				if (!ccv.getJtaChatDisplay().getText().isEmpty()) {
					capture();
				} else {
					JOptionPane.showMessageDialog(ccv, "저장할 내용이 없습니다.");
				}
			} catch (IOException ie) {
				JOptionPane.showMessageDialog(ccv, "대화내용 저장에 실패했습니다.");
				ie.printStackTrace();
			}
		}
		if (e.getSource() == ccv.getJbClose()) {
			ccv.dispose();
		}
		if (e.getSource() == ccv.getJbUser()) {
			new ClientSelectUserView(ccv);
		}
		if (e.getSource() == ccv.getJtfTalk()) {
			JTextField jtf = ccv.getJtfTalk();
			if(!jtf.getText().isEmpty()) {
				try {
					sendMsg("["+nick+"] : "+jtf.getText());
					jtf.setText("");
				} catch (IOException ie) {
					JOptionPane.showMessageDialog(ccv, "메시지 전송에 실패했습니다.");
					ie.printStackTrace();
				}
			}
		}
	}
	
	public void capture() throws IOException {
		
		switch (JOptionPane.showConfirmDialog(ccv, "대화 내용을 저장하시겠습니까?")) {
		case JOptionPane.OK_OPTION:
			
			File saveDir = new File("C:/dev/multichat");
			saveDir.mkdirs();
			File saveFile = new File(saveDir.getAbsolutePath()+"/java_multi_chat["
					+System.currentTimeMillis()+"].dat");
			
			BufferedWriter bw = null;
			try {
				bw = new BufferedWriter(new FileWriter(saveFile));
				bw.write(ccv.getJtaChatDisplay().getText());
				bw.flush();
				JOptionPane.showMessageDialog(ccv, saveDir+"에 대화내용이 기록되었습니다.");
			} finally {
				try {
					if(bw != null) {
						bw.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void sendMsg(String msg) throws IOException {
		if (writeStream != null) {
			writeStream.writeUTF(msg);
			writeStream.flush();
		}
	}
	
	@Override
	public void windowClosing(WindowEvent e) {
		ccv.dispose();
	}
	@Override
	public void windowClosed(WindowEvent e) {
		
		try {
			if (readStream != null) {
				readStream.close();
			}
			if (writeStream != null) {
				writeStream.close();
			}
			if (client != null) {
				client.close();
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			System.exit(0);
		}
	}
}
