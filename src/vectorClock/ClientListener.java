package vectorClock;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientListener implements Runnable {

	SingleClient client;
	int port;
	
	public ClientListener(SingleClient c,int port){
		client=c;
		this.port=port;
	}
	@Override
	public void run() {
		try{
			ServerSocket clientServer = new ServerSocket(port);
			BufferedReader is;
			while(true){
				Socket socket = clientServer.accept();
				is = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				String content=is.readLine();
				client.syncWithOthers(content);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
