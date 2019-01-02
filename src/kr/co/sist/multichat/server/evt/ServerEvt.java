package kr.co.sist.multichat.server.evt;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import kr.co.sist.multichat.server.view.ServerView;

public class ServerEvt extends WindowAdapter implements ActionListener {

	private ServerView sv;
	
	public ServerEvt(ServerView sv) {
		this.sv = sv;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == sv.getJbStart()) {
			
		}
		if (e.getSource() == sv.getJbClose()) {
			sv.dispose();
		}
	}
	
	@Override
	public void windowClosing(WindowEvent e) {
		sv.dispose();
	}
}
