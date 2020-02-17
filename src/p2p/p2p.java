//Peer-to-peer networking class
//
package p2p;

import java.net.*;
import java.io.*;
import java.nio.ByteBuffer;
import p2p.p2pHost.*;


public class p2p {
	

	// Max number if hosts in hostlist
	private int maxHosts = 100;
	
	// Next empty entryvariable
	private int next_index = 0;

	// Hostlist
	private p2pHost hostlist[];
	private DatagramSocket socket;

	// Constructor
	// Creates the UDP socket
	public p2p(DatagramSocket s) throws SocketException, UnknownHostException{
		hostlist = new p2pHost[maxHosts];
		socket = s;
	}

	// Method for broadcasting ==> sends a byte array to all hosts in the hostlist
	public int broadcast(byte[] message_bytes, int length)
	{
		for(int i = 0; i < next_index; i++){
			try{
				DatagramPacket msg = new DatagramPacket(message_bytes, length, hostlist[i].getAddress(), hostlist[i].getPort()); 
				this.socket.send(msg);
			}catch(Exception e){
				return i+1;
			}
		}
		return 0;
	}

	// Method for synchronizing the hostlist with other known hosts
	public int sync(DatagramPacket p)
	{	
		byte data[] = p.getData();

		ByteBuffer l_buf = ByteBuffer.allocate(4);
		l_buf.put(data, 1, 4);
		int length = l_buf.getInt(0);

		if((length % 6) != 0){
			System.out.println("Host data malformed! Aborting execution");
			return -1;
		}
		
		for(int i = 5; i < length; i+= 6){
			byte addr[] = new byte[4];
			addr[0] = data[i];
			addr[1] = data[i+1];
			addr[2] = data[i+2];
			addr[3] = data[i+3];

			ByteBuffer port = ByteBuffer.allocate(4);
			port.put((byte) 0);
			port.put((byte) 0);
			port.put(data[i+4]);
			port.put(data[i+5]);

			try{
				this.addHost(InetAddress.getByAddress(addr), port.getInt(0));
			}catch(Exception e){
				System.out.println("Encountered error while adding new host: " + e.getMessage());
				continue;
			}
		}

		return 0;
	}

	// Method for adding a known host in the first place
	public int addHost(InetAddress ip, int port){
		if(next_index < maxHosts-1){
			hostlist[next_index] = new p2pHost(ip, port);
		
			//Debug
			System.out.println("Added Host: Index: " + next_index + " with ip: " + hostlist[next_index].getAddress().toString() + " on port: " + hostlist[next_index].getPort()); 
		
			next_index++;
			sortHostlist();
		}else{
			System.out.println("Hostlist is full. Can't add host.");
		}
		return 0;
	}

	// Method for removing an unneeded host from the host list
	public void removeHost(InetAddress ip, int port)
	{
		for(int i = 0; i < next_index; i++){
			if(hostlist[i].getAddress().equals(ip) && hostlist[i].getPort() == port){
				hostlist[i].setPort(0);
				break;
			}
		}
		this.sortHostlist();
	}


	// Method for sorting the hostlist
	// Duplicates are removed and the array ist shrinked to close possible holes
	// a new next_index is generated
	public int sortHostlist(){
		for(int i = 0; i < next_index; i++){
		// Pick every entry once and zero the duplicates
			p2pHost tmp = hostlist[i];

			if(tmp.getPort() == 0){
				continue;
			}else{
				for(int k = i+1; k < next_index; k++){
					if(tmp.getAddress().equals(hostlist[k].getAddress()) && tmp.getPort() == hostlist[k].getPort()){
						hostlist[k].setPort(0);
					}
				}
			}
		}
		
		// Sort the zero entrys to the bottom
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

		// Find the new next_index variable
		// and print the new hostlist
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
