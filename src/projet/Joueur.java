package projet;

/**
 * La classe joeur permet de stocker l'argent et le nom du joueur. Elle permet d'ajouter et de retirer de l'argent lors de 
 * l'achat d'entité, de puit ou de fertilisant par exemple. Le nom permet d'avoir l'affichage du nom du joueur dans le titre.
 */
public class Joueur
{
	String nom;
	double argent;	//15000
	Ferme f;

	
	public Joueur(String nom, double argent, Ferme f)
	{
		this.nom = nom;
		this.argent = argent;
		this.f = f;
	}
	
	/**
	 * Renvoie le nom du joueur.
	 * @return (String)
	 */
	public String toString()
	{
		return nom;
	}
	
	/**
	 * Permet au joueur d'acheter une poule, vérifie uniquement que le joueur ait assez d'argent et retire le prix d'une poule à l'argent joueur s'il a assez.
	 * Lance AchatImpossibleException si le joueur n'a pas assez d'argent.
	 * @param x (int)
	 * @param y (int)
	 * @throws AchatImpossibleException
	 * @throws CasePleineException
	 * @throws AjoutImpossibleException
	 */
	public void acheterPoule(int x, int y) throws AchatImpossibleException, CasePleineException, AjoutImpossibleException
	{
		Entite e = new Poule();
		if (argent < e.getPrix())
		{
			e = null;
			throw new AchatImpossibleException("Vous n'avez plus assez d'argent pour acheter une poule. Il vous reste : "+argent+"Y");
		}

		argent -= e.getPrix();
		f.ajtAnimal((Poule) e, x, y);
		
	}
	
	/**
	 * Permet au joueur d'acheter une vache, vérifie uniquement que le joueur ait assez d'argent et retire le prix d'une vache à l'argent joueur s'il a assez.
	 * Lance AchatImpossibleException si le joueur n'a pas assez d'argent.
	 * @param x (int)
	 * @param y (int)
	 * @throws AchatImpossibleException
	 * @throws CasePleineException
	 * @throws AjoutImpossibleException
	 */
	public void acheterVache(int x, int y) throws AchatImpossibleException, CasePleineException, AjoutImpossibleException
	{
		Entite e = new Vache();
		if (argent < e.getPrix())
		{
			e = null;
			throw new AchatImpossibleException("Vous n'avez plus assez d'argent pour acheter une vache. Il vous reste : "+argent+"Y");
		}
		argent -= e.getPrix();
		f.ajtAnimal((Vache) e, x, y);
			
	}
	
	/**
	 * Permet au joueur d'acheter une tomate, vérifie uniquement que le joueur a assez d'argent et retire le prix d'une tomate à l'argent joueur s'il a assez.
	 * Lance AchatImpossibleException si le joueur n'a pas assez d'argent.
	 * @param x (int)
	 * @param y (int)
	 * @throws AchatImpossibleException
	 * @throws CasePleineException
	 * @throws AjoutImpossibleException
	 */
	public void acheterTomate(int x, int y) throws AchatImpossibleException, CasePleineException, AjoutImpossibleException
	{
		Entite e = new Tomate();
		if (argent < e.getPrix())
		{
			e = null;
			throw new AchatImpossibleException("Vous n'avez plus assez d'argent pour acheter une tomate. Il vous reste : "+argent+"Y");
		}
		argent -= e.getPrix();
		f.ajtPlante((Tomate) e, x, y);
	}
	
	
	/**
	 * Permet au joueur d'acheter du blé, vérifie uniquement que le joueur ait assez d'argent et retire le prix d'une tige de blé à l'argent joueur s'il a assez.
	 * Lance AchatImpossibleException si le joueur n'a pas assez d'argent.
	 * @param x (int)
	 * @param y (int)
	 * @throws AchatImpossibleException
	 * @throws CasePleineException
	 * @throws AjoutImpossibleException
	 */
	public void acheterBle(int x, int y) throws AchatImpossibleException, CasePleineException, AjoutImpossibleException
	{
		Entite e = new Ble();
		if (argent < e.getPrix())
		{
			e = null;
			throw new AchatImpossibleException("Vous n'avez plus assez d'argent pour acheter du blé. Il vous reste : "+argent+"Y");
		}

		argent -= e.getPrix();
		f.ajtPlante((Ble) e, x, y);
			
	}
	
	
	/**
	 * Permet au joueur d'acheter un puit, vérifie uniquement que le joueur ait assez d'argent et retire le prix d'un puit à l'argent joueur s'il a assez.
	 * Lance AchatImpossibleException si le joueur n'a pas assez d'argent.
	 * @param x (int)
	 * @param y (int)
	 * @throws AchatImpossibleException
	 * @throws CasePleineException
	 * @throws AjoutImpossibleException
	 */
	public void acheterPuit(int x, int y) throws AchatImpossibleException, CasePleineException, AjoutImpossibleException
	{
		if (argent < Constante.prixCons)
		{
			throw new AchatImpossibleException("Vous n'avez pas assez d'argent pour acheter un puit. Il vous reste : "+argent+"Y");
		}
	
		argent -= Constante.prixCons;
		f.ajtPuit(x, y);

	}

