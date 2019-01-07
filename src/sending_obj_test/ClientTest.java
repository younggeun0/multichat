package sending_obj_test;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;

public class ClientTest {
	
	public ClientTest() throws IOException, ClassNotFoundException {
		
		Socket client = new Socket("localhost", 40000);
		System.out.println("클라이언트 서버접속");
		System.out.println("list전달 완료, 출력");

		ObjectInputStream ois = null;
		try {
			ois = new ObjectInputStream(client.getInputStream());
		
			ArrayList<String> list = (ArrayList<String>)ois.readObject();
			
			for(String str : list) {
				System.out.println(str);
			}
		} finally {
			// TODO: handle finally clause
		}
	}
	
	public static void main(String[] args) {
		try {
			new ClientTest();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
