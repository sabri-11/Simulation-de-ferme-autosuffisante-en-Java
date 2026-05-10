package projet;

/**
 * La classe Entite est abstraite car on instanciera uniquement des poules, des vaches, du blé ou des tomates mais jamais d'entitée.
 * Cette classe permet d'initialiser la méthode gererDeplacement(Ferme f) qui sera à implémenter dans les classes filles.
 * Elle permet également d'implémenter les méthodes communes aux plantes et aux animaux.
 */
public abstract class Entite
{
	
	private double prix;
	private Reservoir resEau;
	private double consoEau;
	private double consoNutriment;
	private int capDep;

	
	protected Entite(double prix, double capEau, double consoEau, double consoNF, int capDep)
	{
		this.prix = prix;
		this.resEau = new Reservoir(capEau, capEau);
		this.consoEau = consoEau;
		this.consoNutriment = consoNF;
		this.capDep = capDep;
	}
	
	/**
	 * Permet d'obliger les classes qui hériteront de Entite à implémenter la méthode gererDeplacement (Ferme f) car les animaux doivent pouvoir se déplacer et
	 * on pourrait penser à rajouter un déplacement de plante à l'avenir sans que les deux entités suivent la même logique de déplacement. 
	 * @param f
	 */
	protected abstract void gererDeplacement(Ferme f);

	/**
	 * Renvoie true si l'entité a soif et false sinon.
	 * @return (booleen)
	 */
	public boolean aSoif()	
	{
		if (resEau.getNiveau() < resEau.getCapacite()/2)
		{
			return true;
		}
		return false;
	}
	
	/**
	 * Renvoie le prix que coûte l'entité.
	 * @return (booleen)
	 */
	public double getPrix()
	{
		return prix;
	}
	

	/**
	 * Les plantes et les animaux doivent consommer de l'eau chaque jours. On peut donc implémenter cette méthode ici.
	 * @param qte (double)
	 * @throws NiveauInsuffisantException, si le reservoir ne contient pas assez d'eau pour retirer la quantité demandée.
	 * @throws CapaciteDepasseeException, si le reservoir d'eau est vide. 
	 */
	public void consommerEau(double qte) throws NiveauInsuffisantException, CapaciteDepasseeException
	{
		try
		{
			resEau.recuperer(qte);
		}
		catch (IllegalArgumentException e)
		{
			System.err.println(e.getMessage());
		}
	}
	
	
	public void remplirResEau()
	{
		resEau.remplir();
	}
	
	
	public void ajtQteEau(double qte)
	{
		resEau.ajtQte(qte);
	}
	
	
	public double getNiveauEau()
	{
		return resEau.getNiveau();
	}
	
	public double getCapEau()
	{
		return resEau.getCapacite();
	}
	
	public double getConsoEau()
	{
		return consoEau;
	}
	
	public int getCapDep()
	{
		return capDep;
	}
	
	public double getConsoNutriment()
	{
		return consoNutriment;
	}

}
