package de.erdtmann.soft.mainservice.pv.entities;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Entity;

@Entity
public class PvVerbrauch extends PvDaten implements Serializable {

	private static final long serialVersionUID = -7401100261934643532L;
	
	private int typ;
	
	PvVerbrauch() {}
	
	public PvVerbrauch(float wert, LocalDateTime zeit, int typ) {
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
