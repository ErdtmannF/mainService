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

	private PvService pvService;

	PvTimer() {
	}

	@Inject
	PvTimer(PvService pvService) {
		this.pvService = pvService;
	}

	@Schedule(second = "0", minute = "*/2", hour = "*", persistent = false)
	public void alleZweiMinuten() {
		try {
			pvService.speichereDaten();

		} catch (PvException e) {
			log.error("Fehler beim Speichern der PV Daten");
			log.error(e.getMessage());
		}
	}

	@Schedule(second = "0", minute = "30", hour = "23", persistent = false)
	public void einmalProTag() {
		try {
			pvService.speichereErzeugung();
		} catch (PvException e) {
			log.error("Fehler beim Speichern der Erzeugungsdaten");
			log.error(e.getMessage());
		}
	}

}
