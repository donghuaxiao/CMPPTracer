package com.ericsson.protocol;

import java.nio.ByteBuffer;

public class CMPPSubmitResp extends Response {
	
	private byte[] msgId = new byte[8];

	private int result;


	public byte[] getMsgId() {
		return msgId;
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	@Override
	public void setBody(ByteBuffer buffer) {
		buffer.get(this.msgId);
		setResult(buffer.getInt());
	}

	@Override
	public ByteBuffer getBody() {
		ByteBuffer buffer = ByteBuffer.allocate(12);
		buffer.put(this.msgId);
		buffer.putInt(getResult());
		return buffer;
	}

	@Override
	public void setData(ByteBuffer buffer) {
		this.header.setData(buffer);
		this.setBody(buffer);
	}

	@Override
	public ByteBuffer getData() {
		ByteBuffer buffer = this.header.getData();
		buffer.put( this.getBody().array());
		return buffer;
	}
	
}
