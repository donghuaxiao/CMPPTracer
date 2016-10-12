package com.ericsson.test;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;

public class TestLoadProperties {

	public static void main(String[] args) throws IOException {
		URL url = TestLoadProperties.class.getClassLoader().getResource("conf/cmpp.conf");
		Properties prop = new Properties();
		prop.load(url.openStream());
		String devs = prop.getProperty("devs");
		
		for (String dev : devs.split(",")) {
			System.out.println( dev );
		}
	}
}
