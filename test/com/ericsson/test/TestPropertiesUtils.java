package com.ericsson.test;

import java.io.IOException;
import java.util.Properties;

import org.junit.Test;

import com.ericsson.util.PropertiesUtils;

public class TestPropertiesUtils {
	
	static final String CONF_FILE = "conf/cmpp.conf";

	@Test
	public void testLoadPropertiesFile() {
		try {
			Properties props = PropertiesUtils.getProperties(CONF_FILE);
			String devs = props.getProperty("devs");
			System.out.println( devs );
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
