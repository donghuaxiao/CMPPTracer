package com.ericsson.protocol;

import java.nio.ByteBuffer;

import com.ericsson.util.Utils;

public class CMPPConnect extends Request {

	    private String sourceAddr;
	    
	    private String authenticatorSource;
	    
	    private byte version;
	    
	    private int timestamp;


		public String getSourceAddr() {
			return sourceAddr;
		}

		public void setSourceAddr(String sourceAddr) {
			this.sourceAddr = sourceAddr;
		}

		public String getAuthenticatorSource() {
			return authenticatorSource;
		}

		public void setAuthenticatorSource(String authenticatorSource) {
			this.authenticatorSource = authenticatorSource;
		}

		public byte getVersion() {
			return version;
		}

		public void setVersion(byte version) {
			this.version = version;
		}

		public int getTimestamp() {
			return timestamp;
		}

		public void setTimestamp(int timestamp) {
			this.timestamp = timestamp;
		}

		@Override
		public void setBody(ByteBuffer buffer) {
			setSourceAddr(Utils.getStringFromByteBuffer(buffer, 6));
			setAuthenticatorSource(Utils.getStringFromByteBuffer(buffer, 16));
			setVersion(buffer.get());
			setTimestamp(buffer.getInt());
		}

		@Override
		public ByteBuffer getBody() {
			ByteBuffer buffer = ByteBuffer.allocate(27);
			buffer.put( Utils.toBytes(getSourceAddr(), 6));
			buffer.put( Utils.toBytes(getAuthenticatorSource(), 16));
			buffer.put(getVersion());
			buffer.putInt(getTimestamp());
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
