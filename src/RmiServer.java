import java.rmi.*;
import java.rmi.registry.*;
import java.rmi.server.*;
import java.net.*;
import java.net.UnknownHostException;

public class RmiServer extends java.rmi.server.UnicastRemoteObject implements ReceiveMessageInterface {
	String thisAddress;
	int thisPort;
	Registry registry; // rmi registry for lookup the remote objects.
	CallbackMessageInterface client;

	// This method is called from the remote client by the RMI.
	// This is the implementation of the �gReceiveMessageInterface�h.
	@Override
	public void receiveMessage(String x) throws RemoteException {
		System.out.println(x);
	}
	

	@Override
	public void login(CallbackMessageInterface client) throws RemoteException {
		this.client = client;
		client.sayHello();
	}

	public RmiServer(String thisPort) throws RemoteException, UnknownHostException {
		thisAddress = (InetAddress.getLocalHost()).toString();
		this.thisPort = Integer.parseInt(thisPort);
		System.out.println("this address=" + thisAddress + ",port=" + this.thisPort);
		
		// create the registry and bind the name and object.
		registry = LocateRegistry.createRegistry(this.thisPort);
		registry.rebind("rmiServer", this);
	}

}