package com.ericsson.filter;

import java.nio.ByteBuffer;

import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.protocol.tcpip.Tcp;

import com.ericsson.parser.CMPPParser;
import com.ericsson.protocol.CMPP;
import com.ericsson.protocol.CMPPHeader;
import com.ericsson.protocol.CMPPSubmit;

public class DestTerminalFilter implements PacketFilter {

    private String[] filter;

    public DestTerminalFilter(String[] filter) {
        this.filter = filter;
    }

    private Tcp tcp = new Tcp();

    public boolean isMatch(PcapPacket packet) {
        if (filter == null) {
            return true;
        }

        if (packet.hasHeader(tcp)) {

            byte[] data = tcp.getPayload();
            if (data.length > 0) {
                ByteBuffer buffer = ByteBuffer.wrap(data);
                CMPPHeader cmppheader = CMPPParser.parserHeader(buffer);

                if (cmppheader.getCmdtype() == CMPP.CMPP_SUBMIT) {
                    CMPPSubmit submit = CMPPParser.parserSubmit(buffer);
                    System.out.println(submit.getMsgContent());
                    String[] destTerminalIds = submit.getDestTerminalId();
                    return filterByTerminalId(filter, destTerminalIds);
                }

            }

        }
        return false;
    }

    public boolean filterByTerminalId(String[] filterTerminalIds, String[] targetTerminalIds) {
        for (String filterTerminal : filterTerminalIds) {
            for (String targetTerminal : targetTerminalIds) {
                System.out.println( filterTerminal + "----> " + targetTerminal);
                if (filterTerminal.equals(targetTerminal.trim())) {
                    System.out.println("DestTerminalFilter: true");
                    return true;
                }
            }
        }
        return false;
    }

}
