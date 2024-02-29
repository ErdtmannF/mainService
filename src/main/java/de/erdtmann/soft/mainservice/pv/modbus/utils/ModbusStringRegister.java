package de.erdtmann.soft.mainservice.pv.modbus.utils;

public enum ModbusStringRegister implements ModbusRegister {

	// Inverter article number
	INVERTER_ARTIKEL_NR(6, null, 8),
	// Inverter serial number
	INVERTER_SERIEN_NR(14, null, 16),
	// Software-Version Maincontroller
	INVERTER_SW_MAINCONTROLLER(38, null, 8),
	// Software-Version IO-Controller
	INVERTER_SW_IO_CONTROLLER(46, null, 8),
	// Inverter network name
	INVERTER_NETWORK_NAME(384, null, 32),
	// Inverter IP address
	INVERTER_IP_ADDRESS(420, null, 8),
	// Inverter IP supnetmask
	INVERTER_IP_SUBNET(428, null, 8),
	// Inverter IP gateway
	INVERTER_IP_GATEWAY(436, null, 8),
	// Inverter IP DNS1
	INVERTER_IP_DNS1(446, null, 8),
	// Inverter IP DNS2
	INVERTER_IP_DNS2(454, null, 8),
	// Battery Manufacturer
	BATT_HERSTELLER(517, null, 8),
	// Inverter Manufacturer
	INVERTER_HERSTELLER(768, null, 16),
	// Inverter serial number
	INVERTER_SERIAL_NUMBER(768, null, 16),
	// Inverter product name
	INVERTER_NAME(768, null, 32),
	// Inverter power class
	INVERTER_NR(800, null, 32);
	
	private int addr;
	private String einheit;
	private int anzahl;
	
	ModbusStringRegister(int addr, String einheit, int anzahl){
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
