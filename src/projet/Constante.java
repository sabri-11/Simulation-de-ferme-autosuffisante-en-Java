package projet;

/**
 * Fichier regroupant toutes les constantes du jeu
 */
public class Constante
{
	// Constantes des vaches : 
	
	public static final double prixV = 1500.0;
	public static final double consoEauV = 0.5;
	public static final double consoNourV = 300.0;
	public static final double capNourV = 6000.0;	
	public static final double capEauV = 6.0;		
	public static final double prodNourV = 600.0;
	public static final int vieV = 3;
	public static final double prodFertilV = 800.0;
	public static final double faimDimNourV = 0.3;
	public static final double soifDimNourV = 0.5;
	public static final int capDepV = 1;
	public static final double capReproV = 1.0/7.0;
	
	// Constantes des poules :
	
	public static final double prixP = 500.0;
	public static final double consoEauP = 0.1;
	public static final double consoNourP = 200.0;
	public static final double capNourP = 2000.0;
	public static final double capEauP = 0.7;
	public static final double prodNourP = 100.0;
	public static final int vieP = 2;
	public static final double prodFertilP = 100.0;
	public static final double faimDimNourP = 0.1;
	public static final double soifDimNourP = 0.1;
	public static final int capDepP = 1; 
	public static final double capReproP = 1.0/5.0;

	
	// Constantes du blé
	public static final double prixB = 400.0;
	public static final double consoEauB = 0.01;
	public static final double capEauB = 0.3;
	public static double qteNourB = 700.0;
	public static final double consoFertilB = 50.0;
	public static final double capCroissanceB = qteNourB*0.1; 	// 10% de sa taille actuelle
	public static final double faimDimCroissB = 0.4;
	public static final double soifDimCroissB = 0.1;
	public static final int capDepB = 0;

	
	// Constantes des tomates
	public static final double prixT = 1200.0;
	public static final double consoEauT = 0.05;
	public static final double capEauT = 0.4;
	public static double qteNourT = 1000.0;
	public static final double consoFertilT = 20.0;
	public static final double capCroissanceT = 50.0;
	public static final double faimDimCroissT = 0.2;
	public static final double soifDimCroissT = 0.3;
	public static final int capDepT = 0;
	
	// Constantes des puits

	public static final double prixCons = 3000.0;
	public static final double prixDes = 200.0;
	
//	Constantes du fertilisant
	public static final double prixFertilKG = 2000.0;
	public static final int typeFertil = 6;
	
	// Constante de la grille
	public static final int n = 6;	// longueur
	public static final int m = 6;	// largeur
	
	// Constante du joueur
	public static final double argentDepart = 15000.0;
	public static final double pourcentage = 0.75; // pourcentage de conversion entre calories et Y
	
	// Constante cases
	
	// qte initial d'eau et de fertilisant sur les cases non précisé dans le sujet, on les mets donc à 0.
	public static final double qteInitEau = 0.0;	// La pluie se chargera d'alimenter l'eau sur les cases. 
	public static final double qteInitFertil = 0.0;	// en g
	
	
	public Constante() {}
	
	
}
