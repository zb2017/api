package websocketx.client;

import org.apache.log4j.Logger;

public class RunExample {
	
	private static Logger log = Logger.getLogger(RunExample.class);
	
	public static WebSocketClient client;
	
	public static void main(String[] args) {
		try {
			if(client==null){
				log.info("链接到"+WebSocketClient.serverUrl);
				client = new WebSocketClient( WebSocketClient.serverUrl );
			}
			client.connect();
			log.info("================================"+client.isAlive());
			client.doTask();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
