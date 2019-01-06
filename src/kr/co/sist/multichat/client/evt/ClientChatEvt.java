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
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import kr.co.sist.multichat.client.view.ClientChatView;
import kr.co.sist.multichat.client.view.ClientSelectUserView;
import kr.co.sist.multichat.server.helper.ServerHelper.DlmVO;

public class ClientChatEvt extends WindowAdapter implements ActionListener, Runnable {
	
	private String nick;
	private ClientChatView ccv;
	private int portNum;
	private Socket client;
	private DataInputStream readStream;
	private DataOutputStream writeStream;
	private ObjectInputStream readObjectStream;
	private Thread readThread;
	private ClientChatEvt.ThreadReadDlmUser rdu;
	private boolean connectFlag;
	private DefaultListModel<String> dlmUser;

	public ClientChatEvt(ClientChatView ccv, int portNum) {
		this.ccv = ccv;
		this.portNum = portNum;
		dlmUser = new DefaultListModel<String>();
	}
	
	@Override
	public void run() {
		
		if (readStream != null) {
			try {
				String revMsg = "";

				while (true) {
					revMsg = readStream.readUTF();
					
					ccv.getJtaChatDisplay().append(revMsg+"\n");
					ccv.getJspChatDisplay().getVerticalScrollBar().setValue(
							ccv.getJspChatDisplay().getVerticalScrollBar().getMaximum());
				}
			} catch (IOException e) {
				JOptionPane.showMessageDialog(ccv, "�������� ������ ������ϴ�.");
				ccv.getJtaChatDisplay().append("�������� ������ ������ϴ�.\n");
				ccv.getJspChatDisplay().getVerticalScrollBar().setValue(
						ccv.getJspChatDisplay().getVerticalScrollBar().getMaximum());
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if (e.getSource() == ccv.getJbConnect()) {
			if(!connectFlag) {
				try {
					nick = ccv.getJtfNick().getText().trim();
					if(nick.isEmpty()) {
						JOptionPane.showMessageDialog(ccv, "��ȭ���� �Է����ּ���.");
						ccv.getJtfNick().requestFocus();
						return;
					}
					client = new Socket("localhost", portNum);
					ccv.getJtaChatDisplay().setText("������ ����Ǿ����ϴ�.\n");
					
					readStream = new DataInputStream(client.getInputStream());
					writeStream = new DataOutputStream(client.getOutputStream());

					readObjectStream = new ObjectInputStream(client.getInputStream());
					
					writeStream.writeUTF(nick);
					writeStream.flush();
					
					readThread = new Thread(this);
					rdu = this.new ThreadReadDlmUser();

					readThread.start();
					rdu.start();
					
					connectFlag = !connectFlag;
				} catch (UnknownHostException uhe) {
					uhe.printStackTrace();
				} catch (IOException ie) {
					ie.printStackTrace();
				}
			} else {
				JOptionPane.showMessageDialog(ccv, "�̹� ������ �������Դϴ�.");
			}
		}
		
		if (e.getSource() == ccv.getJbCapture()) {
			try {
				if (!ccv.getJtaChatDisplay().getText().isEmpty()) {
					capture();
				} else {
					JOptionPane.showMessageDialog(ccv, "������ ������ �����ϴ�.");
				}
			} catch (IOException ie) {
				JOptionPane.showMessageDialog(ccv, "��ȭ���� ���忡 �����߽��ϴ�.");
				ie.printStackTrace();
			}
		}
		
		if (e.getSource() == ccv.getJbClose()) {
			ccv.dispose();
		}
		
		if (e.getSource() == ccv.getJbUser()) {
			if (connectFlag) {
				if (dlmUser.size() != 0) {
					new ClientSelectUserView(ccv, dlmUser);
				} else {
					JOptionPane.showMessageDialog(ccv, "������ ������ �����ϴ�.");
				}
			} else {
				JOptionPane.showMessageDialog(ccv, "������ ���� �������ּ���.");
			}
		}
		
		if (e.getSource() == ccv.getJtfTalk()) {
			JTextField jtf = ccv.getJtfTalk();
			if(!jtf.getText().isEmpty()) {
				try {
					sendMsg("["+nick+"] : "+jtf.getText());
					jtf.setText("");
				} catch (IOException ie) {
					JOptionPane.showMessageDialog(ccv, "�޽��� ���ۿ� �����߽��ϴ�.");
					ie.printStackTrace();
				}
			}
		}
	}
	
	public void capture() throws IOException {
		
		switch (JOptionPane.showConfirmDialog(ccv, "��ȭ ������ �����Ͻðڽ��ϱ�?")) {
		
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
				JOptionPane.showMessageDialog(ccv, saveDir+"�� ��ȭ������ ��ϵǾ����ϴ�.");
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
			if (readObjectStream != null) {
				readObjectStream.close();
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
	
	public class ThreadReadDlmUser extends Thread {
		
		@Override
		public void run() {
			if (readObjectStream != null) {
				DlmVO tempDlmVO = null;
				
				while(true) {
					try {
						System.out.println("1111");
						tempDlmVO = (DlmVO) readObjectStream.readObject();
						System.out.println("2222");
						System.out.println("Ŭ���̾�Ʈ�� : " +tempDlmVO.toString());
						dlmUser = tempDlmVO.getDlmUser();
						
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						JOptionPane.showMessageDialog(ccv, "dlm������ �������� ������ ������ϴ�.");
						ccv.getJtaChatDisplay().append("dlm������ �������� ������ ������ϴ�.\n");
						ccv.getJspChatDisplay().getVerticalScrollBar()
								.setValue(ccv.getJspChatDisplay().getVerticalScrollBar().getMaximum());
						e.printStackTrace();
						break;
					}
				}
			}
		}
	}
}
