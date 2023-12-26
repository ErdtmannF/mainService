package de.erdtmann.soft.mainService.pv.modbus.utils;

public enum BatterieFloatRegister implements ModbusRegister {

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
	// Home own consumption from battery
	VERBRAUCH_FROM_BAT(106, "W", 2);

	private int addr;
	private String einheit;
	private int anzahl;
	
	private BatterieFloatRegister(int addr, String einheit, int anzahl) {
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
