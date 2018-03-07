//Peer-to-peer providing class
//

import java.net.*;
import java.io.*;

class p2p {
	

	//max number if hosts in hostlist
	private int maxhosts = 100;
	
	//next empty entryvariable
	private int next_index = 0;

	//hostlist
	private p2pHost hostlist[] = new p2pHost[maxhosts];
	private DatagramSocket socket;

	//constructor
	//creates the UDP socket
	public p2p() throws SocketException{
		socket = new DatagramSocket(9876);
	}

	//method for broadcasting ==> sends a byte array to all hosts in the hostlist
	public int broadcast(byte[] message_bytes) throws IOException{
		for(int i = 0; i < next_index; i++){
			DatagramPacket msg = new DatagramPacket(message_bytes, message_bytes.length, hostlist[i].getAddress(), hostlist[i].getPort()); 
			socket.send(msg);
		}
		return 0;
	}

	//method for synchronizing the hostlist with other known hosts
	public int sync(){
		return 0;
	}

	//method for adding a known host in the first place
	public int addHost(InetAddress ip, int port){
		if(next_index < maxhosts-1){
			hostlist[next_index] = new p2pHost(ip, port);
		
			//Debug
			//System.out.println("Added Host: Index: " + next_index + " with ip: " + hostlist[next_index].getAddress().toString() + " on port: " + hostlist[next_index].getPort()); 
		
			next_index++;
			sortHostlist();
		}else{
			System.out.println("Hostlist is full. Can't add host.");
		}
		return 0;
	}


	//method for sorting the hostlist
	//duplicates are removed and the array ist shrinked to close possible holes
	//a new next_index is generated
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
		for(int i = 0; i < next_index; i++){
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
		//and print the new hostlist
		int old_index = next_index;
		next_index = 0;
		System.out.println("Hostlist:");
		for(int i = 0; i < old_index && hostlist[i].getPort() != 0; i++){
			next_index++;
			System.out.println("IP: " + hostlist[i].getAddress().toString() + " on port: " + hostlist[i].getPort());
		}
		return 0;
	}

}
