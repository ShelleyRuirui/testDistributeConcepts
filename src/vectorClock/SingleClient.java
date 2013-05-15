package vectorClock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SingleClient {

	String name;
	int port;
	int mytsCounter=0;
	VectorClock maxOther;
	
	Map<String,ArrayList<ValueWithTimestamp>> keyvalStore=new HashMap<String,ArrayList<ValueWithTimestamp>>();
	
	public SingleClient(String name,int port){
		this.name=name;
		this.port=port;
		ClientListener listen=new ClientListener(this,port);
		Thread t=new Thread(listen);
		t.start();
	}
	
	public void syncWithOthers(String content){
		String[] parts=content.split("=");
		String value=parts[1];
		String times=parts[0];
		VectorClock clock=new VectorClock();
		String[] singleTimes=times.split(" ");
		for(int i=0;i<singleTimes.length;i++){
			String[] singleTime=singleTimes[i].split(":");
			String name=singleTime[0];
			int ts=Integer.parseInt(singleTime[1]);
			clock.addServerTime(name, ts);
		}
	}
	
	public String read(String key){
		ArrayList<ValueWithTimestamp> values=keyvalStore.get(key);
		if(values==null)
			return null;
		if(values.size()==1)
			return values.get(0).value;
		//Conflict. Return all to user
		String result="";
		for(int i=0;i<values.size();i++){
			ValueWithTimestamp vts=values.get(i);
			result+=vts.value+"  ";
			result+=vts.clock;
			result+="\n";
		}
		return result;
	}
	
	public void write(String key,String value){
		//First write to local
		mytsCounter++;
		ArrayList<ValueWithTimestamp> values=keyvalStore.get(key);
		if(values==null || values.size()==0){
			//Just store in the local
			VectorClock clock=new VectorClock();
			clock.addServerTime(name, mytsCounter);
			ValueWithTimestamp vs=new ValueWithTimestamp(value,clock);
			ArrayList<ValueWithTimestamp> list=new ArrayList<ValueWithTimestamp>();
			keyvalStore.put(key,list);
		}else{
			ValueWithTimestamp toUpdate=values.get(0);
			for(int i=1;i<values.size();i++){
				ValueWithTimestamp current=values.get(i);
//				if(current.value.equals(name))
			}
		}
		
		//Propagate to other servers
	}
	
	public void write(String key,String value,VectorClock time){
		
	}
}
