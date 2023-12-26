package de.erdtmann.soft.mainservice.pv.rest;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import de.erdtmann.soft.mainservice.exceptions.PvException;
import de.erdtmann.soft.mainservice.pv.PvService;
import de.erdtmann.soft.utils.pv.PvDaten;
import de.erdtmann.soft.utils.pv.model.DcDaten;
import de.erdtmann.soft.utils.pv.model.VerbrauchDaten;

@Path("/pv")
@Consumes({ "application/json" })
@Produces({ "application/json" })
public class pvRestService {

	@Inject
	PvService pvService;
	
	@GET
	@Path("/pvDaten")
	public PvDaten getHomeDaten() {
		PvDaten pv = null;
		try {
			pv =  pvService.ladePvHomeDaten(999,999,999,999,999);
		} catch (PvException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pv;	
	}
	
	@GET
	@Path("/verbrauchDaten")
	public VerbrauchDaten getVerbrauchDaten() {
		VerbrauchDaten verbrauch = null;
		try {
			verbrauch = pvService.ladeVerbrauchDaten();
		} catch (PvException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return verbrauch;
	}

	@GET
	@Path("/dcDaten")
	public DcDaten getDcDaten() {
		DcDaten dcDaten = null;
		try {
			dcDaten = pvService.ladeDcDaten();
		} catch (PvException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dcDaten;
	}

}
