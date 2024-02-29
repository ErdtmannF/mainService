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
		name="Batterie.holeLadungByTag",
		query="select id, wert, zeit from PV_BATT_LADUNG bl where date(bl.zeit) = :tag",
		resultClass=BattLadungE.class)

@Entity
@Table(name = "PV_BATT_LADUNG")
public class BattLadungE implements Serializable {

	private static final long serialVersionUID = -2620894058347005323L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private float wert;
	private LocalDateTime zeit;
	
	protected BattLadungE() { }
	
	private BattLadungE(Builder builder) {
		setWert(builder.wert);
		setZeit(builder.zeit);
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
	
	public static final class Builder {
		private float wert;
		private LocalDateTime zeit;

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

		public BattLadungE build() {
			return new BattLadungE(this);
		}
	}

}
