package de.erdtmann.soft.mainservice.timer;

import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.ejb.Schedule;

import org.jboss.logging.Logger;

import de.erdtmann.soft.mainservice.pool.PoolService;


@Singleton
public class PoolTimer {
	
	Logger log = Logger.getLogger(PoolTimer.class);
	
	@Inject
	PoolService poolService; 
	
	@Schedule(second = "0", minute = "*/5", hour = "*", persistent = false)
	public void alleFuenfMinuten() {
//			coreService.speichereTemperaturen();
	}

	@Schedule(second = "0", minute = "*/10", hour = "*", persistent = false)
	public void alleZehnMinuten() {
			poolService.poolSteuerung();
	}
}
