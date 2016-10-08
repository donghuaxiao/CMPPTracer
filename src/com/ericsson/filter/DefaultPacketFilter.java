package com.ericsson.filter;

import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.protocol.network.Ip4;

public class DefaultPacketFilter implements PacketFilter {

    Ip4 ip = new Ip4();

    public boolean isMatch(PcapPacket packet) {
        return true;
    }
}
