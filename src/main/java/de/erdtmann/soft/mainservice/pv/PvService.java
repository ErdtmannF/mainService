package de.erdtmann.soft.mainservice.pv;

import java.time.LocalDateTime;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.logging.Logger;

import de.erdtmann.soft.mainservice.exceptions.PvException;
import de.erdtmann.soft.mainservice.pv.entities.BattLadungE;
import de.erdtmann.soft.mainservice.pv.entities.LeistungE;
import de.erdtmann.soft.mainservice.pv.modbus.PvModbusClient;
import de.erdtmann.soft.mainservice.pv.modbus.utils.ModbusFloatRegister;
import de.erdtmann.soft.utils.pv.PvDaten;
import de.erdtmann.soft.utils.pv.model.DcDaten;
import de.erdtmann.soft.utils.pv.model.VerbrauchDaten;

@ApplicationScoped
public class PvService {

	Logger log = Logger.getLogger(PvService.class);
	
	private PvModbusClient pvModbusClient;
	private PvRepository pvRepo;

	static final String RECHTS = "rechts";
	static final String LINKS = "links";
	
	private float leistung;
	private float battStand;
	private String battRichtung = RECHTS;
	private float battLeistung;
	private float home;
	private float netz;
	private String netzRichtung = LINKS;

	public PvService() { }
	
	@Inject
	public PvService(PvModbusClient pvModbusClient,PvRepository pvRepo) {
		this.pvModbusClient = pvModbusClient;
		this.pvRepo = pvRepo;
	}
	
	
	public void speichereDaten() throws PvException {
		
		if (pvModbusClient != null) {
			LocalDateTime zeit = LocalDateTime.now();

			float verbrauchVonBatt = pvModbusClient.holeModbusRegisterFloat(ModbusFloatRegister.VERBRAUCH_VON_BAT); 
			float verbrauchVonPv = pvModbusClient.holeModbusRegisterFloat(ModbusFloatRegister.VERBRAUCH_VON_PV);
			float verbrauchVonNetz = pvModbusClient.holeModbusRegisterFloat(ModbusFloatRegister.VERBRAUCH_VON_NETZ); 
			float pvString1 = pvModbusClient.holeModbusRegisterFloat(ModbusFloatRegister.DC_W_1);
			float pvString2 = pvModbusClient.holeModbusRegisterFloat(ModbusFloatRegister.DC_W_2);
			float battLadeStand = pvModbusClient.holeModbusRegisterFloat(ModbusFloatRegister.BATT_STAND);
			
			LeistungE verbrauchBatt = LeistungE.builder()
												.withTyp(1)
												.withWert(checkWert(verbrauchVonBatt))
												.withZeit(zeit)
												.build();
	
			LeistungE verbrauchPv = LeistungE.builder()
												.withTyp(2)
												.withWert(checkWert(verbrauchVonPv))
												.withZeit(zeit)
												.build();
	
			LeistungE verbrauchGrid = LeistungE.builder()
												.withTyp(3)
												.withWert(checkWert(verbrauchVonNetz))
												.withZeit(zeit)
												.build();
	
			LeistungE pvLeistung1 = LeistungE.builder()
												.withTyp(4)
												.withWert(checkWert(pvString1))
												.withZeit(zeit)
												.build();
			
			LeistungE pvLeistung2 = LeistungE.builder()
												.withTyp(5)
												.withWert(checkWert(pvString2))
												.withZeit(zeit)
												.build();
			
			LeistungE pvLeistung = LeistungE.builder()
												.withTyp(6)
												.withWert(checkWert(pvString1 + pvString2))
												.withZeit(zeit)
												.build();
			
			LeistungE pvOhneVerbrauch = LeistungE.builder()
												.withTyp(7)
												.withWert(checkWert((pvString1 + pvString2) - verbrauchVonPv))
												.withZeit(zeit)
												.build();
	
			LeistungE hausverbrauchGesamt = LeistungE.builder()
												.withTyp(8)
												.withWert(checkWert(verbrauchVonBatt) + checkWert(verbrauchVonPv) + checkWert(verbrauchVonNetz))
												.withZeit(zeit)
												.build();
	
			
			pvRepo.speichereLeistung(verbrauchPv);
			pvRepo.speichereLeistung(verbrauchBatt);
			pvRepo.speichereLeistung(verbrauchGrid);
			pvRepo.speichereLeistung(pvLeistung1);
			pvRepo.speichereLeistung(pvLeistung2);
			pvRepo.speichereLeistung(pvLeistung);
			pvRepo.speichereLeistung(pvOhneVerbrauch);
			pvRepo.speichereLeistung(hausverbrauchGesamt);
			
			BattLadungE battLadung = BattLadungE.builder()
												.withWert(battLadeStand)
												.withZeit(zeit)
												.build();
			
			pvRepo.speichereBattLadung(battLadung);
			
			log.info("PV Daten wurden gespeichert");
		}
	}

