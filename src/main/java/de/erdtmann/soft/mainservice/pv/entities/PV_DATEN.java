package de.erdtmann.soft.mainservice.pv.entities;


import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public abstract class PV_DATEN  implements Serializable {

	private static final long serialVersionUID = -3334238962038277868L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private float wert;
	private LocalDateTime zeit;

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
