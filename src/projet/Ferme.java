package projet;
import java.util.ArrayList;
// import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;
// import java.util.Iterator;


/**
 * La classe Ferme permet de modéliser la ferme sous forme de grille.
 * Elle nous permet d'accèder aux cases autour d'une case c de la ferme, ce qui est
 * utile pour gérer le déplacement des animaux ou savoir ce que contiennent les cases
 * autour pour y placer des bébés ou faire boire les plantes par exemple 
 */
public class Ferme 
{
	// tableau 2D de Cases 
	private ArrayList<Poule> poules;
	private ArrayList<Vache> vaches;
	private ArrayList<Tomate> tomates;
	private ArrayList<Ble> bles;
	private ArrayList <Animal> animauxMorts;
	private ArrayList <Plante> plantesASupp;
	private ArrayList <Animal> animauxAPlacer;
	
	private Case[][] grille;

	private Random rand;
	Scanner scan;
	
	/**
	 * Constructeur permettant d'initialiser des ArrayListe pour tous les animaux, plantes et puits de la ferme
	 * Contruit la ferme avec les constantes n et m. 
	 */
	public Ferme()
	{

		poules = new ArrayList<Poule>();	
		vaches = new ArrayList<Vache>();
		tomates = new ArrayList<Tomate>();
		bles = new ArrayList<Ble>();
//		puits = new ArrayList<Puit>();
		animauxMorts = new ArrayList<Animal>();
		plantesASupp = new ArrayList<Plante>();
		animauxAPlacer = new ArrayList<Animal>();
		
		grille = new Case[Constante.n][Constante.m];
		rand = new Random();
		scan = new Scanner(System.in);
		
		for (int i=0; i<Constante.n; i++)
		{
			for (int j=0; j<Constante.m; j++)
			{
				grille[i][j] = new Case(i, j);
			}
		}
	}
	
	
	/**
	 * Permet d'ajouter complètement un animal dans notre ferme, en le rajoutant sur la case (x, y) et en l'ajoutant dans notre liste 
	 * d'animaux présent dans la ferme.
	 * @param a (Animal)
	 * @param x (int)
	 * @param y (int)
	 * @throws IllegalArgumentException
	 * @throws AjoutImpossibleException
	 */
	public void ajtAnimal(Animal a, int x, int y) throws IllegalArgumentException, AjoutImpossibleException
	{
		Case c = casePosition(x, y);
		c.ajtEntite(a);
		if (a instanceof Poule)
		{
			poules.add((Poule)a);
		}
		else if(a instanceof Vache)
		{
			vaches.add((Vache)a);
		}
	}
	
	/**
	 * Permet d'ajouter complètement une plante dans notre ferme, en la rajoutant sur la case (x, y) et en l'ajoutant dans notre liste 
	 * de plantes présentes dans la ferme.
	 * @param p (Plante)
	 * @param x (int)
	 * @param y (int)
	 * @throws IllegalArgumentException
	 * @throws AjoutImpossibleException
	 */
	public void ajtPlante(Plante p, int x, int y) throws IllegalArgumentException, AjoutImpossibleException
	{

		Case c = casePosition(x, y);
		c.ajtEntite(p);		// peut lever AjoutImpossibleException
		if (p instanceof Tomate)
		{
			tomates.add((Tomate) p);
		}
		else if(p instanceof Ble)
		{
			bles.add((Ble) p);
		}
	
	}
	
	/**
	 * Permet d'ajouter complètement un puit dans notre ferme, en le rajoutant sur la case (x, y) et en l'ajoutant dans notre liste 
	 * de puits présent dans la ferme.
	 * @param p (Puit)
	 * @param x (int)
	 * @param y (int)
	 * @throws IllegalArgumentException
	 * @throws AjoutImpossibleException
	 */
	public void ajtPuit(int x, int y) throws IllegalArgumentException, AjoutImpossibleException
	{
		Case c = casePosition(x, y);
		c.ajtPuit();
	}
	
	/**
	 * Permet d'ajouter une quantité (qte) de fertilisant sur une case de notre ferme.
	 * @param qte (double)
	 * @param x (int)
	 * @param y (int)
	 */
	public void ajtFertil(double qte, int x, int y)
	{
		Case c = casePosition(x, y);
		c.ajtFertil(qte);
	}

