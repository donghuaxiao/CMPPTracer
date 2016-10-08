package com.ericsson.filter;

import java.nio.ByteBuffer;

import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.protocol.tcpip.Tcp;

import com.ericsson.parser.CMPPParser;
import com.ericsson.protocol.CMPP;
import com.ericsson.protocol.CMPPHeader;

public class CMPPSubmitPacketFilter implements PacketFilter{

	Tcp tcp = new Tcp();
	public boolean isMatch(PcapPacket packet) {
		if (packet.hasHeader(tcp)) {
			byte [] data = tcp.getPayload();
                        
            if ( data.length < CMPP.CMPP_HEAD_LEN) {
                return false;
            }
			byte [] header = new byte[CMPP.CMPP_HEAD_LEN];
			System.arraycopy(data, 0, header, 0, CMPP.CMPP_HEAD_LEN);
			ByteBuffer headerBuffer = ByteBuffer.wrap(header);
			CMPPHeader cmppheader = CMPPParser.parserHeader(headerBuffer);
			
			/* not a CMPP packet, the packet length not equals */
			if ( cmppheader.getPacketLength() != data.length) {
				return false;
			}
			if (cmppheader.getCmdtype() == CMPP.CMPP_SUBMIT) {
				return true;
			}
		}
		return false;
	}

}
