package projet;

/**
 * La classe Vache permet de créer une vache en initialisant toutes les constante de la vache présente dans le  fichier Constante.java
 */
public class Vache extends Animal
{
	
	public Vache()
	{
		super(Constante.prixV, Constante.consoEauV, Constante.consoNourV, Constante.capNourV, Constante.capEauV, Constante.prodNourV, Constante.vieV,
				Constante.prodFertilV, Constante.faimDimNourV, Constante.soifDimNourV, Constante.capDepV, Constante.capReproV);
		
		
	}
	
	/**
	 * Permet de créer un bébé vache lors d'une reproduction en initialisant ses constantes.
	 */
	public Animal creerBebe()
	{
		Vache bebeV = new Vache();
		bebeV.configBebe();
		this.setA_Bebe(true);
		
		return bebeV;
	}
}
