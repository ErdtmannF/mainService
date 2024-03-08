package de.erdtmann.soft.mainservice.pv.entities;

import java.time.LocalDateTime;
import java.io.Serializable;

import javax.persistence.Entity;

//@NamedNativeQuery(
//		name="Batterie.holeLadungByTag",
//		query="select id, wert, zeit from PV_BATT_LADUNG bl where date(bl.zeit) = :tag and DTYPE = 'BattLadung'",
//		resultClass=BattLadung.class)

@Entity
public class BattLadung extends PvDaten implements Serializable {

	private static final long serialVersionUID = -2620894058347005323L;

	BattLadung() {}
	
	public BattLadung(float wert,LocalDateTime zeit) {
		setWert(wert);
		setZeit(zeit);
	}

}
