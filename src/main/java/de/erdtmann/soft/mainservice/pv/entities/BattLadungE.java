package de.erdtmann.soft.mainservice.pv.entities;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.NamedNativeQuery;
import javax.persistence.Table;

@NamedNativeQuery(
		name="Batterie.holeLadungByTag",
		query="select id, wert, zeit from PV_BATT_LADUNG bl where date(bl.zeit) = :tag",
		resultClass=BattLadungE.class)

@Entity
@Table(name = "PV_BATT_LADUNG")
public class BattLadungE extends WertE implements Serializable {

	private static final long serialVersionUID = -2620894058347005323L;

	public BattLadungE(float wert,LocalDateTime zeit) {
		setWert(wert);
		setZeit(zeit);
	}
}
