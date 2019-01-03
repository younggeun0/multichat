package kr.co.sist.multichat.client.view;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import kr.co.sist.multichat.client.evt.ClientChatEvt;

public class ClientChatView extends JFrame {

	private JButton jbConnect, jbCapture, jbClose, jbUser;
	private JTextArea jtaChatDisplay;
	private JScrollPane jspChatDisplay;
	private JTextField jtfTalk, jtfNick;
	
	public ClientChatView(int portNum) {
		super("채팅 클라이언트");
		
		JLabel jlNick = new JLabel("대화명", JLabel.CENTER);
		JPanel jpNorth = new JPanel();
		
		jbConnect = new JButton("접속");
		jbCapture = new JButton("갈무리");
		jbClose = new JButton("종료");
		jbUser = new JButton("접속자");
		jtaChatDisplay = new JTextArea();
		jtaChatDisplay.setEditable(false);
		jspChatDisplay = new JScrollPane(jtaChatDisplay);
		jtfTalk = new JTextField();
		jtfNick = new JTextField();

		jpNorth.setLayout(new GridLayout(1, 6));
		
		jpNorth.add(jlNick);
		jpNorth.add(jtfNick);
		jpNorth.add(jbConnect);
		jpNorth.add(jbCapture);
		jpNorth.add(jbClose);
		jpNorth.add(jbUser);
		
		add(BorderLayout.NORTH, jpNorth);
		add(BorderLayout.CENTER, jspChatDisplay);
		add(BorderLayout.SOUTH, jtfTalk);
		
		ClientChatEvt cce = new ClientChatEvt(this, portNum);
		jbConnect.addActionListener(cce);
		jbCapture.addActionListener(cce);
		jbClose.addActionListener(cce);
		jbUser.addActionListener(cce);
		jtfTalk.addActionListener(cce);
		
		addWindowListener(cce);
		
		setResizable(false);
		setBounds(400, 300, 500, 300);
		setVisible(true);
	}

	public JButton getJbConnect() {
		return jbConnect;
	}
	public void setJbConnect(JButton jbConnect) {
		this.jbConnect = jbConnect;
	}
	public JButton getJbCapture() {
		return jbCapture;
	}
	public void setJbCapture(JButton jbCapture) {
		this.jbCapture = jbCapture;
	}
	public JButton getJbClose() {
		return jbClose;
	}
	public void setJbClose(JButton jbClose) {
		this.jbClose = jbClose;
	}
	public JButton getJbUser() {
		return jbUser;
	}
	public void setJbUser(JButton jbUser) {
		this.jbUser = jbUser;
	}
	public JTextArea getJtaChatDisplay() {
		return jtaChatDisplay;
	}
	public void setJtaChatDisplay(JTextArea jtaChatDisplay) {
		this.jtaChatDisplay = jtaChatDisplay;
	}
	public JScrollPane getJspChatDisplay() {
		return jspChatDisplay;
	}
	public void setJspChatDisplay(JScrollPane jspChatDisplay) {
		this.jspChatDisplay = jspChatDisplay;
	}
	public JTextField getJtfTalk() {
		return jtfTalk;
	}
	public void setJtfTalk(JTextField jtfTalk) {
		this.jtfTalk = jtfTalk;
	}
	public JTextField getJtfNick() {
		return jtfNick;
	}
	public void setJtfNick(JTextField jtfNick) {
		this.jtfNick = jtfNick;
	}
}
