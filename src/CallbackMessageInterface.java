import java.rmi.*;

public interface CallbackMessageInterface extends Remote {
	void sayHello() throws RemoteException;
}