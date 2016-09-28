package com.ericsson.parser;

import java.nio.ByteBuffer;

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
		int count = length / 21;
		String[] destTerminals = new String[count];
		int start = 0, end=21;
		for ( int i=0; i < count; i++) {
			destTerminals[i] = destids.substring(start, end);
			start += 21;
			end+=21;
		}
		return destTerminals;
		
	}

	public static CMPPSubmit parserSubmit( ByteBuffer buffer) {
		CMPPSubmit submit = new CMPPSubmit();
		
		submit.setMsgId(buffer.getLong());
		submit.setPkTotal(buffer.get());
		submit.setPkNumber(buffer.get());
		submit.setRegisteredDelivery(buffer.get());
		submit.setMsgLevel( buffer.get() );
		
		submit.setServiceId(CMPPParser.getString(buffer, 10) );
		submit.setFeeUserType( buffer.get());
		submit.setFeeTerminalId(CMPPParser.getString(buffer, 21));
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
		String destTerminals = CMPPParser.getString(buffer, 21 * count);
		submit.setDestTerminalId( CMPPParser.getDestTerminalId(destTerminals));
		byte length = buffer.get();
		submit.setMsgLength(length);
		submit.setMsgContent(CMPPParser.getString(buffer, length));
		
		return submit;
	}
	
	public static CMPPHeader parserHeader(ByteBuffer buffer) {
		CMPPHeader cmppHeader = new CMPPHeader();
		cmppHeader.setPacketLength(buffer.getInt());
		cmppHeader.setCmdtype( buffer.getInt());
		cmppHeader.setSeq( buffer.getInt());
		return cmppHeader;
	};
}