	/**
	 * Permet d'ajouter un animal à une liste indiquant les animaux à supprimer de notre ferme. Est utile lorsque l'on parcourt notre liste d'animaux
	 * dans une boucle foreach et qu'on ne peut donc pas supprimer un animal de cette liste pendant qu'on la parcourt.
	 * @param a (Animal)
	 */
	public void suppAnimalTemp(Animal a) 
	{
		animauxMorts.add(a);
	}
	
	/**
	 * Permet de supprimer complètement les animaux présent dans notre liste d'animaux à supprimer. On appelle cette méthode à la fin de la boucle foreach.
	 */
	public void nettoyerAnimauxMorts()
	{
		for (Animal a : animauxMorts)
		{
			if (a instanceof Poule) poules.remove(a);

			else if (a instanceof Vache) vaches.remove(a);
			
			try 
			{
				Case c = caseAnimal(a);
				c.suppEntite(a);
			}
			catch (EntiteNonTrouveeException e)
			{
				System.err.println("nettoyage raté car : "+e.getMessage());
			}
		}
		animauxMorts.clear();
	}
	
	/**
	 * Enchaîne successivement suppAnimalTemp et nettoyerAnimauxMorts, permet donc de supprimer complètement un animal de notre ferme.
	 * @param a (Animal)
	 */
	public void suppAnimal(Animal a)
	{
		suppAnimalTemp(a);
		nettoyerAnimauxMorts();
	}

	
	/**
	 * Permet d'ajouter une plante à une liste indiquant les plantes à supprimer de notre ferme. Est utile lorsque l'on parcourt notre liste de plantes
	 * dans une boucle foreach et qu'on ne peut donc pas supprimer une plante de cette liste pendant qu'on la parcourt.
	 * @param p (Plante)
	 */
	public void suppPlantesTemp(Plante p) 
	{
		plantesASupp.add(p);
	}
	
	/**
	 *  * Permet de supprimer complètement les plantes présentes dans notre liste de plantes à supprimer. On appelle cette méthode à la fin de la boucle foreach.
	 */
	public void nettoyerPlantesMortes()
	{
		for (Plante p : plantesASupp)
		{
			if (p instanceof Tomate) tomates.remove(p);

			else if (p instanceof Ble) bles.remove(p);
			
			try 
			{
				Case c = casePlante(p);
				c.suppEntite(p);
			}
			catch (EntiteNonTrouveeException e)
			{}
		}
		plantesASupp.clear();
	}
	
	/**
	 * * Enchaîne successivement suppPlantesTemp et nettoyerPlantesMortes, permet donc de supprimer complètement une plante de notre ferme.
	 * @param p (Plante)
	 */
	public void suppPlante(Plante p)
	{
		suppPlantesTemp(p);
		nettoyerPlantesMortes();
	}
	
	
	/**
	 * Renvoie le puit contenu sur une case à la position (x, y).
	 * 
	 * @param x (int)
	 * @param y (int)
	 * @return (Puit)
	 * @throws IllegalArgumentException
	 */
//	public Puit puitCase(int x, int y) throws IllegalArgumentException
//	{
//		if (! grille[x][y].contientPuit()) throw new IllegalArgumentException("La case demandé ne contient pas de puit.");
//		
//		return grille[x][y].getPuit();
//	}
	
	/**
	 * Supprime complètement un puit de notre ferme.
	 * 
	 * @param p (Puit)
	 */
	public void suppPuit(int x, int y) throws DestructionImpossibleException
	{
		Case c = casePosition(x, y);
		c.suppPuit();
//		puits.remove(p.getId());

		
	}
	
	/**
	 * Renvoie la case situé aux coordonnées (x, y)
	 * 
	 * @param x (int)
	 * @param y (int)
	 * @return (Case)
	 */
	public Case casePosition(int x, int y)
	{
		if (x >=0 && x < Constante.n && y >=0 && y<Constante.m)
		{
			return grille[x][y];
		}
		else
		{
			throw new IllegalArgumentException("Vous essayez d'acceder à une case en dehors de votre ferme.");
		}
	}
	
