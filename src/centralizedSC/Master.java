package centralizedSC;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class Master {

	private int serverPort = -1;

	// Both store the port id
	private Map<String, Set<String>> copySet = new HashMap<String, Set<String>>();
	private Map<String, String> owners = new HashMap<String, String>();

	public Master(int port) {
		serverPort = port;
	}

	public void init() {
		ServerSocket serverSocket = null;

		try {
			serverSocket = new ServerSocket(serverPort);
			while (true) {
				Socket connection = serverSocket.accept();
				MasterWork mw = new MasterWork(this, connection);
				Thread t = new Thread(mw);
				t.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public synchronized void invalidateVariables(String variable, int clientPort)
			throws InvalidateException {
		// System.out.println("Master:in invalidateVariables");
		// First invalidate the original owner
		String curOwner = owners.get(variable);
		if (curOwner != null && !curOwner.equals(clientPort + "")) {
			invalidateSingle(Integer.parseInt(curOwner), variable);
		}

		Set<String> set = copySet.get(variable);
		if (set == null)
			return;
		else {
			Iterator<String> it = set.iterator();

			// Then invalidate the copy set
			while (it.hasNext()) {
				int currentClientPort = Integer.parseInt(it.next());
				invalidateSingle(currentClientPort, variable);
			}

		}
	}

	private void invalidateSingle(int clientPort, String variable)
			throws InvalidateException {
		Socket socket;
		try {
			socket = new Socket("127.0.0.1", clientPort);
			String request = "INVALIDATE:" + variable;
			PrintWriter os = new PrintWriter(socket.getOutputStream());
			os.println(request);
			os.flush();

			BufferedReader is = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			String response = is.readLine();
			if (!response.equals("OK")) {
				throw new InvalidateException();
			}
			socket.close();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public synchronized void overwriteOwner(String variable, String clientPort) {
		System.out.println("Master:in overwriteOwner:"+variable+"---"+clientPort);
		owners.put(variable, clientPort);
	}

	public static void main(String[] args) {
		Master master = new Master(5000);
		master.init();
	}
}
