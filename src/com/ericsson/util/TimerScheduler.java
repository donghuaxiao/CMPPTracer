package com.ericsson.util;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import com.ericsson.TracerExecutor;

public class TimerScheduler {

	private Timer startTimer;
	
	private Timer stopTimer;
	
	public TimerScheduler( TracerExecutor executor, Date start, Date stop) {
		startTimer = new Timer();
		stopTimer = new Timer();
		startTimer.schedule(new TimerTask() {
			public void run() {
				System.out.println("Start to Capture");
				startTimer.cancel();
				executor.start();
				
			}}, start);
		
		stopTimer.schedule( new TimerTask() {

			@Override
			public void run() {
				System.out.println("stop capture");
				stopTimer.cancel();
				executor.stop();
				System.exit(1);
			}}, stop);
		
	}
}
