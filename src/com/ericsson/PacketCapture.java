package com.ericsson;

import java.util.concurrent.BlockingQueue;

import org.jnetpcap.Pcap;
import org.jnetpcap.PcapBpfProgram;
import org.jnetpcap.nio.JBuffer;
import org.jnetpcap.packet.PcapPacket;

import com.ericsson.filter.DefaultPacketFilter;
import com.ericsson.filter.PacketFilter;

public class PacketCapture implements Runnable {
	
	private final BlockingQueue<PcapPacket> shareQueue;
	
	private boolean stop = false;
	
	private StringBuilder errbuf = new StringBuilder();
	
	private Pcap pcap;
	
	private String dev;
	
	private PcapPacket packet = new PcapPacket(JBuffer.POINTER);
	
	private PacketFilter packetFilter = new DefaultPacketFilter();
	

	public PacketCapture(String dev, BlockingQueue<PcapPacket> queue){
		this.shareQueue = queue;
		this.dev = dev;
		this.pcap = Pcap.openLive(this.dev, 64*1024, Pcap.MODE_PROMISCUOUS, 10*1000, this.errbuf);
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

	public PacketFilter getPacketFilter() {
		return packetFilter;
	}
	

	public void setPacketFilter(PacketFilter packetFilter) {
		this.packetFilter = packetFilter;
	}
	
	public void setFilter( String filter) {
		PcapBpfProgram  bpf = new PcapBpfProgram();
		if ( !(this.pcap.compile(bpf, filter, 0, 0) == Pcap.OK || 
				pcap.setFilter(bpf) == Pcap.OK) ) {
			System.out.println("Filter to set filter: " + pcap.getErr());
		}
	}
	
	public Pcap getPcap() {
		return this.pcap;
	}
	
}
