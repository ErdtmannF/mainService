package de.erdtmann.soft.mainservice.pv;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import de.erdtmann.soft.mainservice.exceptions.PvException;
import de.erdtmann.soft.mainservice.pv.modbus.PvModbusClient;
import de.erdtmann.soft.mainservice.pv.modbus.utils.AktuellVerbrauchFloatRegister;
import de.erdtmann.soft.mainservice.pv.modbus.utils.BatterieFloatRegister;
import de.erdtmann.soft.mainservice.pv.modbus.utils.NetzFloatRegister;
import de.erdtmann.soft.mainservice.pv.modbus.utils.PvFloatRegister;
import de.erdtmann.soft.mainservice.pv.modbus.utils.TotalVerbrauchFloatRegister;
import de.erdtmann.soft.utils.pv.PvDaten;
import de.erdtmann.soft.utils.pv.model.DcDaten;
import de.erdtmann.soft.utils.pv.model.VerbrauchDaten;

@ApplicationScoped
public class PvService {

	@Inject
	PvModbusClient pvModbusClient;

	public PvDaten ladePvHomeDaten() throws PvException {
		float leistung = 999;
		float battStand = 999;
		String battRichtung = "rechts";
		float battLeistung = 999;
		float home = 999;
		float netz = 999;
		String netzRichtung = "links";

		if (pvModbusClient != null) {
			float pv1 = pvModbusClient.holeModbusRegisterFloat(PvFloatRegister.DC_W_1);
			float pv2 = pvModbusClient.holeModbusRegisterFloat(PvFloatRegister.DC_W_2);
			leistung = pv1 + pv2;

			battStand = pvModbusClient.holeModbusRegisterFloat(BatterieFloatRegister.BATT_STAND);

			float battStrom = pvModbusClient.holeModbusRegisterFloat(BatterieFloatRegister.BATT_STROM);
			float battSpannung = pvModbusClient.holeModbusRegisterFloat(BatterieFloatRegister.BATT_SPANNUNG);
			battLeistung = battStrom * battSpannung;
			if (battLeistung > 0) {
				battRichtung = "rechts";
			} else {
				battRichtung = "links";
			}

			float verbrauchBatt = pvModbusClient
					.holeModbusRegisterFloat(AktuellVerbrauchFloatRegister.VERBRAUCH_VON_BAT);
			float verbrauchPv = pvModbusClient.holeModbusRegisterFloat(AktuellVerbrauchFloatRegister.VERBRAUCH_VON_PV);
			float verbrauchGrid = pvModbusClient
					.holeModbusRegisterFloat(AktuellVerbrauchFloatRegister.VERBAUCH_VON_NETZ);
			home = verbrauchBatt + verbrauchGrid + verbrauchPv;

			netz = pvModbusClient.holeModbusRegisterFloat(NetzFloatRegister.GRID_LEISTUNG);
			if (netz > 0) {
				netzRichtung = "links";

			} else {
				netzRichtung = "rechts";
			}
		}
		PvDaten pvDaten = new PvDaten(leistung, battStand, battRichtung, battLeistung, home, netz, netzRichtung);
		return pvDaten;
	}

