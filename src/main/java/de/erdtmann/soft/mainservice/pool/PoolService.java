package de.erdtmann.soft.mainservice.pool;

import java.time.LocalTime;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
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

@ApplicationScoped
public class PoolService {

	Logger log = Logger.getLogger(PoolService.class);

	private CoreRepository coreRepo;
	private PoolPiService poolPi;
	private PvService pvService;

	private Map<KonfigNames, KonfigurationE> konfiguration;

	private static final String FLAG_TRUE = "1";
	private static final String FLAG_FALSE = "0";
	private static final String WERT_EIN = "ein";
	private static final String WERT_AUS = "aus";
	private static final int SLEEP_TIME = 30000;

	private boolean isPumpeAn;
	private boolean isHeizungAn;

	private boolean isPvMin;
	private boolean isPvMax;

	private boolean isBattMin;
	private boolean isBattMax;

	boolean schaltePumpeAn;
	boolean schalteHeizungAn;

	
	public PoolService() {	}
	
	@Inject
	public PoolService(CoreRepository coreRepo,PoolPiService poolPi,PvService pvService) {
		this.coreRepo = coreRepo;
		this.poolPi = poolPi;
		this.pvService = pvService;
		ladeKonfiguration();
	}
	
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
		
		log.info("Pool Automatik: " + isPoolAutomatikEin());
		
		if (isPoolAutomatikEin()) {
			try {
				// PV Daten holen
				PvDaten pvDaten = pvService.ladePvHomeDaten(999, 999, 999, 999, 999);

				log.info("PV Leistung: " + pvDaten.getLeistung());
				log.info("Batt Ladung: " + pvDaten.getBattLadung());
				
				initDaten(pvDaten);

				log.info("Pumpe an: " + isPumpeAn);
				log.info("Heizung an: " + isHeizungAn);
				log.info("PvMin: " + isPvMin);
				log.info("PvMax: " + isPvMax);
				log.info("BattMin: " + isBattMin);
				log.info("BattMax: " + isBattMax);

				this.schaltePumpeAn = und(isPvMin, isBattMin);
				this.schalteHeizungAn = und(isPvMax, isBattMax);

				if (und(gleich(isPumpeAn, this.schaltePumpeAn),gleich(isHeizungAn, this.schalteHeizungAn))) {
					log.info("Keine Änderung.");
				} else {
					log.info("Zustand hat sich geändert.");
					schalteVerbraucher();
				}
			} catch (PoolPiException e) {
				log.error("Poolsteuerung: Fehler beim Schalten");
				log.error(e.getMessage());
			} catch (PvException e) {
				log.error("Poolsteuerung: Fehler beim holen der PV Daten");
				log.error(e.getMessage());
			} catch (InterruptedException e) {
				log.error("Poolsteuerung: Fehler im Thread");
				log.error(e.getMessage());
				Thread.currentThread().interrupt();
			}

		}
	}

	private void schalteVerbraucher() throws PoolPiException, InterruptedException {
		// Heizung soll eingeschaltet werden
		if (und(this.schalteHeizungAn,!isPoolWinterEin())) {
			// Pumpe ausschalten wenn an
			this.isPumpeAn = schaltePumpeAus(this.isPumpeAn);
			// Heizung einschalten wenn aus
			this.isHeizungAn = schalteHeizungEin(this.isHeizungAn);
			// Pumpe einschalten wenn aus
			this.isPumpeAn = schaltePumpeEin(this.isPumpeAn);
			// Heizung soll ausgeschaltet werden
		} else {
			// Pumpe ausschalten wenn an
			this.isPumpeAn = schaltePumpeAus(this.isPumpeAn);
			// Heizung ausschalten wenn an
			this.isHeizungAn = schalteHeizungAus(this.isHeizungAn);
			// Pumpe soll eingeschaltet werden
			if (this.schaltePumpeAn) {
				// Pumpe einschalten wenn aus
				this.isPumpeAn = schaltePumpeEin(this.isPumpeAn);
			}
		}
	}

	// Initialisierung der Daten fuer die Pool-Steuerung
	private void initDaten(PvDaten pvDaten) throws PoolPiException, PvException {
		this.isPumpeAn = holePumpenStatus();
		this.isHeizungAn = holeHeizungStatus();

		this.isPvMin = isPvUeberMin(pvDaten);
		this.isPvMax = isPvUeberMax(pvDaten);

		this.isBattMin = isBattUeberMin(pvDaten);
		this.isBattMax = isBattUeberMax(pvDaten);

		this.schaltePumpeAn = false;
		this.schalteHeizungAn = false;
	}

	private boolean gleich(boolean isPumpeAn, boolean schaltePumpeAn) {
		return schaltePumpeAn == isPumpeAn;
	}

	private boolean und(boolean isPvMin, boolean isBattMin) {
		return isPvMin && isBattMin;
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
