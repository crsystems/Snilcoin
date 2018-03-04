//Class to ease hostmanagement by the p2p class


import java.net.*;

class p2pHost{

	private InetAddress address = new InetAddress;
	private int portnum = 0;


	public p2pHost(InetAddress ip, int port){
		address = ip;
		portnum = port;
	}

	public InetAddress getAddress(){
		return address;
	}

	public int getPort(){
		return portnum;
	}
}

