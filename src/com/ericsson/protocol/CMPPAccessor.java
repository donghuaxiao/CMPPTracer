package com.ericsson.protocol;

import java.nio.ByteBuffer;

public interface CMPPAccessor {

	/**
	 *  ����CMPP Э����� ��Ϣ��
	 * @param buffer
	 */
	public void setBody(ByteBuffer buffer);
	
	/**
	 * ��ȡCMPP Э�����Ϣ��
	 * @return
	 */
	public ByteBuffer getBody();
	
	/**
	 * ��������CMPP ��Ϣ
	 * @param buffer
	 */
	public void setData( ByteBuffer buffer);
	
	/**
	 * ��ȡ����CMPP ��Ϣ
	 * @return
	 */
	public ByteBuffer getData();
}