	/**
	 * Renvoie la case où se situe l'animal a.
	 * 
	 * @param a (Animal)
	 * @return (Case)
	 * @throws EntiteNonTrouveeException
	 */
	public Case caseAnimal(Animal a) throws EntiteNonTrouveeException
	{

	    for (int i=0; i<Constante.n; i++)
	    {
	        for (int j=0; j<Constante.m; j++)
	        {
	            if (grille[i][j].contientAnimal() && grille[i][j].getAnimal().getId() == a.getId())
	            {
	                return grille[i][j];
	            }
	        }
	    }
	    throw new EntiteNonTrouveeException("L'animal demandé n'a pas été trouvé sur la grille");
	}
	
	/**
	 * Renvoie la case où se situé la plante p.
	 * 
	 * @param p (Plante)
	 * @return (Case)
	 * @throws EntiteNonTrouveeException
	 */
	public Case casePlante(Plante p) throws EntiteNonTrouveeException
	{
	
	    for (int i=0; i<Constante.n; i++)
	    {
	        for (int j=0; j<Constante.m; j++)
	        {
	            if (grille[i][j].contientPlante() && grille[i][j].getPlante().getId() == p.getId())
	            {
	                return grille[i][j];
	            }
	        }
	    }
	    throw new EntiteNonTrouveeException("La plante demandée n'a pas été trouvée sur la grille");
	}
	

	/**
	 * Renvoie true s'il y a une poule autour de la case c et false s'il n'y en a pas.
	 * 
	 * @param c (Case)
	 * @return (booleen)
	 */
	public boolean pouleAutour(Case c)
	{
		try 
		{
			Case droite = casePosition(c.getX()+1, c.getY());
			if (droite.contientPoule())
				return true;
			
		} catch(IllegalArgumentException e) {}
		
		try
		{
			Case haut = casePosition(c.getX(), c.getY()+1);
			if (haut.contientPoule())
				return true;
		} catch(IllegalArgumentException e) {}
		
		try
		{
			Case gauche = casePosition(c.getX()-1, c.getY());
			if (gauche.contientPoule())
				return true;
		} catch(IllegalArgumentException e) {}
		
		try
		{
			Case bas = casePosition(c.getX(), c.getY()-1);
			if (bas.contientPoule())
				return true;
		} catch(IllegalArgumentException e) {}
		
		return false;
	}
	
	/**
	 * Renvoie true s'il y a une vache autour de la case c et false s'il n'y en a pas.
	 * 
	 * @param c (Case)
	 * @return (booleen)
	 */
	public boolean vacheAutour(Case c)
	{
		try 
		{
			Case droite = casePosition(c.getX()+1, c.getY());
			if (droite.contientVache())
				return true;
			
		} catch(IllegalArgumentException e) {}
		
		try
		{
			Case haut = casePosition(c.getX(), c.getY()+1);
			if (haut.contientVache())
				return true;
		} catch(IllegalArgumentException e) {}
		
		try
		{
			Case gauche = casePosition(c.getX()-1, c.getY());
			if (gauche.contientVache())
				return true;
		} catch(IllegalArgumentException e) {}
		
		try
		{
			Case bas = casePosition(c.getX(), c.getY()-1);
			if (bas.contientVache())
				return true;
		} catch(IllegalArgumentException e) {}
		
		return false;
	}
	
	/**
	 * Renvoie la case libre pour recevoir un animal (pas d'animal sur la case) autour de la case c s'il y en a une et lance une exception AucuneCaseLibreAutourException s'il n'y en a pas.
	 * Cette méthode est utilisée pour savoir si on peut placer un bébé animal  sur cette
	 * 
	 * @param c (Case)
	 * @return (Case)
	 * @throws AucuneCaseLibreAutourException
	 */
	public Case caseLibreAutour(Case c) throws AucuneCaseLibreAutourException
	{
		// Si une case est au bord, alors une des cases autour ne sera pas trouvée mais on continue tout de même de vériier les autres cases 
		try 
		{
			Case droite = casePosition(c.getX()+1, c.getY());
			if (! droite.contientAnimal())
				return droite;
			
		} catch(IllegalArgumentException e) {}
		
		try
		{
			Case haut = casePosition(c.getX(), c.getY()+1);
			if (! haut.contientAnimal())
				return haut;
		} catch(IllegalArgumentException e) {}
		
		try
		{
			Case gauche = casePosition(c.getX()-1, c.getY());
			if (! gauche.contientAnimal())
				return gauche;
		} catch(IllegalArgumentException e) {}
		
		try
		{
			Case bas = casePosition(c.getX(), c.getY()-1);
			if (! bas.contientAnimal())
				return bas;
		} catch(IllegalArgumentException e) {}
		
		throw new AucuneCaseLibreAutourException("Il n'y a pas de cases libre autour.");
		
	}
	
