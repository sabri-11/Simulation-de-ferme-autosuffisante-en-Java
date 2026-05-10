	package projet;
	
	/**
	 * La classe Blé, qui hérite de plante permet de récupérer toutes les constantes du blé de la classe Constante et de les rentrer dans la 
	 * classe Animal ou Entité en appelant son constructeur.
	 */
	public class Ble extends Plante 
	{
		
		private static final boolean estGelee = false;
		
		public Ble()
		{
			super(Constante.prixB, Constante.consoEauB, Constante.capEauB, Constante.qteNourB, Constante.consoFertilB, Constante.capCroissanceB,
					Constante.faimDimCroissB, Constante.soifDimCroissB, Constante.capDepB, estGelee);
		}
		
	}
