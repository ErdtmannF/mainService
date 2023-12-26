package de.erdtmann.soft.mainservice.pv.modbus.utils;

public enum PvFloatRegister implements ModbusRegister {

	// Total DC power
	TOTAL_DC_POWER(100, "W", 2),
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
	// Total DC power (sum of all PV inputs)
	SUM_DC_POWER(1066, "W", 2);

	private int addr;
	private String einheit;
	private int anzahl;
	
	PvFloatRegister(int addr, String einheit, int anzahl){
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
