package projet;

public class Reservoir
{
	private final double capacite;
	private double niveau;
	
	public Reservoir(double capacite, double niveau)
	{
		this.capacite = capacite;
		this.niveau = niveau;
	}
	
	/**
	 * Remplis le reservoir en mettant son niveau égal à sa capacité.
	 */
	public void remplir()
	{
		niveau = capacite;
	}
	
	/*
	 * Ajoute une quantité demandée dans le reservoir. 
	 */
	public void ajtQte(double qte) 
	{
		if (qte + niveau > capacite)
		{
			remplir();
		}
		niveau += qte;
	}
	
	/**
	 * Récupère du reservoir une quantitée demandée. 
	 * Si le reservoir est vide, cette méthode lance l'exception CapaciteDepasseeException
	 * Si le reservoir n'est pas vide mais ne peut pas donner toute la quantitée demandée, cette méthode lance NiveauInsuffisantException
	 * 
	 * La marge est importante car nous opérons avec une qte qui est un double. Donc les valeurs sont arrondies et lorsque 
	 * l'on veut retirer une quantité du réservoir, cela calcule un arrondi. On condidère donc que le reservoir est vide si son 
	 * niveau est en dessous de 0+marge avec une marge très petite.
	 * 
	 * @param qte (double)
	 * @throws NiveauInsuffisantException
	 * @throws IllegalArgumentException
	 * @throws CapaciteDepasseeException
	 */
	public void recuperer(double qte) throws NiveauInsuffisantException, IllegalArgumentException, CapaciteDepasseeException
	{
		double marge = 0.000001;	// la marge permet d'ajuster le manque de précision des flottants, par exemple lorsque l'on décrémente le reservoir de la poule de 0.1 tous les jours
		if (niveau <= 0 + marge)
		{
			throw new CapaciteDepasseeException("Vous ne pouvez pas retirer : "+qte+" car le reservoir est vide.");
		}
		if (qte + marge > niveau)
		{
			throw new NiveauInsuffisantException("Niveau du réservoir insufisant pour retirer "+qte+".");
		}
		if (qte < 0)
		{
			throw new IllegalArgumentException("Vous essayez de récupérer une capacité négative du réservoir !");
		}
		niveau -= qte;
	}
	
	public void vider()
	{
		niveau = 0.0;
	}
	
	
	public double getCapacite()
	{
		return capacite;
	}
	
	public double getNiveau()
	{
		return niveau;
	}
	

}