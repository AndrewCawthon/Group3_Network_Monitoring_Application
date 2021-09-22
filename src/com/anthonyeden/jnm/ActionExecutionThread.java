package com.anthonyeden.jnm;

import java.util.List;
import java.util.Iterator;

public class ActionExecutionThread extends Thread{
	
	public ActionExecutionThread(Monitor monitor, List actions){
		this.monitor = monitor;
		this.actions = actions;
	}
	
	public void run(){
		Iterator iter = actions.iterator();
		while(iter.hasNext()){
			Action action = (Action)iter.next();
			try{
				action.execute(monitor);
			} catch(Exception e){
				e.printStackTrace();
			}
			
			try{
				Thread.sleep(delay);
			} catch(InterruptedException e){
				// do nothing
			}
		}
	}
	
	public int getDelay(){
		return delay;
	}
	
	public void setDelay(int delay){
		this.delay = delay;
	}
	
	/**	Default delay between action execution (1 second). */
	public static final int DEFAULT_DELAY = 1000;
	
	private Monitor monitor;
	private List actions;
	
	private int delay = DEFAULT_DELAY;
	
}
