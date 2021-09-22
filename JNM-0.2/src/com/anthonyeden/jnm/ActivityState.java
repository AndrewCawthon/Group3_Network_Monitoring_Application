package com.anthonyeden.jnm;

public final class ActivityState{
	
	/**	Construct a new ActivityState.
	
		@param value An int value
	*/
	
	private ActivityState(int value){
		this.value = value;
	}
	
	/**	Get an int value.  Useful for case statements.
	
		@return The int value
	*/
	
	public int getValue(){
		return value;
	}
	
	/**	Return a String representation of the activity state.
	
		@return A String represenation of the activity state
	*/
	
	public String toString(){
		switch(value){
			case STATE_STARTING:
				return "Starting";
			case STATE_RUNNING:
				return "Running";
			case STATE_STOPPING:
				return "Stopping";
			case STATE_STOPPED:
				return "Stopped";
			case STATE_TESTING:
				return "Testing";
			default:
				return "Unknown";
		}
	}
	
	public static final int STATE_STARTING = 1;
	public static final int STATE_RUNNING = 2;
	public static final int STATE_STOPPING = 3;
	public static final int STATE_STOPPED = 4;
	public static final int STATE_TESTING = 5;
	
	/**	The monitor is starting. */
	public static final ActivityState STARTING = new ActivityState(STATE_STARTING);
	
	/**	The Monitor is running */
	public static final ActivityState RUNNING = new ActivityState(STATE_RUNNING);
	
	/**	The Monitor is stopping. */
	public static final ActivityState STOPPING = new ActivityState(STATE_STOPPING);
	
	/**	The Monitor is stopped. */
	public static final ActivityState STOPPED = new ActivityState(STATE_STOPPED);
	
	/**	The Monitor is testing. */
	public static final ActivityState TESTING = new ActivityState(STATE_TESTING);
	
	private int value;
	
}
