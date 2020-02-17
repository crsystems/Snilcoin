import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;

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

	public static void main(String args[]) throws IOException, SocketException, UnknownHostException
	{

		DatagramSocket sock = new DatagramSocket(5432, InetAddress.getByName("0.0.0.0"));

		p2p network = new p2p(sock);
		bootstrap(network);

		String snc = new String("sync");
		network.broadcast(snc.getBytes(), snc.length());
		
		// Byte buffer for incoming packets
		// 1541 bytes = packet header + 256 hostlist entries
		byte pckt_buf[] = new byte[1541];

		DatagramPacket pckt = new DatagramPacket(pckt_buf, 1541);

		while(true){
			sock.receive(pckt);

			switch((int) pckt_buf[0]){
				case 1: network.sync(pckt);
					continue;

				default:
					break;
			}
		}

	}

}
