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
	
	protected LeistungE() { }
	
	private LeistungE(Builder builder) {
		setWert(builder.wert);
		setZeit(builder.zeit);
		setTyp(builder.typ);
	}
	
	public static Builder builder() {
		return new Builder();
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
	
	public static final class Builder {
		private float wert;
		private LocalDateTime zeit;
		private int typ;

		private Builder() {
		}

		public Builder withWert(float wert) {
			this.wert = wert;
			return this;
		}
		
		public Builder withZeit(LocalDateTime zeit) {
			this.zeit = zeit;
			return this;
		}

		public Builder withTyp(int typ) {
			this.typ = typ;
			return this;
		}

		public LeistungE build() {
			return new LeistungE(this);
		}
	}
}
