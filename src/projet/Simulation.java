package projet;

import java.util.Random;
import java.util.ArrayList;

/**
 * La classe Simulation permet de gérer le comportement des différentes entités de la ferme ainsi que de commencer une saison, passer 
 * des jours, finir une saison en récoltant l'argent gagné, tuant les plantes... et de gérer l'interaction de fin de saison avec l'utilisateur.
 */
public class Simulation
{
	
    public enum Saison
    {
    	HIVER, PRINTEMPS, ETE, AUTOMNE
    }
    private Ferme f;
    private Joueur joueur;
    private boolean enPause, placementPhase;
    private Random rand;
    
    private int jours;
    private int saison;
    private Saison saisonActuelle;
    private double entrepot;
    
    private int poulesRestantes, vachesRestantes;
    
    private ArrayList<Poule> poulesASauver;
    private ArrayList<Vache> vachesASauver;
    
    public Simulation(String nom, double argentInit)
    {
    	this.f = new Ferme();
    	this.joueur = new Joueur(nom, argentInit, f);
    	this.rand = new Random();
    	this.jours = 1;
    	this.saison = rand.nextInt(4); // 0 : Hiver, 1 : Printemps, 2 : Ete, 3 : Automne
    	setSaison(saison);
    	this.entrepot = 0.0;
    	this.enPause = true;
    	this.placementPhase = false;
    }
    

	/**
	 * Cette méthode permet de fixer une première saison aléatoirement en fonction d'un entier tiré au hasard.
	 * @param saison (int)
	 */
	public void setSaison(int saison) 
	{
		switch(saison)
		{
			case 0:
                saisonActuelle = Saison.HIVER;
                break;
			case 1:
                saisonActuelle = Saison.PRINTEMPS;
                break;
			case 2:
                saisonActuelle = Saison.ETE;
                break;
			case 3:
                saisonActuelle = Saison.AUTOMNE;
                break;
            default:
            	throw new IllegalArgumentException("Mauvais indice de saison : "+saison+", il doit être compris entre 0 et 3 inclusS");
		}
	}
    
    /**
     * Permet de retirer une vie à tous les animaux ayant passé une saison et de les tuer s'ils n'ont plus de vies.
     */
    private void reglerVie()
    {
    	ArrayList<Animal> animauxMorts = new ArrayList<>();
    	
    	for (Poule p : f.getPoules())
    	{
    		p.retirerVie();
    		if (p.getVie() <= 0)
    			animauxMorts.add(p);
    	}
    	
    	for (Vache v : f.getVaches())
    	{
    		v.retirerVie();
    		if (v.getVie() <= 0)
    			animauxMorts.add(v);	
    	}
    	
    	for (Animal a : animauxMorts)
    		f.suppAnimal(a);
    }
    
    /**
     * Permet de passer la saison jours par jours en gérant les différents processus qui arrive chaque jours : la pluie, le comportement des animaux et
     * des plantes. Passé 30 jours, on appelle reglerVie pour retirer une vie aux animaux encore en vie, on vide la grille et on reset les paramètres
     * @return (boolean)
     */
    public boolean unJour()
    {
    	if (placementPhase) return false;
    	
    	gererPluie();
    	gererAnimaux();
    	gererPlante();
    	
    	jours++;
    	
    	if (jours > 30)
    	{
    		reglerVie();
    		
    		if (! f.getPoules().isEmpty() || ! f.getVaches().isEmpty() || !f.getTomates().isEmpty() || ! f.getBles().isEmpty())
    			viderGrille();
    		else 
    			resetParametres();
    		
    		return false;
    			
    	}
    	return true;
    }
    
    /**
     * Vide la grille des animaux et plantes restants en fin de saison tout en remplissant les reservoirs d'eau et de nourriture des animaux.
     * Si un bébé à survécu, il devient adulte en doublant ses constantes de bases 
     * Appelle recolter au début pour gagner l'argent que nous rapportent les plantes avant de les supprimer.
     */
    