	/**
	 * Déplace l'animal a d'une position sur la ferme où reste à sa place (aléatoirement). 
	 * 
	 * @param a (Animal)
	 * @throws EntiteNonTrouveeException
	 * @throws AjoutImpossibleException
	 */
	public void deplacementAnimal(Animal a)	throws EntiteNonTrouveeException, AjoutImpossibleException
	{
		Case cInit = caseAnimal(a);
		int x = cInit.getX(), y = cInit.getY();
		ArrayList<Case> casesLibres = new ArrayList<Case>();
		
		casesLibres.add(cInit);
		try 
		{
			Case droite = casePosition(x, y+1);	//car sur la grille, les positions x et y sont inversées.
			if (! droite.contientAnimal()) casesLibres.add(droite);
		} catch(IllegalArgumentException e) {}	// si on est au bord on ajoute pas la case à côté dans la liste de cases libres 
		
		try 
		{
			Case haut = casePosition(x-1, y);
			if (! haut.contientAnimal()) casesLibres.add(haut);
		} catch(IllegalArgumentException e) {}
		
		try 
		{
			Case gauche = casePosition(x, y-1);
			if (! gauche.contientAnimal()) casesLibres.add(gauche);
		} catch(IllegalArgumentException e) {}
		
		
		try 
		{
			Case bas = casePosition(x+1, y);
			if (! bas.contientAnimal()) casesLibres.add(bas);
		} catch(IllegalArgumentException e) {}	
	
		int rnd = rand.nextInt(casesLibres.size());	
		
		Case nvCase = casesLibres.get(rnd);
		
		if (nvCase.equals(cInit)) return;
		
		allerCase(a, nvCase);

	}
	
	/**
	 * Déplace la plante p d'une position sur la grille où reste sur place aléatoirement (si les plantes pouvaient se déplacer).
	 * @param p (Plante)
	 * @throws EntiteNonTrouveeException
	 * @throws AjoutImpossibleException
	 */
	public void deplacementPlante(Plante p)	throws EntiteNonTrouveeException, AjoutImpossibleException
	{
		Case cInit = casePlante(p);
		int x = cInit.getX(), y = cInit.getY();
		ArrayList<Case> casesLibres = new ArrayList<Case>();
		
		casesLibres.add(cInit);
		try 
		{
			Case droite = casePosition(x+1, y);
			if (! (droite.contientPlante() || droite.contientPuit())) casesLibres.add(droite);
		} catch(IllegalArgumentException e) {}	// si on est au bord on ajoute pas la case à côté dans la liste de cases libres 
		
		try 
		{
			Case haut = casePosition(x, y+1);
			if (! (haut.contientPlante() || haut.contientPuit())) casesLibres.add(haut);
		} catch(IllegalArgumentException e) {}
		
		try 
		{
			Case gauche = casePosition(x-1, y);
			if (! (gauche.contientPlante() || gauche.contientPuit())) casesLibres.add(gauche);
		} catch(IllegalArgumentException e) {}
		
		
		try 
		{
			Case bas = casePosition(x, y-1);
			if (! (bas.contientPlante() || bas.contientPuit())) casesLibres.add(bas);
		} catch(IllegalArgumentException e) {}	
	
		int rnd = rand.nextInt(casesLibres.size());	
		
		Case nvCase = casesLibres.get(rnd);
		
		if (nvCase.equals(cInit)) return;
		
		allerCase(p, nvCase);
	
	}
	
