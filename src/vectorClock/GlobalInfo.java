package vectorClock;

import java.util.ArrayList;

public class GlobalInfo {

	static ArrayList<SingleClient> clients=new ArrayList<SingleClient>();
	
	public static void addClient(SingleClient sc){
		clients.add(sc);
	}
	
	public static ArrayList<SingleClient> findOthers(SingleClient sc){
		ArrayList<SingleClient> copy=(ArrayList<SingleClient>) clients.clone();
		copy.remove(sc);
		return copy;
	}
}
