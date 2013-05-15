package vectorClock;

import java.util.Map;
import java.util.TreeMap;

public class VectorClock {

	TreeMap<String,Integer> timestamp=new TreeMap<String,Integer>();
	
	public void addServerTime(String server,int time){
		timestamp.put(server, time);
	}
	
	public int getServerTime(String server){
		return timestamp.get(server);
	}
	
	public String toString(){
		String result="";
		for(Map.Entry<String, Integer> entry:timestamp.entrySet()){
			String machine=entry.getKey();
			int clock=entry.getValue();
			result+=machine+"->"+clock+"\t";
		}
		
		return result;
	}
}