	/**
	 * 	Effectue le déplacement de l'entité e sur la nouvelle case en supprimant l'entité de sa case actuelle puis en le rajoutant sur la nouvelle case.
	 * L'entité n'est supprimée que de la case avec suppEntite mais pas de l'arrayList de la ferme.
	 * 
	 * @param e (Entite)
	 * @param c (Case)
	 * @throws EntiteNonTrouveeException
	 * @throws AjoutImpossibleException
	 */
	private void allerCase(Entite e, Case c) throws EntiteNonTrouveeException, AjoutImpossibleException	
	{
		
		if (e instanceof Animal)
		{
			Case cInit = caseAnimal((Animal) e);
			cInit.suppEntite((Animal) e);
			c.ajtEntite((Animal) e);
		}
		else if (e instanceof Plante)
		{
			Case cInit = casePlante((Plante) e);
			cInit.suppEntite((Plante) e);
			c.ajtEntite((Plante) e);
		}
	}
	
	/**
	 * Renvoie true s'il y a un puit autour de la case c et false sinon.
	 * @param c (Case)
	 * @return (booleen)
	 */
	public boolean puitAutour(Case c)
	{
		int x = c.getX(), y = c.getY();
		
		try 
		{
			Case droite = casePosition(x+1, y);
			if (droite.contientPuit()) return true;
		} catch(IllegalArgumentException e) {}	// si on est au bord on ajoute pas la case à côté dans la liste de cases libres 
		
		try 
		{
			Case haut = casePosition(x, y+1);
			if (haut.contientPuit()) return true;
		} catch(IllegalArgumentException e) {}
		
		try 
		{
			Case gauche = casePosition(x-1, y);
			if (gauche.contientPuit()) return true;
		} catch(IllegalArgumentException e) {}
		
		
		try 
		{
			Case bas = casePosition(x, y-1);
			if (bas.contientPuit()) return true;
		} catch(IllegalArgumentException e) {}	
		
		return false;
	}
	
	/**
	 * Implémente la tentative de manger une plante d'un animal. Vérifie si la plante n'est pas gelée, si une plante est sur la même case que l'animal,
	 * si la plante a assez de nourriture pour nourir l'animal. Si l'animal peut manger la plante, on renvoit true (même s'il n'y en a pas assez pour
	 * remplir son réservoir) et false sinon
	 * @param a (Animal)
	 * @param p (Plante)
	 * @return (booleen)
	 */
	public boolean animalTryManger(Animal a, Plante p)
	{
		if (p==null) return false; 	// cas ou il n'y a pas de plante sur la même case que l'animal.
		if (p.getEstGelee()) return false;
		
		try
		{
			if (! caseAnimal(a).contientPlante()) return false;
		}
		catch (EntiteNonTrouveeException e)
		{
			return false;
		}
		
		
		double qte = a.getCapNour() - a.getNiveauNour();
		
		try
		{
			double peutManger = planteEstMangee(p, qte);
			a.mangeQte(peutManger);

			return true;
		}
		catch (IllegalArgumentException e)
		{
			System.err.println("Vous essayez de manger une qte de plante négative !");
			return false;
		}
		
	}
	
