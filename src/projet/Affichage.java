package projet;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.EnumMap;
import javax.imageio.ImageIO;

/**
 * La classe Affichage permet de dessiner la grille de la ferme dans le jeu. Elle gère l'affichage des entités (animaux,
 * plantes, puits, fertilisant, pluie) ainsi que l'arrière plan qui change en fonction des saisons.
 *
 * elle interagit avec la simulation en cours pour connaître l'état de chaque case
 * et utilise des images chargées en mémoire pour représenter les éléments graphiquement.
 *
 * Cette classe permet également de dessiner les cases, animaux et tout les éléments que contient la grille.
 */
public class Affichage extends JPanel {
	
	/**
	 * Permet de recevoir les coordonnées d’une case cliquée dans la grille.
	 */
    public interface ClicListener { void onCaseCliquee(int x, int y); }

    private ClicListener listener;	// clics sur la grille
    private Simulation sim;
    private int tailleCase = 80;	// taille d'une case
    private BufferedImage imgPoule, imgVache, imgBle, imgTomate, imgPuit, imgFertil1, imgFertil2, imgPluie1, imgPluie2, imgBebePoule, imgBebeVache, imgTomateGelee;
    private EnumMap<Simulation.Saison, BufferedImage> fonds = new EnumMap<>(Simulation.Saison.class);

    
    /**
     * Dessine la ferme, ses entités et les fonds d'écrans en fonction des saisons. Charge les images depuis les ressources du projet.
     * @param sim (Simulation)
     */
    public Affichage(Simulation sim) {
        this.sim = sim;
        setOpaque(false); // transparent pour voir le fond global
        setPreferredSize(new Dimension(Constante.n * tailleCase,
                                       Constante.m * tailleCase));
        try {
            fonds.put(Simulation.Saison.HIVER,     ImageIO.read(getClass().getResourceAsStream("/projet/images/hiver.jpg")));
            fonds.put(Simulation.Saison.PRINTEMPS, ImageIO.read(getClass().getResourceAsStream("/projet/images/printemps.jpg")));
            fonds.put(Simulation.Saison.ETE,       ImageIO.read(getClass().getResourceAsStream("/projet/images/ete.jpg")));
            fonds.put(Simulation.Saison.AUTOMNE,   ImageIO.read(getClass().getResourceAsStream("/projet/images/automne.jpg")));
            imgPoule  = ImageIO.read(getClass().getResourceAsStream("/projet/images/poule.jpg"));
            imgVache  = ImageIO.read(getClass().getResourceAsStream("/projet/images/vache.jpg"));
            imgBle    = ImageIO.read(getClass().getResourceAsStream("/projet/images/ble.jpg"));
            imgTomate = ImageIO.read(getClass().getResourceAsStream("/projet/images/tomate.jpg"));
            imgPuit   = ImageIO.read(getClass().getResourceAsStream("/projet/images/puit.jpg"));
            imgFertil1 = ImageIO.read(getClass().getResourceAsStream("/projet/images/fertilisant1.jpg"));
            imgFertil2 = ImageIO.read(getClass().getResourceAsStream("/projet/images/fertilisant2.jpg"));
            imgPluie1 = ImageIO.read(getClass().getResourceAsStream("/projet/images/pluie1.jpg"));
            imgPluie2 = ImageIO.read(getClass().getResourceAsStream("/projet/images/pluie2.jpg"));
            imgBebePoule = ImageIO.read(getClass().getResourceAsStream("/projet/images/bebePoule.jpg"));
            imgBebeVache = ImageIO.read(getClass().getResourceAsStream("/projet/images/bebeVache.jpg"));
            imgTomateGelee = ImageIO.read(getClass().getResourceAsStream("/projet/images/tomateGelee.jpg"));
            
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                "Impossible de charger les images :\n" + e.getMessage(),
                "Erreur", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int x = e.getX() / tailleCase;
                int y = e.getY() / tailleCase;
                if (listener != null) listener.onCaseCliquee(x, y);
            }
        });
    }

    
    public void setClicListener(ClicListener l) {
    	this.listener = l;
    	}

    @Override
    
    /**
     * Dessine l’ensemble de la grille de la ferme avec toutes les entités (animaux, plantes, puits...),
     * en prenant en compte les superpositions possibles (par exemple animal + plante).
     */
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int half = tailleCase / 2;
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.6f)); // grille semi-transparente
        for (int i = 0; i < Constante.n; i++) {
            for (int j = 0; j < Constante.m; j++) {
                int px = i * tailleCase, py = j * tailleCase;
                g2.setColor(new Color(230, 230, 230));
                g2.fillRect(px, py, tailleCase, tailleCase);
                g2.setColor(Color.GRAY);
                g2.drawRect(px, py, tailleCase, tailleCase);
            }
        }
        g2.dispose();

        Ferme f = sim.getFerme();
        // dessine entités en opaque
        for (int i = 0; i < Constante.n; i++) {
            for (int j = 0; j < Constante.m; j++) {
                int px = i * tailleCase, py = j * tailleCase;
                Case c = f.casePosition(i, j);
                boolean hasAnimal = c.contientAnimal();
                boolean hasPlante = c.contientPlante();
                boolean hasPuit = c.contientPuit();
                
                if (c.contientFertil1()) {
                    // on centre l'image de fertilisant
                    int size = tailleCase / 4-1;  
                    int x = px + (tailleCase - size)/tailleCase;
                    int y = py + (tailleCase - size);
                    g.drawImage(imgFertil1, x, y, size, size, this);
                  }
                
                if (c.contientFertil2()) {
                    // on centre l'image de fertilisant
                    int size = tailleCase / 4+1;  
                    int x = px + (tailleCase - size)/tailleCase;
                    int y = py + (tailleCase - size);
                    g.drawImage(imgFertil2, x, y, size, size, this);
                }
                
                if (c.contientEau1())
                {
                	int size = tailleCase / 4-1;  
                    int x = px + (tailleCase - size);
                    int y = py + (tailleCase - size);
                    g.drawImage(imgPluie1, x, y, size, size, this);
                }
                
                if (c.contientEau2())
                {
                	int size = tailleCase / 4+1;  
                    int x = px + (tailleCase - size);
                    int y = py + (tailleCase - size);
                    g.drawImage(imgPluie2, x, y, size, size, this);
                }

                if (hasAnimal && hasPlante) {
                    Animal a = c.getAnimal();
                    BufferedImage imgA;
                    if (a instanceof Poule) {
                        imgA = a.getEstBebe() ? imgBebePoule : imgPoule;
                    } else {
                        imgA = a.getEstBebe() ? imgBebeVache : imgVache;
                    }
                    g.drawImage(imgA, px, py, half, tailleCase, this);

                    BufferedImage imgP;
                    Plante p = c.getPlante();
                    if (p instanceof Tomate) {
                        imgP = p.getEstGelee() ? imgTomateGelee : imgTomate;
                    } else {
                        imgP = imgBle;
                    }
                    g.drawImage(imgP, px + half, py, half, tailleCase, this);
                }
                else if (hasAnimal && hasPuit) {
                    Animal a = c.getAnimal();
                    BufferedImage imgA;
                    if (a instanceof Poule) {
                        imgA = a.getEstBebe() ? imgBebePoule : imgPoule;
                    } else {
                        imgA = a.getEstBebe() ? imgBebeVache : imgVache;
                    }
                    g.drawImage(imgA, px, py, tailleCase, half, this);
                    g.drawImage(imgPuit, px + 17, py + half, tailleCase - 7, half, this);
                }
                else {
                	if (hasAnimal) {
                	    Animal a = c.getAnimal();
                	    BufferedImage imgA;
                	    if (a instanceof Poule) {
                	        imgA = a.getEstBebe() ? imgBebePoule : imgPoule;
                	    } else {
                	        imgA = a.getEstBebe() ? imgBebeVache : imgVache;
                	    }
                	    g.drawImage(imgA, px, py, tailleCase, tailleCase, this);
                	}

                    if (hasPlante) {
                    	BufferedImage imgP;
                        Plante p = c.getPlante();
                        if (p instanceof Tomate) {
                            imgP = p.getEstGelee() ? imgTomateGelee : imgTomate;
                        } else {
                            imgP = imgBle;
                        }
                        g.drawImage(imgP, px , py, tailleCase, tailleCase, this);
                    }
                    if (hasPuit) {
                        g.drawImage(imgPuit, px+17, py, tailleCase-7, tailleCase-7, this); // +17 et -7 afin de ne pas cacher le fertilisant 
                    }
                }
            }
        }
    }

    
    public BufferedImage getFondCourant() {
        return fonds.get(sim.getSaison());
    }
}
