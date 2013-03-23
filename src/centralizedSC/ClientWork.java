package centralizedSC;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/*
 * This class mainly did what master expects the client to do
 */
public class ClientWork implements Runnable {
	
	Client client;
	Socket socket;

	public ClientWork(Client c,Socket s){
		client=c;
		socket=s;
	}
	
	public void run() {
		BufferedReader is;
		try {
			is = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String response=is.readLine();
			String[] contents=response.split(":");
			String action=contents[0];
			String variable=contents[1];
			switch(action){
				case "INVALIDATE":
					client.invalidate(variable);  // Call the client's invalidate method
					String handlerResponse="OK";
					PrintWriter os=new PrintWriter(socket.getOutputStream());
					os.println(handlerResponse);
					os.flush();
					socket.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
