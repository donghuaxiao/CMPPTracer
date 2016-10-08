package com.ericsson.protocol;

public abstract  class Response implements CMPPAccessor{

	protected CMPPHeader header;
	
	public Response() {}

	public CMPPHeader getHeader() {
		return header;
	}

	public void setHeader(CMPPHeader header) {
		this.header = header;
	}
	
}
