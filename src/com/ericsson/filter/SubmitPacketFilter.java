package com.ericsson.filter;

import com.ericsson.protocol.CMPPSubmit;

/**
 *  CMPP Submit �����˽ӿڣ� ���ƥ����򷵻�true, ���򷵻�false
 * @author donghua
 *
 */
public interface SubmitPacketFilter {

	public boolean isMatch(CMPPSubmit submit);
}
