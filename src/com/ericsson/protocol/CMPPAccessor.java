package com.ericsson.protocol;

import java.nio.ByteBuffer;

public interface CMPPAccessor {

	/**
	 *  设置CMPP 协议包的 消息体
	 * @param buffer
	 */
	public void setBody(ByteBuffer buffer);
	
	/**
	 * 获取CMPP 协议的消息体
	 * @return
	 */
	public ByteBuffer getBody();
	
	/**
	 * 设置整个CMPP 消息
	 * @param buffer
	 */
	public void setData( ByteBuffer buffer);
	
	/**
	 * 获取整个CMPP 消息
	 * @return
	 */
	public ByteBuffer getData();
}
