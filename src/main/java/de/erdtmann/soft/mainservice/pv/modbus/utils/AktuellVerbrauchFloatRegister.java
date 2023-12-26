package de.erdtmann.soft.mainservice.pv.modbus.utils;

public enum AktuellVerbrauchFloatRegister implements ModbusRegister {


	// Home own consumption from battery
	VERBRAUCH_VON_BAT(106, "W", 2),
	// Home own consumption from grid
	VERBAUCH_VON_NETZ(108, "W", 2),
	// Home own consumption from PV
	VERBRAUCH_VON_PV(116, "W", 2);

	private int addr;
	private String einheit;
	private int anzahl;
	
	private AktuellVerbrauchFloatRegister(int addr, String einheit, int anzahl) {
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
