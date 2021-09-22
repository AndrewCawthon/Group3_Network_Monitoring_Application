package com.anthonyeden.jnm;

import java.util.HashMap;
import java.util.Iterator;

public final class MonitorState{
	
	/**	Construct a new MonitorState.
	
		@param value An int value
	*/
	
	private MonitorState(int value){
		this.value = value;
	}
	
	/**	Get an int value.  Useful for case statements.
	
		@return The int value
	*/
	
	public int getValue(){
		return value;
	}
	
	/**	Return a String representation of the state.
	
		@return A String represenation of the state
	*/
	
	public String toString(){
		switch(value){
			case STATE_OK:
				return "OK";
			case STATE_DOWN:
				return "Down";
			case STATE_FAILED:
				return "Failed";
			case STATE_ERROR:
				return "Error";
			case STATE_TIMEOUT:
				return "Timeout";
			case STATE_NOT_TESTED:
				return "Not Tested";
			default:
				return "Unknown";
		}
	}
	
	/**	Convert a String to a MonitorState object.
	
		@param str The String
		@return The MonitorState or null
	*/
	
	public static MonitorState stringToState(String str){
		return (MonitorState)stateMap.get(str);
	}
	
	/**	Convert a MonitorState to a String object.
	
		@param state The MonitorState
		@return The String or null
	*/
	
	public static String stateToString(MonitorState state){
		Iterator keys = stateMap.keySet().iterator();
		while(keys.hasNext()){
			String key = (String)keys.next();
			if(((MonitorState)stateMap.get(key)).equals(state)){
				return key;
			}
		}
		return null;
	}
	
	/**	Get an array of all possible monitor states.
	
		@return An array of all possible monitor states
	*/
	
	public static MonitorState[] listAll(){
		return list;
	}
	
	public static final int STATE_OK = 1;
	public static final int STATE_DOWN = 2;
	public static final int STATE_ERROR = 3;
	public static final int STATE_FAILED = 4;
	public static final int STATE_TIMEOUT = 5;
	public static final int STATE_NOT_TESTED = 6;
	
	/**	The monitor is running fine. */
	public static final MonitorState OK = new MonitorState(STATE_OK);
	
	/**	The monitor target is down. */
	public static final MonitorState DOWN = new MonitorState(STATE_DOWN);
	
	/**	An error occurred in the monitor (not necessarily with the
	    target.
	*/
	public static final MonitorState ERROR = new MonitorState(STATE_ERROR);
	
	/**	Connection established, but the monitor test failed. */
	public static final MonitorState FAILED = new MonitorState(STATE_FAILED);
	
	/**	The target is timed out. */
	public static final MonitorState TIMEOUT = new MonitorState(STATE_TIMEOUT);
	
	/**	The target has never been tested. */
	public static final MonitorState NOT_TESTED = new MonitorState(STATE_NOT_TESTED);
	
	/**	Array of all MonitorStates available. */
	private static final MonitorState[] list = {
		OK, DOWN, ERROR, FAILED, TIMEOUT, NOT_TESTED
	};
	
	/**	Map of Strings to MonitorState objects. */
	private static final HashMap stateMap = new HashMap();
	
	// initialize a String -> MonitorState map
	static{
		stateMap.put("OK", OK);
		stateMap.put("DOWN", DOWN);
		stateMap.put("ERROR", ERROR);
		stateMap.put("FAILED", FAILED);
		stateMap.put("TIMEOUT", TIMEOUT);
		stateMap.put("NOT_TESTED", NOT_TESTED);
	}
	
	private int value;
	
}
