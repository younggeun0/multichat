package kr.co.sist.multichat.client.view;

import java.awt.BorderLayout;
import java.net.InetAddress;
import java.net.Socket;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.border.TitledBorder;

import kr.co.sist.multichat.client.evt.ClientSelectUserEvt;
import kr.co.sist.multichat.server.helper.ServerHelper;

public class ClientSelectUserView extends JDialog {

	private JList<String> listUser;
	private DefaultListModel<String> dlmUser;
	
	public ClientSelectUserView(ClientChatView ccv, Socket client) {
		super(ccv, "������ ���", true);
		
		dlmUser = new DefaultListModel<>();
		
		listUser = new JList<String>(dlmUser);
		listUser.setBorder(new TitledBorder("������ ���"));
		
		add(BorderLayout.CENTER, listUser);
		
		ClientSelectUserEvt csue = new ClientSelectUserEvt(this, client, dlmUser);
		addWindowListener(csue);
		
		setBounds(600, 400, 200, 300);
		setResizable(false);
		setVisible(true);
	}
}
