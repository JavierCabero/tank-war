package util;

import java.util.ArrayList;

/**
 * Manages debug messages. You can specify all up to 5 different debug modes: <br>
 * 
 * <ul>
 * <li><b>Aggressive</b>: show every debug message.</li>
 * <li><b>Active</b>: show every debug message except those from classes in ignore list.</li>
 * <li><b>Normal</b>: show debug messages from classes that are on add list.</li>
 * <li><b>Passive</b>: show debug messages from classes that are on add list and not on ignore list.</li>
 * <li><b>Suppressed</b>: don't show any debug message.</li>
 * </ul>
 * 
 * Debug mode and classes in each list (add/ignore list) can be set on config.cfg file. <br> <br>
 * 
 * @author Javier Cabero Guerra
 * 
 */
public class Debugger {

	public static final int MODE_AGGRESSIVE = +2;
	public static final int MODE_ACTIVE 	= +1;
	public static final int MODE_NORMAL 	=  0;
	public static final int MODE_PASSIVE 	= -1;
	public static final int MODE_SUPPRESSED = -2;
	
	private static int mode = MODE_AGGRESSIVE;
	
	private static ArrayList<String> 	addList = new ArrayList<String>();
	private static ArrayList<String> ignoreList = new ArrayList<String>();
	
	public static void log(String msg) {
		
		// Get caller class name
		String sourceClassName = Thread.currentThread().getStackTrace()[2].getClassName();
		
		switch(mode) {
		
		case MODE_AGGRESSIVE:
			
			// Always print
			System.out.println("[" + sourceClassName + "] " + msg);
			break;
			
		case MODE_ACTIVE:
			
			// Class NOT in ignore list
			if(!ignoreList.contains(sourceClassName)) 
				System.out.println("[" + sourceClassName + "] " + msg);
			
			break;
			
		case MODE_NORMAL:
			
			// Class is IN add list
			if(addList.contains(sourceClassName)) 
				System.out.println("[" + sourceClassName + "] " + msg);
			
			break;
			
		case MODE_PASSIVE:
			
			// Class IN add list and NOT in ignore list
			if(addList.contains(sourceClassName) && !ignoreList.contains(sourceClassName))
				System.out.println("[" + sourceClassName + "] " + msg);
			
			break;
			
		case MODE_SUPPRESSED:
			
			// Nothing :D
			
			break;
		}
	}
	
	/**
	 * This codes are public static vars in Debugger class (e.g. Debugger.MODE_PASSIVE).
	 * 
	 * <ul>
	 * <li><b>Aggressive</b>: show every debug message.</li>
	 * <li><b>Active</b>: show every debug message except those from classes in ignore list.</li>
	 * <li><b>Normal</b>: show debug messages from classes that are on add list.</li>
	 * <li><b>Passive</b>: show debug messages from classes that are on add list and not on ignore list.</li>
	 * <li><b>Suppressed</b>: don't show any debug message.</li>
	 * </ul>
	 * 
	 * @param m is the debug mode code.
	 */
	public static void setMode(int m) {
		
		// Invalid mode
		if(m < -2 || 2 < m) {
			System.out.println("[DEBUGGER] Invalid debug mode (" + m + ")");
			return;
		}
		
		// Valid mode
		mode = m;
	}
}
