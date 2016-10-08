package com.ericsson;

import java.util.concurrent.BlockingQueue;

import org.jnetpcap.Pcap;
import org.jnetpcap.nio.JBuffer;
import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.protocol.tcpip.Tcp;

import com.ericsson.filter.DefaultPacketFilter;
import com.ericsson.filter.PacketFilter;

public class PacketCapture implements Runnable {
	
	private final BlockingQueue<PcapPacket> shareQueue;
	
	private boolean stop = false;
	
	private Pcap pcap;
	
	private PcapPacket packet = new PcapPacket(JBuffer.POINTER);
	
	private PacketFilter packetFilter = new DefaultPacketFilter();
	
	private Thread _thread;

	public PacketCapture(BlockingQueue<PcapPacket> queue, Pcap pcap){
		this.shareQueue = queue;
		this.pcap = pcap;
	}
	
	public void run() {
		while( !stop && pcap.nextEx(packet) == Pcap.NEXT_EX_OK) {
			PcapPacket copy = new PcapPacket(packet);
			if ( packetFilter.isMatch(copy)) {
				try {
					this.shareQueue.put(copy);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}				
		}
	}

	public void start() {
		this._thread = new Thread(this);
		this.stop = false;
		this._thread.start();
	}
	
	public void stop() {
		this.stop = true;
	}
	
	public void join() throws InterruptedException {
		this._thread.join();
	}

	public PacketFilter getPacketFilter() {
		return packetFilter;
	}
	

	public void setPacketFilter(PacketFilter packetFilter) {
		this.packetFilter = packetFilter;
	}
	
}
