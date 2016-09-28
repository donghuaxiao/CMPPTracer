package com.ericsson.protocol;

public class CMPPConnect {

	  private int pktLength;
	    private int cmdType;
	    private int seq;
	    
	    private String sourceAddr;
	    
	    private String authenticatorSource;
	    
	    private byte version;

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
	    
	    
}
