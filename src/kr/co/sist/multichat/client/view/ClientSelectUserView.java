package kr.co.sist.multichat.client.view;

import java.awt.BorderLayout;

import javax.swing.DefaultListModel;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.border.TitledBorder;

import kr.co.sist.multichat.client.evt.ClientSelectUserEvt;

@SuppressWarnings("serial")
public class ClientSelectUserView extends JDialog {

	private JList<String> listUser;
	
	public ClientSelectUserView(ClientChatView ccv, DefaultListModel<String> dlmUser) {
		super(ccv, "������ ���", true);
		
		System.out.println(dlmUser.toString());
		listUser = new JList<String>(dlmUser);
		listUser.setBorder(new TitledBorder("������ ���"));
		
		add(BorderLayout.CENTER, listUser);
		
		ClientSelectUserEvt csue = new ClientSelectUserEvt(this);
		addWindowListener(csue);
		
		setBounds(600, 400, 200, 300);
		setResizable(false);
		setVisible(true);
	}
}
