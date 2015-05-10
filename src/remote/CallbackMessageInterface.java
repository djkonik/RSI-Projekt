package remote;
import java.rmi.Remote;
import java.rmi.RemoteException;

import meanshift.Space;

public interface CallbackMessageInterface extends Remote {
	void sayHello() throws RemoteException;
	void sheduleTask (Space space, double radius, int maxIter, int precision, int[] startPoints) throws RemoteException;
}