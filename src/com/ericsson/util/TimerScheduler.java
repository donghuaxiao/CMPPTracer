package com.ericsson.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import com.ericsson.TracerExecutor;

public class TimerScheduler {

	private Timer startTimer;
	
	private Timer stopTimer;
	
	private DateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	
	public TimerScheduler( TracerExecutor executor, Date start, Date stop) {
		startTimer = new Timer();
		stopTimer = new Timer();
		startTimer.schedule(new TimerTask() {
			public void run() {
				System.out.println("Start to Capture:" + formater.format(start));
				startTimer.cancel();
				executor.start();
				
			}}, start);
		
		stopTimer.schedule( new TimerTask() {

			@Override
			public void run() {
				System.out.println("stop capture: " + formater.format(stop));
				stopTimer.cancel();
				executor.stop();
				System.exit(1);
			}}, stop);
		
	}
}