	public VerbrauchDaten ladeVerbrauchDaten() throws PvException {
		
		VerbrauchDaten verbrauchDaten = new VerbrauchDaten();
		
		if (pvModbusClient != null) {
		
			verbrauchDaten.setTotalVonBatt(pvModbusClient.holeModbusRegisterFloat(TotalVerbrauchFloatRegister.TOTAL_VON_BATT));
			verbrauchDaten.setTotalVonNetz(pvModbusClient.holeModbusRegisterFloat(TotalVerbrauchFloatRegister.TOTAL_VON_NETZ));
			verbrauchDaten.setTotalVonPv(pvModbusClient.holeModbusRegisterFloat(TotalVerbrauchFloatRegister.TOTAL_VON_PV));
			verbrauchDaten.setTotalVerbrauch(pvModbusClient.holeModbusRegisterFloat(TotalVerbrauchFloatRegister.TOTAL_VERBRAUCH));
			verbrauchDaten.setTotalVerbrauchRate(pvModbusClient.holeModbusRegisterFloat(TotalVerbrauchFloatRegister.TOTAL_VERBRAUCH_RATE));
			verbrauchDaten.setWorktime(pvModbusClient.holeModbusRegisterFloat(TotalVerbrauchFloatRegister.WORKTIME));
			verbrauchDaten.setTotalErtrag(pvModbusClient.holeModbusRegisterFloat(TotalVerbrauchFloatRegister.TOTAL_ERTRAG));
			verbrauchDaten.setTaeglicherErtrag(pvModbusClient.holeModbusRegisterFloat(TotalVerbrauchFloatRegister.TAEGLICHER_ERTRAG));
			verbrauchDaten.setJaehrlicherErtrag(pvModbusClient.holeModbusRegisterFloat(TotalVerbrauchFloatRegister.JAEHRLICHER_ERTRAG));
			verbrauchDaten.setMonatlicherErtrag(pvModbusClient.holeModbusRegisterFloat(TotalVerbrauchFloatRegister.MONATLICHER_ERTRAG));
			verbrauchDaten.setDcZuBatt(pvModbusClient.holeModbusRegisterFloat(TotalVerbrauchFloatRegister.DC_ZU_BATT));
			verbrauchDaten.setDcVonBatt(pvModbusClient.holeModbusRegisterFloat(TotalVerbrauchFloatRegister.DC_VON_BATT));
			verbrauchDaten.setAcZuBatt(pvModbusClient.holeModbusRegisterFloat(TotalVerbrauchFloatRegister.AC_ZU_BATT));
			verbrauchDaten.setBattZuNetz(pvModbusClient.holeModbusRegisterFloat(TotalVerbrauchFloatRegister.BATT_ZU_NETZ));
			verbrauchDaten.setNetzZuBatt(pvModbusClient.holeModbusRegisterFloat(TotalVerbrauchFloatRegister.NETZ_ZU_BATT));
			verbrauchDaten.setSumPvInputs(pvModbusClient.holeModbusRegisterFloat(TotalVerbrauchFloatRegister.SUM_PV_INPUTS));
			verbrauchDaten.setTotalDcVonPv1(pvModbusClient.holeModbusRegisterFloat(TotalVerbrauchFloatRegister.TOTAL_DC_VON_PV1));
			verbrauchDaten.setTotalDcVonPv2(pvModbusClient.holeModbusRegisterFloat(TotalVerbrauchFloatRegister.TOTAL_DC_VON_PV2));
			verbrauchDaten.setTotalDcVonPv3(pvModbusClient.holeModbusRegisterFloat(TotalVerbrauchFloatRegister.TOTAL_DC_VON_PV3));
			verbrauchDaten.setAcZuNetz(pvModbusClient.holeModbusRegisterFloat(TotalVerbrauchFloatRegister.AC_ZU_NETZ));
			
		}
		return verbrauchDaten;
	}
	
	public DcDaten ladeDcDaten() throws PvException {
		
		DcDaten dcDaten = new DcDaten();
		
		if (pvModbusClient != null) {
		
			dcDaten.setTotalDcPower(pvModbusClient.holeModbusRegisterFloat(PvFloatRegister.TOTAL_DC_POWER));
			dcDaten.setDcA1(pvModbusClient.holeModbusRegisterFloat(PvFloatRegister.DC_A_1));
			dcDaten.setDcW1(pvModbusClient.holeModbusRegisterFloat(PvFloatRegister.DC_W_1));
			dcDaten.setDcV1(pvModbusClient.holeModbusRegisterFloat(PvFloatRegister.DC_V_1));
			dcDaten.setDcA2(pvModbusClient.holeModbusRegisterFloat(PvFloatRegister.DC_A_2));
			dcDaten.setDcW2(pvModbusClient.holeModbusRegisterFloat(PvFloatRegister.DC_W_2));
			dcDaten.setDcV2(pvModbusClient.holeModbusRegisterFloat(PvFloatRegister.DC_V_2));
			dcDaten.setSumDcPower(pvModbusClient.holeModbusRegisterFloat(PvFloatRegister.SUM_DC_POWER));
			
		}
		return dcDaten;
	}
}
