package com.ericsson.protocol;

import java.nio.ByteBuffer;

public class CMPPHeader  implements CMPPAccessor{
	protected int packetLength;
	
	protected int cmdtype;
	
	protected int sequenceNumber;
	
	public CMPPHeader() {}
	public CMPPHeader( int length, int type, int seq) {
		this.packetLength = length;
		this.cmdtype  = type;
		this.sequenceNumber = seq;
	}
	public int getPacketLength() {
		return packetLength;
	}
	public void setPacketLength(int packetLength) {
		this.packetLength = packetLength;
	}
	public int getCmdtype() {
		return cmdtype;
	}
	public void setCmdtype(int cmdtype) {
		this.cmdtype = cmdtype;
	}
	
	
	public int getSequenceNumber() {
		return sequenceNumber;
	}
	public void setSequenceNumber(int sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}
	@Override
	public void setBody(ByteBuffer buffer) {
		this.setPacketLength(buffer.getInt());
		this.setCmdtype(buffer.getInt());
		this.setSequenceNumber( buffer.getInt());
	}
	
	@Override
	public ByteBuffer getBody() {
		ByteBuffer headerBuffer = ByteBuffer.allocate(CMPP.CMPP_HEAD_LEN);
		headerBuffer.putInt(getPacketLength());
		headerBuffer.putInt(getCmdtype());
		headerBuffer.putInt(getSequenceNumber());
		return headerBuffer;
	}
	
	@Override
	public void setData(ByteBuffer buffer) {
		setBody(buffer);
	}
	
	@Override
	public ByteBuffer getData() {
		return getBody();
	}
	
}
