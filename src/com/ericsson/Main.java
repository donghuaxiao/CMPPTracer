package com.ericsson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.ericsson.filter.DestTerminalFilter;
import com.ericsson.filter.TcpFilter;
import com.ericsson.util.PropertiesUtils;
import com.ericsson.util.TimerScheduler;
import com.ericsson.util.Utils;

public class Main {

    public static void main(String[] args) {
        
        Options options = new Options();
        Option conf = new Option("f", "conf", true, "configuration file of CMPPParser");
        
        Option msisdn = new Option("m", "msisdn", true, "The Destination terminal Id to tracer");
        
        options.addOption(conf);
        options.addOption(msisdn);
        
        CommandLineParser cmdparser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        
        CommandLine cmd = null;
        
        try {
            cmd = cmdparser.parse(options, args);
        } catch (ParseException e) {
            System.out.println( e.getMessage());
            formatter.printHelp("utility-name", options);
            System.exit(1);
        }
        
        String confFile = cmd.getOptionValue("conf");
        String destTerminalId = cmd.getOptionValue("msisdn");
        
        Properties props = null;
        

        //String filter = "tcp port 7890";
        String filter = null;
        String fileName = "packet-out.pcap";
        
        String confFileName = "conf/cmpp.conf";
        
        Properties confProperties = null;
        
        try {
        	confProperties = PropertiesUtils.getProperties(confFileName);
        } catch(IOException ex) {
        	System.out.println(ex.getMessage());
        	System.exit(1);
        }
        
       String devs = confProperties.getProperty("devs");
       
       List<String> alldevs = new ArrayList<>();
       for (String dev : devs.split(",")) {
    	   alldevs.add( dev );
       }

      		
		if (confFile != null) {
			destTerminalId = null;
		  try {
				props = PropertiesUtils.loadProperties(confFile);
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(1);
			}
	        
	        String msisdns = props.getProperty("msisdns");
	        String[] phoneNumbers = msisdns.split(",");
	        String s_startTime = props.getProperty("starttime");
	        String s_endTime = props.getProperty("stoptime");        

	        DestTerminalFilter terminalFilter = new DestTerminalFilter(phoneNumbers);

	        TracerExecutor executor = new TracerExecutor( alldevs, filter,  fileName, terminalFilter);
	        
	        TimerScheduler scheduler = new TimerScheduler(executor, Utils.getTime(s_startTime),
	        		Utils.getTime(s_endTime));
	        
		} else if ( destTerminalId != null) {

	        DestTerminalFilter terminalFilter = new DestTerminalFilter(new String[]{destTerminalId});

	        TracerExecutor executor = new TracerExecutor( alldevs,filter, fileName, terminalFilter);
	        executor.start();
	        
		} else {
	        TracerExecutor executor = new TracerExecutor(alldevs, filter,  fileName, new TcpFilter());
	        executor.start();
		}
		
    }

}
