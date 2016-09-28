package com.ericsson.protocol;

public class CMPPHeader {
	protected int packetLength;
	
	protected int cmdtype;
	
	protected int seq;
	
	public CMPPHeader() {}
	public CMPPHeader( int length, int type, int seq) {
		this.packetLength = length;
		this.cmdtype  = type;
		this.seq = seq;
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
	public int getSeq() {
		return seq;
	}
	public void setSeq(int seq) {
		this.seq = seq;
	}
	
	
}
