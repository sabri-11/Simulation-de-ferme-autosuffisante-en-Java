// FenetreJeu.java
package projet;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;


/**
 * La classe FenetreJeu construit et pilote l’interface graphique principale du jeu en gérant les transitions entre les saisons
 * et les options d'achats dans la boutique.
 */


public class FenetreJeu {
    private enum Phase { SAISON, PLACEMENT }
    private Phase phase = Phase.SAISON;

    private Simulation sim;
    private Affichage affichage;
    private Timer timer;
    private JPanel root;


    // UI
    private JLabel lblArgent, lblJour, lblEntrepot, lblRestant;
    private JButton bStartPause;
    private JButton bAchatPoule, bAchatVache, bAchatBle, bAchatTomate, bAchatPuit, bDetruirePuit, bPlacePoule, bPlaceVache, bDeposerFertil;
    private ModeAchat modeAchat = ModeAchat.NONE;
    DecimalFormat df;

    private enum ModeAchat {
        NONE,
        POULE, VACHE, BLE, TOMATE, PUIT, DPUIT, DFERTIL,
        PLACE_POULE,   
        PLACE_VACHE    
    }

    	/**
    	 * Constructeur, affiche un écran noir pour demander de rentrer le nom du joueur et l'afficher ensuite dans le titre.
    	 * @throws JeuInterrompuException
    	 */
    public FenetreJeu() throws JeuInterrompuException
    {
    	df = new DecimalFormat("0.00");
        sim = new Simulation("Sans nom", Constante.argentDepart);
        if (Constante.m <= 6)
        {
        	JFrame splash = new JFrame();
            splash.setUndecorated(true);
            splash.getContentPane().setBackground(Color.BLACK);
            // si vous voulez plein écran, sinon choisissez une Dimension
            splash.setSize(800, 600);
            splash.setLocationRelativeTo(null);
            splash.setVisible(true);
            String nom = JOptionPane.showInputDialog(
    	            null,
    	            "Entrez votre nom :",
    	            "Bienvenue dans votre Ferme",
    	            JOptionPane.QUESTION_MESSAGE
    	        );
            if (nom != null && !nom.trim().isEmpty()) {
                sim.getJoueur().setNom(nom.trim());
            }
            if (nom == null)
        	{
            	splash.dispose();
        		throw new JeuInterrompuException("Partie annulée");
        	}
            splash.dispose();
        }
        
        JOptionPane.showMessageDialog(
	            null,
	            "Mettez-vous en plein écran pour un bon affichage !",
	            "Astuce",
	            JOptionPane.INFORMATION_MESSAGE
	        );
	    
        buildUI();
    }
    
