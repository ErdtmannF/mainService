package de.erdtmann.soft.mainservice.pv.entities;

import java.time.LocalDateTime;
import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table(name = "PV_ERZEUGUNG")
public class ErzeugungE extends WertE implements Serializable {

	private static final long serialVersionUID = -1871435847950063194L;

	private int typ;
	
	public ErzeugungE(float wert, LocalDateTime zeit, int typ) {
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