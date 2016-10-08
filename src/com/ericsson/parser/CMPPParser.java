package com.ericsson.parser;

import java.nio.ByteBuffer;

import com.ericsson.protocol.CMPPConnect;
import com.ericsson.protocol.CMPPConnectResp;
import com.ericsson.protocol.CMPPHeader;
import com.ericsson.protocol.CMPPSubmit;

public class CMPPParser {
	
	public static String getString( ByteBuffer buf,  int length) {
		byte[] byteArray = new byte[length];
		buf.get(byteArray, 0, length);
		return new String(byteArray );
	}
	
	public static String[] getDestTerminalId( String destids ) {
		int length = destids.length();
		int count = length / 32;
		String[] destTerminals = new String[count];
		int start = 0, end=32;
		for ( int i=0; i < count; i++) {
			destTerminals[i] = destids.substring(start, end).trim();
			start += 32;
			end += 32;
		}
		return destTerminals;
	}

	public static CMPPSubmit parserSubmit( ByteBuffer buffer) {
		CMPPSubmit submit = new CMPPSubmit();
		byte[] msgId = new byte[8];
		buffer.get(msgId);
		submit.setMsgId(msgId);
		
		submit.setPkTotal(buffer.get());
		submit.setPkNumber(buffer.get());
		submit.setRegisteredDelivery(buffer.get());
		submit.setMsgLevel( buffer.get() );
		
		submit.setServiceId(CMPPParser.getString(buffer, 10) );
		submit.setFeeUserType( buffer.get());
		submit.setFeeTerminalId(CMPPParser.getString(buffer, 32));
                submit.setFeeTerminalType(buffer.get());
                
		submit.setTpPid(buffer.get());
		submit.setTpUdhi(buffer.get());
		submit.setMsgFormat(buffer.get());
		submit.setMsgSrc(CMPPParser.getString(buffer, 6));
		submit.setFeeType(CMPPParser.getString(buffer, 2));
		submit.setFeeCode( CMPPParser.getString(buffer, 6));
                
		submit.setValidTime(CMPPParser.getString(buffer, 17));
		submit.setAtTime(CMPPParser.getString(buffer, 17));
		submit.setSrcId(CMPPParser.getString(buffer, 21));
                
		byte count = buffer.get();
		submit.setDestUsrtl(count);
		String destTerminals = CMPPParser.getString(buffer, 32 * count);
		submit.setDestTerminalId( CMPPParser.getDestTerminalId(destTerminals));
		submit.setDestTerminalType(buffer.get());
		byte length = buffer.get();
		submit.setMsgLength(length);
		submit.setMsgContent(CMPPParser.getString(buffer, length));
		
		return submit;
	}
	
	public static CMPPHeader parserHeader(ByteBuffer buffer) {
		CMPPHeader cmppHeader = new CMPPHeader();
		cmppHeader.setPacketLength(buffer.getInt());
		cmppHeader.setCmdtype( buffer.getInt());
		cmppHeader.setSequenceNumber( buffer.getInt());
		return cmppHeader;
	};
	
	public static CMPPConnect parseCMPPConnect( ByteBuffer buffer) {
		CMPPConnect connect = new CMPPConnect();
		connect.setSourceAddr( CMPPParser.getString(buffer, 6));
		connect.setAuthenticatorSource( CMPPParser.getString(buffer, 16));
		connect.setVersion(buffer.get());
		connect.setTimestamp(buffer.getInt());
		return connect;
	}
	
	public static CMPPConnectResp parseCMPPConnectResp( ByteBuffer buffer) {
		return null;
	}
}
