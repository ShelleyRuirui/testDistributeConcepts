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
			String variable=contents[0];
			String value=contents[1];
			String clientId=contents[2];
			String clientIdPort=contents[3];
			
			PrintWriter os=new PrintWriter(clientSocket.getOutputStream());
			switch(action){
				case "WRITE":
					System.out.println("MasterWork:WRITE");
					master.invalidateVariables(variable,Integer.parseInt(clientIdPort));
					master.overwriteOwner(variable, clientIdPort);
					String serverRes="OK";
					os.println(serverRes);
					os.flush();
					break;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	

}