    private void viderGrille()
    {
    	stop();
    	recolter();
    	if (! f.getPoules().isEmpty() || ! f.getVaches().isEmpty() )
    		placementPhase = true;
    	
    	poulesASauver = new ArrayList<Poule>(f.getPoules());
    	vachesASauver = new ArrayList<Vache>(f.getVaches());
    	
    	for (Poule p : poulesASauver)
    	{
        	if (p.getEstBebe())
        		p.devientAdulte();
        	
    		p.remplirResEau();
    		p.remplirResNour();
    		f.suppAnimalTemp(p);
    	}
    	
    	for (Vache v : vachesASauver)
    	{
        	if (v.getEstBebe())
        		v.devientAdulte();
        	
    		v.remplirResEau();
    		v.remplirResNour();
    		f.suppAnimalTemp(v);
    	}
    	f.nettoyerAnimauxMorts();
    	
    	ArrayList<Ble> bleRestant = new ArrayList<Ble>(f.getBles());
    	ArrayList<Tomate> tomateRestantes = new ArrayList<Tomate>(f.getTomates());
    	
    	for (Plante pl : bleRestant)
    	{
    		f.suppPlante(pl);
    	}
    	
    	for (Plante pl : tomateRestantes)
    	{
    		f.suppPlante(pl);
    	}
    	
    	poulesRestantes = poulesASauver.size();
    	vachesRestantes = vachesASauver.size();
    	

    }
    

    /**
     * Permet de récolter l'argent représenté par la quantité de calories produites par les vaches et les poules durant la saison ainsi
     * que la nourriture que représente les plantes encore en vie. 
     * Ce n'est pas précisé dans l'ennoncé mais on suppose que 1 calories représente 0.75Y.
     */
    private void recolter()
    {
    	double argentGagnee = 0.0;
    	argentGagnee += entrepot * Constante.pourcentage;
    	
    	for (Plante pl : new ArrayList<Ble>(f.getBles()))
    	{
    		argentGagnee += pl.getQteNour() * Constante.pourcentage;
    		f.suppPlante(pl);
    	}
    	
    	for (Plante pl : new ArrayList<Tomate>(f.getTomates()))
    	{
    		argentGagnee += pl.getQteNour() * Constante.pourcentage;
    		f.suppPlante(pl);
    	}
    	
    	try
    	{
    		joueur.gagnerArgent(argentGagnee);
    	}
    	catch (IllegalArgumentException e)
    	{
    		System.err.println(e.getMessage());
    	}
    	
    	entrepot = 0.0;
    	jours = 1;
    	saison = (saison+1)%4;
		setSaison(saison);
    	
    }
    
    /**
     * Une fois que la grile est vidée, resetParametres permet de réinitialiser les paramètres de l'entrepôt, le jours et de changer de saison.
     * On mets également le booleen enPause à true pour pouvoir stopper la simulation et laisser l'utilisateur intéragir. 

     */
    private void resetParametres()
    {	
    	
    	entrepot = 0.0;
    	jours = 1;
    	saison = (saison+1)%4;
		setSaison(saison);
		
		enPause = true;
		placementPhase = false;
		poulesRestantes = 0;
		vachesRestantes = 0;
    	
    }
    
    /**
     * Est appelé dans la méthode handleGridClick de la Classe FenetreJeu pour placer les poules encore en vie de la dernière saison.
     * @param x (int)
     * @param y (int)
     * @throws AjoutImpossibleException
     */
    public void placerPoulesEnVie(int x, int y) throws  AjoutImpossibleException
    {
    	if (! placementPhase || poulesRestantes <= 0)
    		throw new IllegalArgumentException("Aucune poule à placer");
    	
    	Poule p = poulesASauver.remove(0);

    	f.ajtAnimal(p, x, y);
    	poulesRestantes--;
    	
    	if (poulesRestantes <= 0 && vachesRestantes <= 0) placementPhase = false;
    	
    }

    /**
     * Pareil que placerPoulesEnVie mais pour les vaches.
     * @param x (int)
     * @param y (int)
     * @throws AjoutImpossibleException
     */
    public void placerVachesEnVie(int x, int y) throws  AjoutImpossibleException
    {
    	if (! placementPhase || vachesRestantes <= 0)
    		throw new IllegalArgumentException("Aucune poule à placer");
    	
    	Vache v = vachesASauver.remove(0);
    	
    	f.ajtAnimal(v, x, y);
    	vachesRestantes--;
    	
    	if (poulesRestantes <= 0 && vachesRestantes <= 0) placementPhase = false;
    }
    