	/**
	 * Permet au joueur de détruire un puit, vérifie uniquement que le joueur ait assez d'argent et retire le prix de la destruction d'un 
	 * puit à l'argent joueur s'il a assez.
	 * Lance AchatImpossibleException si le joueur n'a pas assez d'argent.
	 * @param x (int)
	 * @param y (int)
	 * @throws AchatImpossibleException
	 * @throws CasePleineException
	 * @throws AjoutImpossibleException
	 */
	public void detruirePuit(int x, int y) throws DestructionImpossibleException
	{
		
		if (argent < Constante.prixDes) throw new DestructionImpossibleException("Vous n'avez pas assez d'argent pour détruire le puit. Il vous reste : "+argent+"Y");

		try 
		{
			Case c = f.casePosition(x, y);
			c.suppPuit();
			argent -= Constante.prixDes;
		}
		catch (IllegalArgumentException | DestructionImpossibleException e)
		{
			throw new DestructionImpossibleException(e.getMessage());
		}
		
	}
	
	/**
	 * Permet au joueur d'acheter et de déposer une quantité de fertilisant sur une case (x, y). Avant l'appel de cette méthode, un pop up 
	 * demandera au joueur la quantité de fertilisant qu'il veut déposer en g et on calcule ici le prix que cela coûte en fonction du prix du 
	 * fertilisant au kilogramme. On vérifie que le joueur ait assez d'argent et on le crédite.  
	 * S'il n'a pas assez d'argent, on lance AchatImpossibleException.
	 * 
	 * @param qte (double)
	 * @param x (int)
	 * @param y (int)
	 * @throws AchatImpossibleException
	 */
	public void deposerFertil(double qte, int x, int y) throws AchatImpossibleException
	{
		double prix = (qte/1000)*Constante.prixFertilKG;
		if (argent < prix)
			throw new AchatImpossibleException("Vous n'avez pas assez d'argent pour déposer "+qte+"g.");
		
		f.ajtFertil(qte, x, y);
		argent -= prix;
		
	}

	
	/**
	 * Ajoute une quantité d'argent à l'argent du joueur.
	 * @param qte (double)
	 * @throws IllegalArgumentException
	 */
	public void gagnerArgent(double qte) throws IllegalArgumentException
	{
		if (qte < 0) throw new IllegalArgumentException("Vous essayer d'utiliser 'gagnerArgent' avec une valeure négative : "+qte+". Utilisez plutôt 'payer(double qte)'");
		argent += qte;
	}

	/**
	 * Renvoie l'argent que possède le joueur.
	 * @return (double)
	 */
	public double getArgent()
	{
		return argent;
	}
	
	/**
	 * Permet de modifier le nom du joueur pour pouvoir l'afficher en titre : "Ferme autosouternable de : 'nom'"
	 * @param nom (String)
	 */
	public void setNom(String nom)
	  {
		  if (nom == null || nom.trim().isEmpty()) return;
		  this.nom = nom;
	  }
	
}
