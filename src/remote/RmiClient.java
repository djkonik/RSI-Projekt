package remote;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import meanshift.MeanShift;
import meanshift.Space;

public class RmiClient extends UnicastRemoteObject implements CallbackMessageInterface {
	String serverAddress;
	int serverPort;
	Registry registry;
	ReceiveMessageInterface rmiServer;
	
	public RmiClient(String serverAddress, String serverPort) throws RemoteException {
		this.serverAddress = serverAddress;
		this.serverPort = Integer.parseInt(serverPort);
		System.out.println("Connecting to " + serverAddress + " : " + serverPort);
		
		try {
			registry = LocateRegistry.getRegistry(this.serverAddress, this.serverPort);
			rmiServer = (ReceiveMessageInterface) (registry.lookup("rmiServer"));
			
			// call the remote method
			rmiServer.login(this);
			System.out.println("Client logged in and waiting for task");
			
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
		
	}

	public void sayHello() {
		System.out.println("Callback: Hello");
	}

	@Override
	public void sheduleTask(Space space, double radius, int maxIter, int precision, int[] startPoints) throws RemoteException {
		System.out.println("Received space with " + space.getSize() + " points. This node will evaluate " + startPoints.length + " of them");
		new MeanShift(rmiServer, space, radius, maxIter, precision, startPoints).start();
	}

}