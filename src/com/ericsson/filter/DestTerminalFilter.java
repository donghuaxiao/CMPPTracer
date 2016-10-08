package com.ericsson.filter;

import java.nio.ByteBuffer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.protocol.tcpip.Tcp;

import com.ericsson.parser.CMPPParser;
import com.ericsson.protocol.CMPP;
import com.ericsson.protocol.CMPPHeader;
import com.ericsson.protocol.CMPPSubmit;
import com.ericsson.util.Utils;

public class DestTerminalFilter implements PacketFilter {

    private String[] filter;
    
    private DateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

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
                    //CMPPSubmit submit = CMPPParser.parserSubmit(buffer);
                	CMPPSubmit submit = new CMPPSubmit();
                    submit.setHeader(cmppheader);
                    submit.setBody(buffer);
                    String[] destTerminalIds = submit.getDestTerminalId();
                    
                    if ( filter == null || filter.length == 0) {
                    	printSubmitMessage(submit);
                    	return true;
                    } else {
                    	boolean isMatched =  filterByTerminalId(filter, destTerminalIds);
                    	if ( isMatched == true) {
                    		printSubmitMessage(submit);
                    	}
                    	return isMatched;
                    }
                }

            }

        }
        return false;
    }

    public boolean filterByTerminalId(String[] filterTerminalIds, String[] targetTerminalIds) {
        for (String filterTerminal : filterTerminalIds) {
            for (String targetTerminal : targetTerminalIds) {
                if (filterTerminal.equals(targetTerminal.trim())) {
                    return true;
                }
            }
        }
        return false;
    }
    
    /**
     * 打印 CMPP Submit 消息
     * 格式： 序列号  时间   手机号  消息内容
     * @param packet
     */
    public void printSubmitMessage( CMPPSubmit packet) {
    	String msisdns = Utils.join(packet.getDestTerminalId(), ", ");
    	System.out.printf("%s\t%s\t%s\t%s\n", 
    			packet.getHeader().getSequenceNumber(), 
    			dataFormat.format(new Date()), 
    			msisdns, 
    			packet.getMsgContent().trim());
    }
}