	private float checkWert(float wert) {
		return (wert < 0) ? 0 : wert;
	}
	
	public PvDaten ladePvHomeDaten(float leistung, float battStand, float battLeistung, float home, float netz) throws PvException {
		
		this.leistung = leistung;
		this.battStand = battStand;
		this.battLeistung = battLeistung;
		this.home = home;
		this.netz = netz;

		if (pvModbusClient != null) {
			float pv1 = pvModbusClient.holeModbusRegisterFloat(ModbusFloatRegister.DC_W_1);
			float pv2 = pvModbusClient.holeModbusRegisterFloat(ModbusFloatRegister.DC_W_2);
			this.leistung = pv1 + pv2;

			this.battStand = pvModbusClient.holeModbusRegisterFloat(ModbusFloatRegister.BATT_STAND);

			float battStrom = pvModbusClient.holeModbusRegisterFloat(ModbusFloatRegister.BATT_STROM);
			float battSpannung = pvModbusClient.holeModbusRegisterFloat(ModbusFloatRegister.BATT_SPANNUNG);
			this.battLeistung = battStrom * battSpannung;
			if (battLeistung > 0) {
				battRichtung = RECHTS;
			} else {
				battRichtung = LINKS;
			}

			float verbrauchBatt = pvModbusClient.holeModbusRegisterFloat(ModbusFloatRegister.VERBRAUCH_VON_BAT);
			float verbrauchPv = pvModbusClient.holeModbusRegisterFloat(ModbusFloatRegister.VERBRAUCH_VON_PV);
			float verbrauchGrid = pvModbusClient.holeModbusRegisterFloat(ModbusFloatRegister.VERBRAUCH_VON_NETZ);
			this.home = verbrauchBatt + verbrauchGrid + verbrauchPv;

			this.netz = pvModbusClient.holeModbusRegisterFloat(ModbusFloatRegister.GRID_LEISTUNG);
			if (netz > 0) {
				this.netzRichtung = LINKS;

			} else {
				this.netzRichtung = RECHTS;
			}
		}
		return new PvDaten(this.leistung, this.battStand, this.battRichtung, this.battLeistung, this.home, this.netz, this.netzRichtung);
		
	}

