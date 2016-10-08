package test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.jnetpcap.Pcap;
import org.jnetpcap.PcapIf;

import com.ericsson.util.Utils;

public class Test {
	public static String toFixLengthString( String str, int length) {
		String format = String.format("%%%ds", length);
		return String.format(format, str).replace(" ", "0");
	}
	
	public static void main(String[] args) throws IOException {
		Properties props = Utils.loadProperties("H:\\config.properties");
		props.list(System.out);
	}
}
