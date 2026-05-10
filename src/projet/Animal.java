package projet;



/**
 * La classe Animal est une classe abstraite car on instanciera jamais d'animal mais uniquememnt des poules ou des vaches.
 * Cette classe permet de gérer les déplacements d'un animal, de lui ajouter ou retirer une quantité d'eau ou de nourriture 
 * et de gérer s'il est ou à un bébé, en réglant ses constantes en fonction.
 */
public abstract class Animal extends Entite
{
	private Reservoir resNour;
	
	private double consoEau; // utilisé dans configBebe() et devientAdulte()
	private double consoNour; // utilisé dans configBebe() et devientAdulte()
	private double capNour;	
	private double capEau;	// utilisé dans configBebe() et devientAdulte()
	private double prodNour;
	private int vie;	// Un animal ne peut pas mourir en cours de saison, donc vie n'est pas un double.
	private double prodFertil;
	private double faimDimNour;
	private double soifDimNour;
	private int capDep;
	private double capRepro;

	private static int cpt=0;
	private int id;
	
	private boolean aBebe;
	private boolean estBebe;
	
	
	protected Animal(double prix, double consoEau, double consoNour, double capNour, double capEau, 
			double prodNour, int vie, double prodFertil, double faimDimNour, double soifDimNour, int capDep, double capRepro)
	{
		super(prix, capEau, consoEau, consoNour, capDep);
		resNour = new Reservoir(capNour, capNour);
		this.capEau = capEau;
		this.capNour = capNour;
		
		this.prodNour = prodNour;
		this.vie = vie;
		this.prodFertil = prodFertil;	
		this.faimDimNour = faimDimNour;
		this.soifDimNour = soifDimNour;
		this.capRepro = capRepro;
		this.id = cpt++;

		aBebe = false;
		estBebe = false;

	}
	
	/**
	 * Permet d'obliger les classes qui hériteront de Animal à implémenter une méthode creerBebe() qui renverra un animal.
	 * @return (Animal)
	 */
	protected abstract Animal creerBebe();
	
	
	/**
	 * Renvoie true si l'animal a faim (son reservoir de nourriture est vide à moitié) et false sinon.
	 * @return
	 */
	public boolean aFaim()
	{
		if (resNour.getNiveau()< capNour/2)
		{
			return true;
		}
		return false;
	}

	
	
	/**
	 * permet de gérer si l'animal à plus que 1 déplacement possible par jours.
	 * On part du principe qu'un animal cherche à boire ou manger. Une fois son objectif atteint il arrête de se déplacer.
	 * @param f (Ferme)
	 */
	public void gererDeplacement(Ferme f)		
	{

		for (int i=0; i<=capDep; i++)
		{

			try	// l'animal se déplace d'abord puis on regarde ce qu'il peut faire (manger, boire...)
			{
				f.deplacementAnimal(this);
				Case c = f.caseAnimal(this);
				
				// Si l'animal a faim, on regarde s'il peut manger. On regarde ensuite s'il a soif et si il peut boire. Si il a bu et manger, il arrête de se déplacer.
				if (this.aFaim())
				{
					// mange une qte de plante sur la même case si il y en a une jusqu'à plus faim.
					
					if (f.animalTryManger(this, c.getPlante()))
					{
						if (this.aSoif())
						{
							if (c.contientPuit())
							{
								this.boitAuPuit();
								return;
							}
						}
					}
						
					
					// Si l'animal ne peut pas manger, on vérifie tout de même s'il a soif et s'il peut boire. S'il boit il continue quand même de se déplacer car il a toujours faim donc pas de return.
					else if (this.aSoif())
					{
						if (c.contientPuit()) this.boitAuPuit();
					}
					
				}
				// Si l'animal n'a pas faim mais soif, on regarde s'il peut boire. S'il boit, il arrête de se déplacer
				else if (this.aSoif()) 
				{
					// bois jusqu'à plus soif dans un puit si sur même case que puit. if (! essayerBoire()) modif prodNour;
					if (c.contientPuit())
					{
						this.boitAuPuit();
						return;
					}
				}
				
				// Si l'animal n'a ni soif ni faim, il arrête de se déplacer
				else if ((! this.aFaim()) && (! this.aSoif())) return;

			}
			catch (AjoutImpossibleException | EntiteNonTrouveeException e)
			{
				System.err.println("Impossible de déplacer l'animal car : "+e.getMessage());
			}
		}
	}
	
	