	public VerbrauchDaten ladeVerbrauchDaten() throws PvException {
		
		VerbrauchDaten verbrauchDaten = new VerbrauchDaten();
		
		if (pvModbusClient != null) {
		
			verbrauchDaten.setTotalVonBatt(pvModbusClient.holeModbusRegisterFloat(ModbusFloatRegister.TOTAL_VON_BATT));
			verbrauchDaten.setTotalVonNetz(pvModbusClient.holeModbusRegisterFloat(ModbusFloatRegister.TOTAL_VON_NETZ));
			verbrauchDaten.setTotalVonPv(pvModbusClient.holeModbusRegisterFloat(ModbusFloatRegister.TOTAL_VON_PV));
			verbrauchDaten.setTotalVerbrauch(pvModbusClient.holeModbusRegisterFloat(ModbusFloatRegister.TOTAL_VERBRAUCH));
			verbrauchDaten.setTotalVerbrauchRate(pvModbusClient.holeModbusRegisterFloat(ModbusFloatRegister.TOTAL_VERBRAUCH_RATE));
			verbrauchDaten.setWorktime(pvModbusClient.holeModbusRegisterFloat(ModbusFloatRegister.INVERTER_LAUFZEIT));
			verbrauchDaten.setTotalErtrag(pvModbusClient.holeModbusRegisterFloat(ModbusFloatRegister.TOTAL_ERTRAG));
			verbrauchDaten.setTaeglicherErtrag(pvModbusClient.holeModbusRegisterFloat(ModbusFloatRegister.TAEGLICHER_ERTRAG));
			verbrauchDaten.setJaehrlicherErtrag(pvModbusClient.holeModbusRegisterFloat(ModbusFloatRegister.JAEHRLICHER_ERTRAG));
			verbrauchDaten.setMonatlicherErtrag(pvModbusClient.holeModbusRegisterFloat(ModbusFloatRegister.MONATLICHER_ERTRAG));
			verbrauchDaten.setDcZuBatt(pvModbusClient.holeModbusRegisterFloat(ModbusFloatRegister.DC_ZU_BATT));
			verbrauchDaten.setDcVonBatt(pvModbusClient.holeModbusRegisterFloat(ModbusFloatRegister.DC_VON_BATT));
			verbrauchDaten.setAcZuBatt(pvModbusClient.holeModbusRegisterFloat(ModbusFloatRegister.AC_ZU_BATT));
			verbrauchDaten.setBattZuNetz(pvModbusClient.holeModbusRegisterFloat(ModbusFloatRegister.BATT_ZU_NETZ));
			verbrauchDaten.setNetzZuBatt(pvModbusClient.holeModbusRegisterFloat(ModbusFloatRegister.NETZ_ZU_BATT));
			verbrauchDaten.setSumPvInputs(pvModbusClient.holeModbusRegisterFloat(ModbusFloatRegister.SUM_PV_INPUTS));
			verbrauchDaten.setTotalDcVonPv1(pvModbusClient.holeModbusRegisterFloat(ModbusFloatRegister.TOTAL_DC_VON_PV1));
			verbrauchDaten.setTotalDcVonPv2(pvModbusClient.holeModbusRegisterFloat(ModbusFloatRegister.TOTAL_DC_VON_PV2));
			verbrauchDaten.setTotalDcVonPv3(pvModbusClient.holeModbusRegisterFloat(ModbusFloatRegister.TOTAL_DC_VON_PV3));
			verbrauchDaten.setAcZuNetz(pvModbusClient.holeModbusRegisterFloat(ModbusFloatRegister.AC_ZU_NETZ));
			
		}
		return verbrauchDaten;
	}
	
	public DcDaten ladeDcDaten() throws PvException {
		
		DcDaten dcDaten = new DcDaten();
		
		if (pvModbusClient != null) {
		
			dcDaten.setTotalDcPower(pvModbusClient.holeModbusRegisterFloat(ModbusFloatRegister.TOTAL_DC_POWER));
			dcDaten.setDcA1(pvModbusClient.holeModbusRegisterFloat(ModbusFloatRegister.DC_A_1));
			dcDaten.setDcW1(pvModbusClient.holeModbusRegisterFloat(ModbusFloatRegister.DC_W_1));
			dcDaten.setDcV1(pvModbusClient.holeModbusRegisterFloat(ModbusFloatRegister.DC_V_1));
			dcDaten.setDcA2(pvModbusClient.holeModbusRegisterFloat(ModbusFloatRegister.DC_A_2));
			dcDaten.setDcW2(pvModbusClient.holeModbusRegisterFloat(ModbusFloatRegister.DC_W_2));
			dcDaten.setDcV2(pvModbusClient.holeModbusRegisterFloat(ModbusFloatRegister.DC_V_2));
			dcDaten.setSumDcPower(pvModbusClient.holeModbusRegisterFloat(ModbusFloatRegister.SUM_DC_POWER));
			
		}
		return dcDaten;
	}
}
