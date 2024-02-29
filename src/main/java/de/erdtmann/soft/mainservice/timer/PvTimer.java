package de.erdtmann.soft.mainservice.timer;

import javax.ejb.Singleton;
import javax.ejb.Schedule;
import javax.inject.Inject;

import org.jboss.logging.Logger;

import de.erdtmann.soft.mainservice.exceptions.PvException;
import de.erdtmann.soft.mainservice.pv.PvService;


@Singleton
public class PvTimer {

	Logger log = Logger.getLogger(PvTimer.class);
	
	@Inject
	PvService pvService;
	
	@Schedule(second = "0", minute = "*/2", hour = "*", persistent = false)
	public void alleZweiMinuten() {
			try {
				pvService.speichereDaten();
			} catch (PvException e) {
				log.error("Fehler beim Speichern der PV Daten");
				log.error(e.getMessage());
			}
	}

//	@Schedule(second = "0", minute = "*/5", hour = "*", persistent = false)
//	public void alleFuenfMinuten() {
//			
//	}

}
