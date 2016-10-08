package com.ericsson.filter;

import com.ericsson.protocol.CMPPSubmit;

/**
 *  CMPP Submit 包过滤接口， 如果匹配规则返回true, 否则返回false
 * @author donghua
 *
 */
public interface SubmitPacketFilter {

	public boolean isMatch(CMPPSubmit submit);
}
