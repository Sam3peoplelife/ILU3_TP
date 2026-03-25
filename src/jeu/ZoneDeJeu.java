package jeu;

import java.util.Collection;
import java.util.List;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import cartes.*;

public class ZoneDeJeu {

	private List<Limite> pileLimitesVitesse = new LinkedList<>();
	private List<Bataille> pileBataille = new LinkedList<>();
	private Collection<Borne> collectionBornes = new LinkedList<>();
	private Set<Botte> ensembleBottes = new HashSet<>();

	public List<Limite> getPileLimitesVitesse() {
		return pileLimitesVitesse;
	}

	public List<Bataille> getPileBataille() {
		return pileBataille;
	}

	public Collection<Borne> getCollectionBornes() {
		return collectionBornes;
	}

	public Set<Botte> getEnsembleBottes() {
		return ensembleBottes;
	}

	public boolean deposer(Carte carte) {
		if (carte instanceof Borne) {
			return collectionBornes.add((Borne) carte);
		} else if (carte instanceof Bataille) {
			pileBataille.add(0, (Bataille) carte);
			return true;
		} else if (carte instanceof Limite) {
			pileLimitesVitesse.add(0, (Limite) carte);
			return true;
		} else if (carte instanceof Botte) {
			return ensembleBottes.add((Botte) carte);
		} else {
			throw new IllegalArgumentException("Invalid card type: " + carte);
		}
	}

	public int donnerKmParcourus() {
		int kmParcourus = 0;
		for (Borne borne : collectionBornes) {
			kmParcourus += borne.getKm();
		}
		return kmParcourus;
	}

	public int donnerLimitationVitesse() {
		if (pileLimitesVitesse.isEmpty() || pileLimitesVitesse.get(0) instanceof FinLimite) {
			return 200;
		}

		for (Carte bottes : ensembleBottes) {
			if (bottes instanceof Botte && ((Botte) bottes).getType() == (Type.FEU)) {
				return 200;
			}
		}

		return 50;
	}

	public boolean ep() {
		for (Botte botte : ensembleBottes) {
			if (botte.getType() == Type.FEU) {
				return true;
			}
		}
		return false;
	}

	private boolean pb(Bataille carte) {
		for (Botte botte : ensembleBottes) {
			if (botte.getType() == carte.getType())
				return true;
		}
		return false;
	}

	public boolean peutAvancer() {
		if (pileBataille.isEmpty() && ep()) {
			return true;
		}
		if (!pileBataille.isEmpty()) {
			Carte carteSommet = pileBataille.get(0);
			if (carteSommet.equals(Cartes.FEU_VERT)) {
				return true;
			}
			if (carteSommet instanceof Parade && ep()) {
				return true;
			}
			if (carteSommet.equals(Cartes.FEU_ROUGE) && ep()) {
				return true;
			}
			if (carteSommet instanceof Attaque && pb((Attaque) carteSommet) && ep()) {
				return true;
			}
			pileBataille.remove(0);
			return false;
		}
		return false;
	}

	private boolean estDepotFeuVertAutorise() {
		if (ep()) {
			return false;
		}
		if (pileBataille.isEmpty()) {
			return true;
		}

		Carte carteSommet = pileBataille.get(0);
		if (carteSommet.equals(Cartes.FEU_ROUGE)) {
			return true;
		}
		if (carteSommet instanceof Parade && !carteSommet.equals(Cartes.FEU_VERT)) {
			return true;
		}
		if (carteSommet instanceof Attaque && pb((Attaque) carteSommet)) {
			return true;
		}
		return false;
	}

	private boolean estDepotBorneAutorise(Borne borne) {
		if (!peutAvancer()) {
			return false;
		}

		if (borne.getKm() > donnerLimitationVitesse()) {
			return false;
		}

		int sommeVitesseBornes = donnerKmParcourus();
		return sommeVitesseBornes + borne.getKm() <= 1000;
	}

	private boolean estDepotLimiteAutorise(Limite limite) {
		if (ep()) {
			return false;
		}
		if (limite instanceof DebutLimite
				&& (pileLimitesVitesse.isEmpty() || pileLimitesVitesse.get(0) instanceof FinLimite)) {
			return true;
		}
		if (limite instanceof FinLimite && pileLimitesVitesse.get(0) instanceof DebutLimite) {
			return true;
		}
		return false;
	}

	private boolean estDepotBatailleAutorise(Bataille bataille) {
		if (pb(bataille)) {
			return false;
		}
		if (bataille instanceof Attaque && peutAvancer())
			return true;
		if (bataille instanceof Parade) {
			if (bataille.getType() == Type.FEU) {
				return estDepotFeuVertAutorise();
			} else {
				return (!pileBataille.isEmpty() && (pileBataille.get(0) instanceof Attaque
						&& pileBataille.get(0).getType() == bataille.getType()));
			}
		}
		return false;
	}

	public boolean estDepotAutorise(Carte carte) {
		if (carte instanceof Borne) {
			return estDepotBorneAutorise((Borne) carte);
		}
		if (carte instanceof Limite) {
			return estDepotLimiteAutorise((Limite) carte);
		}
		if (carte instanceof Bataille) {
			return estDepotBatailleAutorise((Bataille) carte);
		}
		if (carte instanceof Botte) {
			return true;
		} else {
			throw new IllegalArgumentException("Type de carte invalide");
		}
	}
}