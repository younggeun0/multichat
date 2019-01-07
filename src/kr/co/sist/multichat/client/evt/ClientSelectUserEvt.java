package kr.co.sist.multichat.client.evt;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;

import kr.co.sist.multichat.client.view.ClientSelectUserView;

public class ClientSelectUserEvt extends WindowAdapter {

	private ClientSelectUserView csv;
	
	public ClientSelectUserEvt(ClientSelectUserView csv) {
		this.csv = csv;
	}
	
	@Override
	public void windowClosed(WindowEvent e) {
		csv.dispose();
	}

}
