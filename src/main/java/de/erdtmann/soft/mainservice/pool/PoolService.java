package de.erdtmann.soft.mainservice.pool;

import java.time.LocalTime;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import org.jboss.logging.Logger;

import de.erdtmann.soft.mainservice.core.CoreRepository;
import de.erdtmann.soft.mainservice.core.entities.KonfigurationE;
import de.erdtmann.soft.mainservice.exceptions.PoolPiException;
import de.erdtmann.soft.mainservice.exceptions.PvException;
import de.erdtmann.soft.mainservice.model.PoolKonfig;
import de.erdtmann.soft.mainservice.poolpi.PoolPiService;
import de.erdtmann.soft.mainservice.pv.PvService;
import de.erdtmann.soft.utils.KonfigNames;
import de.erdtmann.soft.utils.pool.Heizung;
import de.erdtmann.soft.utils.pool.Pumpe;
import de.erdtmann.soft.utils.pv.PvDaten;

@RequestScoped
public class PoolService {

	Logger log = Logger.getLogger(PoolService.class);

	@Inject
	CoreRepository coreRepo;

	@Inject
	PoolPiService poolPi;

	@Inject
	PvService pvService;

	private Map<KonfigNames, KonfigurationE> konfiguration;

	private static final String FLAG_TRUE = "1";
	private static final String FLAG_FALSE = "0";
	private static final String WERT_EIN = "ein";
	private static final String WERT_AUS = "aus";
	private static final int SLEEP_TIME = 30000;

	
	public void ladeKonfiguration() {
		List<KonfigurationE> konfigDBWerte = coreRepo.ladeKonfiguration();

		if (konfiguration == null) {
			konfiguration = new EnumMap<>(KonfigNames.class);
		}

		konfiguration.clear();

		for (KonfigurationE konfigE : konfigDBWerte) {
			for (KonfigNames konfEnum : KonfigNames.values()) {
				if (konfigE.getRubrik().equals(konfEnum.getRubrik())
						&& konfigE.getKategorie().equals(konfEnum.getKategorie())) {
					konfiguration.put(konfEnum, konfigE);
				}
			}
		}
	}

	public void poolSteuerung() {
		ladeKonfiguration();
		
		log.info("Pool Automatik: " + isPoolAutomatikEin());
		
		if (isPoolAutomatikEin()) {
			try {
				// PV Daten holen
				PvDaten pvDaten = pvService.ladePvHomeDaten(999, 999, 999, 999, 999);

				log.info("PV Leistung: " + pvDaten.getLeistung());
				log.info("Batt Ladung: " + pvDaten.getBattLadung());
				
				boolean isPumpeAn = holePumpenStatus();
				boolean isHeizungAn = holeHeizungStatus();

				boolean isPvMin = isPvUeberMin(pvDaten);
				boolean isPvMax = isPvUeberMax(pvDaten);

				boolean isBattMin = isBattUeberMin(pvDaten);
				boolean isBattMax = isBattUeberMax(pvDaten);

				boolean schaltePumpeAn = false;
				boolean schalteHeizungAn = false;

				log.info("Pumpe an: " + isPumpeAn);
				log.info("Heizung an: " + isHeizungAn);
				log.info("PvMin: " + isPvMin);
				log.info("PvMax: " + isPvMax);
				log.info("BattMin: " + isBattMin);
				log.info("BattMax: " + isBattMax);

				if (isPvMin && isBattMin) {
					schaltePumpeAn = true;
					if (isPvMax && isBattMax) {
						schalteHeizungAn = true;
					}
				}

				if (schaltePumpeAn == isPumpeAn && schalteHeizungAn == isHeizungAn) {
					log.info("Keine Änderung.");
				} else {
					log.info("Zustand hat sich geändert.");
					// Heizung soll eingeschaltet werden
					if (schalteHeizungAn && !isPoolWinterEin()) {
						// Pumpe ausschalten wenn an
						isPumpeAn = schaltePumpeAus(isPumpeAn);
						// Heizung einschalten wenn aus
						isHeizungAn = schalteHeizungEin(isHeizungAn);
						// Pumpe einschalten wenn aus
						isPumpeAn = schaltePumpeEin(isPumpeAn);
						// Heizung soll ausgeschaltet werden
					} else {
						// Pumpe ausschalten wenn an
						isPumpeAn = schaltePumpeAus(isPumpeAn);
						// Heizung ausschalten wenn an
						isHeizungAn = schalteHeizungAus(isHeizungAn);
						// Pumpe soll eingeschaltet werden
						if (schaltePumpeAn) {
							// Pumpe einschalten wenn aus
							isPumpeAn = schaltePumpeEin(isPumpeAn);
						}
					}
				}
			} catch (Exception e) {
				log.error("Fehler in der PoolSteuerung");
				log.error(e.getMessage());
			}

		}
	}

