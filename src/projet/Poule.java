package projet;

/**
 * La classe Poule permet de créer une Poule en initialisant toutes les constante de la poule présente dans le  fichier Constante.java
 */
public class Poule extends Animal
{
	
	public Poule()
	{
		super(Constante.prixP, Constante.consoEauP, Constante.consoNourP, Constante.capNourP, Constante.capEauP, Constante.prodNourP,
				Constante.vieP, Constante.prodFertilP, Constante.faimDimNourP, Constante.soifDimNourP, Constante.capDepP, Constante.capReproP);
		
	}

	/**
	 * Pemret de creer un bébé poule lors d'une reproduction en initialisant ses constantes.
	 */
	public Animal creerBebe()
	{
		Poule bebeP = new Poule();
		bebeP.configBebe();
		this.setA_Bebe(true);
		
		return bebeP;
	}

}