	/**
	 * Renvoie la quantité effective de nourriture qu'un animal peut manger sur la plante p.
	 * 
	 * @param p (Plante)
	 * @param qte (double)
	 * @return (double)
	 * @throws IllegalArgumentException
	 */
	public double planteEstMangee(Plante p, double qte) throws IllegalArgumentException
	{
		if (qte < 0) throw new IllegalArgumentException();
		if (qte >= p.getQteNour())
		{
			suppPlantesTemp(p);
			return p.getQteNour();
		}
		p.retirerQteNour(qte);
		return qte;
	}
	
	
	/**
	 * Permettait d'afficher la grille avant qu'il n'y ait d'interface grapique
	 */
//	public void afficherGrille()
//	{
//		for (int i=0; i<Constante.n; i++)
//		{
//			for (int j=0; j<Constante.m; j++)
//			{
//				if (grille[i][j].contientAnimal() && grille[i][j].contientPlante())	// Si la case contient un animal et une plante
//				{
//					if (grille[i][j].getAnimal() instanceof Poule)
//					{
//						if (grille[i][j].getPlante() instanceof Tomate)
//							System.out.print("P+T ");
//						
//						else if (grille[i][j].getPlante() instanceof Ble)
//							System.out.print("P+B ");
//						
//					}
//					else if (grille[i][j].getAnimal() instanceof Vache)
//					{
//						if (grille[i][j].getPlante() instanceof Tomate)
//							System.out.print("V+T ");
//						
//						else if (grille[i][j].getPlante() instanceof Ble)
//							System.out.print("V+B ");
//					}
//				}
//				else if (grille[i][j].contientAnimal() && grille[i][j].contientPuit()) // Si la case contient un animal et un puit
//				{
//					if (grille[i][j].getAnimal() instanceof Poule)
//						System.out.print("P+E ");
//
//					else if (grille[i][j].getAnimal() instanceof Vache)
//						System.out.print("V+E ");
//					
//				}
//					
//				else if (grille[i][j].contientPoule()) // Si la case ne contient qu'une poule
//				{
//					if (grille[i][j].getAnimal().getEstBebe())
//						System.out.print("(P) ");
//					else
//						System.out.print("P ");
//				}
//					
//				else if (grille[i][j].contientVache()) // Si la case ne contient qu'une vache
//				{
//					if (grille[i][j].getAnimal().getEstBebe())
//						System.out.print("(V) ");
//					else 
//						System.out.print("V ");
//				}
//					
//				else if (grille[i][j].contientPlante()) // Si la case ne contient qu'une plante
//				{
//					if (grille[i][j].getPlante() instanceof Tomate)
//						System.out.print("T ");
//					else 
//						System.out.print("B ");
//
//				}
//				else if (grille[i][j].contientPuit()) 
//					System.out.print("E ");	// E pour eau et ne pas confondre avec une poule.
//					
//				
//				else System.out.print("X ");	// Si la case ne contient rien, on affiche une croix
//			}
//			System.out.print("\n");	// On passe à l'affichage de la ligne suivante
//		}
//		System.out.println("\n");	// L'affichage de la grille est fini et on saute 2 lignes.
//	}
	
	
	/**
	 * Permettait de placer les animaux sur la grille avant qu'il ny ait d'interface graphique
	 * 
	 */
//	public void placerAnimaux()
//	{
//		if (poules.isEmpty() && vaches.isEmpty()) return;
//		
//		Iterator<Poule> pouleIter = poules.iterator();
//		int i=1;
//		while (pouleIter.hasNext())
//		{
//			Poule p = pouleIter.next();
//			boolean placementReussi = false;
//			while (! placementReussi)
//			{
//				System.out.println("\n\nChoisissez où vous souhaitez placer votre "+i+"e poule restante de la dernère saison : ");
//				int x = choixPosX();
//				int y = choixPosY();
//				try 
//				{
//					Case c = casePosition(y, x);
//					c.ajtEntite(p);
//					placementReussi = true;
//					i++;
//				}
//				catch (AjoutImpossibleException | IllegalArgumentException e)
//				
//				{
//					System.err.println("Placement de votre poule impossible car : "+e.getMessage());
//					scan.nextLine();
//				}
//			}
//		}
//		
//		Iterator<Vache> vacheIter = vaches.iterator();
//		i=1;
//		while (vacheIter.hasNext())
//		{
//			Vache v = vacheIter.next();
//			boolean placementReussi = false;
//			while (! placementReussi)
//			{
//				System.out.println("\n\nChoisissez où vous souhaitez placer votre "+i+"e vache restante de la dernère saison : ");
//				int x = choixPosX();
//				int y = choixPosY();
//				try 
//				{
//					Case c = casePosition(y, x);
//					c.ajtEntite(v);
//					placementReussi = true;
//					i++;
//				}
//				catch (AjoutImpossibleException | IllegalArgumentException e)
//				
//				{
//					System.err.println("Placement de votre vache impossible car : "+e.getMessage());
//					scan.nextLine();
//				}
//			}
//			
//		}
//		
//	}

