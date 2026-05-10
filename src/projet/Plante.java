package projet;


/**
 * La classe Plante est une classe abstraite car on instanciera jamais de plantes mais uniquememnt des tomates ou du blé.
 * Elle permet de gérer les possible déplacements d'une plante si on en rajoutait, de lui ajouter ou retirer une quantité 
 * de nourriture (si elle se fait manger) ainsi que d'appliquer les coefficient de geler les tomates et appliquer les coefficient 
 * de dimminution de croissance si elle a soif ou faim
 */
public abstract class Plante extends Entite
{	
	
	private double capEau;
	private double qteNour;
	private double capCroissance;
	private double faimDimCroiss;
	private double soifDimCroiss;
	private int capDep;
	private boolean estGelee;
	private static int cpt=0;
	private int id;
	
	public Plante(double prix, double consoEau, double capEau, double qteNour, double consoFertil, double capCroissance,
			double faimDimCroiss, double soifDimCroiss, int capDep, boolean estGelee)
	{
		super(prix, capEau, consoEau, consoFertil, capDep);
		this.capEau = capEau;

		this.qteNour = qteNour;
		this.capCroissance = capCroissance;
		this.faimDimCroiss = faimDimCroiss;
		this.soifDimCroiss = soifDimCroiss;
		this.id = cpt++;

	}
	
	/**
	 * chaque plante à un identifiant unique, getId permet de retourner cet identifiant.
	 * @return (int)
	 */
	public int getId()
	{
		return id;
	}
	

	/**
	 * Remplis le reservoir d'eau de la plante en récupérant la quantité nécéssaire sur la case.
	 * @param c (Case)
	 * @param qte (double)
	 * @throws NiveauInsuffisantException, si la case ne continet pas assez d'eau pour remplir le réservoir d'eau de la plante.
	 * @throws CapaciteDepasseeException, si la case n'a pas du tout d'eau.
	 */
	public void boitSurCase(Case c, double qte) throws NiveauInsuffisantException, CapaciteDepasseeException
	{
		c.recupEau(qte);
		this.remplirResEau();
	}
	
	/**
	 * Fais croître la plante de la capacité de croissance données en paramètre car elle a pu être
	 * modifié dans Simulation en fonction de si la plante avait soif ou faim ce jours.
	 * @param capCroissance
	 */
	public void croissance(double capCroissance)
	{
		if (capCroissance < 0) throw new IllegalArgumentException();
		this.qteNour += capCroissance;
	}
	
	/**
	 * Possible cas ou une plante pourrait se déplacer si l'on modifie les règels du jeu, par exemple : chaque jours
	 * un fermier peut venir déplacer une plante de n cases.
	 * 
	 * On part du principe qu'une plante gelée ne peut pas se déplacer. Ensuite, en se déplaçant la plante va chercher à
	 * manger/boire assez de fertilisant/eau pour assouvir sa consommation.
	 * @param (Ferme)
	 */
	public void gererDeplacement(Ferme f)
	{

		if (this.getEstGelee()) return;
		
		for (int i=0; i<=capDep; i++)
		{
			try	// la plante se déplace d'abord puis on regarde ce qu'elle peut faire (manger, boire...)
			{
				f.deplacementPlante(this);	// peut lever AjoutImpossible ou EntiteNonTrouvee
				Case c = f.casePlante(this);
				
				if (this.aSoif())	// On regarde si la plante a soif et si elle peut boire on remplit son reservoir
				{
					if (f.puitAutour(c)) this.remplirResEau();
				}
				
				// On regarde ensuite si elle peut manger 
				try
				{
					c.recupFertil(this.getConsoNutriment());
					if (this.getNiveauEau() == this.capEau) return;  // Si son reservoir d'eau est plein et qu'elle a mangé assez de fertilisant, elle ne se déplace plus
				}
				catch (NiveauInsuffisantException e)
				{
					// S'il n'y a plus de fertilisant la plante va changer de case.
				}
				catch (CapaciteDepasseeException e)
				{
					// S'il n'y a pas assez de fertilisant elle ne le prends pas et change de case s'il lui reste un déplacement.
				}
				catch(IllegalArgumentException e)
				{
					// tente de récupérer une qte négative
					System.err.println(e.getMessage());
				}
			}
			catch (AjoutImpossibleException | EntiteNonTrouveeException e)
			{
				System.err.println("Impossible de déplacer l'animal car : "+e.getMessage());
			}
		}
	}
	
	
	public boolean getEstGelee()
	{
		return estGelee;
	}
	
	/**
	 * Gele une plante
	 */
	public void geler()
	{
		estGelee = true;
	}
	
	
	public double getCapCroissance()
	{
		return capCroissance;
	}
	
	public double getSoifDimCroiss()
	{
		return soifDimCroiss;
	}
	
	public double getFaimDimCroiss()
	{
		return faimDimCroiss;
	}
	
	public double getQteNour()
	{
		return qteNour;
	}
	
	public void retirerQteNour(double qte)
	{
		qteNour -= qte;
	}
	
	
}