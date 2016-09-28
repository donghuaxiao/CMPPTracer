package com.ericsson.filter;

import org.jnetpcap.packet.PcapPacket;

public interface PacketFilter {

	public boolean isMatch( PcapPacket packet );
	
}
