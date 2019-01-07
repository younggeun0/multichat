package kr.co.sist.multichat.server.helper;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerObjectHelper extends Thread {

	private Socket client;
	private List<ServerObjectHelper> listSoh;
	private ArrayList<String> arrListUser;
	private ObjectOutputStream writeObjectStream;
	
	public ServerObjectHelper(Socket client, ArrayList<String> arrListUser, List<ServerObjectHelper> listSoh) {

		listSoh.add(this);
		
		this.client = client;
		this.arrListUser = arrListUser;
		this.listSoh = listSoh;
		
		try {
			writeObjectStream = new ObjectOutputStream(client.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
	@Override
	public void run() {
		try {
			if (writeObjectStream != null) {
				while(true) {
					this.sleep(5000);
					if (arrListUser.size() != 0) {
						broadcast(arrListUser);
					}
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			if (writeObjectStream != null) {
				try {
					writeObjectStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public synchronized void broadcast(ArrayList<String> arrListUser) {
		if (writeObjectStream != null) {
			try {
				ServerObjectHelper tempSoh = null;
				for (int i=0; i<listSoh.size(); i++) {
					tempSoh = listSoh.get(i);
					tempSoh.writeObjectStream.writeObject(arrListUser);
					tempSoh.writeObjectStream.flush();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
