package kr.co.sist.multichat.client.evt;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import kr.co.sist.multichat.client.view.ClientSelectTeamView;

public class ClientSelectTeamEvt implements ActionListener {

	private ClientSelectTeamView cstv;
	
	public ClientSelectTeamEvt(ClientSelectTeamView cstv) {
		this.cstv = cstv;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (cstv.getJrTeam1().isSelected()) {
		} else if (cstv.getJrTeam2().isSelected()) {
		} else if (cstv.getJrTeam3().isSelected()) {
		} else if (cstv.getJrTeam4().isSelected()) {
		}
	}

}