    /*
     * Cette méthode permet de gérer les précipitations en fonction des différentes saison avec leur probabilitées respectives ainsi que d'ajouter de l'eau sur les
     * cases s'il pleut (avec la quantité en fonction de la saison).
     */
	private void gererPluie()
	{
		double probaPluie = 0.0;
		double qteEau = 0.0;
		
		switch(saisonActuelle)
		{
			case HIVER:
				probaPluie = 2.0/5.0;
				qteEau = 0.2;
				break;
			case PRINTEMPS:
				probaPluie = 3.0/5.0;
				qteEau = 0.1;
				break;
			case ETE:
				probaPluie = 1.0/20.0;
				qteEau = 0.1;
				break;
			case AUTOMNE:
				probaPluie = 4.0/5.0;
				qteEau = 0.2;
				break;
		}
		
		double p = rand.nextDouble();
		if (p < probaPluie)
		{
			for (int i=0; i<Constante.n; i++)
			{
				for (int j=0; j<Constante.m; j++)
				{
					Case c = f.casePosition(i, j);
					if (! c.contientPuit())
						c.ajtEau(qteEau);
				}
				
			}
//			System.out.println("Il a plu");
		}
	}
	
	
	
	/**
	 * Cette méthode permet de gérer le comportement des animaux en fonction des saisons en envoyant vers gererAnimal qui gére chaque poule et vache.
	 * On récupère à la fin les nouveaux nés ou les animaux morts s'il y en a eu. pour les ajouter/supprimer complétement.
	 * 
	 */
    private void gererAnimaux()
	{
		
		for (Poule p : f.getPoules())
		{
			gererAnimal(p);
		}
		
		
		for (Vache v : f.getVaches())
		{
			gererAnimal(v);
		}
		f.ajtNvNes();
		f.nettoyerAnimauxMorts();
//		Initialement, l'ajout sur la grille des nouveaux nés ou la suppression des animaux mort se faisait dans dans la fonction gererAnimal
//		mais cela causait un problème car on essaye de modifier les ArrayList de vaches ou poules alors qu'on les parcourt dans la boucle foreach.
		
	}
	