	public PoolKonfig getPoolDaten() {
		ladeKonfiguration();
		
		return PoolKonfig.builder().withAutomatik(isPoolAutomatikEin()).withWinter(isPoolWinterEin())
				.withPvMin(holeMinPv()).withPvMax(holeMaxPv()).withBattMin(holeMinBatt()).withBattMax(holeMaxBatt())
				.withAnfang(holeAnfang()).withEnde(holeEnde()).build();
	}

	private boolean isPoolAutomatikEin() {
		return (konfiguration.get(KonfigNames.POOL_AUTOMATIK).getWert().equals(FLAG_TRUE));
	}

	private boolean isPoolWinterEin() {
		return (konfiguration.get(KonfigNames.POOL_WINTER).getWert().equals(FLAG_TRUE));
	}

	public int setPoolAutomatik(String wert) {
		switch (wert) {
		case WERT_EIN:
			return saveKonfigWert(FLAG_TRUE, KonfigNames.POOL_AUTOMATIK);
		case WERT_AUS:
			return saveKonfigWert(FLAG_FALSE, KonfigNames.POOL_AUTOMATIK);
		default:
			return 0;
		}
	}

	public int setPoolWinter(String wert) {
		switch (wert) {
		case WERT_EIN:
			return saveKonfigWert(FLAG_TRUE, KonfigNames.POOL_WINTER);
		case WERT_AUS:
			return saveKonfigWert(FLAG_FALSE, KonfigNames.POOL_WINTER);
		default:
			return 0;
		}
	}

	private int holeMinPv() {
		return Integer.parseInt(konfiguration.get(KonfigNames.POOL_PV_MIN).getWert());
	}

	private int holeMaxPv() {
		return Integer.parseInt(konfiguration.get(KonfigNames.POOL_PV_MAX).getWert());
	}

	private int holeMinBatt() {
		return Integer.parseInt(konfiguration.get(KonfigNames.POOL_BATT_MIN).getWert());
	}

	private int holeMaxBatt() {
		return Integer.parseInt(konfiguration.get(KonfigNames.POOL_BATT_MAX).getWert());
	}

	private LocalTime holeAnfang() {
		return (LocalTime.parse(konfiguration.get(KonfigNames.POOL_ZEIT_ANFANG).getWert()));
	}

	private LocalTime holeEnde() {
		return (LocalTime.parse(konfiguration.get(KonfigNames.POOL_ZEIT_ENDE).getWert()));
	}

	private int saveKonfigWert(String wert, KonfigNames konfig) {
		ladeKonfiguration();
		
		KonfigurationE konfigE = konfiguration.get(konfig);

		konfigE.setWert(wert);

		return coreRepo.saveKonfigurationsItem(konfigE);
	}

	private boolean holePumpenStatus() throws PoolPiException {
		return poolPi.holePumpenStatus();
	}

	private boolean holeHeizungStatus() throws PoolPiException {
		return poolPi.holeHeizungStatus();
	}

	private boolean schaltePumpeEin(boolean isPumpeAn) throws PoolPiException, InterruptedException {
		if (!isPumpeAn) {
			schaltePumpe(Pumpe.AN);
			Thread.sleep(1000);
		}
		return holePumpenStatus();
	}

	private boolean schaltePumpeAus(boolean isPumpeAn) throws PoolPiException, InterruptedException {
		if (isPumpeAn) {
			schaltePumpe(Pumpe.AUS);
			Thread.sleep(1000);
		}
		return holePumpenStatus();
	}

	private int schaltePumpe(Pumpe zustand) throws PoolPiException {
		return poolPi.schaltePumpe(zustand);
	}

	private boolean schalteHeizungAus(boolean isHeizungAn) throws PoolPiException, InterruptedException {
		if (isHeizungAn) {
			schalteHeizung(Heizung.AUS);
			Thread.sleep(SLEEP_TIME);
		}
		return holeHeizungStatus();
	}

	private boolean schalteHeizungEin(boolean isHeizungAn) throws PoolPiException, InterruptedException {
		if (!isHeizungAn) {
			schalteHeizung(Heizung.AN);
			Thread.sleep(SLEEP_TIME);
		}
		return holeHeizungStatus();
	}

	private int schalteHeizung(Heizung status) throws PoolPiException {
		return poolPi.schalteHeizung(status);
	}

	public boolean isPvUeberMin(PvDaten pvDaten) throws PvException {
		return pvDaten.getLeistung() > Integer.parseInt(konfiguration.get(KonfigNames.POOL_PV_MIN).getWert());
	}

	public boolean isPvUeberMax(PvDaten pvDaten) throws PvException {
		return pvDaten.getLeistung() > Integer.parseInt(konfiguration.get(KonfigNames.POOL_PV_MAX).getWert());
	}

	public boolean isBattUeberMin(PvDaten pvDaten) throws PvException {
		return pvDaten.getBattLadung() > Integer.parseInt(konfiguration.get(KonfigNames.POOL_BATT_MIN).getWert());
	}

	public boolean isBattUeberMax(PvDaten pvDaten) throws PvException {
		return pvDaten.getBattLadung() > Integer.parseInt(konfiguration.get(KonfigNames.POOL_BATT_MAX).getWert());
	}


}
