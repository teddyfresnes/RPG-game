package rpg_game;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.io.File;


import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;



//on reutilise le lecteur de fichiers pour ouvrir les maps
import lecteur_de_fichiers.FilereaderTextCsv;


public class Map implements KeyListener
{
	String map_path;
	String[][] mapData;
	List<WeaponStore> shops;
	List<Monster> monsters;
	
	int playerX;
	int playerY;

	JFrame window;
	public JPanel mapPanel;
	public Player joueur;
	
	public Map(String map_default, JFrame window, Player joueur)
	{
		this.map_path = map_default;
		
		FilereaderTextCsv csvReader = new FilereaderTextCsv(map_path);
		mapData = csvReader.f_import_csv();
		
		this.shops = new ArrayList<>();
		this.monsters = new ArrayList<>();
		
		this.window = window;

		this.joueur = joueur;
	}
	
	
	@Override
    public void keyTyped(KeyEvent e) {
        // Not needed for this example, but required by KeyListener
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // Handle key press events
        int keyCode = e.getKeyCode();
        String next_case_type = "";

        switch (keyCode) {
            case KeyEvent.VK_LEFT:
            	next_case_type = move_player("LEFT");
            	//System.out.println("eho ça marche");
                break;
            case KeyEvent.VK_RIGHT:
            	next_case_type = move_player("RIGHT");
                break;
            case KeyEvent.VK_UP:
            	next_case_type = move_player("UP");
                break;
            case KeyEvent.VK_DOWN:
            	next_case_type = move_player("DOWN");
                break;
        }
        
        
	    if (next_case_type == "shop") // si la prochaine case est un magasin
	    {
	    	System.out.println("Entrée dans le magasin");
	    	WeaponStore magasin = getShopOnPlayer();
	    	//mapPanel.removeAll(); // marche pas on ne sait pas pq
	    	window.getContentPane().removeAll();
	    	window.remove(mapPanel); // obligé d'effacer le mapPanel et de le recréer
	        window.revalidate();
	        window.repaint();
	    	magasin.displayStore(joueur, window, this);
	    }
	    else if (next_case_type == "monster") // si la prochaine case est un monstre
	    {
	    	System.out.println("Attaque monstre");
	    	Monster monster_case = getEntityOnPlayer();
	    	Random r = new Random();
	    	Fight my_fight = new Fight(joueur, monster_case, this, window);
	    	if (monster_case.name != "boss")
	    	{
		    	gen_monster(r.nextInt(4)+monster_case.level, monster_case.type); // regeneration du monstre sur la carte avec une chance de montée de niveau
		    	remove_monster(monster_case);
	    	}
	    }
	    else if (next_case_type == "next_map") // si la prochaine case est un magasin
	    {
	    	if (this.map_path.equals("assets/maps/a.csv")) // début map b, fin du niveau a
	    	{
	    		shops.clear(); monsters.clear();
	    		this.map_path = "assets/maps/b.csv";
	    		FilereaderTextCsv csvReader = new FilereaderTextCsv(map_path);
	    		mapData = csvReader.f_import_csv();
	    		int[] spawnCoordinates = search_spawn();
	    	    this.playerX = spawnCoordinates[1];
	    	    this.playerY = spawnCoordinates[0];
	    	    
	    		WeaponStore shop_default = new WeaponStore("Magasin au milieu de nul part",17,7);
	    		add_shop(shop_default);
	    		
	    		gen_monster(2, "Slime");
	    		gen_monster(3, "Slime");
	    		gen_monster(4, "Slime");
	    		gen_monster(5, "Slime");
	    		gen_monster(6, "Slime");
	    		gen_monster(1, "Squelette");
	    		gen_monster(2, "Squelette");
	    		gen_monster(3, "Squelette");
	    		gen_monster(4, "Squelette");
	    		display_map_ihm();
	    	}
	    	else if (map_path.equals("assets/maps/b.csv")) // début map c, fin du niveau b
	    	{
	    		shops.clear(); monsters.clear();
	    		this.map_path = "assets/maps/c.csv";
	    		FilereaderTextCsv csvReader = new FilereaderTextCsv(map_path);
	    		mapData = csvReader.f_import_csv();
	    		int[] spawnCoordinates = search_spawn();
	    	    this.playerX = spawnCoordinates[1];
	    	    this.playerY = spawnCoordinates[0];
	    		
	    		gen_monster(1, "Boss");
	    		display_map_ihm();
	    	}
	    	else if (map_path.equals("assets/maps/c.csv")) // fin du jeu
	    	{
	    		end_game(true);
            }
	    }
	    else
	    {
	    	display_map_ihm();
	    }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // Not needed for this example, but required by KeyListener
    }
    
    
    public void addKeyListeners()
    {
    	KeyListener[] keyListeners = mapPanel.getKeyListeners();
        for (KeyListener listener : keyListeners) // on supprime les anciens keylisteners pour pas en avoir plusieurs
        {
            mapPanel.removeKeyListener(listener);
        }
        mapPanel.addKeyListener(this);
        mapPanel.setFocusable(true);
        mapPanel.setFocusTraversalKeysEnabled(false);
    }
    
    
    public void display_map_ihm()
    {
    	window.getContentPane().removeAll();
    	this.mapPanel = new JPanel(new GridLayout(mapData.length, mapData[0].length));
    	this.mapPanel.setBackground(new Color(240, 150, 75));

    	// Panel infos joueurs
    	JPanel topLeftPanel = new JPanel(new BorderLayout());
    	topLeftPanel.setPreferredSize(new Dimension(mapPanel.getWidth() / 2, 63));
    	topLeftPanel.setBackground(new Color(229, 206, 177));

    	JLabel classLabel = new JLabel("Classe: " + joueur.category);
    	classLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
    	topLeftPanel.add(classLabel, BorderLayout.NORTH);

    	JPanel playerInfoPanel = new JPanel();
    	playerInfoPanel.setLayout(new BoxLayout(playerInfoPanel, BoxLayout.Y_AXIS));
    	playerInfoPanel.setBackground(new Color(229, 206, 177));

    	JLabel nameLabel = new JLabel("Joueur: " + joueur.username);
    	nameLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
    	playerInfoPanel.add(nameLabel);

    	JLabel levelExpLabel = new JLabel("Level: "+joueur.level+" ("+joueur.exp+"/"+joueur.exp_need+")");
    	levelExpLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
    	playerInfoPanel.add(levelExpLabel);

    	JLabel equippedWeaponLabel = new JLabel("Arme équipée: " + joueur.selectedWeapon.name());
    	equippedWeaponLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
    	playerInfoPanel.add(equippedWeaponLabel);

    	topLeftPanel.add(playerInfoPanel, BorderLayout.CENTER);

    	// Panel supérieur central avec deux boutons carrés
    	JPanel topMiddlePanel = new JPanel(new GridLayout(1, 2));
    	topMiddlePanel.setPreferredSize(new Dimension(mapPanel.getWidth()/2, 63));
    	topMiddlePanel.setBackground(new Color(229, 206, 177));

    	//  boutons avec une image en arrière-plan sans texte
    	JButton button1 = new JButton();
    	ImageIcon originalIcon1 = new ImageIcon("assets/img/" + joueur.getClass().getSimpleName() + ".png");
    	Image originalImage1 = originalIcon1.getImage();
    	originalImage1 = originalImage1.getScaledInstance(160, 120, Image.SCALE_SMOOTH);
    	ImageIcon resizedIcon1 = new ImageIcon(originalImage1);
    	button1.setIcon(resizedIcon1);
    	button1.setPreferredSize(new Dimension(63,63));
    	button1.setBackground(Color.LIGHT_GRAY);
    	button1.addActionListener(new ActionListener() {
    	    @Override
    	    public void actionPerformed(ActionEvent e) {
    	        String infoMessage = "";
    	        switch (joueur.category)
    	        {
    	            case "Guerrier":
    	                infoMessage = "Guerrier - Avantage de dégat";
    	                break;
    	            case "Tank":
    	                infoMessage = "Tank - Avantage de vie";
    	                break;
    	            case "Assassin":
    	                infoMessage = "Assassin - Avantage de dégat, malus de vie";
    	                break;
    	            case "Gobelin":
    	                infoMessage = "Gobelin - Avantage à l'argent";
    	                break;
    	            case "Mage":
    	                infoMessage = "Mage - Avantage d'expérience";
    	                break;
    	            default:
    	                infoMessage = "Classe non reconnue";
    	                break;
    	        }
    	        JOptionPane.showMessageDialog(window, infoMessage, "Infos sur la classe du joueur ", JOptionPane.INFORMATION_MESSAGE);
    	        display_map_ihm();
    	    }
    	});

    	JButton button2 = new JButton();
    	ImageIcon originalIcon2;
    	if (joueur.selectedWeapon.name().equals("Mains nus"))
    	{
    		originalIcon2 = new ImageIcon("assets/img/hand.png");
    	}
    	else
    	{
    		originalIcon2 = new ImageIcon(joueur.selectedWeapon.ascii());
    	}
    	Image originalImage2 = originalIcon2.getImage();
    	originalImage2 = originalImage2.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
    	ImageIcon resizedIcon2 = new ImageIcon(originalImage2);

    	button2.setIcon(resizedIcon2);
    	button2.setPreferredSize(new Dimension(63,63));
    	button2.setBackground(Color.LIGHT_GRAY);
    	button2.addActionListener(new ActionListener() {
    	    @Override
    	    public void actionPerformed(ActionEvent e) {
    	        window.getContentPane().removeAll();
    	        displayInventory(joueur);
    	    }
    	});

    	topMiddlePanel.add(button1);
    	topMiddlePanel.add(button2);

    	// marge entre les boutons
    	topMiddlePanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));

    	// Panneau supérieur droit avec l'argent, les dégâts et la vie
    	JPanel topRightPanel = new JPanel(new BorderLayout());
    	topRightPanel.setPreferredSize(new Dimension(mapPanel.getWidth()/2, 63));
    	topRightPanel.setBackground(new Color(229, 206, 177));

    	// panneau pour aligner les labels à droite
    	JPanel alignRightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    	alignRightPanel.setBackground(new Color(229, 206, 177));

    	JLabel moneyLabel = new JLabel("Argent: "+joueur.money+"💲");
    	moneyLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
    	alignRightPanel.add(moneyLabel);

    	JPanel damageHealthPanel = new JPanel();
    	damageHealthPanel.setLayout(new BoxLayout(damageHealthPanel, BoxLayout.Y_AXIS));
    	damageHealthPanel.setBackground(new Color(229, 206, 177));
    	JLabel damageLabel = new JLabel("Dégâts: "+joueur.damage+"⚔");
    	damageLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
    	damageHealthPanel.add(damageLabel);

    	JLabel healthLabel = new JLabel("Vie: " + joueur.hp+"❤");
    	healthLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
    	damageHealthPanel.add(healthLabel);

    	alignRightPanel.add(damageHealthPanel);

    	topRightPanel.add(damageHealthPanel, BorderLayout.CENTER);

    	alignRightPanel.add(damageHealthPanel);

    	topRightPanel.add(alignRightPanel, BorderLayout.CENTER);

    	// panneaux supérieurs à la fenêtre
    	JPanel topPanel = new JPanel(new GridLayout(1, 3));
    	topPanel.add(topLeftPanel);
    	topPanel.add(topMiddlePanel);
    	topPanel.add(topRightPanel);
    	window.add(topPanel, BorderLayout.NORTH);

        
        for (int i = 0; i < mapData.length; i++) {
            for (int j = 0; j < mapData[i].length; j++) {
                int cellValue = Integer.parseInt(mapData[i][j]);
                String imagePath = "";
	            if (i == playerY && j == playerX)
	            {
	            	imagePath = "assets/img/player_low.png";
	            }
	            else
	            {
			            switch (cellValue) {
		                case 0: // vide
		                    imagePath = "assets/img/grass_low.png";
		                    break;
		                case 1: // mur
		                    imagePath = "assets/img/rock_low.png";
		                    break;
		                case 2: // SPAWN
		                    imagePath = "assets/img/grass_low.png";
		                    break;
		                case 3: // magasin
		                    imagePath = "assets/img/shop_low.png";
		                    break;
		                case 4: // sortie
		                    imagePath = "assets/img/exit_low.png";
		                    break;
		                case 5: // monstre
		                	//System.out.println("try"); System.out.println("i: " + i + ", j: " + j);
		                    for (Monster entity : monsters)
		                    {
		                       // System.out.println("x: " + entity.x + ", y: " + entity.y);
		                        if (entity.x == j && entity.y  == i)
		                        {
		                        	imagePath = "assets/img/"+entity.name+"_low.png";
		                        }
		                    }
		                    break;
		                default: // non défini
		                    imagePath = "assets/img/grass_low.png";
		                    break;
		            }
	            }

	            ImageIcon originalIcon = new ImageIcon(imagePath);
	            
	            JLabel label = new JLabel();
	            label.setIcon(originalIcon);
	            label.setHorizontalAlignment(SwingConstants.CENTER);
	            label.setOpaque(true);

	            mapPanel.add(label);

	            SwingUtilities.invokeLater(new Runnable() {
	                @Override
	                public void run() {
	                    // obtenir la taille réelle du label après l'ajout au conteneur
	                    int labelWidth = label.getWidth();
	                    int labelHeight = label.getHeight();

	                    // redimensionner l'image en fonction de la taille du label
	                    Image originalImage = originalIcon.getImage();
	                    Image resizedImage = originalImage.getScaledInstance(labelWidth, labelHeight, Image.SCALE_SMOOTH);
	                    ImageIcon resizedIcon = new ImageIcon(resizedImage);

	                    label.setIcon(resizedIcon);
	                }
	            });
            }
        }
        
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setPreferredSize(new Dimension(mapPanel.getWidth(), 20));
        JLabel bottomLabel = new JLabel("Utilisez les flèches directionnelles pour vous déplacer, espace pour faire une action");
        bottomLabel.setHorizontalAlignment(JLabel.CENTER);
        bottomPanel.add(bottomLabel, BorderLayout.CENTER);
        bottomPanel.setBackground(new Color(229, 206, 177));
        

        window.add(topPanel, BorderLayout.NORTH);
        window.add(mapPanel, BorderLayout.CENTER);
        window.add(bottomPanel, BorderLayout.SOUTH);

        addKeyListeners();
        
        mapPanel.requestFocusInWindow();
        window.revalidate();
        window.repaint();
    }
    
    

    public static void listFilesInDirectory(String directoryPath) { // fonction pour afficher les fichiers d'un dir pour debugger les fichiers non trouvés
        // Créez un objet File pour représenter le répertoire
        File directory = new File(directoryPath);

        // Obtenez la liste des fichiers dans le répertoire
        File[] files = directory.listFiles();

        // Affichez la liste des fichiers dans la console
        if (files != null) {
            for (File file : files) {
                System.out.println(file.getName());
            }
        } else {
            System.out.println("Le répertoire est vide ou inaccessible.");
        }
    }
    
    
    private ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = getClass().getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("File not found: " + path);
            return null;
        }
    }
    
    
    public void displayInventory(Player joueur)
    {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(229, 206, 177));

        // titre
        JLabel titleLabel = new JLabel("Inventaire de " + joueur.username);
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // boutons d'achat
        JPanel buttonsPanel = new JPanel(new GridLayout(1, joueur.bag.size()));
        buttonsPanel.setBackground(new Color(229, 206, 177));
        for (Weapon itemStock : joueur.bag) {
            //pannel bouton
            JPanel itemPanel = new JPanel(new BorderLayout());
            itemPanel.setBorder(BorderFactory.createEmptyBorder(30, 10, 30, 10)); // marge
            itemPanel.setBackground(new Color(229, 206, 177));

            // label top
            JLabel nameLabel = new JLabel(itemStock.name());
            nameLabel.setHorizontalAlignment(JLabel.CENTER);
            itemPanel.add(nameLabel, BorderLayout.NORTH);

            // fat button
            JButton useButton = new JButton();
            useButton.setIcon(new ImageIcon(itemStock.ascii()));
            useButton.setBackground(Color.LIGHT_GRAY);
            itemPanel.add(useButton, BorderLayout.CENTER);

            // labels degat
            JLabel damageLabel = new JLabel("Dégâts: " + itemStock.damage()+joueur.selectedWeapon.name() + "⚔");

            JPanel infoPanel = new JPanel(new GridLayout(2, 1));
            damageLabel.setHorizontalAlignment(JLabel.CENTER); // Centrer le texte
            infoPanel.add(damageLabel);
            itemPanel.add(infoPanel, BorderLayout.SOUTH);
            infoPanel.setBackground(new Color(229, 206, 177));

            // ajout gestionnaire d'événements au bouton d'utilisation
            useButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                	
                	window.getContentPane().removeAll();
                	
                	JPanel mainPanel = new JPanel(new BorderLayout());
                    window.add(mainPanel, BorderLayout.CENTER);
                	
                    Weapon selectedWeapon = itemStock;
                    joueur.selectedWeapon = selectedWeapon;

                    JLabel resultLabel = new JLabel("Vous avez équipé "+selectedWeapon.name());
                    resultLabel.setHorizontalAlignment(JLabel.CENTER);
                    mainPanel.add(resultLabel, BorderLayout.CENTER);

                    // Ajouter un bouton "OK" pour revenir à la carte
                    JButton exitButton = new JButton("OK");
                    exitButton.setBackground(new Color(70, 48, 21)); // Couleur marron
                    exitButton.setForeground(Color.WHITE); // Texte en blanc
                    exitButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            mainPanel.removeAll();
                            display_map_ihm(); 
                        }
                    });
                    mainPanel.add(exitButton, BorderLayout.SOUTH);

                    // Rafraîchir l'affichage
                    mainPanel.revalidate();
                    mainPanel.repaint();
                }
            });

            // ajout du pannel final de l'item
            buttonsPanel.add(itemPanel);
        }
        mainPanel.add(buttonsPanel, BorderLayout.CENTER);

        // infos joueurs
        JPanel playerInfoPanel = new JPanel(new GridLayout(2, 1));
        playerInfoPanel.add(new JLabel("Argent: " + joueur.money));
        playerInfoPanel.add(new JLabel("Dégâts: " + joueur.damage));
        playerInfoPanel.setBackground(new Color(229, 206, 177));
        mainPanel.add(playerInfoPanel, BorderLayout.SOUTH);

        // boutons store
        JButton exitButton = new JButton("Quitter l'inventaire");
        exitButton.setBackground(new Color(70, 48, 21)); // Couleur marron
        exitButton.setForeground(Color.WHITE); // Texte en blanc
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                window.getContentPane().removeAll();
                display_map_ihm();
            }
        });
        mainPanel.add(exitButton, BorderLayout.SOUTH);

        // ajout final
        window.add(mainPanel, BorderLayout.CENTER);
        window.revalidate();
        window.repaint();
    }
    
    
    public void end_game(boolean win)
    {
        window.getContentPane().removeAll();
        JPanel mainPanel = new JPanel(new BorderLayout());
        window.add(mainPanel, BorderLayout.CENTER);

        JLabel resultLabel;
        if (win) {
            resultLabel = new JLabel("Félicitations, "+joueur.username+"! Vous avez fini Sorbonne RPG!");
        } else {
            resultLabel = new JLabel("Et bien, "+joueur.username+". Vous n'avez pas été à la hauteur de Sorbonne RPG.");
        }
        resultLabel.setHorizontalAlignment(JLabel.CENTER);
        mainPanel.add(resultLabel, BorderLayout.CENTER);

        // détails du joueur
        JLabel levelLabel = new JLabel("Niveau: " + joueur.level);
        JLabel moneyLabel = new JLabel("Argent: " + joueur.money + "💲");
        JLabel classLabel = new JLabel("Classe: " + joueur.category);

        JPanel detailsPanel = new JPanel(new GridLayout(3, 1));
        detailsPanel.add(levelLabel);
        detailsPanel.add(moneyLabel);
        detailsPanel.add(classLabel);
        mainPanel.add(detailsPanel, BorderLayout.NORTH);

        // bouton pour quitter
        JButton exitButton = new JButton("QUITTER");
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainPanel.removeAll();
                window.dispose();
            }
        });
        mainPanel.add(exitButton, BorderLayout.SOUTH);

        mainPanel.revalidate();
        mainPanel.repaint();
    }
    

    public WeaponStore getShopOnPlayer()
    {
        for (WeaponStore shop : shops)
        {
            //System.out.println("shop.shopX: " + shop.shopX);
            //System.out.println("shop.shopY: " + shop.shopY);
            //System.out.println("this.playerX: " + this.playerX);
            //System.out.println("this.playerY: " + this.playerY);
            if (shop.shopX == this.playerX && shop.shopY == this.playerY)
            {
                return shop;
            }
        }
        return null; // Aucun magasin trouvé sur la case spécifiée
    }
    
    
    public Monster getEntityOnPlayer()
    {
        for (Monster entity : monsters)
        {
            System.out.println("monster.x : " + entity.x);; System.out.println("monster.y: " + entity.y); System.out.println("this.playerX: " + this.playerX); System.out.println("this.playerY: " + this.playerY);
            if (entity.x == this.playerX && entity.y  == this.playerY)
            {
            	//System.out.println("found it");
                return entity;
            }
        }
        return null;
    }
	
	
	public void add_shop(WeaponStore shop) // magasin plage de 100 à 200
	{
		mapData[shop.shopY][shop.shopX] = "3";
		shops.add(shop);
	}
	
	
    public int[] search_spawn()
    {
        for (int i = 0; i < this.mapData.length; i++)
        {
            for (int j = 0; j < this.mapData[i].length; j++)
            {
                if (Integer.parseInt(this.mapData[i][j]) == 2)
                {
                    return new int[]{i, j};
                }
            }
        }
        // non trouvé on renvoit des coordonnées par défaut
        return new int[]{1, 1};
    }
	
    
    
    
    public String move_player(String direction)
    {
    	String case_you_go = "normal";
    	switch (direction) 
    	{
    	    case "UP":
    	        if (playerY > 0) 
    	        {
    	            int cell_value = Integer.parseInt(mapData[playerY - 1][playerX]); 
    	            if (cell_value == 3) {case_you_go = "shop";} 
    	            if (cell_value == 5) {case_you_go = "monster";} 
    	            if (cell_value == 4) {case_you_go = "next_map";} 
    	            if (cell_value != 1) {playerY--;} // sinon deplacement
    	        }
    	        break;
    	    case "DOWN":
    	        if (playerY < mapData.length - 1) 
    	        {
    	            int cell_value = Integer.parseInt(mapData[playerY + 1][playerX]);
    	            if (cell_value == 3) {case_you_go = "shop";} 
    	            if (cell_value == 5) {case_you_go = "monster";} 
    	            if (cell_value == 4) {case_you_go = "next_map";} 
    	            if (cell_value != 1) {playerY++;}
    	        }
    	        break;
    	    case "LEFT":
    	        if (playerX > 0) 
    	        {
    	            int cell_value = Integer.parseInt(mapData[playerY][playerX - 1]);
    	            if (cell_value == 3) {case_you_go = "shop";} 
    	            if (cell_value == 5) {case_you_go = "monster";} 
    	            if (cell_value == 4) {case_you_go = "next_map";} 
    	            if (cell_value != 1) {playerX--;}
    	        }
    	        break;
    	    case "RIGHT":
    	        if (playerX < mapData[playerY].length - 1) 
    	        {
    	            int cell_value = Integer.parseInt(mapData[playerY][playerX + 1]);
    	            if (cell_value == 3) {case_you_go = "shop";} 
    	            if (cell_value == 5) {case_you_go = "monster";} 
    	            if (cell_value == 4) {case_you_go = "next_map";} 
    	            if (cell_value != 1) {playerX++;}
    	        }
    	        break;
    	}

        //System.out.println("this.playerX: " + this.playerX);
        //System.out.println("this.playerY: " + this.playerY);
    	return case_you_go;
    }
    

    
    public void gen_monster(int lvl, String monster_type)
    {
    	Random random = new Random();
        int x, y;

        do
        {
            x = random.nextInt(this.mapData[0].length);    
            y = random.nextInt(this.mapData.length);  
            //System.out.println("x: " + x + ", y: " + y);
        } while (Integer.parseInt(this.mapData[y][x]) != 0);

        Monster monster = new Monster(x, y, monster_type, lvl);
        this.monsters.add(monster);
        this.mapData[y][x] = "5"; // ajout monstre
    }
    
    public void remove_monster(Monster monster)
    {
        monsters.remove(monster);
        mapData[monster.y][monster.x] = "0";
    }
    
    public void death()
    {
    	//pass
    }
}
