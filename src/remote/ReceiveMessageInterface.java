package remote;
import java.rmi.*;
import java.util.List;

import meanshift.Point;

public interface ReceiveMessageInterface extends Remote {
	void receiveMessage(String x) throws RemoteException;
	void login(CallbackMessageInterface client) throws RemoteException;
	void sendResult(List<Point> result) throws RemoteException;
}