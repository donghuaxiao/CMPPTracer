package com.ericsson.protocol;

import java.nio.ByteBuffer;

import com.ericsson.util.Utils;

public class CMPPConnectResp extends Response {

	private int status = 0;
	
	private String authenticatorISMG = "";
	
	private byte version;

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getAuthenticatorISMG() {
		return authenticatorISMG;
	}

	public void setAuthenticatorISMG(String authenticatorISMG) {
		this.authenticatorISMG = authenticatorISMG;
	}

	public byte getVersion() {
		return version;
	}

	public void setVersion(byte version) {
		this.version = version;
	}

	@Override
	public void setBody(ByteBuffer buffer) {
		setStatus(buffer.getInt());
		setAuthenticatorISMG(Utils.getStringFromByteBuffer(buffer, 16));
		setVersion(buffer.get());
	}

	@Override
	public ByteBuffer getBody() {
		ByteBuffer buffer = ByteBuffer.allocate(21);
		buffer.putInt(getStatus());
		buffer.put(Utils.toBytes(getAuthenticatorISMG(),16));
		buffer.put(getVersion());
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
		ByteBuffer bodyBuffer = this.getBody();
		buffer.put(bodyBuffer.array());
		return buffer;
	}
}
