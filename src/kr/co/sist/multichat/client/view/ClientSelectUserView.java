package kr.co.sist.multichat.client.view;

import java.awt.BorderLayout;

import javax.swing.DefaultListModel;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.border.TitledBorder;

public class ClientSelectUserView extends JDialog {

	private JList<String> listUser;
	private DefaultListModel<String> dlmUser;
	
	public ClientSelectUserView(ClientChatView ccv) {
		super(ccv, "立加磊 格废", true);
		
		dlmUser = new DefaultListModel<String>();
		listUser = new JList<String>(dlmUser);
		listUser.setBorder(new TitledBorder("立加磊 格废"));
		
		add(BorderLayout.CENTER, listUser);
		
		setBounds(600, 400, 200, 300);
		setResizable(false);
		setVisible(true);
	}
}