    /**
     *Cette fonction gère les comportements des animaux en fonction des saisons ainsi que leur déplacement en envoyant vers gererDeplacementAnimal
     *On ajoute le fertilisant produit naturellement par les animaux sur la case où ils sont, on vérifie si ils ont soif, faim et s'ils peuvent boire où manger.
     *La quantité d'eau et de nourriture consommé chaque jours par les animaux est également retirée et si leur reservoir est vide, on les ajoute dans une liste
     *d'animaux à supprimer pour ensuite les supprimer avec f.nettoyerAnimauxMorts dans gererAnimaux.
     * @param a (Animal)
     */
	private void gererAnimal(Animal a) 
	{
		
		double consoEau = a.getConsoEau();
		double consoNour = a.getConsoNutriment();
		double prodNour = a.getProdNour();
		double prodFertil = a.getProdFertil();
		
		if (a.getCapDep() > 0) a.gererDeplacement(f);
		
		try 
		{
			Case c = f.caseAnimal(a);	// peut lever EntiteNonTrouvee
			if (a.aFaim())
			{
				// mange une qte de plante sur la même case si il y en a une jusqu'à plus faim.

				if (! f.animalTryManger(a, c.getPlante()))
				{
					prodNour -= prodNour*a.getFaimDimNour();	// on retire 10% à la production de nourriture si la poule a faim.
				}

			}
			if (a.aSoif())
			{
				
				// bois jusqu'à plus soif dans un puit si sur même case que puit. if (! essayerBoire()) modif prodNour;
				if (c.contientPuit())
				{
					a.boitAuPuit();
				}
				else prodNour -= prodNour*a.getSoifDimNour();
				
			}
			
			
			if (saisonActuelle == Saison.ETE)
			{
				if (a instanceof Poule) consoNour *= 1.25;
				else if (a instanceof Vache) consoEau *= 1.3;
				
			}
			
			if (saisonActuelle == Saison.HIVER)
			{
				if (a instanceof Poule)
				{
					double proba = 1.0/3.0;
					double rnd = rand.nextDouble();
					if (rnd > proba) prodNour = 0.0;	// proba de 2/3 que la poule ne produise pas d'oeufs.
				}
				else if (a instanceof Vache)
				{
					prodNour *= 0.5;
				}
				 
			}
			entrepot += prodNour;
			
			c.ajtFertil(prodFertil);	// on ajoute le fertilisant naturel que produit l'animal sur la case ou il se trouve
			
			if (saisonActuelle == Saison.PRINTEMPS)
			{
				f.tryReproAnimal(a);	// peut lever ReproImpossibleException 
			}
		}
		catch(EntiteNonTrouveeException | ReproImpossibleException e)
		{
			System.err.println("Erreur lors du déplacement de l'animal car : "+e.getMessage());
			
		}
		try
		{
			a.consommerEau(consoEau);
//			System.out.println("niveau réservoir eau "+a.getClass()+" : "+a.getNiveauEau()+".");
			a.consommerNour(consoNour);
//			System.out.println("niveau réservoir nourriture "+a.getClass()+" : "+a.getNiveauNour()+".");
		}
		catch (NiveauInsuffisantException | CapaciteDepasseeException e)
		{
			//a.meurt();	//	ptt juste faire f.suppAnimal(a);
			f.suppAnimalTemp(a);
		}
		
		
	}
	
	/**
	 * Pareil que gérerAnimaux mais pour les plantes.
	 */
	private void gererPlante()
	{
		for (Plante p : new ArrayList<Tomate>(f.getTomates()))
		{
			if (saisonActuelle == Saison.HIVER) p.geler();
			else gererPlanteType(p);
//			System.out.println("qteNour tomate : "+p.getQteNour());
//			System.out.println("qteEau tomate : "+p.getNiveauEau());
		}
		for (Plante p : new ArrayList<Ble>(f.getBles()))
		{
			gererPlanteType(p);
//			System.out.println("qteEau blé : "+p.getNiveauEau());
		}
		f.nettoyerPlantesMortes();

	}
	
