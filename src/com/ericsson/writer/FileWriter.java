package com.ericsson.writer;

import java.util.concurrent.BlockingQueue;

import org.jnetpcap.PcapDumper;
import org.jnetpcap.nio.JBuffer;
import org.jnetpcap.packet.PcapPacket;

public class FileWriter implements Runnable {

    private BlockingQueue<PcapPacket> queue;

    /**
     * PcapDumper 负责把数据包写到文件中去
     */
    private PcapDumper dumper;
    
    /**
     *  停止线程标志
     */
    private boolean isStopped = false;
    
    private Thread thread;
    

    public FileWriter(BlockingQueue<PcapPacket> queue, PcapDumper dumper) {
        this.queue = queue;
        this.dumper = dumper;
    }

    public void run() {
        while ( !isStopped || queue.isEmpty() ) {
            try {
                PcapPacket packet = queue.take();
                JBuffer jbuf = new JBuffer(packet.getTotalSize());
                packet.transferTo(jbuf);
                dumper.dump(packet.getCaptureHeader(), jbuf);
            } catch (InterruptedException e) {
                dumper.flush();
                e.printStackTrace();
            }

        }
        dumper.flush();
        dumper.close();
    }
    
    
    public void stop() {
    	this.isStopped = true;
    }
    
    public void start() {
    	thread = new Thread(this);
    	thread.start();
    }
    
    public void join() throws InterruptedException {
    	thread.join();
    }
       
}