	// Permettaient de demander à l'utilisateur des coordonnées pour entrer les animaux avant l'interface graphique
	
//	private int choixPosX()
//	{
//		while (true)
//		{
//			try 
//			{
//				System.out.println("Choix de la position horizontal (entre 0 et "+(Constante.n-1)+") : ");
//				int x = scan.nextInt();
//				return x;
//			}
//			catch(InputMismatchException e)
//			{
//				System.err.println("Vous devez entrez un entier entre 0 et "+Constante.n);
//			}
//			scan.nextLine();
//		}
//		
//	}
//	
//	private int choixPosY()
//	{
//		while (true)
//		{
//			try 
//			{
//				System.out.println("Choix de la position vertical (entre 0 et "+(Constante.n-1)+") : ");
//				int y = scan.nextInt();
//				return y;
//			}
//			catch(InputMismatchException e)
//			{
//				System.err.println("Vous devez entrez un entier entre 0 et "+(Constante.m-1)+".");
//				scan.nextLine();
//			}
//		}
//		
//	}
	
	/**
	 * Permet de gérer la tentative de reproduction des animaux, s'ils sont à cotés et selon une certaine probabilité
	 * @param a
	 * @throws ReproImpossibleException
	 */
	public void tryReproAnimal(Animal a) throws ReproImpossibleException
	{
		if (a.getABebe() || a.getEstBebe()) return;
		
		try
		{
			Case c = this.caseAnimal(a); // peut lever EntiteNonTrouvee
			
			Case c1 = this.caseLibreAutour(c); // peut lever AucuneCaseLibreAutour

			if (a instanceof Poule)
			{
				if (this.pouleAutour(c))
				{
			//		System.out.println("Tente de se reproduire");
					double probaRepro = ((Poule) a).getCapRepro();
					double rnd = rand.nextDouble();
					if (probaRepro > rnd)
					{
						Poule pouleActuelle = (Poule)a;
						Animal bebeA = pouleActuelle.creerBebe();
						Poule bebeP = (Poule)bebeA;
						
						//this.ajtAnimal(bebeP, c1.getX(), c1.getY());
						c1.ajtEntite(bebeP);
						animauxAPlacer.add(bebeP);
//				    	 System.out.println("Normalement bébé poule crée en : "+c1.getY()+", "+c1.getX());
//				    	 System.out.println("grille aprés ajout entité");
//				 		afficherGrille();
					}
				}
			}
			else if (a instanceof Vache)
			{
				if (this.vacheAutour(c))
				{
					double probaRepro = ((Vache) a).getCapRepro();
					double rnd = rand.nextDouble();
					if (probaRepro > rnd)
					{
						Vache vacheActuelle = (Vache)a;
						Animal bebeA = vacheActuelle.creerBebe();
						Vache bebeV = (Vache)bebeA;
						
					//	this.ajtAnimal(bebeV, c1.getX(), c1.getY());
						c1.ajtEntite(bebeV);
						animauxAPlacer.add(bebeV);
//						System.out.println("Normalement bébé vache crée en : "+c1.getY()+", "+c1.getX());
//				    	 System.out.println("grille aprés ajout entité");
//				 		afficherGrille();
						
					}
				}
			}
		}
		catch (EntiteNonTrouveeException | AucuneCaseLibreAutourException | AjoutImpossibleException e)
		{
//			throw new ReproImpossibleException("Reproduction impossible car : "+e.getMessage());	// pas besoin d'afficher des messages d'erreurs sur la 																						// console si la reproduction échoue.

		}
	}
	
	
	/**
	 * Permet d'ajouter sur la grille les animaux présent dans la liste des bébés animaux.
	 */
	public void ajtNvNes()
	{
		
		for (Animal a : animauxAPlacer)
		{
			if (a instanceof Poule)
			{
				poules.add((Poule)a);
			}
			else if(a instanceof Vache)
			{
				vaches.add((Vache)a);
			}
		}
		animauxAPlacer.clear();
	}
	
	
	


	public ArrayList<Poule> getPoules()
	{
		return poules;
	}
	
	public ArrayList<Vache> getVaches()
	{
		return vaches;
	}
	
	public ArrayList<Ble> getBles()
	{
		return bles;
	}
	
	public ArrayList<Tomate> getTomates()
	{
		return tomates;
	}

	
	public int getNbPoules()
	{
		return poules.size();
	}
	
	public int getNbVaches()
	{
		return vaches.size();
	}
	
	public int getNbTomates()
	{
		return tomates.size();
	}
	
	public int getNbBle()
	{
		return bles.size();
	}
}