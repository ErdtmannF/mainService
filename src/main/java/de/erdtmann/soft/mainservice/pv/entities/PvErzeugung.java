package de.erdtmann.soft.mainservice.pv.entities;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Entity;


@Entity
public class PvErzeugung extends PvDaten implements Serializable {

	private static final long serialVersionUID = -1871435847950063194L;
	
	private int typ;
	
	PvErzeugung() {}
	
	public PvErzeugung(float wert, LocalDateTime zeit, int typ) {
		setWert(wert);
		setZeit(zeit);
		setTyp(typ);
	}
	
	public int getTyp() {
		return typ;
	}
	public void setTyp(int typ) {
		this.typ = typ;
	}

}
