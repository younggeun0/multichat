package kr.co.sist.multichat.client.view;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;

import kr.co.sist.multichat.client.evt.ClientSelectTeamEvt;

public class ClientSelectTeamView extends JFrame {

	private JRadioButton jrTeam1, jrTeam2, jrTeam3, jrTeam4;
	private JButton jbConnect;
	
	public ClientSelectTeamView() {
		
		JLabel jlSelectTeam = new JLabel("채팅방 선택");

		jrTeam1 = new JRadioButton("1조");
		jrTeam1.setSelected(true);
		jrTeam2 = new JRadioButton("2조");
		jrTeam3 = new JRadioButton("3조");
		jrTeam4 = new JRadioButton("4조");
		
		ButtonGroup bg = new ButtonGroup();
		bg.add(jrTeam1);
		bg.add(jrTeam2);
		bg.add(jrTeam3);
		bg.add(jrTeam4);
		
		jbConnect = new JButton("접속");
		
		setLayout(null);
		
		jlSelectTeam.setBounds(30, 20, 140, 30);
		jrTeam1.setBounds(30, 50, 150, 30);
		jrTeam2.setBounds(30, 90, 150, 30);
		jrTeam3.setBounds(30, 130, 150, 30);
		jrTeam4.setBounds(30, 170, 150, 30);
		jbConnect.setBounds(95, 210, 60, 30);
		
		add(jlSelectTeam);
		add(jrTeam1);
		add(jrTeam2);
		add(jrTeam3);
		add(jrTeam4);
		add(jbConnect);
		
		ClientSelectTeamEvt cste = new ClientSelectTeamEvt(this);
		jbConnect.addActionListener(cste);
		
		setBounds(400, 300, 250, 300);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public JRadioButton getJrTeam1() {
		return jrTeam1;
	}
	public void setJrTeam1(JRadioButton jrTeam1) {
		this.jrTeam1 = jrTeam1;
	}
	public JRadioButton getJrTeam2() {
		return jrTeam2;
	}
	public void setJrTeam2(JRadioButton jrTeam2) {
		this.jrTeam2 = jrTeam2;
	}
	public JRadioButton getJrTeam3() {
		return jrTeam3;
	}
	public void setJrTeam3(JRadioButton jrTeam3) {
		this.jrTeam3 = jrTeam3;
	}
	public JRadioButton getJrTeam4() {
		return jrTeam4;
	}
	public void setJrTeam4(JRadioButton jrTeam4) {
		this.jrTeam4 = jrTeam4;
	}
	public JButton getJbConnect() {
		return jbConnect;
	}
	public void setJbConnect(JButton jbConnect) {
		this.jbConnect = jbConnect;
	}
}
