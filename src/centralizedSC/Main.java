package centralizedSC;

import java.io.IOException;
import java.net.UnknownHostException;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class Main {

	/**
	 * @param args
	 * @throws WriteException
	 * @throws InterruptedException 
	 * @throws ReadException 
	 * @throws IOException 
	 * @throws UnknownHostException 
	 */
	public static void main(String[] args) throws WriteException, UnknownHostException, IOException, ReadException, InterruptedException {
//		testLog();
		 int serverPort=5000;
		 Client c1=new Client("client1",8888,serverPort);
		 c1.writeValue("a", "a1");
		 
		 Client c2=new Client("client2",8889,serverPort);
		 c2.writeValue("a", "a2");
		 
		 String result2=c2.readValue("a");
		 System.out.println(result2);
		 
		 result2=c1.readValue("a");
		 System.out.println(result2);
	}

	public static void testLog() {
		Logger logger = Logger.getLogger(Main.class.getName());
		PropertyConfigurator
				.configure("D:\\Programmes\\eclipseEE\\Juno\\workspace\\testDistributeConcepts\\src\\centralizedSC\\logClient.properties");
		logger.info("Entering application.");
		logger.error("Error");
	}

}