	/**
	 * Permet de gérer le comportement des plantes. La méthode gererDeplacement a été rajouté dans la mesure où l'on déciderait de rajouter une 
	 * capacité de déplacement aux plantes. on gère ensuite si les plantes ont soif, faim, si elles peuvent boire ou manger du fertilisant en ajustant 
	 * leur croissance en fonction du résultat.
	 * Enfin, on consomme leur consommation d'eau journalière et on les tue si leur résevoir atteint 0.
	 * @param p (Plante)
	 */
	private void gererPlanteType(Plante p)
	{
		double consoEau = p.getConsoEau();
		double consoFertil = p.getConsoNutriment();
		double capCroissance = p.getCapCroissance();

		if (p.getCapDep() > 0) p.gererDeplacement(f);	// Ne fait normalement rien car capDep vaut 0 pour une plante dans notre cas.
		try
		{
			Case c = f.casePlante(p);	// peut lever EntiteNonTrouvee
			
			if (f.puitAutour(c)) p.remplirResEau();		// Si il y a un puit autour d'elle, la plante va chercher à boire même si elle n'a pas forcément soif afin de remplir son reservoir.
			if (p.aSoif())
			{
//				System.out.println("qte eau sur case : "+c.getNiveauEau());
//				System.out.println("qte fertil sur case : "+c.getNiveauFertil());
				double qte = p.getCapEau() - p.getNiveauEau();
				try 
				{
					p.boitSurCase(c, qte);
				}
				catch (NiveauInsuffisantException e)
				{
					c.viderEau();
					p.ajtQteEau(qte);
					capCroissance *= 1-(p.getSoifDimCroiss()*0.8);		// Si la plante peut boire une certaine quantité mais pas assez pour remplir son reservoir, elle
																		// boit tout de même cette quantité d'eau mais diminue sa croissance de 80% de la diminution de croissance si soif.
				}
				catch (CapaciteDepasseeException e)
				{
					capCroissance *= 1-p.getSoifDimCroiss();
				}
			}
			
			try
			{
				c.recupFertil(consoFertil);
//				System.out.println("consomme fertil");
			}
			catch (CapaciteDepasseeException e)
			{
				capCroissance *= 1-p.getFaimDimCroiss();
			}
			catch (NiveauInsuffisantException e)
			{
				// Non présicé dans le sujet mais on considère que si la case contient du fertilisant mais pas assez pour nourir complètement la plante,
				// on diminue la production de nourriture de 80% de la diminution de nourriture produite si faim
				c.viderFertil();
				capCroissance *= (1 - p.getFaimDimCroiss()*0.8);
			}
			catch(IllegalArgumentException e)
			{
				System.err.println(e.getMessage());
			}
			
			
			if (saisonActuelle == Saison.HIVER)
			{
				// prod de ble baisse de 30% signifie que sa capacité de croissance baisse ? Les tomates ne rentrent pas dans cette fonction.
				capCroissance *= 0.3;				
				
			}
			else if (saisonActuelle == Saison.ETE)
			{
				if (p instanceof Tomate) capCroissance *= 1.2;
				else if (p instanceof Ble)
				{
					consoEau *= 1.3;
					capCroissance *= 1.15;
				}
			}
			
			p.croissance(capCroissance);	// peut lever IllegalArgument
			
			try 
			{
				p.consommerEau(consoEau);
//				System.out.println("qte Eau "+p.getClass()+" : "+p.getNiveauEau());
			}
			catch (NiveauInsuffisantException | CapaciteDepasseeException e)
			{
				f.suppPlante(p);
			}
			
		}
		catch (EntiteNonTrouveeException | IllegalArgumentException e)
		{
			System.err.println(e.getMessage());
		}

	}


	public void deposerFertil(double qte, int x, int y) throws AchatImpossibleException
	{
		joueur.deposerFertil(qte, x, y);
	}
	
	public void start()
	{
		enPause = false;
	}
	
	public void stop()
	{
		enPause = true;
	}

    
    
	  public void acheterPoule(int x, int y) throws AchatImpossibleException, CasePleineException, AjoutImpossibleException
	  {
		  joueur.acheterPoule(x, y);
	  }
	  
	  public void acheterVache(int x, int y) throws AchatImpossibleException, CasePleineException, AjoutImpossibleException
	  {
		  joueur.acheterVache(x, y);
	  }
	  
	  public void acheterTomate(int x, int y) throws AchatImpossibleException, CasePleineException, AjoutImpossibleException
	  {
		  joueur.acheterTomate(x, y);
	  }
	  
	  public void acheterBle(int x, int y) throws AchatImpossibleException, CasePleineException, AjoutImpossibleException
	  {
		  joueur.acheterBle(x, y);
	  }
	  
	  public void acheterPuit(int x, int y) throws AchatImpossibleException, CasePleineException, AjoutImpossibleException
	  {
		  joueur.acheterPuit(x, y);
	  }
	  
	  public void detruirePuit(int x, int y) throws DestructionImpossibleException
	  {
		  joueur.detruirePuit(x, y);
	  }
	  
	  public int getPoulesRestante()
	  {
		  return poulesRestantes;
	  }
	  
	  public int getVachesRestante()
	  {
		  return vachesRestantes;
	  }
	  
	  public Ferme getFerme()
	  {
		  return f;
	  }
	  
	  public Joueur getJoueur()
	  {
		  return joueur;
	  }
	  
	  
	  public Saison getSaison()
	  {
		  return saisonActuelle;
	  }
	  
	  public int getJour()
	  {
		  return jours;
	  }
	  
	  public double getEntrepot()
	  {
		  return entrepot;
	  }
	  
	  public boolean estEnPause()
	  {
		  return enPause;
	  }
	  
	  public boolean enPhasePlacement()
	  {
		  return placementPhase;
	  }


}