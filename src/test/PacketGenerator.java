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
		packet.setCmdType(CMPP.CMPP_SUBMIT);
		
		packet.setMsgId(0);
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
		packet.setReserve( PacketGenerator.toFixLengthString("0", 20));
		
		packet.setPktLength( 163 + destTerminalId.length * 32 + message.length());
		
		ByteBuffer buffer = ByteBuffer.allocate(packet.getPktLength());
		//set Header
		buffer.putInt(packet.getPktLength());
		buffer.putInt(packet.getCmdType());
		buffer.putInt(packet.getSeq());
		
		// set body
		buffer.putLong(packet.getMsgId());
		buffer.put(packet.getPkTotal());
		buffer.put(packet.getPkNumber());
		buffer.put(packet.getRegisteredDelivery());
		buffer.put(packet.getMsgLevel());
		
		buffer.put(packet.getServiceId().getBytes());
		buffer.put(packet.getFeeUserType());
		buffer.put(packet.getFeeTerminalId().getBytes());
		buffer.put(packet.getFeeTerminalType());
		
		buffer.put(packet.getTpPid());
		buffer.put(packet.getTpUdhi());
		buffer.put(packet.getMsgFormat());
		buffer.put( packet.getMsgSrc().getBytes());
		buffer.put( packet.getFeeType().getBytes());
		buffer.put(packet.getFeeCode().getBytes());
		
		buffer.put( packet.getValidTime().getBytes());
		buffer.put( packet.getAtTime().getBytes());
		
		buffer.put( packet.getSrcId().getBytes());
		
		buffer.put( packet.getDestUsrtl());
		
		for ( String terminalId: packet.getDestTerminalId()) {
			buffer.put( PacketGenerator.toFixLengthString(terminalId, 32).getBytes());
		}
		
		buffer.put(packet.getDestTerminalType());
		buffer.put( packet.getMsgLength());
		buffer.put( packet.getMsgContent().getBytes());
		buffer.put( packet.getReserve().getBytes());
		
		return buffer;
		
	}
}
