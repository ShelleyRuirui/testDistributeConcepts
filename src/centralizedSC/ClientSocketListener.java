package centralizedSC;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/*
 * This class is mainly a standby thread to handle requests from master to assist its work
 */
public class ClientSocketListener implements Runnable {

	private int idPort;
	Client client;

	public ClientSocketListener(Client c, int port) {
		client = c;
		idPort = port;
	}

	@Override
	public void run() {
		try {
			@SuppressWarnings("resource")
			ServerSocket clientServer = new ServerSocket(idPort);
			while (true) {
				Socket socket = clientServer.accept();
				ClientWork cw = new ClientWork(client, socket);// This new thread actually did the work
				Thread t=new Thread(cw);
				t.start();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
