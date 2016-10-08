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

	/* ץ�� */
	private PacketCapture capture;
	
	/* �ѹ��˺�İ�д��ָ�����ļ� */
	private FileWriter fileWriter;
	
	/* ��������*/
	private PacketParser parser;
	
	/* ����Ѿ����� CMPP Submit ��*/
	private BlockingQueue<PcapPacket> filterQueue = new LinkedBlockingQueue<>();
	
	/* ���Ҫд���ļ��İ� */
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
