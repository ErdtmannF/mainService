package de.erdtmann.soft.mainservice.pv.entities;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.NamedNativeQuery;
import javax.persistence.Table;


@NamedNativeQuery(
		name="Leistung.holeLeistungByTagAndTyp",
		query="select id, wert, zeit, typ from PV_LEISTUNGEN l where date(l.zeit) = :tag and l.typ = :typ",
		resultClass=LeistungE.class)

@Entity
@Table(name = "PV_LEISTUNGEN")
public class LeistungE extends WertE implements Serializable {

	private static final long serialVersionUID = 2937762937452553953L;

	private int typ;
	
	public LeistungE(float wert, LocalDateTime zeit, int typ) {
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
