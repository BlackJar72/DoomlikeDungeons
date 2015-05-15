package jaredbgreat.dldungeons.debug;

import java.util.logging.Logger;

public class Logging {
	private static Logger logger;
	private static Logging log;
	
	private Logging() {
		logger = Logger.getGlobal();
	}
	
	
	public static Logging getInstance() {
		if(log == null) {
			log = new Logging();
		}
		return log;
	}
	
	
	public static void LogError(String error) {
		if(log == null) {
			log = new Logging();
		}
		System.err.println(error);
		Logger logger = Logger.getGlobal();
		logger.severe(error);
	}
	
	
	public static void LogInfo(String info) {
		if(log == null) {
			log = new Logging();
		}
		//System.out.println(info);
		Logger logger = Logger.getGlobal();
		logger.info(info);
	}

}
