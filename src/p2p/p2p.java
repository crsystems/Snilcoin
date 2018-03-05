//Peer-to-peer providing class
//

import java.net.*;
import java.io.*;

class p2p{
	

	private int maxhosts = 100;
	private int next_index = 0;
	private p2pHost hostlist[] = new p2pHost[maxhosts];
	private DatagramSocket socket = new DatagramSocket(9876);

	public p2p(){}

	public int broadcast(String message) {
		for(int i = 0; i < maxhosts; i++){
			byte message_bytes[] = message.getBytes();
			DatagramPacket msg = new DatagramPacket(message_bytes, message_bytes.length, hostlist[i].getAddress(), hostlist[i].getPort); 
			socket.send(msg);
		}
	}

	public int sync(){



	}

	public int addHost(InetAddress ip, int port){
		hostlist[next_index] = new p2pHost(ip, port);
		next_index++;
		self.sortHostlist();
		return 0;
	}

	public int sortHostlist(){
		for(int i = 0; i < next_index; i++){
		//Pick every entry once and zero the duplicates
			p2pHost tmp = hostlist[i];

			if(tmp.getPort() == 0){
				continue;
			}else{
				for(int k = i+1; k < next_index; k++){
					if(tmp.getAddress().equals(hostlist[k].getAddress())){
						hostlist[k].setPort(0);
					}
				}
			}
		}
		
		//sort the zero entrys to the bottom
		for(int i = 0; i < next_entry; i++){
			p2pHost tmp = hostlist[i];
			if(tmp.getPort() == 0){
				for(int k = i+1; k < next_index; k++){
					if(hostlist[k].getPort() != 0){
						hostlist[i].setPort(hostlist[k].getPort());
						hostlist[i].setAddress(hostlist[k].getAddress());

						hostlist[k].setPort(0);
						break;
					}
				}
			}
		}

		//find the new next_index variable
		p2pHost tmp = hostlist[0];
		next_entry = 0;
		while(tmp.getPort() != 0){
			next_entry++;
			tmp = hostlist[next_entry];
		}
		return 0;
	}

}
