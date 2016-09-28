package com.ericsson.filter;

import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.protocol.tcpip.Http;
import org.jnetpcap.protocol.tcpip.Tcp;

public class HttpFilter implements PacketFilter {
	private Tcp tcp = new Tcp();
	Http http = new Http();
	
	public boolean isMatch(PcapPacket packet) {
		if ( packet.hasHeader(tcp) && packet.hasHeader(http)) {
			return true;
		}
		return false;
	}

}
