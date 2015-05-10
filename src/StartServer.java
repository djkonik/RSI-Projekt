
public class StartServer {

	static public void main(String args[]) {
		try {
			RmiServer server = new RmiServer("3357");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
