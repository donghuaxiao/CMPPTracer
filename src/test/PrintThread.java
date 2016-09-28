package test;

public class PrintThread implements Runnable {

	private boolean isExit = false;
	
	private class ExitHandler extends Thread {
		@Override
		public void run() {
			System.out.println( "Capture Ctrl +C in PrintThread");
			isExit = true;
		}
	}
	
	public PrintThread() {
		Runtime.getRuntime().addShutdownHook( new ExitHandler());
	}
	
	public void run() {
		while( !isExit ) {
			System.out.println( "in PrintThread");
		}
		System.out.println( "PrintThread exit");
	}

}
