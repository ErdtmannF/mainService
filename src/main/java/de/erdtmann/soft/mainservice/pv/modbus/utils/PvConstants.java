package de.erdtmann.soft.mainservice.pv.modbus.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.jboss.logging.Logger;


public final class PvConstants {
	
	private static final String PROP_FILE = "/prop.properties";
	public static final String INVERTER_URL = "192.168.178.71";
	public static final int INVERTER_PORT = 1502;
	public static final int INVERTER_UNIT_ID = 71;
	
//	static {
//		Logger log = Logger.getLogger(PvConstants.class);
//		
//		Properties prop = new Properties();
//		
//	    try {
//	    	
//	    	
//	    	InputStream inStream = ClassLoader.getSystemResourceAsStream(PROP_FILE);
//			prop.load(inStream);
//			log.info("Properties geladen!");
//		} catch (IOException e) {
//			log.error("Fehler beim lesen der Properties: " + e.getMessage());
//		}
//	   
//	    INVERTER_URL = prop.getProperty("kostal_ip");
//	    INVERTER_PORT = Integer.parseInt(prop.getProperty("kostal_port"));
//	    INVERTER_UNIT_ID = Integer.parseInt(prop.getProperty("kostal_unitId"));
//	  }
	private PvConstants() {}


	
}
