import java.io.*;
import java.net.*;

import p2p.*;

class controller {

	private static void bootstrap(p2p network)
	{
		try{
			File f = new File("./coordinators.conf");
			FileInputStream in = new FileInputStream(f);

			int tmp = in.read();
			while(tmp != -1){
				byte host[] = new byte[256];
				byte port[] = new byte[5];

				String hostString, portString;

				int pos = 0;
				while(tmp != -1 && tmp != (int) ';' && pos < 256){
					host[pos] = (byte) tmp;
					tmp = in.read();
					pos++;
				}

				hostString = new String(host, 0, pos);

				pos = 0;
				if(tmp == -1)
					break;

				tmp = in.read();
				while(tmp != -1 && tmp != (int) ';' && pos < 6){
					port[pos] = (byte) tmp;
					tmp = in.read();
					pos++;
				}
				if(tmp != -1)
					tmp = in.read();

				if(tmp == (int) '\n')
					tmp = in.read();


				portString = new String(port, 0, pos);

				InetAddress ip = InetAddress.getByName(hostString);
				Integer port_int = new Integer(portString);

				network.addHost(ip, port_int.intValue());
			}

			in.close();

		}catch(Exception e){
			System.out.println("Error while adding the initial hosts!");
			System.out.println(e.getMessage());
			return;
		}
		return;
	}

	public static void main(String args[]) throws SocketException, UnknownHostException
	{
		p2p network = new p2p();
		bootstrap(network);
	}

}
