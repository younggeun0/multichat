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

public class ClientChatView extends JFrame {

	private JButton jbConnect, jbCapture, jbClose, jbUser;
	private JTextArea jtaChatDisplay;
	private JScrollPane jspChatDisplay;
	private JTextField jtfTalk, jtfNick;
	
	public ClientChatView() {

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
		
		setBounds(400, 300, 500, 300);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
