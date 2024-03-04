package de.erdtmann.soft.mainservice.pv.entities;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "PV_ERZEUGUNG")
public class ErzeugungE implements Serializable {

	private static final long serialVersionUID = -1871435847950063194L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private float wert;
	private LocalDateTime zeit;
	private int typ;
	
	public ErzeugungE(float wert, LocalDateTime zeit, int typ) {
		setWert(wert);
		setZeit(zeit);
		setTyp(typ);
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
	public int getTyp() {
		return typ;
	}
	public void setTyp(int typ) {
		this.typ = typ;
	}
	
}
