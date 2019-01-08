package sending_obj_test;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ServerTest {
	
	public ServerTest() throws IOException {
		
		ServerSocket ss = new ServerSocket(40000);
		System.out.println("서버소켓 생성");
		Socket client = ss.accept();
		System.out.println("클라이언트 받음");
		ArrayList<String> list = new ArrayList<String>();
		ObjectOutputStream oos = null;
		
		try {
			oos = new ObjectOutputStream(client.getOutputStream());
			
			list.add("test1");
			list.add("test2");
			
			oos.writeObject(list);
			oos.flush();
			System.out.println("list전달");
		} finally {
			if (oos != null) { oos.close(); }
		}
	}
	
	public static void main(String[] args) {
		try {
			new ServerTest();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
