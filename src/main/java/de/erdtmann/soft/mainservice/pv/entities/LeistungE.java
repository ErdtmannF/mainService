package de.erdtmann.soft.mainservice.pv.entities;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedNativeQuery;
import javax.persistence.Table;


@NamedNativeQuery(
		name="Leistung.holeLeistungByTagAndTyp",
		query="select id, wert, zeit, typ from PV_LEISTUNGEN l where date(l.zeit) = :tag and l.typ = :typ",
		resultClass=LeistungE.class)

@Entity
@Table(name = "PV_LEISTUNGEN")
public class LeistungE implements Serializable {

	private static final long serialVersionUID = 2937762937452553953L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private float wert;
	private LocalDateTime zeit;
	private int typ;
	
	public LeistungE(float wert, LocalDateTime zeit, int typ) {
		setWert(wert);
		setZeit(zeit);
		setTyp(typ);
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
