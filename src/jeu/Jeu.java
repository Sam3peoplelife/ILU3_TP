package jeu;
import java.util.Arrays;
import java.util.List;

import cartes.Carte;
import cartes.JeuDeCartes;
import utils.GestionCartes;

public class Jeu {
	private Sabot sabot;
	private JeuDeCartes jeuDeCarte= new JeuDeCartes();
	
	public Jeu () {
		Carte[] carte = jeuDeCarte.donnerCartes();
		
		List<Carte> listeCartes = Arrays.asList(carte);
		List<Carte> listeMelanger = GestionCartes.melanger(listeCartes);
		
		sabot = new Sabot(listeMelanger.toArray(carte));
	}
	
	public Sabot getSabot() {
		return sabot;
	}
}