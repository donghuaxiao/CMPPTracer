package test;

import java.nio.ByteBuffer;

import com.ericsson.protocol.CMPP;
import com.ericsson.protocol.CMPPSubmit;

public class PacketGenerator {
	
	public static String toFixLengthString( String str, int length) {
		str = (str == null) ? "" : str;
		String format = String.format("%%%ds", length);
		return String.format(format, str);
	}

	public static ByteBuffer  createMessage( String spId,  byte messageFormat, String srcId, String[] destTerminalId, String message) {
		
		CMPPSubmit packet = new CMPPSubmit();
		packet.getHeader().setCmdtype(CMPP.CMPP_SUBMIT);
		
		packet.setMsgId(new byte[8]);
		packet.setPkTotal((byte)1);
		packet.setPkNumber((byte)1);
		packet.setRegisteredDelivery((byte)1);
		packet.setMsgLevel((byte)0);
		packet.setServiceId(PacketGenerator.toFixLengthString("0", 10));
		packet.setFeeUserType((byte)1);
		packet.setFeeTerminalId(PacketGenerator.toFixLengthString("0", 32));
		packet.setFeeTerminalType((byte)1);
		
		packet.setTpUdhi((byte)0);
		packet.setTpPid((byte)0);
		packet.setMsgFormat(messageFormat);
		packet.setMsgSrc(spId);
		packet.setFeeType("01");
		packet.setFeeCode("000010");
		packet.setValidTime(PacketGenerator.toFixLengthString("0", 17));
		packet.setAtTime( PacketGenerator.toFixLengthString("0", 17));
		packet.setSrcId( PacketGenerator.toFixLengthString(srcId, 21));
		
		packet.setDestUsrtl((byte)destTerminalId.length);
		packet.setDestTerminalId(destTerminalId);
		packet.setDestTerminalType((byte)1);
		packet.setMsgLength((byte)message.length());
		packet.setMsgContent(message);
		packet.setLinkId( PacketGenerator.toFixLengthString("0", 20));
		
		packet.getHeader().setPacketLength( 163 + destTerminalId.length * 32 + message.length());
		
		return packet.getData();
		
	}
}
