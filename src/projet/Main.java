package projet;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 * Creer une fenêtre de jeu, ce qui va nous permettre d'avoir l'affichage et ensuite toute la simulation.
 * @param args
 */
public class Main {
    
    public static void main(String[] args) {
    	SwingUtilities.invokeLater(() -> {
            try 
            {
                new FenetreJeu();
            }
	    	catch (JeuInterrompuException e)
	    	{
	    		 JOptionPane.showMessageDialog(
	    			        null,
	    			        e.getMessage(),
	    			        "Jeu interrompu",
	    			        JOptionPane.WARNING_MESSAGE
	    			    );
	    			    System.exit(0);
	    	}
    	});
        
    }

}
