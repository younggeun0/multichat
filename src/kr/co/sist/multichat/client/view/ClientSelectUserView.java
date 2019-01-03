package kr.co.sist.multichat.client.view;

import java.awt.BorderLayout;
import java.net.InetAddress;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.border.TitledBorder;

import kr.co.sist.multichat.server.helper.ServerHelper;

public class ClientSelectUserView extends JDialog {

	private JList<String> listUser;
	private DefaultListModel<String> dlmUser;
	
	public ClientSelectUserView(ClientChatView ccv, DefaultListModel<String> dlmUser) {
		super(ccv, "立加磊 格废", true);
		
		this.dlmUser = dlmUser;
		listUser = new JList<String>(dlmUser);
		listUser.setBorder(new TitledBorder("立加磊 格废"));
		
		add(BorderLayout.CENTER, listUser);
		
		
		
		setBounds(600, 400, 200, 300);
		setResizable(false);
		setVisible(true);
	}
}
