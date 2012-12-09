package my.b1701.SB.ChatService;

import android.os.RemoteException;


public class XMPPAPIs extends IXMPPAPIs.Stub {
	
	private final XMPPConnectionListenersAdapter mConnectionAdapter;
	
	
	public XMPPAPIs(final XMPPConnectionListenersAdapter connection) {
			this.mConnectionAdapter = connection;			
		    }

	@Override
	public void connect() throws RemoteException {
		mConnectionAdapter.connect();
		
	}

	

	@Override
	public void disconnect() throws RemoteException {
		mConnectionAdapter.disconnect();
		
	}

	@Override
	public IChatManager getChatManager() throws RemoteException {		
		return mConnectionAdapter.getChatManager();
	}

	@Override
	public void changeStatus(int status, String msg) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void login(String login,String password) throws RemoteException {
		mConnectionAdapter.login(login, password);
		
	}

	@Override
	public boolean isLoggedIn() throws RemoteException {
		// TODO Auto-generated method stub
		return mConnectionAdapter.isLoggedIn;
	}

	@Override
	public boolean isConnected() throws RemoteException {
		// TODO Auto-generated method stub
		return mConnectionAdapter.isConnected;
	}

	

}
