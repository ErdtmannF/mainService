package de.erdtmann.soft.mainservice.pv.entities;

import java.time.LocalDateTime;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public abstract class WertE {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private float wert;
	private LocalDateTime zeit;
	
	protected WertE() {}
	
	protected WertE(float wert,LocalDateTime zeit) {
		setWert(wert);
		setZeit(zeit);
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public float getWert() {
		return wert;
	}
	public void setWert(float wert) {
		this.wert = wert;
	}
	public LocalDateTime getZeit() {
		return zeit;
	}
	public void setZeit(LocalDateTime zeit) {
		this.zeit = zeit;
	}

}
