package centralizedSC;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class Client {

	private String clientId="";
	private int idPort=-1;
	private int serverPort=-1;
	private Map<String,String> currentValues=new HashMap<String,String>();
	static Logger logger=Logger.getLogger(Client.class.getName());
	private Lock lock=new ReentrantLock();
	private Map<String,Condition> conditions=new HashMap<String,Condition>();
	
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
				socket.close();
				throw new WriteException();
			}else{
				currentValues.put(variable, value);
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
		currentValues.remove(variable);
	}
	
	public String readValue(String variable) throws UnknownHostException, IOException, ReadException, InterruptedException{
		String result="";
		
		String localValue=currentValues.get(variable);
		if(localValue!=null)
			return localValue;
		//TODO every request needs to reconnect vs hold the connection?
		Socket socket=new Socket("127.0.0.1",serverPort);
		String request="READ:"+variable+"/"+clientId+"/"+idPort;
		Client.log(clientId+":Before send read");
		PrintWriter os=new PrintWriter(socket.getOutputStream());
		os.println(request);
		os.flush();
		socket.close();
		
		waitForReadVar(variable);
		result=currentValues.get(variable);
		Client.log("Wakenup:"+result);
		return result;
	}
	
	public static void log(String msg){
		logger.info(msg);
	}
	
	public String getLocalValue(String var){
		return currentValues.get(var);
	}
	
	public void putLocalValue(String var,String val){
		currentValues.put(var, val);
	}
	
	private void waitForReadVar(String var) throws InterruptedException{
		lock.lock();
		Condition c=conditions.get(var);
		if(c!=null)
			c.await();
		else{
			c=lock.newCondition();
			conditions.put(var, c);
			c.await();
		}
		lock.unlock();
	}
	
	public void notifyReadVar(String var,String val) throws ReadException{
		lock.lock();
		Condition c=conditions.get(var);
		currentValues.put(var, val);
		if(c==null)
			throw new ReadException();
		c.signal();
		lock.unlock();
	}
	
	
}
