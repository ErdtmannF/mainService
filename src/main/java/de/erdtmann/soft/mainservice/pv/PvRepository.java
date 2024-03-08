package de.erdtmann.soft.mainservice.pv;


import java.util.List;
import java.time.LocalDate;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.jboss.logging.Logger;

import de.erdtmann.soft.mainservice.pv.entities.BattLadung;
import de.erdtmann.soft.mainservice.pv.entities.PvErzeugung;
import de.erdtmann.soft.mainservice.pv.entities.PvLeistung;
import de.erdtmann.soft.mainservice.pv.entities.PvVerbrauch;




@Stateless
public class PvRepository {

	Logger log = Logger.getLogger(PvRepository.class);
	
	@PersistenceContext
	EntityManager em;

	public void speichereLeistung(PvLeistung leistung) {
		em.persist(leistung);	
	}
	
	public void speichereErzeugung(PvErzeugung erzeugung) {
		em.persist(erzeugung);
	}
	
	public void speichereVerbrauch(PvVerbrauch verbrauch) {
		em.persist(verbrauch);
	}

	@SuppressWarnings("unchecked")
	public List<PvLeistung> holeLeistungTagTyp(LocalDate tag, int typ) {
		Query query = em.createNamedQuery("Leistung.holeLeistungByTagAndTyp", PvLeistung.class);
		query.setParameter("tag", tag);
		query.setParameter("typ", typ);
		
		return query.getResultList();
	}

	public void speichereBattLadung(BattLadung battLadung) {
		em.persist(battLadung);
	}

	@SuppressWarnings("unchecked")
	public List<BattLadung> holeBattLadungTag(LocalDate tag) {
		Query query = em.createNamedQuery("Batterie.holeLadungByTag", BattLadung.class);
		query.setParameter("tag", tag);
		
		return query.getResultList();
	}
}
