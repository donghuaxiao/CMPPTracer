package com.ericsson.protocol;

public abstract class Request implements CMPPAccessor {

	protected CMPPHeader header;
	
	public Request() {		
	}
	
	public CMPPHeader getHeader() {
		return header;
	}

	public void setHeader(CMPPHeader header) {
		this.header = header;
	}
	
}
