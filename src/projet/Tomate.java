package projet;

/**
 * La classe Blé, qui hérite de plante permet de récupérer toutes les constantes du blé de la classe Constante et de les rentrer dans la
 * classe Animal ou Entité en appelant son constructeur.
 */
public class Tomate extends Plante
{	
	private static final boolean estGelee = false;
	
	
	public Tomate()
	{
		super(Constante.prixT, Constante.consoEauT, Constante.capEauT, Constante.qteNourT, Constante.consoFertilT,
				Constante.capCroissanceT, Constante.faimDimCroissT, Constante.soifDimCroissT, Constante.capDepT, estGelee);
	}
	
}