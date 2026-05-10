package projet;

/**
 * La classe Case permet de donner des informations sur ce que contient une case de la ferme. 
 * On peut savoir si ele contient quelle instance d'animal, de plante, si elle contient un puit ou du fertilisant ainsi que si elle est pleine ou vide.
 * Elle permet également de gérer l'eau et le fertilisant présents sur la case et d'ajouter ou supprimé une entité de cette case. 
 */
public class Case
{
	private Reservoir resEau;
	private Reservoir resFertil;

	private Animal a;
	private Plante pl;
	private boolean puit; 
	
	int x, y;
	
	public Case(int x, int y)
	{
		
		resEau = new Reservoir(Double.POSITIVE_INFINITY, Constante.qteInitEau);	// Capacité d'eau d'une case ? Niveau initial d'eau sur une case ?
		resFertil = new Reservoir(Double.POSITIVE_INFINITY, Constante.qteInitFertil);	// niveau initial de fertilisant non précisé
		this.x = x;
		this.y = y;
		this.puit = false;
	}

	/**
	 * Renvoie true si la case contient un animal et false sinon.
	 * @return (booleen)
	 */
	public boolean contientAnimal()
	{
		if (a != null) return true;
			
		return false;
	}
	
	/**
	 * Renvoie true si la case contient une poule et false sinon.
	 * @return (booleen)
	 */
	public boolean contientPoule()
	{
		if (a != null && a instanceof Poule) return true;
		return false;
	}
	
	/**
	 * Renvoie true si la case contient une vache et false sinon.
	 * @return (booleen)
	 */
	public boolean contientVache()
	{
		if (a != null && a instanceof Vache) return true;
		return false;
	}

	
	/**
	 * Renvoie true si la case contient une plante et false sinon.
	 * @return (booleen)
	 */
	public boolean contientPlante()
	{
		if (pl != null) return true;
		return false;
	}
	
	/**
	 * Renvoie true si la case contient du fertilisant et false sinon.
	 * @return (booleen)
	 */
	public boolean contientFertil1()
	{
		if (resFertil.getNiveau() > 0 && resFertil.getNiveau() < 300)
			return true;
		
		return false;
	}
	
	public boolean contientFertil2()
	{
		if (resFertil.getNiveau() >= 300)
			return true;
		
		return false;
	}
	
	public boolean contientEau1()
	{
		if (resEau.getNiveau() > 0 && resEau.getNiveau() <=3.0)
			return true;
		
		return false;
	}
	
	public boolean contientEau2()
	{
		if (resEau.getNiveau() > 3.0)
			return true;
		
		return false;
	}
	
	
	/**
	 * Renvoie true si la case est vide et false sinon.
	 * @return (booleen)
	 */
	public boolean estVide()
	{
		if (a == null && pl == null && puit == false) return true;
		return false;
	}
	
	
	/**
	 * Renvoie true si la case contient est pleine (animal + plante ou plante + puit) et false sinon.
	 * @return (booleen)
	 */
	public boolean estPleine()
	{
		if (a != null && pl != null) return true;
		if (a != null && puit == true) return true;
		return false;
	}
	
	/**
	 * Renvoie true si la case contient un puit et false sinon.
	 * @return (booleen)
	 */
	public boolean contientPuit()
	{
		if (puit == true) return true;
		return false;
	}

	
	/**
	 * Ajoute une entité (Animal ou Plante) sur la case mais pas dans l'ArrayList des entités de la ferme car elle se situe dans la classe ferme.
	 * @param e (Entite)
	 * @throws AjoutImpossibleException
	 */
	public void ajtEntite(Entite e) throws AjoutImpossibleException
	{
		if (estPleine())
		{
			throw new AjoutImpossibleException("Ajout de l'entité sur la case impossible, la case est déjà pleine");
		}
		
		if (e instanceof Animal)
		{
			if (contientAnimal())
			{
				throw new AjoutImpossibleException("Ajout d'un animal impossible, la case contient déjà un animal");
			}

			a = (Animal) e;
		}
		else if (e instanceof Plante)
		{
			if (contientPlante())
			{
				throw new AjoutImpossibleException("Ajout d'une plante impossible, la case contient déjà une plante");
			}
			if (contientPuit())
			{
				throw new AjoutImpossibleException("Ajout d'une plante impossible, la case contient déjà un puit");
			}
			pl = (Plante)e;

		}
	}
	
