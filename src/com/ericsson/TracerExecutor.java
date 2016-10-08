package com.ericsson;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.jnetpcap.Pcap;
import org.jnetpcap.PcapDumper;
import org.jnetpcap.packet.PcapPacket;

import com.ericsson.filter.CMPPSubmitPacketFilter;
import com.ericsson.filter.PacketFilter;
import com.ericsson.writer.FileWriter;

public class TracerExecutor {

	/* 抓包 */
	private PacketCapture capture;
	
	/* 把过滤后的包写入指定的文件 */
	private FileWriter fileWriter;
	
	/* 包解析器*/
	private PacketParser parser;
	
	/* 存放已经过滤 CMPP Submit 包*/
	private BlockingQueue<PcapPacket> filterQueue = new LinkedBlockingQueue<>();
	
	/* 存放要写入文件的包 */
	private BlockingQueue<PcapPacket> storeQueue = new LinkedBlockingQueue<>();
	
	private Pcap pcap;
	
	private PcapDumper dumper;
	
	class ExitHandler extends Thread {
		@Override
		public void run() {
			capture.stop();
			parser.stop();
			fileWriter.stop();
		}
	}
	
	public TracerExecutor(Pcap pcap, PcapDumper dumper, PacketFilter filter) {
		this.pcap = pcap;
		this.dumper = dumper;
		
		this.capture = new PacketCapture(filterQueue, pcap);
		capture.setPacketFilter( new CMPPSubmitPacketFilter());
		
		
		this.parser = new PacketParser(filterQueue, storeQueue);
		if ( filter != null) {
			this.parser.setPacketFilter(filter);
		}
		
		
		this.fileWriter = new FileWriter(storeQueue, dumper);
		Runtime.getRuntime().addShutdownHook( new ExitHandler());
	}
	
	public void start() {
		this.fileWriter.start();
		this.parser.start();
		this.capture.start();
		try {
			this.fileWriter.join();
			this.parser.join();
			this.capture.join();
		} catch(InterruptedException ex) {
			ex.printStackTrace();
		}
	}
	
	public void stop() {
		this.capture.stop();
		this.parser.stop();
		this.fileWriter.stop();
	}
	
}
