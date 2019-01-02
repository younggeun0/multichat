package kr.co.sist.multichat.client.view;

import java.awt.BorderLayout;

import javax.swing.DefaultListModel;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.border.TitledBorder;

public class ClientSelectUserView extends JDialog {

	private JList<String> listUser;
	private DefaultListModel<String> dlmUser;
	
	public ClientSelectUserView() {
		
		dlmUser = new DefaultListModel<String>();
		listUser = new JList<String>(dlmUser);
		listUser.setBorder(new TitledBorder("접속자 목록"));
		
		add(BorderLayout.CENTER, listUser);
		
		setBounds(600, 400, 200, 300);
		setVisible(true);
	}
}
