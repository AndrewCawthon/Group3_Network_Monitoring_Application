package com.anthonyeden.jnm;

public class MonitorThread implements Runnable{
	
	public MonitorThread(Monitor monitor){
		this.monitor = monitor;
	}
	
	public synchronized void startMonitor(){
		if(running){
			return;
		}
		
		monitor.setActivity(ActivityState.STARTING);
		
		running = true;
		thread = new Thread(this);
		thread.start();
	}
	
	public synchronized void stopMonitor(){
		if(!running){
			return;
		}
		
		monitor.setActivity(ActivityState.STOPPING);
		
		running = false;
		thread.interrupt();
	}
	
	public void run(){
		monitor.setActivity(ActivityState.RUNNING);
		LOOP: while(running){
			monitor.setActivity(ActivityState.TESTING);
			monitor.execute();
			monitor.setActivity(ActivityState.RUNNING);
			
			try{
				Thread.sleep(monitor.getDelay());
			} catch(InterruptedException e){
				if(!running){
					break LOOP;
				}
			}
		}
		monitor.setActivity(ActivityState.STOPPED);
	}
	
	private Monitor monitor;
	private Thread thread;
	private boolean running;
	
}
