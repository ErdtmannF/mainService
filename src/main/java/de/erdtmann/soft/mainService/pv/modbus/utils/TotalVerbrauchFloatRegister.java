package de.erdtmann.soft.mainService.pv.modbus.utils;

public enum TotalVerbrauchFloatRegister implements ModbusRegister {

	// Total home consumption Battery
	TOTAL_VON_BATT(110, "Wh", 2),
	// Total home consumption Grid
	TOTAL_VON_NETZ(112, "Wh", 2),
	// Total home consumption PV
	TOTAL_VON_PV(114, "Wh", 2),
	// Total home consumption
	TOTAL_VERBRAUCH(118, "Wh", 2),
	// Total home consumption rate
	TOTAL_VERBRAUCH_RATE(124, "%", 2),
	// Worktime
	WORKTIME(144, "s", 2),
	// Total yield
	TOTAL_ERTRAG(320, "Wh", 2),
	// Daily yield
	TAEGLICHER_ERTRAG(322, "Wh", 2),
	// Yearly yield
	JAEHRLICHER_ERTRAG(324, "Wh", 2),
	// Monthly yield
	MONATLICHER_ERTRAG(326, "Wh", 2),
	// Total DC charge energy (DC-side to battery)
	DC_ZU_BATT(1046, "Wh", 2),
	// Total DC discharge energy (DC-side from battery)
	DC_VON_BATT(1048, "Wh", 2),
	// Total AC charge energy (AC-side to battery)
	AC_ZU_BATT(1050, "Wh", 2),
	// Total AC discharge energy (battery to grid)
	BATT_ZU_NETZ(1052, "Wh", 2),
	// Total AC charge energy (grid to battery)
	NETZ_ZU_BATT(1054, "Wh", 2),
	// Total DC PV energy (sum of all PV inputs)
	SUM_PV_INPUTS(1056, "Wh", 2),
	// Total DC energy from PV1
	TOTAL_DC_VON_PV1(1058, "Wh", 2),
	// Total DC energy from PV2
	TOTAL_DC_VON_PV2(1060, "Wh", 2),
	// Total DC energy from PV3
	TOTAL_DC_VON_PV3(1062, "Wh", 2),
	// Total energy AC-side to grid
	AC_ZU_NETZ(1064, "Wh", 2);

	private int addr;
	private String einheit;
	private int anzahl;
	
	private TotalVerbrauchFloatRegister(int addr, String einheit, int anzahl) {
		this.addr = addr;
		this.einheit = einheit;
		this.anzahl = anzahl;
	}

	public int getAddr() {
		return addr;
	}

	public String getEinheit() {
		return einheit;
	}

	public int getAnzahl() {
		return anzahl;
	}
}
