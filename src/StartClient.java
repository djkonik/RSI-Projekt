import remote.RmiClient;


public class StartClient {
	
	private static String ADDRESS = "localhost";
	private static String PORT = "3357";
	
	static public void main(String args[]) {
		try {
			RmiClient client = new RmiClient(ADDRESS, PORT);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
