package vectorClock;

import java.util.HashMap;
import java.util.Map;

public class ValueWithTimestamp {

	String value;
	VectorClock clock;
	public ValueWithTimestamp(String value, VectorClock clock) {
		super();
		this.value = value;
		this.clock = clock;
	}
	
	public void update(String value,String serverName){
		
	}
	
	public int curServerClock(String serverName){
		return clock.getServerTime(serverName);
	}
	
}