    /**
     * Permet de construire tous les composants Swing de la fenêtre principal (magasin, grille, titre)
     */
    private void buildUI() {
        JFrame frame = new JFrame("Ferme Autosuffisante");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setPreferredSize(new Dimension(1000, 700));

        // ========================================================================
        // Conteneur principal pour centrer les deux panneaux
        // ========================================================================
        JPanel mainContainer = new JPanel(new GridBagLayout());
        mainContainer.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainContainer.setOpaque(false);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.anchor = GridBagConstraints.CENTER;

        // ========================================================================
        // Panneau horizontal pour grille + magasin
        // ========================================================================
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.X_AXIS));
        contentPanel .setOpaque(false);
        
        // ============================ Partie Grille =============================
        JPanel gridWrapper = new JPanel(new GridBagLayout());
        gridWrapper.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 30));
        gridWrapper   .setOpaque(false);
        
        affichage = new Affichage(sim);
        affichage.setClicListener((x, y) -> handleGridClick(x, y, frame));
        affichage.setAlignmentX(Component.CENTER_ALIGNMENT);
        gridWrapper.add(affichage);
        
        contentPanel.add(gridWrapper);

        // ============================ Partie Magasin ============================
        JPanel shopPanel = new JPanel();
        shopPanel.setLayout(new BoxLayout(shopPanel, BoxLayout.Y_AXIS));
        shopPanel.setBorder(BorderFactory.createTitledBorder("Contrôles"));

        shopPanel.setBackground(new Color(255, 255, 255, 140)); // Afficahge de fond pour le shop.

        
        // Labels d'information
        DecimalFormat df = new DecimalFormat("#.###");
        lblArgent = new JLabel("Argent : " + df.format(sim.getJoueur().getArgent()));
        lblJour = new JLabel("Saison : Chargement...");
        lblEntrepot = new JLabel("Entrepôt : 0 cal");
        lblRestant = new JLabel("");
        
        // Style des labels
        Font labelFont = new Font("Arial", Font.PLAIN, 14);
        lblArgent.setFont(labelFont);
        lblJour.setFont(labelFont);
        lblEntrepot.setFont(labelFont);
        lblRestant.setFont(labelFont);

        // Ajout des labels
        shopPanel.add(Box.createVerticalStrut(10));
        shopPanel.add(lblArgent);
        shopPanel.add(Box.createVerticalStrut(5));
        shopPanel.add(lblJour);
        shopPanel.add(Box.createVerticalStrut(5));
        shopPanel.add(lblEntrepot);
        shopPanel.add(Box.createVerticalStrut(5));
        shopPanel.add(lblRestant);
        shopPanel.add(Box.createVerticalStrut(20));

        // Bouton de contrôle principal
        bStartPause = new JButton("Démarrer");
        bStartPause.setAlignmentX(Component.CENTER_ALIGNMENT);
        bStartPause.setPreferredSize(new Dimension(150, 30));
        bStartPause.addActionListener(e -> startSeason());
        
        // ========================== Boutons d'achat =============================
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(new JSeparator(SwingConstants.HORIZONTAL));
        buttonPanel.add(Box.createVerticalStrut(10));
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBorder(BorderFactory.createTitledBorder("Achats"));
        buttonPanel   .setOpaque(false);
        
        // Initialisation des boutons
        bAchatPoule = new JButton("Acheter poule (" + Constante.prixP + "Y)");
        bAchatPoule.setAlignmentX(Component.CENTER_ALIGNMENT);
        bAchatPoule.addActionListener(e -> modeAchat = ModeAchat.POULE);
        
        bAchatVache = new JButton("Acheter vache (" + Constante.prixV + "Y)");
        bAchatVache.setAlignmentX(Component.CENTER_ALIGNMENT);
        bAchatVache.addActionListener(e -> modeAchat = ModeAchat.VACHE);
        
        bAchatBle = new JButton("Acheter blé (" + Constante.prixB + "Y)");
        bAchatBle.setAlignmentX(Component.CENTER_ALIGNMENT);
        bAchatBle.addActionListener(e -> modeAchat = ModeAchat.BLE);
        
        bAchatTomate = new JButton("Acheter tomate (" + Constante.prixT + "Y)");
        bAchatTomate.setAlignmentX(Component.CENTER_ALIGNMENT);
        bAchatTomate.addActionListener(e -> modeAchat = ModeAchat.TOMATE);
        
        bAchatPuit = new JButton("Acheter puit (" + Constante.prixCons + "Y)");
        bAchatPuit.setAlignmentX(Component.CENTER_ALIGNMENT);
        bAchatPuit.addActionListener(e -> modeAchat = ModeAchat.PUIT);
        
        bDetruirePuit = new JButton("Détruire puit (" + Constante.prixDes + "Y)");
        bDetruirePuit.setAlignmentX(Component.CENTER_ALIGNMENT);
        bDetruirePuit.addActionListener(e -> modeAchat = ModeAchat.DPUIT);
        
        bPlacePoule = new JButton("Placer poule");
        bPlacePoule.setAlignmentX(Component.CENTER_ALIGNMENT);
        bPlacePoule.addActionListener(e -> {
            if (phase == Phase.PLACEMENT) {
                modeAchat = ModeAchat.PLACE_POULE;
            }
        });
        
        bPlaceVache = new JButton("Placer vache");
        bPlaceVache.setAlignmentX(Component.CENTER_ALIGNMENT);
        bPlaceVache.addActionListener(e -> {
            if (phase == Phase.PLACEMENT) {
                modeAchat = ModeAchat.PLACE_VACHE;
            }
        });
        bPlaceVache.setEnabled(false);
        bPlacePoule.setEnabled(false);

        bDeposerFertil = new JButton("Déposer fertilisant ("+Constante.prixFertilKG+"Y/kg)");
        bDeposerFertil.setAlignmentX(Component.CENTER_ALIGNMENT);
        bDeposerFertil.addActionListener(e -> modeAchat = ModeAchat.DFERTIL);
        
        
        
        buttonPanel.add(Box.createVerticalStrut(5));
        
        // Ajout des boutons au panneau
        buttonPanel.add(bAchatPoule);
        buttonPanel.add(Box.createVerticalStrut(5));
        buttonPanel.add(bAchatVache);
        buttonPanel.add(Box.createVerticalStrut(5));
        buttonPanel.add(bAchatBle);
        buttonPanel.add(Box.createVerticalStrut(5));
        buttonPanel.add(bAchatTomate);
        buttonPanel.add(Box.createVerticalStrut(5));
        buttonPanel.add(bAchatPuit);
        buttonPanel.add(Box.createVerticalStrut(5));
        buttonPanel.add(bDetruirePuit);
        

        buttonPanel.add(Box.createVerticalStrut(5));
        buttonPanel.add(bDeposerFertil);
        buttonPanel.add(Box.createVerticalStrut(10));
        buttonPanel.add(new JSeparator(SwingConstants.HORIZONTAL));
        buttonPanel.add(Box.createVerticalStrut(5));
        buttonPanel.add(bPlacePoule);
        buttonPanel.add(bPlaceVache);
        buttonPanel.add(Box.createVerticalStrut(5));

        // Assemblage final du magasin
        shopPanel.add(bStartPause);
        shopPanel.add(Box.createVerticalStrut(20));
        shopPanel.add(buttonPanel);
        shopPanel.add(Box.createVerticalGlue());

        contentPanel.add(shopPanel);
        
        // ========================================================================
        // Assemblage final
        // ========================================================================
        mainContainer.add(contentPanel, gbc);
        root = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                BufferedImage bg = affichage.getFondCourant();
                if (bg != null) {
                    g.drawImage(bg, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        
     // titre transparent
        if (Constante.m <=6)
        {
        	JLabel titre;
            if (sim.getJoueur().toString().equals("Sans nom"))	// permet de n'afficher que le titre Ferme autosoutenable si l'utilisateur ne rentre pas de nom
            	titre = new JLabel("Ferme autosuffisante");

            else 
            	titre = new JLabel("" + sim.getJoueur().toString()+"'s farm");
            
            titre.setFont(new Font("Serif", Font.BOLD, 35));
            titre.setForeground(Color.BLACK);
            titre.setHorizontalAlignment(SwingConstants.CENTER);
            titre.setOpaque(false);
            titre.setBorder(BorderFactory.createEmptyBorder(65, 0, 10, 0));
            root.add(titre, BorderLayout.NORTH);
        }
        

        // Ajoute ton UI par‑dessus
        root.add(mainContainer, BorderLayout.CENTER);
        frame.setContentPane(root);

        // Configuration du timer
        timer = new Timer(500, e -> {
            boolean encore = sim.unJour();
            updateLabels();
            affichage.repaint();
            root.repaint();
            if (!encore) {
            	 
                if (sim.enPhasePlacement()) {
                    // --- phase placement ---
                    JOptionPane.showMessageDialog(frame,
                        "Fin de saison ! Placez vos survivants.",
                        "Placement", JOptionPane.INFORMATION_MESSAGE);
                    phase = Phase.PLACEMENT;
                    setControlsEnabled(false);
                    bPlacePoule.setEnabled(sim.getPoulesRestante()>0);
                    bPlaceVache.setEnabled(sim.getVachesRestante()>0);
                } else {
                    // --- fin sans survivants ---
                    JOptionPane.showMessageDialog(frame,
                        "Fin de saison ! Aucun survivant. Vous pouvez maintenant acheter ou démarrer.",
                        "Fin de saison", JOptionPane.INFORMATION_MESSAGE);
                    phase = Phase.SAISON;
                    // réactive boutique
                    setControlsEnabled(true);
                }
                sim.stop();
                // **TOUJOURS** réactiver Démarrer, quel que soit le cas
                bStartPause.setEnabled(true);
                bStartPause.setText("Démarrer");
                timer.stop();
            }
        });


       
        
        updateLabels();
        
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    
    /**
     * Affiche un pop up demandant à l'utilisateur de rentrer une quantité pour l'achat de fertilisant.
     * @return
     * @throws AjoutImpossibleException
     */
    private double dmdQte() throws AjoutImpossibleException {
        String input = JOptionPane.showInputDialog(
            null,
            "Quantité de fertilisant à déposer (en g) :",
            "Déposer fertilisant",
            JOptionPane.QUESTION_MESSAGE
        );
        if (input == null) {
            throw new AjoutImpossibleException("Opération annulée.");
        }
        try {
            double qte = Double.parseDouble(input.trim());
            if (qte <= 0) {
                throw new NumberFormatException();
            }
            return qte;
        } catch (NumberFormatException e) {
            throw new AjoutImpossibleException("Veuillez entrer un nombre positif valide.");
        }
    }


    /**
     * Gère les clics sur la grille de l'utilisateur
     * @param x
     * @param y
     * @param frame
     */
    private void handleGridClick(int x, int y, JFrame frame) {
        try {
            if (phase == Phase.SAISON) {
                // on ne retourne plus ici, même si modeAchat == NONE
                switch(modeAchat) {
                    case POULE:   sim.acheterPoule(x, y); break;
                    case VACHE:   sim.acheterVache(x, y); break;
                    case BLE:     sim.acheterBle(x, y);    break;
                    case TOMATE:  sim.acheterTomate(x, y); break;
                    case PUIT:    sim.acheterPuit(x, y);   break;
                    case DPUIT:     
	                	try
	                	{
	                		sim.detruirePuit(x, y);
	                	}
	                	catch (DestructionImpossibleException e)
	                	{
	                		JOptionPane.showMessageDialog(
	                	            frame,
	                	            e.getMessage(),
	                	            "Erreur destruction puit",
	                	            JOptionPane.ERROR_MESSAGE);
	                	}
	                	break;
                    case DFERTIL: 
                    	try
                    	{
                    		double qte = dmdQte();
                    		sim.deposerFertil(qte, x, y);
                    	}
                    	catch (AchatImpossibleException e)
                    	{
                    		JOptionPane.showMessageDialog(
                    	            frame,
                    	            e.getMessage(),
                    	            "Erreur fertilisant",
                    	            JOptionPane.ERROR_MESSAGE);
                    	}
                    	break;
                    
                    default:      
                        return; // pas d'achat demandé
                }

                updateLabels();
                affichage.repaint();

            }
            else { // Phase.PLACEMENT
                switch(modeAchat) {
                case PLACE_POULE:
                    sim.placerPoulesEnVie(x, y);
                    break;
                case PLACE_VACHE:
                    sim.placerVachesEnVie(x, y);
                    break;
                default:
                    return;
            }
            
            // Permet de griser les boutons lorsqu'il n'y a plus de vaches ou de poules à placer.
            bPlacePoule.setEnabled(sim.getPoulesRestante() > 0);
            bPlaceVache.setEnabled(sim.getVachesRestante() > 0);

            // Si tout est placé, repasse en phase SAISON
            if (!sim.enPhasePlacement()) {
                phase = Phase.SAISON;
                // réactive Démarrer / Achats
                setControlsEnabled(true);
                bStartPause.setEnabled(true);
            }

            updateLabels();
            affichage.repaint();
        }
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame,
                ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Mets à jours les statistiques de jeux (l'argent du joueu, les saisons, les calories de l'entrepôtr...)
     */
    private void updateLabels()
    {
    	DecimalFormat df = new DecimalFormat("#.###");
    	lblArgent.setText("Argent : " + df.format(sim.getJoueur().getArgent()) + " Y.");
        lblJour    .setText("Saison : " + sim.getSaison() + " – Jour : " + sim.getJour()+".       ");
        lblEntrepot.setText("Entrepôt (œufs+lait) : " + sim.getEntrepot()+" cal.       ");

        lblRestant .setText(phase==Phase.PLACEMENT
                           ? "À placer P:"+sim.getPoulesRestante()+"/V:"+sim.getVachesRestante()
                           : "");
    }
    
    /**
     * Active et désactive les boutons du magasin.
     * @param enabled
     */
    private void setControlsEnabled(boolean enabled) {
        bAchatPoule   .setEnabled(enabled);
        bAchatVache   .setEnabled(enabled);
        bAchatBle     .setEnabled(enabled);
        bAchatTomate  .setEnabled(enabled);
        bAchatPuit    .setEnabled(enabled);
        bDetruirePuit .setEnabled(enabled);
        bDeposerFertil .setEnabled(enabled);
    }
    
    /**
     * Lance la prochaine saison de la simulation (la première saison est choisie aléatoirement) 
     */
    private void startSeason() {
        if (!sim.estEnPause()) return;         // déjà lancée, on ne fait rien
        sim.start();                           // débloque la simulation
        timer.start();                         // démarre les jours
        // désactive le bouton Démarrer et la boutique
        bStartPause    .setEnabled(false);
        setControlsEnabled(false);
        // et aussi les boutons placement pour sécurité
        bPlacePoule    .setEnabled(false);
        bPlaceVache    .setEnabled(false);

        bDeposerFertil .setEnabled(false);
        phase = Phase.SAISON;
        updateLabels();
    }
    
}
