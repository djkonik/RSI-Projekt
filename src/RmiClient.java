import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class RmiClient extends UnicastRemoteObject implements CallbackMessageInterface {
	String serverAddress;
	int serverPort;
	Registry registry;
	ReceiveMessageInterface rmiServer;
	
	protected RmiClient(String serverAddress, String serverPort) throws RemoteException {
		this.serverAddress = serverAddress;
		this.serverPort = Integer.parseInt(serverPort);
		String text = "Wiadomoœæ do wys³ania: klient";
		System.out.println("sending " + text + " to " + serverAddress + ":" + serverPort);
		
		try {
			registry = LocateRegistry.getRegistry(this.serverAddress, this.serverPort);
			rmiServer = (ReceiveMessageInterface) (registry.lookup("rmiServer"));
			
			// call the remote method
			rmiServer.receiveMessage(text);
			rmiServer.login(this);
			
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
		
	}

	public void sayHello() {
		System.out.println("Callback: Hello");
	}

}