package com.ericsson.writer;

import java.util.concurrent.BlockingQueue;

import org.jnetpcap.PcapDumper;
import org.jnetpcap.nio.JBuffer;
import org.jnetpcap.packet.PcapPacket;

public class FileWriter implements Runnable {

    private BlockingQueue<PcapPacket> queue;

    private PcapDumper dumper;
    
    private boolean isExit = false;
    
    private class ExitHandler extends Thread {

        @Override
        public void run() {
            System.out.println("Exit...");
            isExit = true;
        }
       
    }

    public FileWriter(BlockingQueue<PcapPacket> queue, PcapDumper dumper) {
        this.queue = queue;
        this.dumper = dumper;
        Runtime.getRuntime().addShutdownHook( new ExitHandler());
    }

    public void run() {
        while ( !isExit || queue.isEmpty() ) {
            try {
                PcapPacket packet = queue.take();
                JBuffer jbuf = new JBuffer(packet.getTotalSize());
                packet.transferTo(jbuf);
                dumper.dump(packet.getCaptureHeader(), jbuf);
                System.out.println(packet.toHexdump());
            } catch (InterruptedException e) {
                dumper.flush();
                e.printStackTrace();
            }

        }
        dumper.flush();
        dumper.close();
    }
    
}
