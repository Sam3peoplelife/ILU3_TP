package jeu;

import cartes.Carte;

public class Coup {
	private Joueur JoueurCourant;
	private Carte carteJoue;
	private Joueur joueurCible;

	public Coup(Joueur j1, Carte c, Joueur j2) {
		this.carteJoue = c;
		this.joueurCible = j2;
		this.JoueurCourant = j1;
	}
	public Joueur getJoueurCourant() {
        return JoueurCourant;
    }

    public Carte getCarteJouee() {
        return carteJoue;
    }

    public Joueur getJoueurCible() {
        return joueurCible;
    }
	private boolean estValide() {
		
		return false;
	}
}