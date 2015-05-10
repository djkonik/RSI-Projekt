import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;


public class StartClient {
	
	static public void main(String args[]) {
		try {
			RmiClient client = new RmiClient("localhost", "3357");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
