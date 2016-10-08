package com.ericsson.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

public class Utils {

	public static String join( String[] items, String joinStr ) {
		if (items == null) {
			return null;
		}
		
		if (items.length == 0) {
			return "";
		}
		
		StringBuffer buffer = new StringBuffer();
		
		buffer.append(items[0]);
		for (int i = 1; i < items.length; i++) {
			buffer.append( joinStr);
			buffer.append(items[i]);
		}
		
		return buffer.toString();
	}
	
	public static Date getTime( int hh, int minute, int second) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, hh);
		calendar.set(Calendar.MINUTE, minute);
		calendar.set(Calendar.SECOND, second);
		return calendar.getTime();
	}
	
	public static Properties loadProperties( String filename) throws IOException {
		File file= new File(filename);
		InputStream input = null;
		try {
			input = new FileInputStream(file);
		} catch( FileNotFoundException ex ) {
			throw new IOException(ex.getMessage());
		}
		Properties prop = new Properties();
		prop.load(input);
		return prop;
	}
	
	public static Date getTime(String time) {
		String[] times = time.split(":");
		return Utils.getTime(Integer.parseInt(times[0]), 
				Integer.parseInt(times[1]), Integer.parseInt(times[2]));
		
	}
	
	public static String getStringFromByteBuffer( ByteBuffer buffer, int length) {
		byte[] strbuf = new byte[length];
		buffer.get(strbuf);
		try {
			return new String(strbuf,"UTF8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return new String(strbuf);
	}
	
	public static byte[] toBytes(String str, int length) {
		str = (str == null) ? "" : str;
		String format = String.format("%%%ds", length);
		return String.format(format, str).getBytes();
	}
	
	public static String[] getDestTerminalId( String destids ) {
		int length = destids.length();
		int count = length / 32;
		String[] destTerminals = new String[count];
		int start = 0, end=32;
		for ( int i=0; i < count; i++) {
			destTerminals[i] = destids.substring(start, end).trim();
			start += 32;
			end += 32;
		}
		return destTerminals;
	}
}
