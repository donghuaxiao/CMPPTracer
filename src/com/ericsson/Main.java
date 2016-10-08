package com.ericsson;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.jnetpcap.Pcap;
import org.jnetpcap.PcapBpfProgram;
import org.jnetpcap.PcapDumper;
import org.jnetpcap.PcapIf;

import com.ericsson.filter.DestTerminalFilter;
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
        

        String interfaceName = "\\Device\\NPF_{DF7EFF6C-DFBF-4E18-B418-4C33D41257FA}";

        List<PcapIf> alldevs = new ArrayList<PcapIf>();
        StringBuilder errbuf = new StringBuilder();
        if (Pcap.findAllDevs(alldevs, errbuf) != Pcap.OK) {
            System.err.println("Can't read list of device, error is :" + errbuf.toString());
            System.exit(1);
        }

        PcapBpfProgram filter = new PcapBpfProgram();

        PcapIf targetDev = null;

        for (PcapIf eth : alldevs) {
            if (interfaceName.equals(eth.getName())) {
                targetDev = eth;
                break;
            }
        }

        if (targetDev == null) {
            System.err.println("Failed to open interface " + interfaceName);
            return;
        }

        Pcap pcap = Pcap.openLive(targetDev.getName(), 64 * 1024, Pcap.MODE_PROMISCUOUS, 10 * 1000, errbuf);
        if (pcap == null) {
            System.err.println("Error while opening device for capture: " + errbuf.toString());
            return;
        }


		if (pcap.compile(filter, "tcp port 7890", 0, 0) != Pcap.OK) {
			System.err.println("Failed compile filter: " + pcap.getErr());
			System.exit(1);
		}

		if (pcap.setFilter(filter) != Pcap.OK) {
			System.err.println("Failed to set Filter: " + pcap.getErr());
			System.exit(1);
		}
		
		if (confFile != null) {
			destTerminalId = null;
		  try {
				props = Utils.loadProperties(confFile);
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(1);
			}
	        
	        String msisdns = props.getProperty("msisdns");
	        String[] phoneNumbers = msisdns.split(",");
	        String s_startTime = props.getProperty("starttime");
	        String s_endTime = props.getProperty("stoptime");
	        
	        PcapDumper dumper = pcap.dumpOpen("out.pcap");

	        DestTerminalFilter terminalFilter = new DestTerminalFilter(phoneNumbers);

	        TracerExecutor executor = new TracerExecutor(pcap, dumper, terminalFilter);
	        
	        TimerScheduler scheduler = new TimerScheduler(executor, Utils.getTime(s_startTime),
	        		Utils.getTime(s_endTime));
		} else if ( destTerminalId != null) {
			
			PcapDumper dumper = pcap.dumpOpen(destTerminalId + ".pcap");

	        DestTerminalFilter terminalFilter = new DestTerminalFilter(new String[]{destTerminalId});

	        TracerExecutor executor = new TracerExecutor(pcap, dumper, terminalFilter);
	        executor.start();
	        
		} else {
			PcapDumper dumper = pcap.dumpOpen("out.pcap");
	        TracerExecutor executor = new TracerExecutor(pcap, dumper, null);
	        executor.start();
		}
		
    }

}
