package com.ericsson.protocol;

public class CMPPSubmitResp {
	
	private int pktLength;
	private int cmdType;
	private int seq;

	private long msgId;

	private byte result;

	public int getPktLength() {
		return pktLength;
	}

	public void setPktLength(int pktLength) {
		this.pktLength = pktLength;
	}

	public int getCmdType() {
		return cmdType;
	}

	public void setCmdType(int cmdType) {
		this.cmdType = cmdType;
	}

	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

	public long getMsgId() {
		return msgId;
	}

	public void setMsgId(long msgId) {
		this.msgId = msgId;
	}

	public byte getResult() {
		return result;
	}

	public void setResult(byte result) {
		this.result = result;
	}
	
	
}
