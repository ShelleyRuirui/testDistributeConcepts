package centralizedSC;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class Client {

	private String clientId="";
	private int idPort=-1;
	private int serverPort=-1;
	private Map<String,String> currentValues=new HashMap<String,String>();
	static Logger logger=Logger.getLogger(Client.class.getName());
	
	static{
		PropertyConfigurator.configure("D:\\Programmes\\eclipseEE\\Juno\\workspace\\testDistributeConcepts\\src\\centralizedSC\\logClient.properties");
	}
	
	public Client(String id,int port,int sPort){
		clientId=id;
		idPort=port;
		serverPort=sPort;
		init();
	}
	
	public void init(){
		ClientSocketListener csl=new ClientSocketListener(this, idPort);
		Thread t=new Thread(csl);
		t.start();
	}

	public void writeValue(String variable,String value) throws WriteException{
		try {
			Socket socket=new Socket("127.0.0.1",serverPort);
			String request="WRITE:"+variable+"/"+value+"/"+clientId+"/"+idPort;
			Client.log(clientId+":Before send");
			PrintWriter os=new PrintWriter(socket.getOutputStream());
			os.println(request);
			os.flush();
			
			BufferedReader is=new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String response=is.readLine();
			Client.log(response);
			if(!response.equals("OK")){
				throw new WriteException();
			}else{
				Client.log(clientId+":Success!");
			}
			socket.close();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void invalidate(String variable){
		Client.log(clientId+":Invalidate--"+variable);
	}
	
	public static void log(String msg){
		logger.info(msg);
	}
	
	
}
