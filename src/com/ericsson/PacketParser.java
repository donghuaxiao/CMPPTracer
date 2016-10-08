package com.ericsson;

import java.util.concurrent.BlockingQueue;

import org.jnetpcap.packet.PcapPacket;

import com.ericsson.filter.DefaultPacketFilter;
import com.ericsson.filter.PacketFilter;

public class PacketParser implements Runnable {

    private BlockingQueue<PcapPacket> shareQueue;

    private BlockingQueue<PcapPacket> cmppSubmitQueue;

    private PacketFilter packetFilter = new DefaultPacketFilter();
    
    private boolean isExit = false;
    
    private Thread _thread;

    public PacketParser(BlockingQueue<PcapPacket> readQueue, BlockingQueue<PcapPacket> writeQueue) {
        this.shareQueue = readQueue;
        this.cmppSubmitQueue = writeQueue;
    }

    public void run() {
        while ( !isExit ) {
            try {
                PcapPacket packet = shareQueue.take();
                if (packetFilter.isMatch(packet)) {
                    cmppSubmitQueue.put(packet);
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    public PacketFilter getPacketFilter() {
        return packetFilter;
    }

    public void setPacketFilter(PacketFilter packetFilter) {
        this.packetFilter = packetFilter;
    }
    
    public void stop() {
        this.isExit = true;
    }
    
    public void start() {
        this._thread = new Thread(this);
        this._thread.start();
    }
    
    public void join() throws InterruptedException {
    	this._thread.join();
    }
}
