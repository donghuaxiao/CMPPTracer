package com.ericsson;

import com.ericsson.filter.DestTerminalFilter;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.jnetpcap.Pcap;
import org.jnetpcap.PcapBpfProgram;
import org.jnetpcap.PcapDumper;
import org.jnetpcap.packet.PcapPacket;

import com.ericsson.writer.FileWriter;
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
import org.jnetpcap.PcapIf;

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
        
        InputStream input = Main.class.getResourceAsStream(confFile);
        
        Properties prop = new Properties();
        try {
            prop.load(input);
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        

        StringBuilder errbuf = new StringBuilder();

        String interfaceName = "eno16777736";

        List<PcapIf> alldevs = new ArrayList<PcapIf>();

        if (Pcap.findAllDevs(alldevs, errbuf) != Pcap.OK) {
            System.err.println("Can't read list of device, error is :" + errbuf.toString());
            System.exit(1);
        }

        for (PcapIf dev : alldevs) {
            System.out.println(dev.getDescription());
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

//               String fileName = "ipmc_smsgw_test_03.pcap";
//               
//                
//                URL url = Main.class.getResource(fileName);
//		String pcapFile = url.getFile();
//
//		Pcap pcap = Pcap.openOffline(pcapFile, errbuf);
//
//		if (pcap == null) {
//			System.err.printf("Failed to open File %s to capture: ", pcapFile);
//		}
//		if (pcap.compile(filter, "tcp dst port 7890", 0, 0) != Pcap.OK) {
//			System.err.println("Failed compile filter: " + pcap.getErr());
//			System.exit(1);
//		}
//
//		if (pcap.setFilter(filter) != Pcap.OK) {
//			System.err.println("Failed to set Filter: " + pcap.getErr());
//			System.exit(1);
//		}
        PcapDumper dumper = pcap.dumpOpen("out.pcap");

        BlockingQueue<PcapPacket> captureQueue = new LinkedBlockingQueue<PcapPacket>();
        BlockingQueue<PcapPacket> writeQueue = new LinkedBlockingQueue<PcapPacket>();

        PacketCapture capture = new PacketCapture(captureQueue, pcap);
        //capture.setPacketFilter( new CMPPSubmitPacketFilter());
        PacketParser parser = new PacketParser(captureQueue, writeQueue);
        DestTerminalFilter terminalFilter = new DestTerminalFilter(new String[]{"8613656197474"});

		//parser.setPacketFilter(terminalFilter);
        Thread writePacket = new Thread(new FileWriter(writeQueue, dumper));

        capture.start();
        parser.start();
        writePacket.start();
        try {
            writePacket.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
