package centralizedSC;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class MasterWork implements Runnable {

	Master master;
	Socket clientSocket;
	
	public MasterWork(Master m,Socket s){
		master=m;
		clientSocket=s;
	}
	
	@Override
	public void run() {
		BufferedReader is;
		try {
			is = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			String response=is.readLine();
			String[] parts=response.split(":");
			String action=parts[0];
			String[] contents=parts[1].split("/");
			
			
			PrintWriter os=new PrintWriter(clientSocket.getOutputStream());
			switch(action){
				case "WRITE":
					System.out.println("MasterWork:WRITE");
					String variable=contents[0];
					String value=contents[1];
					String clientId=contents[2];
					String clientIdPort=contents[3];
					master.invalidateVariables(variable,Integer.parseInt(clientIdPort));
					master.overwriteOwner(variable, clientIdPort);
					String serverRes="OK";
					os.println(serverRes);
					os.flush();
					break;
				case "READ":
					System.out.println("MasterWork:READ");
					String variable2=contents[0];
					String clientId2=contents[1];
					String clientIdPort2=contents[2];
					master.forwardRead(variable2, clientIdPort2);
					//TODO better wait here
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (InvalidateException e) {
			e.printStackTrace();
		} catch (ReadException e) {
			e.printStackTrace();
		}
		
	}

	

}
