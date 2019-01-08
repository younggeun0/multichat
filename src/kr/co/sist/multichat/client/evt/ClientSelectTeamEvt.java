package kr.co.sist.multichat.client.evt;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import kr.co.sist.multichat.client.view.ClientChatView;
import kr.co.sist.multichat.client.view.ClientSelectTeamView;

public class ClientSelectTeamEvt implements ActionListener {

	private ClientSelectTeamView cstv;
	
	public ClientSelectTeamEvt(ClientSelectTeamView cstv) {
		this.cstv = cstv;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		int portNum = 0;
		int objPortNum = 0;
		
		if (cstv.getJrTeam1().isSelected()) {
			portNum = 6001;
			objPortNum = 7001;
		} else if (cstv.getJrTeam2().isSelected()) {
			portNum = 6002;
			objPortNum = 7002;
		} else if (cstv.getJrTeam3().isSelected()) {
			portNum = 6003;
			objPortNum = 7003;
		} else if (cstv.getJrTeam4().isSelected()) {
			portNum = 6004;
			objPortNum = 7004;
		}
		
		new ClientChatView(portNum, objPortNum);
		cstv.dispose();
	}

}
