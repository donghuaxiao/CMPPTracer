package test;

import java.io.OutputStream;
import java.util.concurrent.BlockingQueue;

/**
 *
 * @author root
 */
public class SendMessageThread extends Thread{
    
    private OutputStream output = null;
    private BlockingQueue queue = null;
    
    public SendMessageThread(OutputStream output, BlockingQueue queue) {
        this.output = output;
        this.queue = queue;
    }

    @Override
    public void run() {
        while( true) {
            
        }
    }
    
}
