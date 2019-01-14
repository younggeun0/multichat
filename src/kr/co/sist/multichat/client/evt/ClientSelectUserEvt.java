package kr.co.sist.multichat.client.evt;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

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