	/**
	 * Supprime une entité de la case mais pas de son ArrayList de la ferme.
	 * @param e (Entite)
	 */
	public void suppEntite(Entite e)
	{
		if (e instanceof Animal)
		{
			a = null;
		}
		else if (e instanceof Plante)
		{
			pl = null;
		}
		else throw new IllegalArgumentException("Vous utilisez suppEntite sur autre chose qu'une Entité.");
	}
	
	
	/**
	 * Ajoute un puit sur la case mais pas dans l'ArrayList puits de la ferme.
	 * @param p
	 * @throws AjoutImpossibleException
	 */
	public void ajtPuit() throws AjoutImpossibleException
	{
		if (estPleine())
		{
			throw new AjoutImpossibleException("Ajout d'un puit impossible, la case est déjà pleine");
		}
		if (contientPlante())
		{
			throw new AjoutImpossibleException("Ajout d'un puit impossible, la case contient déjà une plante");
		}
		if (contientPuit())
		{
			throw new AjoutImpossibleException("Ajout d'un puit impossible, la case contient déjà un puit");
		}
		
		this.puit = true;
	}
	
	
	/**
	 * Supprime le puit de la case s'il y en avait un.
	 */
	public void suppPuit() throws DestructionImpossibleException
	{
		if (puit == false)
		{
			throw new DestructionImpossibleException("Destruction impossible, la case ne contient pas de puit");
		}
		puit = false;
	}
	
	/**
	 * Renvoie l'animal contenu sur la case s'il y en a un, renvoie null sinon
	 * 
	 * @return (Animal)
	 */
	public Animal getAnimal()
	{
		return a;
	}
	
	/**
	 * Renvoie la plante contenu sur la case s'il y en a une, renvoie null sinon
	 * @return (Plante)
	 */
	public Plante getPlante()
	{
		return pl;
	}
	
	/**
	 * Renvoie le puit contenu sur la case s'il y en a un, renvoie null sinon
	 * @return (Puit)
	 */
	public boolean getPuit()
	{
		return puit;
	}
	
	/**
	 * Ajoute de une quantité d'eau sur la case (utile lors de pluies par exemple).
	 * @param qte (double)
	 */
	public void ajtEau(double qte)
	{
		resEau.ajtQte(qte);	
	}
	
	
	/**
	 * Recupere une quantité d'eau de la case grâce à la méthode recuperer de la classe Reservoir.
	 * 
	 * @param qte (double)
	 * @throws NiveauInsuffisantException
	 * @throws CapaciteDepasseeException
	 */
	public void recupEau(double qte) throws NiveauInsuffisantException, CapaciteDepasseeException
	{
		resEau.recuperer(qte);
	}
	

	public double getNiveauEau()
	{
		return resEau.getNiveau();
	}
	
	public double getNiveauFertil()
	{
		return resFertil.getNiveau();
	}
	
	/**
	 * Vide le reservoir d'eau de la case
	 */
	public void viderEau()
	{
		resEau.vider();
	}
	
	
	public void ajtFertil(double qte)
	{
		resFertil.ajtQte(qte);
	}
	
	/**
	 * Ajoute de une quantité de fertilisant sur la case.
	 * @param qte (double)
	 */
	public void recupFertil(double qte) throws NiveauInsuffisantException, IllegalArgumentException, CapaciteDepasseeException
	{
		resFertil.recuperer(qte);
	}
	
	/**
	 * Vide le reservoir de fertilisant de la case
	 */
	public void viderFertil()
	{
		resFertil.vider();
	}
	
	/**
	 * Renvoie la coordonnée x de la case sur la grille
	 * @return (int)
	 */
	public int getX()
	{
		return x;
	}
	
	/**
	 *  Renvoie la coordonnée y de la case sur la grille
	 * @return (int)
	 */
	public int getY()
	{
		return y;
	}
	
	/**
	 * Renvoie true si la case passée en paramètre est strictement placée à la même position que cette case en comparant leur coordonnées. 
	 * @return (booleen)
	 */
	public boolean equals(Object o)
	{
		if (o == null) return false; 
		if (this == o) return true;
		if (getClass() != o.getClass()) return false;
		
		Case c = (Case) o;
		
		if (this.x == c.x && this.y == c.y) return true;
		return false;
	}

}