	/**
	 * Permet de retirer une certaine quantité de nourriture au reservoir de l'animal.
	 * 
	 * @param qte (double)
	 * @throws NiveauInsuffisantException Dans le cas ou le reseroir n'est pas vide mais ne contient
	 * pas assez de nourriture pour retirer la quantitée demandée.
	 * @throws CapaciteDepasseeException Dans le cas ou le reservoir de nourriture est vide 
	 */
	public void consommerNour(double qte) throws NiveauInsuffisantException, CapaciteDepasseeException
	{
		try
		{
			resNour.recuperer(qte);
		}
		catch(IllegalArgumentException e)
		{
			System.err.println(e.getMessage());
		}
		
	}
	
	/**
	 * Remplis le reservoir d'eau de l'animal.
	 */
	public void boitAuPuit()
	{
		this.remplirResEau();
	}
	
	/**
	 * Chaque animal à un identifiant unique, getId renvoie cet identifiant.
	 * @return
	 */
	public int getId()
	{
		return id;
	}
	


	public double getProdFertil()
	{
	    return prodFertil;
	}
	
	public double getCapRepro()
	{
		return capRepro;
	}
	
	public double getProdNour()
	{
		return prodNour;
	}
	
	public double getFaimDimNour()
	{
	    return faimDimNour;
	}
	
	public double getSoifDimNour()
	{
	    return soifDimNour;
	}
	
	/**
	 * Permet de configurer les constantes de base d'un animal nouveau né : il ne produit pas de nourriture et ses constantes de base
	 * sauf son espérance de vie et sa capacité de déplacement sont divisées par 2.
	 */
	public void configBebe()
	{
		consoEau /= 2;
		consoNour /= 2;
		capEau /= 2;
		capNour /=2 ;
		this.remplirResEau();
		this.remplirResNour();
		prodNour = 0.0;
		prodFertil /= 2;
		estBebe = true;
	}
	
	/**
	 * Si un nouveau né survit à la saison en cours, on remets ses constantes à leurs valeures normales.
	 */
	public void devientAdulte()	
	{
		consoEau *= 2;
		consoNour *= 2;
		capEau *= 2;
		capNour *= 2;
		if (this instanceof Poule)
			prodNour = Constante.prodNourP;
		else
			prodNour = Constante.prodNourV;
		
		prodFertil *= 2;
		estBebe = false;
	}
	
	/**
	 * Indique que l'animal a déjà eu un bébé, il ne pourra donc plus en avoir pendant la saison.
	 * @param b (booleen)
	 */
	public void setA_Bebe(boolean b)
	{
		aBebe = b;
	}
	
	public boolean getABebe()
	{
		return aBebe;
	}
	
	public boolean getEstBebe()
	{
		return estBebe;
	}
	
	public double getNiveauNour()
	{
		return resNour.getNiveau();
	}
	
	/**
	 * Remplis le reservoir de nourriture de l'animal.
	 */
	public void remplirResNour()
	{
		this.resNour.remplir();
	}
	
	public double getCapNour()
	{
		return resNour.getCapacite();
	}
	
	/**
	 * Ajoute une certaine quantité de nourriture au reservoir de l'animal.
	 * @param qte
	 */
	public void mangeQte(double qte)
	{
		resNour.ajtQte(qte);
	}
	
	
	public int getVie()
	{
		return vie;
	}
	
	public void retirerVie()
	{
		this.vie -= 1;
	}
	
	
	
	
}