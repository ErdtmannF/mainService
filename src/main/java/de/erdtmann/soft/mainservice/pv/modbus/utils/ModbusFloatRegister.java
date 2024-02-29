package de.erdtmann.soft.mainservice.pv.modbus.utils;

public enum ModbusFloatRegister implements ModbusRegister {

	// Total DC power
	TOTAL_DC_POWER(100, "W", 2),
	// Home own consumption from battery
	VERBRAUCH_VON_BAT(106, "W", 2),
	// Home own consumption from grid
	VERBRAUCH_VON_NETZ(108, "W", 2),
	// Total home consumption Battery
	TOTAL_VON_BATT(110, "Wh", 2),
	// Total home consumption Grid
	TOTAL_VON_NETZ(112, "Wh", 2),
	// Total home consumption PV
	TOTAL_VON_PV(114, "Wh", 2),
	// Home own consumption from PV
	VERBRAUCH_VON_PV(116, "W", 2),
	// Total home consumption
	TOTAL_VERBRAUCH(118, "Wh", 2),
	// Isolation resistance
	ISOLATIONS_WIDERSTAND(120, "Ohm", 2),
	// Power limit from EVU
	LEISTUNGS_LIMIT(122, "%", 2),
	// Total home consumption rate
	TOTAL_VERBRAUCH_RATE(124, "%", 2),
	// Worktime
	INVERTER_LAUFZEIT(144, "s", 2),
	
	GRID_FR(152, "Hz", 2),
	AC_A_1(154, "A", 2),
	AC_W_1(156, "W", 2),
	AC_V_1(158, "V", 2),
	AC_A_2(160, "A", 2),
	AC_W_2(162, "W", 2),
	AC_V_2(164, "V", 2),
	AC_A_3(166, "A", 2),
	AC_W_3(168, "W", 2),
	AC_V_3(170, "V", 2),

	// Battery charge current
	BATT_CHARGE(190, "A", 2),
	// Number of battery cycles
	BATT_ZYKLUS(194, "", 2),
	// Actual battery charge (-) / discharge (+) current
	BATT_STROM(200, "A", 2),
	// Battery ready flag
	BATT_READY(208, "", 2),
	// Act. state of charge
	BATT_STAND(210, "%", 2),
	// Battery temperature
	BATT_TEMP(214, "°C", 2),
	// Battery voltage
	BATT_SPANNUNG(216, "V", 2),
	
	// Current phase 1 (powermeter)
	GRID_A_1(222, "A", 2),
	// Active power phase 1 (powermeter)
	GRID_W_1(224, "W", 2),
	// Voltage phase 1 (powermeter)
	GRID_V_1(230, "V", 2),
	// Current phase 2 (powermeter)
	GRID_A_2(232, "A", 2),
	// Active power phase 2 (powermeter)
	GRID_W_2(234, "W", 2),
	// Voltage phase 2 (powermeter)
	GRID_V_2(240, "V", 2),
	// Current phase 3 (powermeter)
	GRID_A_3(242, "A", 2),
	// Active power phase 3 (powermeter)
	GRID_W_3(244, "W", 2),
	// Voltage phase 3 (powermeter)
	GRID_V_3(250, "V", 2),
	// Total active power (powermeter)
	// Sensor position 2 (grid connection):
	// (+) Power supply, (-) feed-in
	GRID_LEISTUNG(252, "W", 2),

	
	// Strom DC 1
	DC_A_1(258, "A", 2),
	// Leistung DC 1
	DC_W_1(260, "W", 2),
	// Spannung DC 1
	DC_V_1(266, "V", 2),
	// Strom DC 2
	DC_A_2(268, "A", 2),
	// Leistung DC 2
	DC_W_2(270, "W", 2),
	// Spannung DC 2
	DC_V_2(276, "V", 2),
	
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
	AC_ZU_NETZ(1064, "Wh", 2),
	// Total DC power (sum of all PV inputs)
	SUM_DC_POWER(1066, "W", 2);

	
	private int addr;
	private String einheit;
	private int anzahl;
	
	private ModbusFloatRegister(int addr, String einheit, int anzahl) {
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
