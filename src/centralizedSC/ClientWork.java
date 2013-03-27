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
					break;
				case "READ":
					String[] vars=variable.split("/");
					String var=vars[0];
					String clientPortId=vars[1];
					String value=client.getLocalValue(var);
					String forwardStr="READRESULT:"+var+"/"+value;
					Client.log("Client receive READ:"+forwardStr+"  "+clientPortId);
					Socket s=new Socket("127.0.0.1",Integer.parseInt(clientPortId));
					PrintWriter os2=new PrintWriter(s.getOutputStream());
					os2.println(forwardStr);
					os2.flush();
					s.close();
					break;
				case "READRESULT":
					String var2=variable.split("/")[0];
					String value2=variable.split("/")[1];
					Client.log("Client receive READRESULT:"+var2+" "+value2);
					client.notifyReadVar(var2,value2);
					break;
			}
		} catch (IOException | ReadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
